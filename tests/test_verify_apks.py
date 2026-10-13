"""Exercise the upload gate without requiring an Android SDK or real APKs.

The fake tools model success/failure only; real signature/alignment validation
is performed with Android build-tools in each APK-producing workflow.
"""
import os
from pathlib import Path
import subprocess
import tempfile
import unittest


SCRIPT = Path(__file__).resolve().parents[1] / "scripts" / "verify-apks.sh"
ABIS = ("arm64-v8a", "armeabi-v7a", "x86", "x86_64", "universal")


class VerifyApksTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.sdk = self.root / "Android SDK"
        self.tools = self.sdk / "build-tools" / "37.0.0"
        self.tools.mkdir(parents=True)
        self.log = self.root / "calls.log"
        for name in ("apksigner", "zipalign"):
            tool = self.tools / name
            tool.write_text('''#!/usr/bin/env bash
set -eu
apk="${@: -1}"
printf '%s:%s\\n' "$(basename "$0")" "$apk" >> "$TOOL_LOG"
case "$(basename "$0")" in
  apksigner) ! grep -q unsigned "$apk" ;;
  zipalign) ! grep -q misaligned "$apk" ;;
esac
''')
            tool.chmod(0o755)
        self.env = dict(os.environ, ANDROID_HOME=str(self.sdk), TOOL_LOG=str(self.log))

    def directory(self, flavor="github", contents="signed"):
        directory = self.root / flavor / "release"
        directory.mkdir(parents=True)
        for abi in ABIS:
            (directory / f"app-{flavor}-{abi}-release.apk").write_text(contents)
        return directory

    def run_gate(self, *directories):
        return subprocess.run(
            ["bash", str(SCRIPT), *map(str, directories)],
            env=self.env, text=True, capture_output=True,
        )

    def test_all_abis_and_both_flavors_are_verified(self):
        github = self.directory()
        foss = self.directory("foss")
        result = self.run_gate(github, foss)
        self.assertEqual(result.returncode, 0, result.stdout + result.stderr)
        calls = self.log.read_text().splitlines()
        self.assertEqual(len(calls), 20)
        for directory in (github, foss):
            for apk in directory.glob("*.apk"):
                self.assertIn(f"apksigner:{apk}", calls)
                self.assertIn(f"zipalign:{apk}", calls)

    def test_unsigned_apk_is_rejected(self):
        result = self.run_gate(self.directory(contents="unsigned"))
        self.assertNotEqual(result.returncode, 0)
        self.assertIn("no valid Android signature", result.stdout)
        self.assertNotIn("zipalign:", self.log.read_text())

    def test_unsigned_among_signed_apks_is_rejected(self):
        directory = self.directory()
        (directory / "app-github-x86_64-release.apk").write_text("unsigned")
        self.assertNotEqual(self.run_gate(directory).returncode, 0)

    def test_misaligned_apk_is_rejected(self):
        self.assertNotEqual(self.run_gate(self.directory(contents="misaligned")).returncode, 0)

    def test_empty_directory_is_rejected(self):
        directory = self.root / "empty"
        directory.mkdir()
        result = self.run_gate(directory)
        self.assertNotEqual(result.returncode, 0)
        self.assertIn("No APKs found", result.stdout)

    def test_missing_second_flavor_is_rejected(self):
        self.assertNotEqual(self.run_gate(self.directory(), self.root / "missing").returncode, 0)

    def test_no_arguments_is_rejected(self):
        self.assertNotEqual(self.run_gate().returncode, 0)

    def test_missing_sdk_is_rejected(self):
        self.env["ANDROID_HOME"] = str(self.root / "missing")
        self.assertNotEqual(self.run_gate(self.directory()).returncode, 0)

    def test_missing_tool_is_rejected(self):
        (self.tools / "apksigner").unlink()
        self.assertNotEqual(self.run_gate(self.directory()).returncode, 0)

    def test_android_sdk_root_fallback(self):
        del self.env["ANDROID_HOME"]
        self.env["ANDROID_SDK_ROOT"] = str(self.sdk)
        self.assertEqual(self.run_gate(self.directory()).returncode, 0)


if __name__ == "__main__":
    unittest.main()
