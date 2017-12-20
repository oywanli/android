package com.dspread.demoui;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by x1Mexico on 14/12/2017.
 */

public class DESUtil {
    private static final String ALGORITHM = "DESede";
    private static final String TRANSFORMATION = "DESede/ECB/NoPadding";

    public static byte[] decryptDES(final byte[] hexaKey, final byte[] array) throws GeneralSecurityException {
        final Cipher instance = Cipher.getInstance(TRANSFORMATION);
        instance.init(2, new SecretKeySpec(hexaKey, ALGORITHM));
        return instance.doFinal(array);
    }

}
