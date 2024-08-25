package com.wakana.samater.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.wakana.samater.dto.ShopRequest;
import com.wakana.samater.services.ShopService;
import com.wakana.samater.util.FileTraferUtil;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor

public class ShopController {

    @Value("${app.image.upload-url}") 
    private String uploadUrl;
    
    private final ShopService shopService;
    private final String fileUploadDirectory = "./files/";

    @PostMapping("/save")
    public ResponseEntity<?> save(@ModelAttribute ShopRequest request) {
        try {
            MultipartFile picture = request.getFile();

            if (picture != null && !picture.isEmpty()) {
                String fileName = generateUniqueFileName(picture.getOriginalFilename());

                File uploadDirectory = new File(fileUploadDirectory);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                String localFilePath = fileUploadDirectory + fileName;
                Files.copy(picture.getInputStream(), Paths.get(localFilePath), StandardCopyOption.REPLACE_EXISTING);
                String remoteFilePath = "/ter/" + fileName;
                FileTraferUtil.  transferFileToRemote(localFilePath, remoteFilePath);
                request.setPicture(fileName) ;
                request.setFile(null); 
            }
           
            var shop = shopService.saveShop(request);
            return ResponseEntity.ok(Collections.singletonMap("shop", shop));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec d'enregistrement : " + e.getMessage());
        }
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@ModelAttribute ShopRequest request,@PathVariable Long id) {
        try {
            MultipartFile picture = request.getFile();
            String fileName = generateUniqueFileName(picture.getOriginalFilename());

            if (picture != null && !picture.isEmpty()) {
                File uploadDirectory = new File(fileUploadDirectory);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                String localFilePath = fileUploadDirectory + fileName;
                Files.copy(picture.getInputStream(), Paths.get(localFilePath), StandardCopyOption.REPLACE_EXISTING);
                String remoteFilePath = "/ter/" + fileName;
                FileTraferUtil.  transferFileToRemote(localFilePath, remoteFilePath);
                request.setPicture(fileName) ;
                request.setFile(null); 
            }
           
            var shop = shopService.updateShop(request,id);
            return ResponseEntity.ok(Collections.singletonMap("shop", shop));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec d'enregistrement : " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShopById(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.getShop(id));

    }
    
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deletShop(@PathVariable Long id) {
        shopService.removeShop(id);
        return ResponseEntity.ok("Shop deleted");

    }
    
    @GetMapping("/all")
    public ResponseEntity<?>   getAll() {
       return ResponseEntity.ok(shopService.getAll());
       
   }
  
    private String generateUniqueFileName(String originalFilename) {
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID().toString();
        return uniqueFileName + (fileExtension != null ? "." + fileExtension : "");
    }
}
