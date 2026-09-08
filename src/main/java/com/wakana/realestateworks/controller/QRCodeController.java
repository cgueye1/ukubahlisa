package com.wakana.realestateworks.controller;

import com.wakana.realestateworks.dto.innov.QrCodeRequest;
import com.wakana.realestateworks.util.QRCodeUtil1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller pour la gestion des QR Codes (génération et décodage).
 */
@RestController
@RequestMapping("/api/qrcode")
public class QRCodeController {
    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody QrCodeRequest request) {
        String qrCode = QRCodeUtil1.generateQrCode(request.getText());
        return ResponseEntity.ok(qrCode);
    }

    @PostMapping("/decode")
    public ResponseEntity<String> decode(@RequestBody QrCodeRequest request) {
        String decodedText = QRCodeUtil1.decodeQrCode(request.getText());
        return ResponseEntity.ok(decodedText);
    }
}
