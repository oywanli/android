package com.dspread.demoui;

import android.annotation.SuppressLint;
import java.nio.charset.*;
import java.util.*;

public final class HexUtil
{
    @SuppressLint("NewApi")
	public static byte[] hex2byte(final String s, final Charset charset) {
        if (s.length() % 2 == 0) {
            return hex2byte(s.getBytes(charset), 0, s.length() >> 1);
        }
        return hex2byte("0" + s, charset);
    }
    
    public static byte[] hex2byte(final byte[] array, final int n, final int n2) {
        final byte[] array2 = new byte[n2];
        for (int i = 0; i < n2 * 2; ++i) {
            int n3;
            if (i % 2 == 0) {
                n3 = 4;
            }
            else {
                n3 = 0;
            }
            final int n4 = i >> 1;
            array2[n4] |= (byte)(Character.digit((char)array[n + i], 16) << n3);
        }
        return array2;
    }
    
    public static String hexStringFromBytes(final byte[] array) {
        return hexStringFromBytes(array, true);
    }
    
    public static String hexStringFromBytes(final byte[] array, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Character.forDigit((array[i] & 0xFF) / 16, 16));
            sb.append(Character.forDigit((array[i] & 0xFF) % 16, 16));
        }
        if (b) {
            return sb.toString().toUpperCase(Locale.getDefault());
        }
        return sb.toString();
    }
    
    public static int hexToInt(final byte[] array) {
        int n = 0;
        for (int i = 0; i < array.length; ++i) {
            n = (n << 8) + (array[i] & 0xFF);
        }
        return n;
    }
    
    public static long hexToLong(final byte[] array) {
        long n = 0L;
        for (int i = 0; i < array.length; ++i) {
            n = (n << 8) + (array[i] & 0xFF);
        }
        return n;
    }
}
