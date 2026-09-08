package com.wakana.realestateworks.controller;

import java.io.IOException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wakana.realestateworks.dto.ChangePasswordRequest;
import com.wakana.realestateworks.dto.ChangeProfilRequest;
import com.wakana.realestateworks.dto.SignUpRequest;
import com.wakana.realestateworks.dto.TechnicalSheetRequest;
import com.wakana.realestateworks.dto.UpdateUserRequest;
import com.wakana.realestateworks.dto.response.ProfilDistributionDTO;
import com.wakana.realestateworks.dto.response.UserFullDto;
import com.wakana.realestateworks.enums.ProfilEnum;
import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.repository.SubscriptionPlanRepository;
import com.wakana.realestateworks.repository.UserRepository;
import com.wakana.realestateworks.services.AuthenticationService;
import com.wakana.realestateworks.services.SubscriptionService;
import com.wakana.realestateworks.services.UserSeervice;
import com.wakana.realestateworks.util.FileTransferUtil;

import io.swagger.v3.oas.annotations.Hidden;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
  @Autowired
  private UserRepository userRepository;
  private final UserSeervice userSeervice;
  private final AuthenticationService authenticationService;
  private final SubscriptionService subscriptionService;

  private final SubscriptionPlanRepository subscriptionPlanRepository;

  @Hidden
  @PostMapping("/save")
  public ResponseEntity<?> save(@RequestBody SignUpRequest signUpRequest) {
    try {
      User savedUser = authenticationService.create(signUpRequest);
      return ResponseEntity.ok(savedUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/me")
  public ResponseEntity<UserFullDto> getMe() {
    // Récupérer l'utilisateur connecté
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = userDetails.getUsername();

    // Chercher l'utilisateur en base
    User user = userRepository.findByTelephone(username)
        .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

    // Convertir en DTO et retourner
    UserFullDto userDto = user.toFullDto();
    return ResponseEntity.ok(userDto);
  }

  @Hidden
  @GetMapping("/all")
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(userSeervice.getAllUsers());
  }

  @Hidden
  @PostMapping("/password/change")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {

    try {
      UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      String username = userDetails.getUsername();
      changePasswordRequest.setEmail(username);
      return ResponseEntity.ok(authenticationService.changePassword(changePasswordRequest));
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(Collections.singletonMap("error", "Email ou mot de passe incorrecte"));
    }

  }

  @DeleteMapping("/{id:.+}")
  public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    User user = userRepository.findById(id).orElse(null);
    userRepository.delete(user);
    return ResponseEntity.ok("User deleted");

  }

  @Hidden
  @PutMapping("/{id:.+}")
  public ResponseEntity<?> putUser(@PathVariable Long id) {
    User user = userRepository.findById(id).orElse(null);
    user.setActivated(!user.isActivated());
    return ResponseEntity.ok(userRepository.save(user));

  }

  @Hidden
  @PutMapping("/notifiable/{id:.+}")
  public ResponseEntity<?> setNotifiable(@PathVariable Long id) {
    User user = userRepository.findById(id).orElse(null);
    user.setNotifiable(!user.isNotifiable());
    return ResponseEntity.ok(userRepository.save(user));

  }

  // @Hidden
  // @PreAuthorize("hasAuthority('ADMIN')")

  @PutMapping(value = "/update/{id:.+}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> update(@PathVariable Long id, @ModelAttribute UpdateUserRequest signUpRequest) {

    return ResponseEntity.ok(userSeervice.update(id, signUpRequest));

  }

  @GetMapping("/{id:.+}")
  public ResponseEntity<?> getUser(@PathVariable Long id) {
    return ResponseEntity.ok(userSeervice.getUser(id));
  }

  @Hidden
  @GetMapping("/notaires")
  public List<User> getNotaires() {
    return userSeervice.getNotaires();
  }

  @Hidden
  @PutMapping("/technical-sheet")
  public ResponseEntity<?> addSheet(@ModelAttribute TechnicalSheetRequest request) {
    try {

      String sheetName = FileTransferUtil.handleFileUpload(request.getFile());
      request.setSheetName(sheetName);
      User user = userSeervice.addTechnicalSheet(request);
      return ResponseEntity.ok(user);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Échec de la mise à jour : " + e.getMessage());
    }
  }

  @Hidden
  @GetMapping("/user-count")
  public ResponseEntity<Long> getUserCountByProfil(@RequestParam("profil") ProfilEnum profil) {
    // long count = userSeervice.getUserCountByProfil(profil);
    return ResponseEntity.ok(userSeervice.getUserCountByProfil(profil));
  }

  @Hidden
  @GetMapping("/search/bank")
  public ResponseEntity<List<User>> searchUsersByCompanyAndProfile(@RequestParam String keyword) {
    List<User> users = userSeervice.searchUsersByCompanyAndProfile(keyword);
    return ResponseEntity.ok(users);
  }

  @Hidden
  @GetMapping("/search/agency")
  public ResponseEntity<List<User>> searchUsersByCompanyAndProfileAgency(@RequestParam String keyword) {
    List<User> users = userSeervice.searchUsersByCompanyAndProfileAgency(keyword);
    return ResponseEntity.ok(users);
  }

  @Hidden
  @PostMapping("/photo/{id:.+}")
  public ResponseEntity<?> updatePhoto(@ModelAttribute SignUpRequest request, @PathVariable Long id) {
    try {
      String file = FileTransferUtil.handleFileUpload(request.getPhoto());
      return ResponseEntity.ok(userSeervice.updatePhoto(id, file));
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to update files: " + e.getMessage());
    }
  }

  @Hidden
  @PostMapping("/idcard/{id:.+}")
  public ResponseEntity<?> updateidCard(@ModelAttribute SignUpRequest request, @PathVariable Long id) {
    try {
      String file = FileTransferUtil.handleFileUpload(request.getPhoto());
      return ResponseEntity.ok(userSeervice.updateIdcard(id, file));
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to update files: " + e.getMessage());
    }
  }

  @Hidden
  // copropriete
  @GetMapping("/{id}/owners")
  public ResponseEntity<Set<User>> getOwners(@PathVariable Long id) {
    Set<User> owners = userSeervice.findOwnersByPropertyId(id);
    return owners.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(owners);
  }

  @Hidden
  /////// ratings///////////:
  @PostMapping("/{userId}/rate")
  public ResponseEntity<String> rateUser(@PathVariable Long userId,
      @RequestParam Integer score,
      @RequestParam Long jobRequestId,

      @RequestParam(required = false) String comment) {
    userSeervice.addRating(userId, jobRequestId, score, comment);
    return ResponseEntity.ok("La note a été ajoutée avec succès.");
  }

  @Hidden
  @GetMapping("/{userId}/rating")
  public ResponseEntity<Double> getUserRating(@PathVariable Long userId) {
    Double rating = userSeervice.getUserRating(userId);
    return ResponseEntity.ok(rating);
  }

  /// @Hidden admin
  @GetMapping("/by-profil")
  public Page<User> getUsersByProfil(
      @RequestParam ProfilEnum profil,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return userSeervice.getUsersByProfil(profil, keyword, pageable);
  }

  // @Hidden
  // espace admin ____ admin _____ admin
  // @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/search")
  public ResponseEntity<Page<User>> search(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String profil,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size);
    Page<User> userPage;
    ProfilEnum profilEnum = null;

    // Conversion du profil en enum si présent
    if (profil != null && !profil.isBlank()) {
      try {
        profilEnum = ProfilEnum.valueOf(profil.toUpperCase());
      } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Page.empty());
      }
    }

    // Logique de filtrage
    if ((keyword == null || keyword.trim().isEmpty()) && profilEnum == null) {
      userPage = userSeervice.findAll(pageable);
    } else if (profilEnum != null && (keyword == null || keyword.trim().isEmpty())) {
      userPage = userSeervice.findByProfil(profilEnum, pageable);
    } else if (profilEnum != null) {
      userPage = userSeervice.findByNomOrPrenomAndProfil(keyword, profilEnum, pageable);
    } else {
      userPage = userSeervice.findByNomContainingOrPrenomContaining(keyword, pageable);
    }

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add("X-Total-Elements", String.valueOf(userPage.getTotalElements()));
    responseHeaders.add("X-Total-Pages", String.valueOf(userPage.getTotalPages()));

    return ResponseEntity.ok()
        .headers(responseHeaders)
        .body(userPage);
  }

  @GetMapping("/profil-distribution")
  public List<ProfilDistributionDTO> getProfilDistribution() {
    return userSeervice.getProfilDistribution();
  }

  @PutMapping("/{userId}/change-profil")
  public ResponseEntity<User> changeProfil(
      @PathVariable Long userId,
      @RequestBody ChangeProfilRequest request) {
    User updatedUser = userSeervice.changeProfil(userId, request);
    return ResponseEntity.ok(updatedUser);
  }

}
