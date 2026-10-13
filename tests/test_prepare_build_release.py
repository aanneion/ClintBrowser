"""Release preparation tests; never create or modify a GitHub release."""
import hashlib
from pathlib import Path
import runpy
import tempfile
import unittest


SCRIPT = Path(__file__).resolve().parents[1] / "scripts" / "prepare-build-release.py"
prepare_release = runpy.run_path(str(SCRIPT))["prepare_release"]


class PrepareBuildReleaseTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.assets = self.root / "assets"
        self.assets.mkdir()
        self.notes = self.root / "notes.md"
        self.env = {
            "BUILD_KIND": "ci",
            "GITHUB_SERVER_URL": "https://github.com",
            "GITHUB_REPOSITORY": "example/browser",
            "GITHUB_RUN_ID": "123456",
            "GITHUB_RUN_ATTEMPT": "2",
            "GITHUB_WORKFLOW": "Continuous Release",
            "GITHUB_REF_NAME": "main",
            "GITHUB_SHA": "abc123",
            "GITHUB_RUN_NUMBER": "42",
        }

    def apk(self, name="app-github-universal-release.apk", contents=b"test apk"):
        path = self.assets / name
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(contents)
        return path

    def prepare(self):
        prepare_release(self.assets, self.notes, self.env)
        return self.notes.read_text()

    def test_checksums_cover_both_flavors_and_every_abi(self):
        for flavor in ("github", "foss"):
            for abi in ("arm64-v8a", "armeabi-v7a", "x86", "x86_64", "universal"):
                self.apk(f"app-{flavor}-{abi}-release.apk")
        notes = self.prepare()
        checksums = (self.assets / "SHA256SUMS.txt").read_text()
        self.assertEqual(len(checksums.splitlines()), 10)
        self.assertIn(checksums, notes)
        digest = hashlib.sha256(b"test apk").hexdigest()
        for apk in self.assets.glob("*.apk"):
            self.assertIn(f"{digest}  {apk.name}\n", checksums)

    def test_ci_notes_explain_signing_and_link_to_exact_attempt(self):
        self.apk()
        notes = self.prepare()
        self.assertIn("com.jhaiian.clint.ci", notes)
        self.assertIn("uninstalling only the old CI app", notes)
        self.assertIn("https://github.com/example/browser/actions/runs/123456/attempts/2", notes)
        self.assertIn("`abc123`", notes)
        self.assertIn("pre-release", notes)

    def test_production_signed_notes_do_not_claim_test_key(self):
        self.apk()
        self.env["BUILD_KIND"] = "release"
        notes = self.prepare()
        self.assertIn("production signing key", notes)
        self.assertNotIn("com.jhaiian.clint.ci", notes)

    def test_empty_artifacts_are_rejected(self):
        (self.assets / "output-metadata.json").write_text("{}")
        with self.assertRaisesRegex(ValueError, "No APKs"):
            self.prepare()
        self.assertFalse(self.notes.exists())

    def test_missing_artifact_directory_is_rejected(self):
        self.assets.rmdir()
        with self.assertRaisesRegex(ValueError, "No APKs"):
            self.prepare()

    def test_unknown_build_kind_is_rejected(self):
        self.apk()
        self.env["BUILD_KIND"] = "unknown"
        with self.assertRaisesRegex(ValueError, "BUILD_KIND"):
            self.prepare()

    def test_duplicate_release_asset_names_are_rejected(self):
        self.apk("first/app.apk")
        self.apk("second/app.apk")
        with self.assertRaisesRegex(ValueError, "unique"):
            self.prepare()

    def test_nested_apk_and_chunked_checksum(self):
        contents = b"binary content" * 100000
        self.apk("nested/app.apk", contents)
        self.prepare()
        self.assertEqual(
            (self.assets / "SHA256SUMS.txt").read_text(),
            f"{hashlib.sha256(contents).hexdigest()}  app.apk\n",
        )


if __name__ == "__main__":
    unittest.main()
