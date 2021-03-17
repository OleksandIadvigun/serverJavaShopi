package com.project.AlexIad.services;
/**
 *
 * @author Alex Iadvigun
 * @version 1.0
 */
import com.project.AlexIad.dao.ShopDAO;
import com.project.AlexIad.dao.UserDAO;
import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.Shop;
import com.project.AlexIad.models.Status;
import com.project.AlexIad.models.User;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
           if (userFromDB != null) {
               try {
                   return shopDAO.findShopsByUserId(userFromDB.getId()).stream().filter(shop -> shop.getStatus().equals(Status.ACTIVE)).collect(Collectors.toList());
               } catch (Exception e) {
                   System.out.println(e.getMessage());
               }
           }
       }
       return null;
   }

    public Shop addShopInCurrentUser(Shop shop, String header){
        if(header!=null) {
            String clearToken = header.substring(7);
            User userFromDB = userDAO.findUserByToken(clearToken);
            if(userFromDB!=null) {
                if (shop != null) {
                    shop.setUser(userFromDB);
                    shopDAO.save(shop);
                    return shop;
                }
            }
        }
        return null;
    }

    public Shop editShopInCurrentUser(Shop shop, String header){
        if(header!=null) {
            String clearToken = header.substring(7);
            User userFromDB = userDAO.findUserByToken(clearToken);
            if(userFromDB!=null) {
                if (shop != null) {
                    System.out.println(shop + " shop from request");
                    Shop shopDAOById = shopDAO.getOne(shop.getId());
                    System.out.println(shopDAOById + " productFromDB");
                    if (shopDAOById != null && shopDAOById.getUser().getId() == userFromDB.getId()) {
                        BeanUtils.copyProperties(shop, shopDAOById, "id", "user", "creationDate");
                        shopDAO.save(shopDAOById);
                        return shopDAOById;
                    }
                }
            }
        }
        return null;
    }

    public Shop deleteProductInCurrentUser(Shop shop, String header){
        if(header!=null) {
            String clearToken = header.substring(7);
            User userFromDB = userDAO.findUserByToken(clearToken);
            Shop byId = shopDAO.getOne(shop.getId());
            if(byId!=null && userFromDB!=null ){
                if(byId.getUser().getId()==userFromDB.getId()) {
                    System.out.println(byId + " shop from db before del");
                    byId.setStatus(Status.DELETED);
                    shopDAO.save(byId);
                    System.out.println("deleted from db");
                    return byId;
                }
            }
        }
        return null;
    }
}
