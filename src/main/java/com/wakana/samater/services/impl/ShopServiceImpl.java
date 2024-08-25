package com.wakana.samater.services.impl;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.wakana.samater.dto.ShopRequest;
import com.wakana.samater.model.Shop;
import com.wakana.samater.repository.ShopRepository;
import com.wakana.samater.services.ShopService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService{  
    private final ShopRepository shopRepository;

    @Override
    public Shop saveShop(ShopRequest shopRequest) {
      Shop  shop = new Shop();
      shop.setLibelle(shopRequest.getLibelle());
      shop.setDescr(shopRequest.getDescr());
      shop.setPicture(shopRequest.getPicture());
      return shopRepository.save(shop);
    }

    @Override
    public Shop updateShop(ShopRequest shopRequest, Long id) {
       Optional<Shop> optionalShop = shopRepository.findById(id);
        if (optionalShop.isEmpty()) {
            return null; 
        }
        Shop shop = optionalShop.get();
        
        shop.setLibelle(shopRequest.getLibelle());
        shop.setDescr(shopRequest.getDescr());
        
        if(shopRequest.getPicture()!= null && !shopRequest.getPicture().isEmpty()){
          shop.setPicture(shopRequest.getPicture());
        }
        
        return shopRepository.save(shop);
      
    }

    @Override
    public List<Shop> getAll() {
     
     return shopRepository.findAll();
    }

    @Override
    public Shop getShop(Long id) {
     
     return shopRepository.findById(id).orElse(null);
    }

    @Override
    public void removeShop(Long id) {
        Optional<Shop> optionalShop = shopRepository.findById(id);
        if (optionalShop.isPresent()) {
            shopRepository.deleteById(id);
        } else {
        }
    }
  


    
     
     
    
}
