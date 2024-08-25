package com.wakana.samater.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.wakana.samater.dto.StopRequest;
import com.wakana.samater.model.Shop;
import com.wakana.samater.model.Stop;
import com.wakana.samater.services.StopService;
import com.wakana.samater.util.FileTraferUtil;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/stop")
@RequiredArgsConstructor
public class StopController {

    @Value("${app.image.upload-url}") 
    private String uploadUrl;
    
    private final StopService stopService;
    private final String fileUploadDirectory = "./files/";

    @PostMapping("/save")
    public ResponseEntity<?> save(@ModelAttribute StopRequest request) {
        try {
            MultipartFile picture = request.getPicture();
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
                request.setPicture(null); // Clear the file from request to avoid duplication
            }

            List<String> pictureUrls = uploadPictures(request.getPictures(), request.getStop_name());
            var stop = stopService.saveStop(request, picture != null ? fileName : null, pictureUrls);
            return ResponseEntity.ok(Collections.singletonMap("stop", stop));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec d'enregistrement : " + e.getMessage());
        }
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@ModelAttribute StopRequest request, @PathVariable String id) {
        var singleImg ="";
        try {
            MultipartFile picture = request.getPicture();

            if (picture != null && !picture.isEmpty()) {
                String fileName = picture != null && !picture.isEmpty() ? generateUniqueFileName(picture.getOriginalFilename()) : null;

                File uploadDirectory = new File(fileUploadDirectory);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                String localFilePath = fileUploadDirectory + fileName;
                Files.copy(picture.getInputStream(), Paths.get(localFilePath), StandardCopyOption.REPLACE_EXISTING);
                String remoteFilePath = "/ter/" + request.getStop_name() + "/" + fileName;
                FileTraferUtil. transferFileToRemote(localFilePath, remoteFilePath);
                singleImg =fileName;
                request.setPicture(null); 
            }

            List<String> pictureUrls = uploadPictures(request.getPictures(), request.getStop_name());
            var stop = stopService.updateStop(request, id,  singleImg , pictureUrls);
            return stop != null 
                ? ResponseEntity.ok(Collections.singletonMap("stop", stop)) 
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stop not found.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec de mise à jour : " + e.getMessage());
        }
    }
    
   @GetMapping("/{id}/stop")
    public ResponseEntity<?> getStopById(@PathVariable String id) {
        
        return ResponseEntity.ok(stopService.findById(id));

    }
    @GetMapping("/removePicture/{picture}/{id}")
    public ResponseEntity<?> removePicture(@PathVariable String picture,@PathVariable String id) {
        
        return ResponseEntity.ok(stopService.removePicture(picture, id));

    }
    @PostMapping("/{id}/shops")
    public ResponseEntity<?>   saveShop(@RequestBody Shop shoRequest,@PathVariable String id) {
        return ResponseEntity.ok(stopService.addShop(shoRequest,id ));
        
    }

    @GetMapping("/{idShop}/{idStop}/shop")
    public ResponseEntity<?> deletShop(@PathVariable Long idShop ,@PathVariable String idStop) {
        
        return ResponseEntity.ok(stopService.removeShop(idShop,idStop));

    }
    
    
    
    @GetMapping("/search")
    public ResponseEntity<Page<Stop>> search(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Stop> stopPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            stopPage = stopService.findAllByOrderByStopCodeAsc(pageable);
        } else {
            stopPage = stopService.findByStopNameOrderByStopCodeAsc(keyword, pageable);
        }
        
        long totalElements = stopPage.getTotalElements();
        int totalPages = stopPage.getTotalPages();
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Total-Elements", String.valueOf(totalElements));
        responseHeaders.add("X-Total-Pages", String.valueOf(totalPages));
        
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(stopPage);
    }



    
    
    
    
    private List<String> uploadPictures(List<MultipartFile> pictures, String stopName) throws IOException {
        List<String> pictureUrls = new ArrayList<>();
        if (pictures != null && !pictures.isEmpty()) {
            for (MultipartFile picture : pictures) {
                if (picture != null && !picture.isEmpty()) {
                    String fileName = generateUniqueFileName(picture.getOriginalFilename());
                    File uploadDirectory = new File(fileUploadDirectory);
                    if (!uploadDirectory.exists()) {
                        uploadDirectory.mkdirs();
                    }
                    String localFilePath = fileUploadDirectory + fileName;
                    Files.copy(picture.getInputStream(), Paths.get(localFilePath), StandardCopyOption.REPLACE_EXISTING);
                    String remoteFilePath = "/ter/" + fileName;
                    FileTraferUtil.transferFileToRemote(localFilePath, remoteFilePath);
                    pictureUrls.add(fileName);
                }
            }
        }
        return pictureUrls;
    }

    private String generateUniqueFileName(String originalFilename) {
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID().toString();
        return uniqueFileName + (fileExtension != null ? "." + fileExtension : "");
    }
}
