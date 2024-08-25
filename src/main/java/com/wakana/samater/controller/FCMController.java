package com.wakana.samater.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wakana.samater.dto.Note;
import com.wakana.samater.services.FirebaseMessagingService;
import com.wakana.samater.util.PayementUtil;

import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FCMController {
    private final FirebaseMessagingService  firebaseService;

    
    @PostMapping("/send/{token:.+}")
   @ResponseBody
   public String sendNotification(@RequestBody Note note,
                              @PathVariable String token) throws FirebaseMessagingException {
    return firebaseService.sendNotification(note, token);
    } 
      @GetMapping("/paye")
     public ResponseEntity<?>   getAllCategorie() {
        PayementUtil.payer();
        return ResponseEntity.ok("");
        
    }

    
}
