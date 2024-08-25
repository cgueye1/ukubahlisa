package com.wakana.samater;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import java.io.File;

@SpringBootApplication
@EnableTransactionManagement
@OpenAPIDefinition(
	info = @Info(
		title = "Wolof Biblio",
		version = "1.0.0",
		description = "Wolof notifications",
		termsOfService = "runcodeNow",
		contact = @Contact(
			name = "Wakana",
			email = "contactwakana@gmail.com"
		),
		license = @License(
			name = "Licence",
			url = "wolof"
		)
		
	)
)
public class SnapvieApplication implements CommandLineRunner {
	
   
	public static void main(String[] args) {
		// Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		 
		SpringApplication.run(SnapvieApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		File directory = new File("files");
        if (!directory.exists()) {
            directory.mkdirs();
        }
      //  User adminAcount = userRepository.findByRole(Role.ADMIN);
		    //    User membreAcount = userRepository.findByRole(Role.USER);

	/*	if(null == adminAcount){
			User user = new User();
			user.setEmail("admin@yopmail.com");
			user.setRole(Role.ADMIN);
			user.setFirstname("admin");
			user.setSecondname("admin");
			user.setTelephone("+221771111111");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);		
		} else{
			adminAcount.setTelephone("+221771111111");
			userRepository.save(adminAcount);
		}*/
		
	/* 	if(!niveauRepository.existsByLibelle("GOLD")){
			Niveau niveau = new Niveau();
		niveau.setLibelle("GOLD");
		niveauRepository.save(niveau);
		
		Niveau niveau1 = new Niveau();
		niveau1.setLibelle("PREMIUM");
		niveauRepository.save(niveau1);
		
		Niveau niveau2 = new Niveau();
		niveau2.setLibelle("ACCES");
		niveauRepository.save(niveau2);
		}*/
	
		
		
		
		
		
	}

}


/*

{
  "messaging_product": "whatsapp",
  "recipient_type": "individual",
  "to": "221774262278",
  "type": "audio",
  "audio": {
    "id" : "415188914242407"
  }
}

@RestController
public class MediaController {

    @Autowired
    private WhatsAppMediaService mediaService;

    @PostMapping("/uploadMedia")
    public ResponseEntity<String> uploadMedia(@RequestParam String mediaId, @RequestParam String accessToken, @RequestParam String filePath) {
        String response = mediaService.uploadMedia(mediaId, accessToken, filePath);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload media.");
        }
    }
}







import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.FileSystemResource;
import java.io.File;
import java.util.Arrays;

@Service
public class WhatsAppMediaService {

    @Autowired
    private RestTemplate restTemplate;

    public String uploadMedia(String mediaId, String accessToken, String filePath) {
        String url = "https://graph.facebook.com/v19.0/" + mediaId + "/media";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        FileSystemResource fileSystemResource = new FileSystemResource(new File(filePath));
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileSystemResource);
        body.add("type", "image/jpeg");
        body.add("messaging_product", "whatsapp");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        } catch (HttpServerErrorException e) {
            // Gérer l'erreur HTTP
            String responseBody = e.getResponseBodyAsString();
            System.err.println("Error response: " + responseBody);
            return responseBody;
        } catch (Exception e) {
            // Gérer les autres exceptions
            e.printStackTrace();
            return null;
        }
    }
}




{ 
"messaging_product": "whatsapp", 
"to": "221774262278",
"recipient_type": "individual",
 "type": "audio",
  "image":
   {
    "link" : "http://89.38.135.235/repertoire_voices/10-1.mp3"
  }
 
 
 }

 */