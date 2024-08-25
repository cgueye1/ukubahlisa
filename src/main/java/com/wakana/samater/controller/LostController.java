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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.wakana.samater.dto.LostRequest;
import com.wakana.samater.model.LostThing;
import com.wakana.samater.services.LostService;
import com.wakana.samater.util.FileTraferUtil;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/lost")
@RequiredArgsConstructor

public class LostController {

    @Value("${app.image.upload-url}") 
    private String uploadUrl;
    
    private final LostService lostService;
    private final String fileUploadDirectory = "./files/";

    @PostMapping("/save")
    public ResponseEntity<?> save(@ModelAttribute LostRequest request) {
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
                request.setImg(fileName) ;
                request.setFile(null); 
            }
           
            var lost = lostService.saveLost(request);
            return ResponseEntity.ok(Collections.singletonMap("lost", lost));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec d'enregistrement : " + e.getMessage());
        }
    }
    
    @PutMapping("/statut/{id}")
    public ResponseEntity<?> founded(@ModelAttribute LostRequest request,@PathVariable Long id) {
            return ResponseEntity.ok(lostService.founded(id));
     
    }
    
   
     
    @GetMapping("/{id}")
    public ResponseEntity<?> getShopById(@PathVariable Long id) {
        return ResponseEntity.ok(lostService.getLost(id));

    }
    
    
    @GetMapping("/search")
    public ResponseEntity<Page<LostThing>> search(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<LostThing> lostThingPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            lostThingPage = lostService.findAllByOrderByDateDesc(pageable);
        } else {
            lostThingPage  = lostService.findByFirstnameContainingOrLastnameContainingOrDescriptionContainingOrderByDateDesc(keyword,keyword,keyword,pageable);
        }
        
        long totalElements =    lostThingPage.getTotalElements();
        int totalPages =   lostThingPage.getTotalPages();
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Total-Elements", String.valueOf(totalElements));
        responseHeaders.add("X-Total-Pages", String.valueOf(totalPages));
        
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(  lostThingPage);
    }


    
  
    private String generateUniqueFileName(String originalFilename) {
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID().toString();
        return uniqueFileName + (fileExtension != null ? "." + fileExtension : "");
    }
}
