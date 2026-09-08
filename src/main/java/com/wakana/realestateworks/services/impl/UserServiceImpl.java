package com.wakana.realestateworks.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wakana.realestateworks.dto.ChangeProfilRequest;
import com.wakana.realestateworks.dto.TechnicalSheetRequest;
import com.wakana.realestateworks.dto.UpdateUserRequest;
import com.wakana.realestateworks.dto.response.ProfilDistributionDTO;
import com.wakana.realestateworks.enums.ProfilEnum;

import com.wakana.realestateworks.model.User;

import com.wakana.realestateworks.repository.RealEstatePropertyRepository;
import com.wakana.realestateworks.repository.UserRepository;
import com.wakana.realestateworks.services.AuthenticationService;
import com.wakana.realestateworks.services.UserSeervice;
import com.wakana.realestateworks.util.FileTransferUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserSeervice {
  private final UserRepository userRepository;

  // private final AuthenticationService authenticationService;

  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {

      @Override
      public UserDetails loadUserByUsername(String username) {
        return userRepository.findByTelephone(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not foound "));
      }
    };
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();

  }

  @Override
  public Page<User> findByNomContainingOrPrenomContaining(String keyword, Pageable pageable) {
    return userRepository.findByNomContainingOrPrenomContainingAndProfilNot(keyword, keyword, ProfilEnum.PROMOTEUR,
        pageable);
  }

  @Override
  public User getUser(Long id) {

    return userRepository.findById(id).orElse(null);
  }

  @Override
  public User update(Long id, UpdateUserRequest signUpRequest) {
    User user = userRepository.findById(id).orElse(null);

    ProfilEnum profilEnum;
    try {
      profilEnum = ProfilEnum.valueOf(signUpRequest.getProfil());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid role: " + signUpRequest.getProfil());
    }

    try {
      String photo = FileTransferUtil.handleFileUpload(signUpRequest.getPhoto());
      user.setPhoto(photo);
      

    } catch (IOException e) {

    }

    user.setNom(signUpRequest.getNom());
    user.setPrenom(signUpRequest.getPrenom());
    user.setProfil(profilEnum);
    user.setEmail(signUpRequest.getEmail());
    user.setTelephone(signUpRequest.getTelephone());
    user.setDateOfBirth(signUpRequest.getDate());

    return userRepository.save(user);
  }

  public Page<User> findAll(Pageable pageable) {
    // Liste des profils que nous voulons récupérer
    List<ProfilEnum> profils = List.of(
        ProfilEnum.PROMOTEUR,
        ProfilEnum.NOTAIRE,
        ProfilEnum.BANK,
        ProfilEnum.AGENCY,
        ProfilEnum.ADMIN);

    // Utilisation de la méthode findByProfilIn pour récupérer les utilisateurs avec
    // ces profils
    return userRepository.findByProfilIn(pageable, profils);
  }

  public List<User> getNotaires() {
    return userRepository.findByProfil(ProfilEnum.NOTAIRE);
  }

  @Override
  public User addTechnicalSheet(TechnicalSheetRequest request) {
    User user = userRepository.findById(request.getId()).orElse(null);
    user.setTechnicalSheet(request.getSheetName());
    return userRepository.save(user);

  }

  public long getUserCountByProfil(ProfilEnum profil) {
    return userRepository.countByProfil(profil);
  }


  @Override
  public Set<User> findOwnersByPropertyId(Long propertyId) {
    return userRepository.findOwnersByPropertyId(propertyId);
  }

  
  public Double getUserRating(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
    return user.getNote();
  }

  @Override
  public User updatePhoto(Long id, String photo) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
    user.setPhoto(photo);

    return userRepository.save(user);

  }

  @Override
  public User updateIdcard(Long id, String idCard) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
    user.setIdCard(idCard);

    return userRepository.save(user);
  }

  // admin

  @Override
  public Page<User> getUsersByProfil(ProfilEnum profil, String keyword, Pageable pageable) {
    return userRepository.findByProfilAndKeyword(profil, keyword, pageable);
  }

  @Override
  public Page<User> findByProfil(ProfilEnum profil, Pageable pageable) {
    return userRepository.findByProfil(profil, pageable);
  }

  @Override
  public Page<User> findByNomOrPrenomAndProfil(String keyword, ProfilEnum profil, Pageable pageable) {
    return userRepository.findByProfilAndNomContainingIgnoreCaseOrProfilAndPrenomContainingIgnoreCase(
        profil, keyword, profil, keyword, pageable);
  }

  public List<ProfilDistributionDTO> getProfilDistribution() {

    List<Object[]> rawData = userRepository.countUsersByProfil();

    Map<ProfilEnum, Long> countMap = new HashMap<>();
    for (Object[] row : rawData) {
      ProfilEnum profil = (ProfilEnum) row[0];
      Long count = (Long) row[1];
      countMap.put(profil, count);
    }

    List<ProfilDistributionDTO> result = new ArrayList<>();
    for (ProfilEnum profilEnum : ProfilEnum.values()) {
      long count = countMap.getOrDefault(profilEnum, 0L);
      result.add(new ProfilDistributionDTO(profilEnum.name(), count));
    }

    return result;
  }

  @Override
  public void addRating(Long userId, Long propertyId, Integer score, String comment) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addRating'");
  }

  @Override
  public List<User> searchUsersByCompanyAndProfile(String keyword) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'searchUsersByCompanyAndProfile'");
  }

  @Override
  public List<User> searchUsersByCompanyAndProfileAgency(String keyword) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'searchUsersByCompanyAndProfileAgency'");
  }
  
  
  
   @Override
    public User changeProfil(Long userId, ChangeProfilRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Vérifier que le profil est bien autorisé
        switch (request.getProfil()) {
            case PROMOTEUR:
            case SITE_MANAGER:
            case WORKER:
                user.setProfil(request.getProfil());
                break;
            default:
                throw new IllegalArgumentException("Profil non autorisé pour la modification");
        }

        return userRepository.save(user);
    }

}
