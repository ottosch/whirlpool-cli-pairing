package com.samourai.wallet;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * RandomUtil - code from ExtLibJ.
 * @author Samourai Wallet
 * {@link} https://code.samourai.io/wallet/ExtLibJ/-/blob/master/java/com/samourai/wallet/util/RandomUtil.java
 *
 */
public class RandomUtil {
    private static RandomUtil instance = null;
    private static final SecureRandom secureRandom = new SecureRandom();

    public static RandomUtil getInstance() {
        if(instance == null) {
            instance = new RandomUtil();
        }
        return instance;
    }

    public static SecureRandom getSecureRandom() {
        return secureRandom;
    }

    public byte[] nextBytes(int length) {
        byte b[] = new byte[length];
        secureRandom.nextBytes(b);
        return b;
    }

    public static int random(int minInclusive, int maxInclusive) {
        return ThreadLocalRandom.current().nextInt(minInclusive, maxInclusive + 1);
    }

    public static long random(long minInclusive, long maxInclusive) {
        return ThreadLocalRandom.current().nextLong(minInclusive, maxInclusive + 1);
    }

}
