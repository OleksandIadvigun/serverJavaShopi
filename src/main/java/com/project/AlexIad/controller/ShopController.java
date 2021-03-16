package com.project.AlexIad.controller;

import com.project.AlexIad.models.Shop;
import com.project.AlexIad.dao.ShopDAO;
import com.project.AlexIad.models.User;
import com.project.AlexIad.services.ShopService;
import com.project.AlexIad.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shops")
@AllArgsConstructor
@CrossOrigin
public class ShopController {

    private final ShopService shopService;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<Shop>> list(@RequestHeader("Authorization") String header) {
        List<Shop> shopsByUserId = shopService.getShopsByUserId( header);
        if (shopsByUserId != null) {
            return new ResponseEntity<List<Shop>>(shopsByUserId, HttpStatus.OK);
        }
        return new ResponseEntity<List<Shop>>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("{userId}/{id}")
    public ResponseEntity<Shop> getOne(@PathVariable("userId") int UserId,
                                       @PathVariable("id") long id,
                                       @RequestHeader("Authorization") String header) {
        Shop shopById = shopService.getShopById(UserId, id, header);
        if (shopById != null) {
            return new ResponseEntity<Shop>(shopById, HttpStatus.OK);
        }
        return new ResponseEntity<Shop>(HttpStatus.FORBIDDEN);
    }

//    @PostMapping
//    public Shop create(@RequestBody Shop shop, @RequestHeader("Authorization") String header) {
//        shop.setCreationDate(LocalDateTime.now());
//        if (header != null) {
//            System.out.println("header From Rec: " + header);
//            String clearToken = header.substring(7);
//            User userFromDB = userService.findUserByToken(clearToken);
//            shop.setUser(userFromDB);
//        }
//        return shopDAO.save(shop);
//    }

//    @PutMapping("{id}")
//    public Shop update(@PathVariable("id") Shop shopFromDB,
//                       @RequestBody Shop shop) {      // message from user
//
//        BeanUtils.copyProperties(shop, shopFromDB, "id", "user");
//        return shopDAO.save(shopFromDB);
//    }
//
//    @DeleteMapping("{id}")
//    public void delete(@PathVariable("id") Shop shop) {
//
//        shopDAO.delete(shop);
//    }

}
