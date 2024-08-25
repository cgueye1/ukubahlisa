package com.wakana.samater.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.wakana.samater.dto.ActuRequest;
import com.wakana.samater.services.BgImageService;
import com.wakana.samater.util.FileTraferUtil;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/bgimage")
@RequiredArgsConstructor
public class BgImageController {
    private final String fileUploadDirectory = "./files/";
    @Value("${app.image.upload-url}") 
    private String uploadUrl;
    private final BgImageService bgImageService;
    
    @PostMapping("/save")
    public ResponseEntity<?> save(@ModelAttribute  ActuRequest  request){
        try {
            MultipartFile file = request.getFile();
       
         
            if ( file != null && !file.isEmpty()) {
                String fileName = generateUniqueFileName(file.getOriginalFilename());
                File uploadDirectory = new File(fileUploadDirectory);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                String localFilePath = fileUploadDirectory + fileName;
                Files.copy(file.getInputStream(), Paths.get(localFilePath), StandardCopyOption.REPLACE_EXISTING);
                String remoteFilePath = "/ter/" + fileName;
                FileTraferUtil.  transferFileToRemote(localFilePath, remoteFilePath);
                request.setImg(fileName) ;
                request.setFile(null); 
            }
            var bgImage= bgImageService.saveBgImage(request);
            return ResponseEntity.ok(bgImage);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec d'enregistrement : " + e.getMessage());
        }
    }
    
 
  

    @GetMapping("/last")
    public ResponseEntity<?> getLastBgImage() {
        return ResponseEntity.ok(bgImageService.findFirstByOrderByDateDesc());

    }
    

  
    private String generateUniqueFileName(String originalFilename) {
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID().toString();
        return uniqueFileName + (fileExtension != null ? "." + fileExtension : "");
    }
  

}
