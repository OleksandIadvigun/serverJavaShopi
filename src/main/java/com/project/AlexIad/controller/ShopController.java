package com.project.AlexIad.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.Shop;
import com.project.AlexIad.dao.ShopDAO;
import com.project.AlexIad.models.User;
import com.project.AlexIad.models.Views;
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

    @GetMapping
//    @JsonView(Views.ShopView.class)
    public ResponseEntity<List<Shop>> getProductsCurrentUser
            (@RequestHeader("Authorization") String header) {
        if (header != null) {
            List<Shop> productsList = shopService.getShopsByUserId(header);
            if (productsList != null) {
                return new ResponseEntity<List<Shop>>(productsList, HttpStatus.OK);
            }
        }
        return new ResponseEntity<List<Shop>>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Shop> create(@RequestBody Shop shop,
                                          @RequestHeader("Authorization") String header  ) {
        Shop addedShop = shopService.addShopInCurrentUser(shop, header);
        if(addedShop!=null){
            return  new ResponseEntity<Shop>(addedShop, HttpStatus.OK);
        }
        return new ResponseEntity<Shop>( HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PutMapping("/edit")
    public ResponseEntity<Shop> update(@RequestBody Shop shop,
                                          @RequestHeader("Authorization") String header ) {      // message from user
        Shop editedShop = shopService.editShopInCurrentUser(shop, header);
        if(editedShop!=null){
            return  new ResponseEntity<Shop>(editedShop, HttpStatus.OK);
        }
        return new ResponseEntity<Shop>( HttpStatus.METHOD_NOT_ALLOWED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody Shop shop,
                                         @RequestHeader("Authorization") String header  ) {
        if( shopService.deleteProductInCurrentUser(shop, header)!=null){
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);
        }
        return new ResponseEntity<String>( HttpStatus.METHOD_NOT_ALLOWED);
    }
}
