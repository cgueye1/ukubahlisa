package com.wakana.realestateworks.dto;

import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RealEstatePropertyRequest {

  private String name;
  private String number;
  private String address;
  private double price;

  @Schema(hidden = true)
  private double feesFile;

  private int numberOfRooms;
  private double area;
  private String latitude;
  private String longitude;
  @Schema(hidden = true)
  private boolean available = true;

  @Schema(hidden = true)
  private double reservationFee = 0.0;

  private String description;
  private int numberOfLots;

  //@Schema(description = "Correspond à l'identifiant du chef de chantier qui enrole ce bien", required = false)
  private Long promoterId;

//  @Schema(description = "Identifiant du chef de chantier (MOA) qui enrole ce bien", required = false)
  private Long moaId;

  ///@Schema(description = "Identifiant du gestionnaire (manager) responsable de ce bien", required = false)
  private Long managerId;

  @Schema(hidden = true)
  private long recipientId;

  @Schema(hidden = true)
  private long notaryId;

  @Schema(hidden = true)
  private long bankId;

  @Schema(hidden = true)
  private long agencyId;

  @Schema(hidden = true)
  private long parentPropertyId;

  private long propertyTypeId;

  @Schema(hidden = true)
  private double discount;

  private MultipartFile plan;

  @Schema(hidden = true)
  private int level = 0;

  @Schema(hidden = true)
  private MultipartFile legalStatus;

  @Schema(hidden = true)
  private List<MultipartFile> pictures;





}
