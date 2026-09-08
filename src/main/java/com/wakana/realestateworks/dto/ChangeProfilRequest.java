package com.wakana.realestateworks.dto;




import com.wakana.realestateworks.enums.ProfilEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO pour changer le profil d'un utilisateur")
public class ChangeProfilRequest {

    @NotNull
    @Schema(
        description = "Profil de l'utilisateur",
        example = "PROMOTEUR",
        allowableValues = {"PROMOTEUR", "SITE_MANAGER", "WORKER"} // <- dropdown dans Swagger
    )
    private ProfilEnum profil;

    public ProfilEnum getProfil() {
        return profil;
    }

    public void setProfil(ProfilEnum profil) {
        this.profil = profil;
    }
}
