package com.wakana.samater.services.impl;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.wakana.samater.dto.FaqRequest;
import com.wakana.samater.model.Faq;
import com.wakana.samater.repository.FaqRepository;
import com.wakana.samater.services.FaqService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService{  
    private final FaqRepository faqRepository;


    @Override
    public Faq saveFaq(FaqRequest faqRequest) {

               ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));

         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
         String formattedDate = now.format(formatter);
         long timestamp =  now .toEpochSecond();
         Faq faq = new Faq();
         faq.setTimestamp( timestamp );
         faq.setTitle(faqRequest.getTitle());
         faq.setDescription(faq.getDescription());
         faq.setDate(formattedDate);
        
        
         return faqRepository.save(faq);
    }

    @Override
    public Faq updateFaq(FaqRequest faqRequest,Long id) {
        Optional<Faq> optionalFaq = faqRepository.findById(id);
          Faq faq = optionalFaq.get();
          faq.setTitle(faqRequest.getTitle());
          faq.setDescription(faq.getDescription());
          return faqRepository.save(faq);
    }

    @Override
    public void removeFaq(Long id) {
       Optional<Faq> optionalFaq = faqRepository.findById(id);
        if (optionalFaq.isPresent()) {
            faqRepository.deleteById(id);
        } else {
        }
    }

    @Override
    public Page<Faq> findAllByOrderByDateDesc(Pageable pageable) {
        return faqRepository.findAllByOrderByTimestampDesc(pageable);
    }

    @Override
    public Page<Faq> findByTitleContainingOrDescriptionContainingOrderByDateDesc(String title, String description,
            Pageable pageable) {
       return faqRepository.findByTitleContainingOrDescriptionContainingOrderByTimestampDesc(title,description,pageable);
    }

    @Override
    public Faq getFaq(Long id) {
      return faqRepository.findById(id).orElse(null);
    }
  


    
     
     
    
}
