<div align="center">

<img src="docs/icon.png" width="120" alt="Clint Browser logo" />

# Clint Browser

**Customizable Layered Internet Navigation Tool**

Lightweight browser with ADM, ad blocking, media capture, and multi-profile support

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blueviolet.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B-blueviolet.svg)](https://developer.android.com)
[![Stable](https://img.shields.io/badge/Stable-1.2.0-blueviolet.svg)](https://github.com/jhaiian/ClintBrowser/releases/tag/v1.2.0)
[![Beta](https://img.shields.io/badge/Beta-1.2.0-blueviolet.svg)](https://github.com/jhaiian/ClintBrowser/releases/tag/v1.2.0)
[![Downloads](https://img.shields.io/github/downloads/jhaiian/ClintBrowser/total?color=blueviolet)](https://github.com/jhaiian/ClintBrowser/releases)
[![Open Source](https://img.shields.io/badge/Open%20Source-Yes-blueviolet.svg)](https://github.com/jhaiian/ClintBrowser)

</div>

---

## What is Clint Browser?

An open source Android browser with no dedicated backend and tons of features that typical browsers have, while offering a modern UI built around Material 3.

We respect your privacy, so the app doesn't collect any kind of data. We don't even count how many active users there are. We collect zero data whatsoever.

Clint Browser's goal is to create an Android browser that does more while being pleasant to look at and highly customizable, with no ads and no paywalls. Clint Browser is completely free to use.

Looking for a Firefox-based version with extensions and background playback? Check out [Clint Gecko](https://github.com/jhaiian/ClintGecko), the official fork of Clint Browser built on GeckoView.

Built and maintained by **[@jhaiian](https://github.com/jhaiian)** — a solo developer from the Philippines 🇵🇭

---

## Screenshots

<div align="center">
  <table>
    <tr>
      <td align="center"><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/1.jpg" width="180" alt="Screenshot 1" /></td>
      <td align="center"><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/2.jpg" width="180" alt="Screenshot 2" /></td>
    </tr>
    <tr>
      <td align="center"><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/3.jpg" width="180" alt="Screenshot 3" /></td>
      <td align="center"><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/4.jpg" width="180" alt="Screenshot 4" /></td>
    </tr>
  </table>
</div>

---

## Features

### 🎬 Media Capture

- Detects video, audio, and subtitle streams on webpages you visit
- Supports normal video, HLS/M3U8, and DASH streams
- Download detected media directly from the browser

### ⬇️ Download Manager

- Multi-threaded and multi-part downloads
- Pause and resume
- Download scheduler
- Speed limiter
- Custom download locations
- Automatically pauses and resumes stuck downloads on its own

### 📜 User Scripts

- Install userscripts from a URL or write your own with a built-in code editor
- Tampermonkey-style metadata support (`@match`, `@include`, `@exclude`, `@require`, `@resource`, `@grant`)
- GM API support, including `GM.setValue`/`GM.getValue`, `GM.xmlHttpRequest`, and `GM.notification`
- Automatic update checks for scripts installed from a URL
- Enable or disable individual scripts at any time

### 🎨 UI & Customization

Clint Browser is built with **Material 3** and **Jetpack Compose**, with a highly customizable interface.

- 50 accent colors
- Light and Dark themes
- 5 surface intensities
- **In total, 500 possible theme combinations**

### 🛡️ Quiver Guard

- Built-in ad and tracker blocker
- Cosmetic and network filtering
- Multiple filter lists
- Per-site controls

### 🚫 Website Blocker

- Blocks pornography, gambling, and other unwanted categories
- Category-based filter lists (abuse, ads, crypto, drugs, fraud, gambling, and more)
- Add your own custom websites to block

### 👤 Profiles

- Stay signed in to several accounts on the same website at the same time
- Each profile keeps its own cookies, logins, and site data
- Pick a profile when opening a new tab
- Give each profile its own name and color

### 🔗 Shortcuts

- Add any website to your home screen with a custom name and icon
- Frameless mode hides the search bar and bottom bar for an app-like feel
- Manage all your shortcuts in one place with the built-in Shortcut Manager

### 🌐 Browser

- Multi-tab browsing
- Incognito mode
- Reader Mode
- Desktop Mode
- Popup protection
- Data Saver

> Just download the app and explore it yourself.

---

## Donations

### Help Keep Clint Free Forever 💜

Clint is an independent, open-source project built by one developer who believes everyone deserves a customizable browsing experience without ads, paywalls, or locked features.

Your donations help support continued development, maintenance, and future improvements. Every contribution, no matter how small, means a lot and helps keep Clint moving forward.

Clint will always remain free. Thank you for being part of this journey. 💜

[![Patreon](https://img.shields.io/badge/Support%20me-Patreon-orange?style=for-the-badge&logo=patreon)](https://patreon.com/Jhaiian)
[![Ko-fi](https://img.shields.io/badge/Buy%20me%20a%20coffee-Ko--fi-red?style=for-the-badge&logo=ko-fi)](https://ko-fi.com/jhaiian)
[![PayPal](https://img.shields.io/badge/Donate-PayPal-00457C?style=for-the-badge&logo=paypal)](https://paypal.me/jhaiian)
[![InstaPay QR](https://img.shields.io/badge/InstaPay%20QR-Philippines%20only-7C3AED?style=for-the-badge)](https://github.com/jhaiian/jhaiian/blob/main/docs/Insta%20Pay.jpg)

---

## Requirements

- Android 8.0 (API 26) or higher
- WebView Implementation

---

## Installation

Download the latest APK from the [Releases](https://github.com/jhaiian/ClintBrowser/releases) page.

### Choosing an APK

- **universal**: use this if you are unsure; it includes all supported CPU architectures.
- **arm64-v8a**: most modern Android phones (64-bit ARM).
- **armeabi-v7a**: older 32-bit ARM devices.
- **x86 / x86_64**: Intel devices and corresponding emulators, not typical ARM phones.

Android 8.0 or newer is required. `arm64-v8a` and `x86_64` are different
architectures; there is no Android ABI named `x64v8a`.

### Installing builds from Actions

Open this repository's **Releases** page and download an APK directly from the
latest automated **pre-release**, or download and extract the **Clint-github** or
**Clint-foss** artifact from a successful **Continuous Release** run. Install the
universal APK (or the APK matching your device). Do not try to install an artifact
ZIP itself.

Continuous builds are optimized release builds signed with a test/debug key,
with application ID `com.jhaiian.clint.ci` and version suffix `-ci`. They install
alongside the production app instead of attempting to replace it with a different
signing certificate. GitHub and FOSS are alternative builds of the same app.
The CI key is generated on each fresh runner, so replacing a CI build from an
older run may require uninstalling **only the CI app** first. Export any data you
want to keep before uninstalling; uninstalling deletes that app's local data.

Older continuous artifacts built without signing secrets were **unsigned** and
cannot be installed on any device. Download a new build after this fix; changing
the ABI does not fix an unsigned APK.

For a local installable test build, run:

```sh
./gradlew assembleGithubRelease assembleFossRelease -PciBuild=true
```

The manual release workflows and tagged releases still require the production
signing keystore (`BASE_64_SIGNING_KEY`) and signing properties
(`LOCAL_PROPERTIES`). They do not silently substitute a test key. All APK
workflows verify signatures and ZIP alignment before uploading any APKs.

### Automatic build releases

Every successful non-pull-request APK build publishes a GitHub **pre-release**:

- **Continuous Release**: pushes to `main` and manual runs publish both GitHub
  and FOSS CI APKs, including every generated architecture and universal APK.
- **Build Clint Browser**: manual runs publish all generated GitHub APKs.
- **Build Clint Browser ARM64**: manual runs publish the selected ARM64 APK
  (or universal APK when no ARM64-specific output exists).
- **Build FOSS APK**: manual runs publish all generated FOSS APKs.

Each run attempt gets a unique `build-<run-id>-<attempt>` tag pointing to its
source commit. Releases include APKs, `SHA256SUMS.txt`, installation/signing
instructions, and a link to the exact workflow run. Existing build releases are
not overwritten. The release remains a draft until its asset uploads complete.

Publishing runs only after a successful build, APK verification, and artifact
upload. Pull requests, including same-repository PRs, stay artifact-only and do
not receive release-write access. Failed or cancelled builds do not run the
publisher. Only the publish job has `contents: write`; it uses the built-in
`GITHUB_TOKEN`, so no additional personal access token is needed for build
pre-releases.

Automated build pre-releases never replace the **Latest** stable release or
modify the in-app update manifests. The existing `v*` tag workflow continues to
publish versioned production releases separately. Native-library-only workflows
do not publish APK releases.

If installation still fails, `adb install path/to/app.apk` reports the specific
reason: `INSTALL_FAILED_NO_MATCHING_ABIS` means the wrong architecture,
`INSTALL_PARSE_FAILED_NO_CERTIFICATES` means an unsigned/invalid APK, and
`INSTALL_FAILED_UPDATE_INCOMPATIBLE` usually means an existing installation was
signed with a different key. Do not uninstall your production app just to test a
CI build.

---

## Contributing

Contributions, bug reports, and feature requests are welcome.

1. [Open an issue](https://github.com/jhaiian/ClintBrowser/issues) to report a bug or suggest a feature.
2. Fork the repo and create a branch for your change.
3. Submit a pull request with a clear description.

To report a crash, use the built‑in **Debug & Crash Reports** screen. It generates a pre‑filled GitHub issue template with your device info and crash log. For more details, see [Contributing.md](https://github.com/jhaiian/ClintBrowser/blob/main/Contributing.md).

---

## Community

Join the official Clint Browser community on Discord and Reddit to share feedback, report bugs, and connect with other users.

- **Discord** – [discord.gg/4kUe4yPQ32](https://discord.gg/4kUe4yPQ32)
- **Reddit** – [reddit.com/r/ClintBrowser](https://reddit.com/r/ClintBrowser)

---

## License

The source code of this project is licensed under the [GNU General Public License v3.0 (GPLv3)](https://github.com/jhaiian/ClintBrowser/blob/main/LICENSE).  

The names "Clint" and "Clint Browser," the logo, screenshots, and all related branding assets are **trademarks** or **proprietary assets** of the author and are **not licensed under the GPL**. Use of these assets requires explicit permission from the author.

All libraries used in this app have their own licenses. Please comply with their respective license terms. See [Attribution.md](https://github.com/jhaiian/ClintBrowser/blob/main/Attribution.md) for details.

---

## Legal

- [Privacy Policy](PRIVACY_POLICY.md)
- [Terms of Service](TERMS_OF_SERVICE.md)
