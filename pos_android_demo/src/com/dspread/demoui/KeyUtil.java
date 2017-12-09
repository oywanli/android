package com.dspread.demoui;

import javax.crypto.spec.*;
import javax.crypto.*;
import java.util.*;
import android.annotation.SuppressLint;
import java.security.*;

public class KeyUtil
{
    private static final SecureRandom RANDOM;
    
    static {
        RANDOM = new SecureRandom();
    }
    
    @SuppressLint("NewApi")
	public static byte[] generateKvc(final byte[] array) throws GeneralSecurityException {
        byte[] array2 = null;
        String s2 = null;
        switch (array.length) {
            default: {
                throw new IllegalArgumentException("la longitud de la clave no es valida");
            }
            case 24: {
                final String s = "DESede";
                array2 = new byte[24];
                System.arraycopy(array, 0, array2, 0, array.length);
                s2 = s;
                break;
            }
            case 16: {
                final String s3 = "DESede";
                array2 = new byte[24];
                System.arraycopy(array, 0, array2, 0, array.length);
                System.arraycopy(array, 0, array2, 16, 8);
                s2 = s3;
                break;
            }
            case 8: {
                final String s4 = "DES";
                array2 = new byte[8];
                System.arraycopy(array, 0, array2, 0, array.length);
                s2 = s4;
                break;
            }
        }
        final SecretKeySpec secretKeySpec = new SecretKeySpec(array2, s2);
        final Cipher instance = Cipher.getInstance(s2 + "/ECB/NoPadding");
        instance.init(1, secretKeySpec);
        return Arrays.copyOfRange(instance.doFinal(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }), 0, 3);
    }
    
    public static byte[] generateRandomKey(final int n) {
        final byte[] array = new byte[n];
        KeyUtil.RANDOM.nextBytes(array);
        return array;
    }
}
