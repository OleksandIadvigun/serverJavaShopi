package com.project.AlexIad.services;

import com.project.AlexIad.dao.ShopDAO;
import com.project.AlexIad.models.Shop;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShopService {
private ShopDAO shopDAO;

   public Shop getShopById(long id){
       Shop shop = shopDAO.getOne(id);
       Hibernate.initialize(shop.getUser());             //<-------------- Solved Lazy exception!
       return shop;
   }

}
