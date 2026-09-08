package com.wakana.realestateworks.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wakana.realestateworks.enums.ProfilEnum;
import com.wakana.realestateworks.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByTelephone(String telephone);

        Optional<User> findByEmail(String email);

        boolean existsByTelephone(String telephone);

        Page<User> findByNomContainingOrPrenomContainingAndProfilNot(String nom, String prenom, ProfilEnum profil,
                        Pageable pageable);

        Page<User> findByProfil(ProfilEnum profil, Pageable pageable);

        Page<User> findByProfilAndNomContainingIgnoreCaseOrProfilAndPrenomContainingIgnoreCase(
                        ProfilEnum profil1, String nom,
                        ProfilEnum profil2, String prenom,
                        Pageable pageable);

        Page<User> findAllByProfilNot(ProfilEnum profil, Pageable pageable);

        List<User> findByProfil(ProfilEnum profil);

        long countByProfil(ProfilEnum profil);

        @Query("SELECT u FROM User u JOIN u.properties rp WHERE rp.id = :propertyId")
        Set<User> findOwnersByPropertyId(Long propertyId);

        /// admin
        @Query("SELECT u FROM User u WHERE u.profil = :profil " +
                        "AND (:keyword IS NULL OR LOWER(u.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                        "OR LOWER(u.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                        "OR u.telephone LIKE CONCAT('%', :keyword, '%'))")
        Page<User> findByProfilAndKeyword(
                        @Param("profil") ProfilEnum profil,
                        @Param("keyword") String keyword,
                        Pageable pageable);

        @Query("SELECT COUNT(s) > 0 FROM Subscription s WHERE s.user.id = :userId AND s.active = true")
        boolean hasActiveSubscription(@Param("userId") Long userId);

        boolean existsByEmail(String email);

        Page<User> findByProfilIn(Pageable pageable, List<ProfilEnum> profils);

        Page<User> findByAssignedCompanyIdAndProfilIn(Long propertyId, List<ProfilEnum> profils, Pageable pageable);

        long countByAssignedCompanyIdAndProfil(Long propertyId, ProfilEnum profil);

        long countByAssignedCompany_Promoter_IdAndProfil(Long promoterId, ProfilEnum profil);

        Page<User> findByManagerIdAndProfil(Long managerId, ProfilEnum profil, Pageable pageable);

        Page<User> findByManagerIdAndProfilNotIn(Long managerId, List<ProfilEnum> excludedProfiles, Pageable pageable);

        @Query("SELECT u.profil, COUNT(u) FROM User u GROUP BY u.profil")
        List<Object[]> countUsersByProfil();

        /**
         * Returns all workers (users) assigned to a specific real estate property.
         * Based on RealEstateProperty.workers = List<User> mapped by assignedCompany.
         */
        @Query("SELECT u FROM User u WHERE u.assignedCompany.id = :realEstateId")
        List<User> findWorkersByRealEstateId(@Param("realEstateId") Long realEstateId);

        /** All workers across all properties — used by the global monthly scheduler */
        @Query("SELECT u FROM User u WHERE u.assignedCompany IS NOT NULL")
        List<User> findAllWorkers();

}
