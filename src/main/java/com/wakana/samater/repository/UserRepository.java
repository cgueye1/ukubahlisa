package com.wakana.samater.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wakana.samater.model.Role;
import com.wakana.samater.model.User;

import java.util.Optional;



public interface UserRepository extends JpaRepository<User, Long> {
   
   // Optional<User>  findByEmail(String  email);
    User  findByRole(Role  role);
    Optional<User> findByTelephone(String telephone);
    
    Optional<User> findByEmail(String email);
       
    boolean existsByTelephone(String telephone);
   // boolean existsByEmail(String email);
 //  List<User> findAllByStatus(Status status);
    
}


