package com.manager.password.security;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Enumeration;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PasswordSecurity {

    public static final String SEC_KEY = "sec_key";
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    public static String ALGO = KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7;
    public static SecretKey generateKey() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(SEC_KEY, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC).setRandomizedEncryptionRequired(false)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(false).build();
        keyGenerator.init(keyGenParameterSpec);
        return keyGenerator.generateKey() ;
    }

    public static byte[] getIVSecureRandom(String algorithm) throws NoSuchAlgorithmException, NoSuchPaddingException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] iv = new byte[Cipher.getInstance(algorithm).getBlockSize()];
        random.nextBytes(iv);
        return iv;
    }
    public static byte[] encryptMessage(byte[] message,byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnrecoverableEntryException, CertificateException, KeyStoreException, IOException, InvalidAlgorithmParameterException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE,getSecretKey(),new IvParameterSpec(iv));
        return cipher.doFinal(message);
    }

    public static SecretKey getSecretKey() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableEntryException, InvalidAlgorithmParameterException, NoSuchProviderException {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
        KeyStore.Entry secretKeyEntry = keyStore.getEntry(SEC_KEY, null);
        if(Objects.isNull(secretKeyEntry)){
          return  generateKey();
        }
        return (SecretKey) keyStore.getKey(SEC_KEY,null);
    }

    public static String decryptMessage(byte[] encryptedText,byte[] iv) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, IOException, UnrecoverableEntryException, CertificateException, KeyStoreException, InvalidAlgorithmParameterException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.DECRYPT_MODE,getSecretKey(), new IvParameterSpec(iv));
        return new String(cipher.doFinal(encryptedText), StandardCharsets.UTF_8);
    }
}
