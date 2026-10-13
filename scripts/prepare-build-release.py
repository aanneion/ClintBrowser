#!/usr/bin/env python3
"""Prepare automatic APK release notes and checksums (no network operations)."""
import hashlib
import os
from pathlib import Path
import sys


def prepare_release(assets, notes_path, env):
    kind = env["BUILD_KIND"]
    if kind not in ("ci", "release"):
        raise ValueError("BUILD_KIND must be ci or release")
    apks = sorted(assets.rglob("*.apk"))
    if not apks:
        raise ValueError("No APKs downloaded; refusing to publish an empty release")
    names = [apk.name for apk in apks]
    if len(names) != len(set(names)):
        raise ValueError("APK asset names must be unique across artifacts")
    checksums = []
    for apk in apks:
        digest = hashlib.sha256()
        with apk.open("rb") as stream:
            for chunk in iter(lambda: stream.read(1024 * 1024), b""):
                digest.update(chunk)
        checksums.append(f"{digest.hexdigest()}  {apk.name}\n")
    (assets / "SHA256SUMS.txt").write_text("".join(checksums), encoding="utf-8")

    run_url = (
        f'{env["GITHUB_SERVER_URL"]}/{env["GITHUB_REPOSITORY"]}'
        f'/actions/runs/{env["GITHUB_RUN_ID"]}/attempts/{env["GITHUB_RUN_ATTEMPT"]}'
    )
    signing = (
        "These are test-key-signed CI builds, not production updates. "
        "Their application ID is `com.jhaiian.clint.ci`, so they install alongside "
        "the production app. A fresh runner generates a new test key; installing "
        "over a CI build from an earlier run may require exporting your data and "
        "uninstalling only the old CI app first. Uninstalling deletes its local data."
        if kind == "ci" else
        "These APKs use the configured production signing key. Updating an existing "
        "installation requires the same signing key and a compatible version code."
    )
    notes_path.write_text(
        f"## Automated APK build\n\n"
        f"- Workflow: {env['GITHUB_WORKFLOW']}\n"
        f"- Ref: `{env['GITHUB_REF_NAME']}`\n"
        f"- Commit: `{env['GITHUB_SHA']}`\n"
        f"- Build: [run {env['GITHUB_RUN_NUMBER']}, attempt {env['GITHUB_RUN_ATTEMPT']}]({run_url})\n\n"
        f"This is an automated **pre-release**, not a stable version announcement. "
        f"It does not replace the latest stable release or update the in-app update manifests.\n\n"
        f"### Installation\n\n{signing}\n\n"
        f"Android 8.0 or newer is required. Choose the **universal** APK if available "
        f"and unsure of your device's architecture, or **arm64-v8a** for most modern "
        f"ARM phones. **x86_64** is not ARM64. GitHub and FOSS APKs are alternative "
        f"flavors; choose one. Download the APK directly from the assets below.\n\n"
        f"### SHA-256 checksums\n\n```text\n{''.join(checksums)}```\n",
        encoding="utf-8",
    )


if __name__ == "__main__":
    if len(sys.argv) != 3:
        sys.exit("Usage: prepare-build-release.py APK_DIRECTORY NOTES_PATH")
    try:
        prepare_release(Path(sys.argv[1]), Path(sys.argv[2]), os.environ)
    except (KeyError, ValueError) as error:
        sys.exit(str(error))
