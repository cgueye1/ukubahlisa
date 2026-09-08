package com.wakana.realestateworks.dto.response;



import com.wakana.realestateworks.enums.ProfilEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFullDto {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String adress;
    private String telephone;
    private ProfilEnum profil;
    private boolean activated;
    private boolean notifiable;
    private double funds;
    private double note;
    private String photo;
    private String idCard;
    private String dateOfBirth;
    private String qrcode;
    private AssignedCompanyDto assignedCompany; // seulement id et name
}
