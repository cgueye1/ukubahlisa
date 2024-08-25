package com.wakana.samater.services;
import java.util.List;
import com.wakana.samater.dto.ShopRequest;
import com.wakana.samater.model.Shop;

public interface ShopService {
     Shop saveShop(ShopRequest shopRequest ) ;
     Shop updateShop(ShopRequest shopRequest ,Long id ) ;
     Shop getShop(Long id ) ;
     void removeShop(Long id ) ;
     List <Shop> getAll();
  

}
