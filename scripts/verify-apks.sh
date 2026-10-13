#!/usr/bin/env bash
# Reject unsigned/corrupt/misaligned APKs before they become downloadable artifacts.
set -euo pipefail

if (( $# == 0 )); then
  echo "Usage: $0 APK_DIRECTORY [APK_DIRECTORY ...]" >&2
  exit 1
fi

sdk_root="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
if [[ -z "$sdk_root" || ! -d "$sdk_root/build-tools" ]]; then
  echo "::error::Set ANDROID_HOME to an Android SDK with build-tools installed."
  exit 1
fi
build_tools="$(find "$sdk_root/build-tools" -mindepth 1 -maxdepth 1 -type d | sort -V | tail -n 1)"
for tool in apksigner zipalign; do
  if [[ ! -x "$build_tools/$tool" ]]; then
    echo "::error::Missing Android build tool: $tool"
    exit 1
  fi
done

shopt -s nullglob
for directory in "$@"; do
  apks=("$directory"/*.apk)
  if (( ${#apks[@]} == 0 )); then
    echo "::error::No APKs found in $directory"
    exit 1
  fi
  for apk in "${apks[@]}"; do
    echo "Verifying $apk"
    if ! "$build_tools/apksigner" verify --verbose --print-certs "$apk"; then
      echo "::error::APK has no valid Android signature: $apk. Configure release signing or use -PciBuild=true for an isolated test build."
      exit 1
    fi
    # Check ZIP alignment, including uncompressed native libraries on 16 KB
    # devices. This does not validate the ELF alignment inside native libraries.
    "$build_tools/zipalign" -c -P 16 -v 4 "$apk"
  done
done
