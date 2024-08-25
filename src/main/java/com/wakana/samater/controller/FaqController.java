package com.wakana.samater.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wakana.samater.dto.FaqRequest;
import com.wakana.samater.model.Faq;
import com.wakana.samater.services.FaqService;


import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/faq")
@RequiredArgsConstructor
public class FaqController {
    private final FaqService faqService;
    
    @PostMapping("/save")
    public ResponseEntity<?>   saveFaq(@RequestBody FaqRequest faqRequest) {
        return ResponseEntity.ok(faqService.saveFaq(faqRequest));
        
    }
    
 
    @PutMapping("/{id}/update")
    public ResponseEntity<?>   updateFaq(@RequestBody FaqRequest faqRequest,@PathVariable Long id) {
        return ResponseEntity.ok(faqService.updateFaq(faqRequest, id));
        
    }
    
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?>   removeFaq(@PathVariable Long id) {
        faqService.removeFaq(id);;
        return ResponseEntity.ok("");
        
    }
    
    
    
    
    
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getFaqById(@PathVariable Long id) {
        return ResponseEntity.ok(faqService.getFaq(id));

    }
    
    
     
    @GetMapping("/search")
    public ResponseEntity<Page<Faq>> search(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Faq> faqPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            faqPage = faqService.findAllByOrderByDateDesc(pageable);
        } else {
            faqPage = faqService.findByTitleContainingOrDescriptionContainingOrderByDateDesc(keyword,keyword,pageable);
        }
        
        long totalElements = faqPage.getTotalElements();
        int totalPages = faqPage.getTotalPages();
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Total-Elements", String.valueOf(totalElements));
        responseHeaders.add("X-Total-Pages", String.valueOf(totalPages));
        
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(faqPage);
    }



    
   

    
}
