package com.wakana.samater.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.wakana.samater.dto.ActuRequest;
import com.wakana.samater.model.Actu;
import com.wakana.samater.services.ActuService;
import com.wakana.samater.util.FileTraferUtil;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/actu")
@RequiredArgsConstructor
public class ActuController {
    private final String fileUploadDirectory = "./files/";
    @Value("${app.image.upload-url}") 
    private String uploadUrl;
    private final ActuService actuService;
    
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
            var actu=  actuService.saveActu(request);
            return ResponseEntity.ok(Collections.singletonMap("actu", actu));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec d'enregistrement : " + e.getMessage());
        }
    }
    
    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(@ModelAttribute  ActuRequest  request,@PathVariable Long id){
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
            var actu=  actuService.updateActu(request, id);
            return ResponseEntity.ok(Collections.singletonMap("actu", actu));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec d'enregistrement : " + e.getMessage());
        }
    }
    
     @GetMapping("/search")
    public ResponseEntity<Page<Actu>> search(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Actu> actuPage ;

        if (keyword == null || keyword.trim().isEmpty()) {
            actuPage = actuService.findAllByOrderByDateDesc(pageable);
        } else {
            actuPage  = actuService.findByTitleContainingOrDescriptionContainingOrderByDateDesc(keyword,keyword,pageable);
        }
        
        long totalElements = actuPage.getTotalElements();
        int totalPages = actuPage.getTotalPages();
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Total-Elements", String.valueOf(totalElements));
        responseHeaders.add("X-Total-Pages", String.valueOf(totalPages));
        
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(actuPage);
    }

    @GetMapping("/{id}/actu")
    public ResponseEntity<?> getActuById(@PathVariable Long id) {
        return ResponseEntity.ok(actuService.getActu(id));

    }
    

  
    private String generateUniqueFileName(String originalFilename) {
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID().toString();
        return uniqueFileName + (fileExtension != null ? "." + fileExtension : "");
    }
  

}
