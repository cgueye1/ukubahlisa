package com.wakana.samater.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.wakana.samater.model.User;

public interface UserSeervice {
    
    UserDetailsService userDetailsService();
    List<User>  getAllUsers();
  //  User  disconnect(Long id) ;
    List<User> findConnectedUsers();
   // List<User>  getAllUsersActivated(boolean activated);
}
