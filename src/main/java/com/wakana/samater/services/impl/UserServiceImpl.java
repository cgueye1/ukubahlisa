package com.wakana.samater.services.impl;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wakana.samater.model.User;
import com.wakana.samater.repository.UserRepository;
import com.wakana.samater.services.UserSeervice;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserSeervice{
    private final UserRepository userRepository;
    
    
      public UserDetailsService userDetailsService() {
        return new UserDetailsService(){
            
            @Override
            public UserDetails loadUserByUsername(String username){
                return  userRepository.findByTelephone(username)
                   .orElseThrow(() -> new UsernameNotFoundException("User not foound "));
            }
        };
    }
  
    public List<User>  getAllUsers(){
        return userRepository.findAll();
       
    }

    @Override
    public List<User> findConnectedUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findConnectedUsers'");
    }
     /* public List<User>  getAllUsersActivated(boolean activated){
        
        return userRepository.findByActivated(activated);
        
    }*/
     
   /*  public User  disconnect(Long id) {
        var storedUser =userRepository.findById(id).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            userRepository.save(storedUser);
            return userRepository.save(storedUser);
        }
        return storedUser; 
        
    }*/

   /* public List<User> findConnectedUsers() {
        return userRepository.findAllByStatus(Status.ONLINE);
    }*/

 
    
}
