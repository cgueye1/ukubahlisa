package com.wakana.realestateworks.util;


import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class QRCodeUtil1 {
    //https://innovimpactafrica.com/NCO6RvEm3uAb774wLcWgCSA65r4/ivwyblwQuG6pY/g=

    // Clé secrète en Base64 (32 octets pour AES-256)
    private static final String SECRET_KEY = "NCO6RvEm3uAb774wLcWgCSA65r4/ivwyblwQuG6pY/g=";
    private static final String ALGORITHM = "AES";

    /**
     * Génère un QR code à partir d'un texte
     */
    public static String generateQrCode(String text) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Déchiffre un QR code (texte chiffré) pour retrouver le texte original
     */
    public static String decodeQrCode(String qrCode) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decodedBytes = Base64.getDecoder().decode(qrCode);
            return new String(cipher.doFinal(decodedBytes), StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
