package com.jhaiian.clint.profiles

import android.content.Context
import android.webkit.WebSettings
import androidx.annotation.StringRes
import androidx.webkit.UserAgentMetadata
import com.jhaiian.clint.R

/**
 * Compatibility-focused request identity presets for a saved browser profile.
 *
 * These values only change WebView's user-agent/client-hint signals. They do not change the
 * Android WebView engine or make a profile anonymous.
 */
enum class ProfileIdentityPreset(
    val storageKey: String,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    val isChromium: Boolean,
    val isMobile: Boolean,
    val clientHintPlatform: String,
    val platformVersion: String,
    val model: String = "",
    val architecture: String = "",
    val bitness: Int = UserAgentMetadata.BITNESS_DEFAULT,
    val desktopPresentationByDefault: Boolean = false,
    val navigatorPlatform: String? = null,
    val navigatorVendor: String? = null
) {
    NATIVE_ANDROID(
        storageKey = "native_android",
        titleRes = R.string.profile_identity_native_android,
        descriptionRes = R.string.profile_identity_native_android_summary,
        isChromium = true,
        isMobile = true,
        clientHintPlatform = "Android",
        platformVersion = "14.0.0"
    ),
    WINDOWS_DESKTOP(
        storageKey = "windows_desktop",
        titleRes = R.string.profile_identity_windows_desktop,
        descriptionRes = R.string.profile_identity_windows_desktop_summary,
        isChromium = true,
        isMobile = false,
        clientHintPlatform = "Windows",
        platformVersion = "15.0.0",
        architecture = "x86",
        bitness = 64,
        desktopPresentationByDefault = true,
        navigatorPlatform = "Win32",
        navigatorVendor = "Google Inc."
    ),
    MACOS_SAFARI(
        storageKey = "macos_safari",
        titleRes = R.string.profile_identity_macos_safari,
        descriptionRes = R.string.profile_identity_macos_safari_summary,
        isChromium = false,
        isMobile = false,
        clientHintPlatform = "macOS",
        platformVersion = "14.0.0",
        architecture = "x86",
        bitness = 64,
        desktopPresentationByDefault = true,
        navigatorPlatform = "MacIntel",
        navigatorVendor = "Apple Computer, Inc."
    ),
    IPHONE_IOS(
        storageKey = "iphone_ios",
        titleRes = R.string.profile_identity_iphone_ios,
        descriptionRes = R.string.profile_identity_iphone_ios_summary,
        isChromium = false,
        isMobile = true,
        clientHintPlatform = "iOS",
        platformVersion = "17.0.0",
        desktopPresentationByDefault = false,
        navigatorPlatform = "iPhone",
        navigatorVendor = "Apple Computer, Inc."
    ),
    PIXEL_10_PRO(
        storageKey = "pixel_10_pro",
        titleRes = R.string.profile_identity_pixel_10_pro,
        descriptionRes = R.string.profile_identity_pixel_10_pro_summary,
        isChromium = true,
        isMobile = true,
        clientHintPlatform = "Android",
        platformVersion = "16.0.0",
        model = "Pixel 10 Pro",
        architecture = "arm",
        bitness = 64
    ),
    GALAXY_S24_ULTRA(
        storageKey = "galaxy_s24_ultra",
        titleRes = R.string.profile_identity_galaxy_s24_ultra,
        descriptionRes = R.string.profile_identity_galaxy_s24_ultra_summary,
        isChromium = true,
        isMobile = true,
        clientHintPlatform = "Android",
        platformVersion = "14.0.0",
        model = "SM-S928B",
        architecture = "arm",
        bitness = 64
    ),
    LINUX_FIREFOX(
        storageKey = "linux_firefox",
        titleRes = R.string.profile_identity_linux_firefox,
        descriptionRes = R.string.profile_identity_linux_firefox_summary,
        isChromium = false,
        isMobile = false,
        clientHintPlatform = "Linux",
        platformVersion = "",
        architecture = "x86",
        bitness = 64,
        desktopPresentationByDefault = true,
        navigatorPlatform = "Linux x86_64",
        navigatorVendor = ""
    );

    fun userAgent(context: Context): String {
        val chromeVersion = chromeVersion(context)
        return when (this) {
            NATIVE_ANDROID ->
                "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/$chromeVersion Mobile Safari/537.36"
            WINDOWS_DESKTOP ->
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/$chromeVersion Safari/537.36"
            MACOS_SAFARI ->
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.6 Safari/605.1.15"
            IPHONE_IOS ->
                "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1"
            PIXEL_10_PRO ->
                "Mozilla/5.0 (Linux; Android 16; Pixel 10 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/$chromeVersion Mobile Safari/537.36"
            GALAXY_S24_ULTRA ->
                "Mozilla/5.0 (Linux; Android 14; SM-S928B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/$chromeVersion Mobile Safari/537.36"
            LINUX_FIREFOX ->
                "Mozilla/5.0 (X11; Linux x86_64; rv:128.0) Gecko/20100101 Firefox/128.0"
        }
    }

    /** Metadata used by WebView for navigator.userAgentData and Chromium client hints. */
    fun userAgentMetadata(context: Context): UserAgentMetadata {
        val builder = UserAgentMetadata.Builder()
            .setBrandVersionList(clientHintBrands(this, context))
            .setMobile(isMobile)
            .setPlatform(clientHintPlatform)

        if (platformVersion.isNotEmpty()) builder.setPlatformVersion(platformVersion)
        if (model.isNotEmpty() || !isChromium || this == WINDOWS_DESKTOP) builder.setModel(model)
        if (architecture.isNotEmpty()) builder.setArchitecture(architecture)
        if (bitness != UserAgentMetadata.BITNESS_DEFAULT) builder.setBitness(bitness)
        if (isChromium) builder.setWow64(false)
        return builder.build()
    }

    /**
     * Low-entropy Client Hint headers for the initial navigation. WebView continues to use its
     * own installed Chromium version; these headers do not claim to replace its rendering engine.
     */
    fun clientHintHeaders(context: Context): Map<String, String> {
        if (!isChromium) return emptyMap()
        val brands = chromiumBrands(context).joinToString(", ") { brand ->
            "\"${brand.brand}\";v=\"${brand.majorVersion}\""
        }
        return mapOf(
            "Sec-CH-UA" to brands,
            "Sec-CH-UA-Mobile" to if (isMobile) "?1" else "?0",
            "Sec-CH-UA-Platform" to "\"$clientHintPlatform\""
        )
    }

    fun presentationLayout(mode: ProfilePresentationMode): String = when (mode) {
        ProfilePresentationMode.MOBILE -> "mobile"
        ProfilePresentationMode.DESKTOP -> "desktop"
        ProfilePresentationMode.FOLLOW_PRESET -> if (desktopPresentationByDefault) "desktop" else "native"
    }

    companion object {
        val default: ProfileIdentityPreset = NATIVE_ANDROID

        fun fromStorageKey(key: String?): ProfileIdentityPreset =
            entries.firstOrNull { it.storageKey == key } ?: default

        private fun chromeVersion(context: Context): String {
            val defaultUserAgent = runCatching { WebSettings.getDefaultUserAgent(context) }.getOrDefault("")
            return Regex("Chrome/([\\d.]+)").find(defaultUserAgent)?.groupValues?.get(1) ?: "134.0.0.0"
        }

        private fun clientHintBrands(
            preset: ProfileIdentityPreset,
            context: Context
        ): List<UserAgentMetadata.BrandVersion> = when (preset) {
            MACOS_SAFARI, IPHONE_IOS -> listOf(
                UserAgentMetadata.BrandVersion.Builder()
                    .setBrand("Safari").setMajorVersion("17").setFullVersion("17.6").build()
            )
            LINUX_FIREFOX -> listOf(
                UserAgentMetadata.BrandVersion.Builder()
                    .setBrand("Firefox").setMajorVersion("128").setFullVersion("128.0").build()
            )
            else -> chromiumBrands(context)
        }

        private fun chromiumBrands(context: Context): List<UserAgentMetadata.BrandVersion> {
            val version = chromeVersion(context)
            val major = version.substringBefore('.')
            return listOf(
                UserAgentMetadata.BrandVersion.Builder()
                    .setBrand("Chromium").setMajorVersion(major).setFullVersion(version).build(),
                UserAgentMetadata.BrandVersion.Builder()
                    .setBrand("Not-A.Brand").setMajorVersion("24").setFullVersion("24.0.0.0").build(),
                UserAgentMetadata.BrandVersion.Builder()
                    .setBrand("Google Chrome").setMajorVersion(major).setFullVersion(version).build()
            )
        }
    }
}

enum class ProfilePresentationMode(
    val storageKey: String,
    @StringRes val titleRes: Int
) {
    FOLLOW_PRESET("auto", R.string.profile_presentation_follow_preset),
    MOBILE("mobile", R.string.profile_presentation_mobile),
    DESKTOP("desktop", R.string.profile_presentation_desktop);

    companion object {
        fun fromStorageKey(key: String?): ProfilePresentationMode =
            entries.firstOrNull { it.storageKey == key } ?: FOLLOW_PRESET
    }
}
