package com.wakana.realestateworks.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wakana.realestateworks.dto.SignUpRequest;
import com.wakana.realestateworks.dto.response.AssignedCompanyDto;
import com.wakana.realestateworks.dto.response.UserFullDto;
import com.wakana.realestateworks.dto.response.UserResponseDto;
import com.wakana.realestateworks.enums.ProfilEnum;
import com.wakana.realestateworks.util.FileTransferUtil;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
      @UniqueConstraint(columnNames = "telephone", name = "unique_telephone"),
      @UniqueConstraint(columnNames = "email", name = "unique_email"),

})

public class User implements UserDetails {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String nom;
   private String prenom;
   private String email;
   private String password;
   private String adress;
   private String technicalSheet;
   private ProfilEnum profil;
   private boolean activated;
   private boolean notifiable;
   @Column(unique = true)
   private String telephone;
   @JsonIgnore
   @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JsonManagedReference
   private Subscription subscription;

   private LocalDateTime createdAt;
   private double funds;

   @JsonIgnore
   @ManyToMany(mappedBy = "owners")
   private Set<RealEstateProperty> properties;

   private double note = 0.0;

   private String photo;
   private String idCard;

   @JsonIgnore
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "assigned_company_id", nullable = true)
   private RealEstateProperty assignedCompany;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "manager_id")
   @JsonIgnore
   private User manager;

   private String dateOfBirth;
   private String qrcode;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority(profil.name()));

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

   public static User fromSignUpRequest(SignUpRequest signUpRequest) {

      ProfilEnum profilEnum;
      try {
         profilEnum = ProfilEnum.valueOf(signUpRequest.getProfil());
      } catch (IllegalArgumentException e) {
         throw new IllegalArgumentException("Invalid role: " + signUpRequest.getProfil());
      }

      User user = new User();

      user.setNom(signUpRequest.getNom());
      user.setPrenom(signUpRequest.getPrenom());
      user.setPassword(signUpRequest.getPassword());
      user.setProfil(profilEnum);
      user.setEmail(signUpRequest.getEmail());
      user.setTelephone(signUpRequest.getTelephone());
      user.setAdress(signUpRequest.getAdress());
      user.setNotifiable(signUpRequest.isNotifiable());
      user.setActivated(true);
      user.setCreatedAt(LocalDateTime.now());
      user.setFunds(0);
      user.setDateOfBirth(signUpRequest.getDate());

      try {
         String photo = FileTransferUtil.handleFileUpload(signUpRequest.getPhoto());
         user.setPhoto(photo);

      } catch (IOException e) {

      }

      return user;

   }

   @Override
   public int hashCode() {
      return Objects.hash(id); // Example; adjust based on your properties
   }

   public UserResponseDto convertToUserResponseDto() {
      return new UserResponseDto(
            this.getId(),
            this.getPrenom(),
            this.getNom(),
            this.getTelephone(), this.getPhoto(), this.getProfil());
   }

   public UserFullDto toFullDto() {
      AssignedCompanyDto companyDto = null;
      if (this.assignedCompany != null) {
         companyDto = new AssignedCompanyDto(
               this.assignedCompany.getId(),
               this.assignedCompany.getName());
      }

      return new UserFullDto(
            this.id,
            this.nom,
            this.prenom,
            this.email,
            this.adress,
            this.telephone,
            this.profil,
            this.activated,
            this.notifiable,
            this.funds,
            this.note,
            this.photo,
            this.idCard,
            this.dateOfBirth,
            this.qrcode,
            companyDto);
   }

}
