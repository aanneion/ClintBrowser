(function (config) {
    'use strict';

    function defineValue(target, key, value) {
        try {
            Object.defineProperty(target, key, {
                get: function () { return value; },
                configurable: true
            });
        } catch (e) {}
    }

    if (typeof config.platform === 'string') defineValue(navigator, 'platform', config.platform);
    if (typeof config.vendor === 'string') defineValue(navigator, 'vendor', config.vendor);

    // Safari and Firefox do not expose Chromium's User-Agent Client Hints API.
    // This is a page-compatibility signal only; network/browser implementation details may differ.
    if (!config.chromium) {
        try {
            Object.defineProperty(navigator, 'userAgentData', {
                get: function () { return undefined; },
                configurable: true
            });
        } catch (e) {}
    }

    if (config.layout !== 'desktop' && config.layout !== 'mobile') return;

    var width = config.layout === 'desktop' ? 1280 : 390;
    var mobile = config.layout === 'mobile';
    defineValue(window, 'innerWidth', width);
    defineValue(window, 'outerWidth', width);
    defineValue(screen, 'width', width);
    defineValue(screen, 'availWidth', width);
    defineValue(navigator, 'maxTouchPoints', mobile ? 5 : 0);

    if (!mobile) {
        try { delete window.ontouchstart; } catch (e) {}
        try { delete window.ontouchmove; } catch (e) {}
        try { delete window.ontouchend; } catch (e) {}
        try { delete window.ontouchcancel; } catch (e) {}
        try { delete window.TouchEvent; } catch (e) {}
    }

    try {
        var originalMatchMedia = window.matchMedia.bind(window);
        window.matchMedia = function (query) {
            var normalized = (query || '').replace(/\s/g, '').toLowerCase();
            var forcedMatch = null;

            if (normalized.indexOf('pointer:coarse') !== -1 || normalized.indexOf('any-pointer:coarse') !== -1) {
                forcedMatch = mobile;
            } else if (normalized.indexOf('pointer:fine') !== -1 || normalized.indexOf('any-pointer:fine') !== -1) {
                forcedMatch = !mobile;
            } else if (normalized.indexOf('hover:none') !== -1 || normalized.indexOf('any-hover:none') !== -1) {
                forcedMatch = mobile;
            } else if (normalized.indexOf('hover:hover') !== -1 || normalized.indexOf('any-hover:hover') !== -1) {
                forcedMatch = !mobile;
            } else {
                var maxWidth = normalized.match(/max-(?:device-)?width:(\d+(?:\.\d+)?)px/);
                var minWidth = normalized.match(/min-(?:device-)?width:(\d+(?:\.\d+)?)px/);
                if (maxWidth) forcedMatch = width <= parseFloat(maxWidth[1]);
                else if (minWidth) forcedMatch = width >= parseFloat(minWidth[1]);
            }

            if (forcedMatch === null) return originalMatchMedia(query);
            return {
                matches: forcedMatch,
                media: query,
                onchange: null,
                addListener: function () {},
                removeListener: function () {},
                addEventListener: function () {},
                removeEventListener: function () {},
                dispatchEvent: function () { return false; }
            };
        };
    } catch (e) {}

    function setViewport() {
        try {
            var meta = document.querySelector('meta[name="viewport"]');
            if (meta) {
                meta.content = 'width=' + width;
            } else if (document.head) {
                meta = document.createElement('meta');
                meta.name = 'viewport';
                meta.content = 'width=' + width;
                document.head.insertBefore(meta, document.head.firstChild);
            }
        } catch (e) {}
    }

    setViewport();
    document.addEventListener('DOMContentLoaded', setViewport);
    if (document.documentElement && typeof MutationObserver !== 'undefined') {
        try {
            new MutationObserver(setViewport).observe(document.documentElement, {
                childList: true,
                subtree: true
            });
        } catch (e) {}
    }
})(__CLINT_PROFILE_IDENTITY_CONFIG__);
