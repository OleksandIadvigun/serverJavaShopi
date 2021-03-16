package com.project.AlexIad.services;

import com.project.AlexIad.dao.ShopDAO;
import com.project.AlexIad.dao.UserDAO;
import com.project.AlexIad.models.Shop;
import com.project.AlexIad.models.User;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShopService {
private ShopDAO shopDAO;
private UserDAO userDAO;

   public Shop getShopById(int userId,Long id, String header){
       if(header!=null) {
           String clearToken = header.substring(7);
           User userFromDB = userDAO.findUserByToken(clearToken);
           if (userFromDB.getId() == userId ) {
               Shop shop = shopDAO.getOne(id);
               if(userFromDB.getId() == shop.getUser().getId()) {
                   return shop;
               }
           }
       }
       return null;
   }

   public List<Shop> getShopsByUserId( String header){
       if(header!=null) {
           String clearToken = header.substring(7);
           User userFromDB = userDAO.findUserByToken(clearToken);
           try {
               return shopDAO.findShopsByUserId(userFromDB.getId()).stream().sorted(Comparator.comparing(Shop::getCreationDate)).collect(Collectors.toList());
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
       }
       return null;
   }
}
