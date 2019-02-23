package com.bosto.atmapi.common;

import java.util.Random;

public class RandomUtils {
    final static char[] chars = new char[] {'1','2','3','4','5','6','7','8','9','0','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    final static int lowercase = 'a';
    final static int uppercase = 'A';
    final static Random random  = new Random();

    public static String password(Integer port) {
        String pwd = "" + (char) (lowercase + random.nextInt() % 26) +
                (char) (uppercase + random.nextInt() % 26) + (char) (uppercase + random.nextInt() % 26) + port;
        return pwd;
    }

    public static String otp() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = Math.abs(random.nextInt() % 10);
            sb.append(chars[index]);
        }

        return sb.toString();

    }

    public static String accountNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(chars[Math.abs(random.nextInt() % 10)]);
        }
        return sb.toString();

    }

    public static String atm() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(chars[Math.abs(random.nextInt() % 36)]);
        }
        return "ATM" + sb.toString();

    }

}
