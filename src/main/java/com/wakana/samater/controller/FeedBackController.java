package com.wakana.samater.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wakana.samater.dto.FeedBackRequest;
import com.wakana.samater.model.FeedBack;
import com.wakana.samater.services.FeedBackService;

import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedBackController {
    private final FeedBackService feedBackService;
    
    @PostMapping("/save")
    public ResponseEntity<?>   saveFeedback(@RequestBody FeedBackRequest feedBackRequest) {
        return ResponseEntity.ok(feedBackService.saveFeedBack(feedBackRequest));
        
    }
    
    @GetMapping("/all")
     public ResponseEntity<?>   getAllFeedBack() {
        return ResponseEntity.ok(feedBackService.getAllFeedBacks());
        
    }
   
     
       @GetMapping("/search")
    public ResponseEntity<Page<FeedBack>> search(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<FeedBack> feedBackPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            feedBackPage = feedBackService.findAllByOrderByDateDesc(pageable);
        } else {
            feedBackPage  = feedBackService.findByTitleContainingOrMessageContainingOrderByDateDesc(keyword,keyword,pageable);
        }
        
        long totalElements =  feedBackPage.getTotalElements();
        int totalPages = feedBackPage.getTotalPages();
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Total-Elements", String.valueOf(totalElements));
        responseHeaders.add("X-Total-Pages", String.valueOf(totalPages));
        
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(feedBackPage);
    }



    
}
