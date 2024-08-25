package com.wakana.samater.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wakana.samater.dto.ChangePasswordRequest;
import com.wakana.samater.dto.SignUpRequest;
import com.wakana.samater.model.User;
import com.wakana.samater.repository.UserRepository;
import com.wakana.samater.services.AuthenticationService;
import com.wakana.samater.services.UserSeervice;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
       	@Autowired
	      private UserRepository userRepository;
        private final  UserSeervice userSeervice;
        private final AuthenticationService authenticationService;
  
     @GetMapping("/me")
public ResponseEntity<User> getMe() {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
     String username = userDetails.getUsername();
        
        User user = userRepository.findByTelephone(username).orElse(null); // 
        return ResponseEntity.ok(user);
}


   
     @GetMapping("/all")
      public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userSeervice.getAllUsers());
      }
    

    @PostMapping("/password/change")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest  changePasswordRequest) {
    
       
        try{
          UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          String username = userDetails.getUsername();
          changePasswordRequest.setEmail(username);
        return ResponseEntity.ok(authenticationService.changePassword(changePasswordRequest));  
        }catch(Exception e){
             return ResponseEntity
                .status(HttpStatus.OK)
                .body(Collections.singletonMap("error", "Email ou mot de passe incorrecte"));  
        }
     
        
    }
   @DeleteMapping("/user/{id:.+}")
     public ResponseEntity<?>   deleteUser(@PathVariable Long id) {
         User user=  userRepository.findById(id).orElse(null);
         userRepository.delete(user);
        return ResponseEntity.ok("User deleted");
        
    }
     
      @PutMapping("/user/{id:.+}")
     public ResponseEntity<?>   putUser(@PathVariable Long id) {
          User user=  userRepository.findById(id).orElse(null);
          user.setActivated(true);
          return ResponseEntity.ok(userRepository.save(user));
        
    }
    @PutMapping("/notifiable/{id:.+}")
    public ResponseEntity<?>   setNotifiable(@PathVariable Long id) {
         User user=  userRepository.findById(id).orElse(null);
         user.setNotifiable(!user.isNotifiable());
         return ResponseEntity.ok(userRepository.save(user));
       
   }
    @PutMapping("/user/update/{id:.+}")
    public ResponseEntity<?>   update(@PathVariable Long id,@RequestBody SignUpRequest signUpRequest) {
        User user=  userRepository.findById(id).orElse(null);
        user.setTelephone(signUpRequest.getTelephone());
        user.setAdress(signUpRequest.getAdress());
        user.setDate(signUpRequest.getDate());
        user.setLieunaissance(signUpRequest.getLieunaissance());
        return ResponseEntity.ok(userRepository.save(user));
       
   }
    
    
    
    /////chat
   /*    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User addUser(
            @Payload User user
    ) {
      userSeervice.saveUser(user);
        return user;
    }*/

   /* @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(
            @Payload User user
    ) {
      userSeervice.disconnect(user.getId());
        return user;
    }*/

    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userSeervice.findConnectedUsers());
    }
    
    
    
    
    
}
