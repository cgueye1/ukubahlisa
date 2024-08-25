package com.wakana.samater.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wakana.samater.dto.SignUpRequest;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_ter",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "telephone", name = "unique_telephone"),
              
       }
       
       )

public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String date;
    private String lieunaissance;
    private String adress;
    private Role role;
    private boolean activated;
    private boolean notifiable;
    @Column(unique = true)
    private String telephone;
   // private Status status;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
        
    }
    @Override
    public String getUsername() {
       return telephone;
    }
    @Override
    public boolean isAccountNonExpired() {
       return true;
    }
    @Override
    public boolean isAccountNonLocked() {
       return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
       return true;
    }
    @Override
    public boolean isEnabled() {
       return true;
    }
    
    
    
    
    // Ajoutez cette méthode pour convertir un SignUpRequest en User
public static User fromSignUpRequest(SignUpRequest signUpRequest) {
    User user = new User();
    user.setNom(signUpRequest.getNom());
    user.setPrenom(signUpRequest.getPrenom());
    user.setPassword(signUpRequest.getPassword());
    user.setRole(Role.USER);
   // user.setRole(signUpRequest.getRole()=="user"?Role.USER:Role.ADMIN); 
    user.setTelephone(signUpRequest.getTelephone());
    user.setDate(signUpRequest.getDate());
    user.setLieunaissance(signUpRequest.getLieunaissance());
    user.setAdress(signUpRequest.getAdress());
    user.setNotifiable(signUpRequest.isNotifiable());
  
    return user;
}

}



/*
@ElementCollection
   @CollectionTable(name = "user_centreinteret", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "centreinteret")
    
     */