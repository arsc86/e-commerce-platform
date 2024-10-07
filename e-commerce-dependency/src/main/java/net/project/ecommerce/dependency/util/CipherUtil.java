package net.project.ecommerce.dependency.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.project.ecommerce.dependency.configuration.SecurityTokenKeysConfig;

@Service
public class CipherUtil {
	
	@Autowired
	private SecurityTokenKeysConfig config;
	
	private SecretKeySpec encryptKey() throws NoSuchAlgorithmException {
        byte[] claveEncriptacion = config.getCypherKey().getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        claveEncriptacion = sha.digest(claveEncriptacion);
        claveEncriptacion = Arrays.copyOf(claveEncriptacion, 16); // AES-128
        return new SecretKeySpec(claveEncriptacion, "AES");
    }

    /**
     * Encrypt a string
     *
     * @param paraEncriptar {@link String}
     * @return {@link String}
     */
    public String encrypt(String paraEncriptar) throws Exception {
        byte[] textoDesen = paraEncriptar.getBytes(StandardCharsets.UTF_8);
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
   
        byte[] iv = new byte[12];
        KeyGenerator.getInstance("AES").generateKey().getEncoded();
        cipher.init(Cipher.ENCRYPT_MODE, encryptKey(), new GCMParameterSpec(128, iv));

        byte[] cryptText = cipher.doFinal(textoDesen);

        //Create a mixed message with IV and crypText
        byte[] message = new byte[iv.length + cryptText.length];
        System.arraycopy(iv, 0, message, 0, iv.length);
        System.arraycopy(cryptText, 0, message, iv.length, cryptText.length);

        // Base64 encoder
        return Base64.getUrlEncoder().withoutPadding().encodeToString(message);
    }

    /**
     * Decrypt a string
     * 
     * @author Allan Suarez <mailto:arsc86@gmail.com>
     *
     * @param encriptado {@link String}
     * @return {@link String}
     */
    public String decrypt(String cryptText) throws Exception {
        // Decode using Base64
        byte[] bytesEncriptados = Base64.getUrlDecoder().decode(cryptText);

        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Extract IV of the first 12 bytes
        GCMParameterSpec params = new GCMParameterSpec(128, bytesEncriptados, 0, 12);
        cipher.init(Cipher.DECRYPT_MODE, encryptKey(), params);

        // Decrypt (after IV)
        byte[] decryptedText = cipher.doFinal(bytesEncriptados, 12, bytesEncriptados.length - 12);

        return new String(decryptedText, StandardCharsets.UTF_8);
    }	
}
