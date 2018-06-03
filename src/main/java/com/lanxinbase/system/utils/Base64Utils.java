package com.lanxinbase.system.utils;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

/**
 * out(Base64Utils.encode("abc"));
 * out(Base64Utils.decode("YWJj"));
 *
 * out(Base64Utils.encodeUrlSafe("alan"));
 * out(Base64Utils.decodeUrlSafe("YWxhbg=="));
 */
public class Base64Utils {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Base64Delegate delegate;

    public Base64Utils() {
    }

    private static void assertDelegateAvailable() {
        ObjectUtils.notNull(delegate, "Neither Java 8 nor Apache Commons Codec found - Base64 encoding between byte arrays not supported");
    }

    public static String encode(String src) {
        assertDelegateAvailable();
        return new String(delegate.encode(src.getBytes()));
    }

    public static String decode(String src) {
        assertDelegateAvailable();
        return new String(delegate.decode(src.getBytes()));
    }

    public static String encodeUrlSafe(String src) {
        assertDelegateAvailable();
        return new String(delegate.encodeUrlSafe(src.getBytes()));
    }

    public static String decodeUrlSafe(String src) {
        assertDelegateAvailable();
        return new String(delegate.decodeUrlSafe(src.getBytes()));
    }

    private static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            classLoader.loadClass(className);
            return true;
        } catch (ClassNotFoundException var9) {
            var9.printStackTrace();
        }
        return false;
    }

    static {
        Base64Delegate delegateToUse = null;
        if (isPresent("java.util.Base64", Base64Utils.class.getClassLoader())) {
            delegateToUse = new JdkBase64Delegate();
        } else if (isPresent("org.apache.commons.codec.binary.Base64", Base64Utils.class.getClassLoader())) {
            delegateToUse = new CommonsCodecBase64Delegate();
        }

        delegate =delegateToUse;
    }

    static class CommonsCodecBase64Delegate implements Base64Delegate {
        private final Base64 base64 = new Base64();
        private final Base64 base64UrlSafe = new Base64(0, (byte[])null, true);

        CommonsCodecBase64Delegate() {
        }

        public byte[] encode(byte[] src) {
            return this.base64.encode(src);
        }

        public byte[] decode(byte[] src) {
            return this.base64.decode(src);
        }

        public byte[] encodeUrlSafe(byte[] src) {
            return this.base64UrlSafe.encode(src);
        }

        public byte[] decodeUrlSafe(byte[] src) {
            return this.base64UrlSafe.decode(src);
        }
    }

    static class JdkBase64Delegate implements Base64Delegate {
        JdkBase64Delegate() {

        }

        public byte[] encode(byte[] src) {
            return src != null && src.length != 0 ? java.util.Base64.getEncoder().encode(src) : src;
        }

        public byte[] decode(byte[] src) {
            return src != null && src.length != 0 ? java.util.Base64.getDecoder().decode(src) : src;
        }

        public byte[] encodeUrlSafe(byte[] src) {
            return src != null && src.length != 0 ? java.util.Base64.getUrlEncoder().encode(src) : src;
        }

        public byte[] decodeUrlSafe(byte[] src) {
            return src != null && src.length != 0 ? java.util.Base64.getUrlDecoder().decode(src) : src;
        }
    }

    interface Base64Delegate {
        byte[] encode(byte[] var1);

        byte[] decode(byte[] var1);

        byte[] encodeUrlSafe(byte[] var1);

        byte[] decodeUrlSafe(byte[] var1);
    }

}
