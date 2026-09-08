package com.wakana.realestateworks.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class QRCodeUtil {

    // Clé secrète en Base64 (32 octets pour AES-256)
    private static final String SECRET_KEY = "NCO6RvEm3uAb774wLcWgCSA65r4/ivwyblwQuG6pY/g=";
    private static final String ALGORITHM = "AES";

    /**
     * Génère le contenu du QR code en chiffrant les données uniques (id, etc.)
     */
    public static String generateQrCode(String realEstateId, String otherInfo) {
        try {
            String data = realEstateId + "|" + otherInfo; // Séparer les infos par un délimiteur

            // Décoder la clé Base64
            byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);

            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Déchiffre le QR code pour récupérer l'ID et autres infos
     */
    public static String[] decodeQrCode(String qrCode) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);

            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decodedBytes = Base64.getDecoder().decode(qrCode);
            String decryptedData = new String(cipher.doFinal(decodedBytes), StandardCharsets.UTF_8);

            return decryptedData.split("\\|"); // [id, otherInfo]
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
