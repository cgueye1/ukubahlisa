package com.wakana.samater.controller;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.wakana.samater.dto.TicketRequest;

import com.wakana.samater.services.TicketService;


import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
public class TicketController {
  
        private final  TicketService ticketService;
        
      @PostMapping("/save")
    public ResponseEntity<?>   saveFeedback(@RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(ticketService.saveTicket(ticketRequest));
        
    }
        
     @GetMapping("/all")
      public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(ticketService.getAllTicket());
      }
      @GetMapping("/byuser/{id:.+}")
      public ResponseEntity<?> getByUser(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getAllTicketByUser(id));
      }
    
      @PutMapping("/chekck/{id:.+}")
     public ResponseEntity<?>   check(@PathVariable Long id) {
          return ResponseEntity.ok(ticketService.check(id));  
      }
     
      @GetMapping("/byid/{id:.+}")
      public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getById(id));
      }
    
  

}
