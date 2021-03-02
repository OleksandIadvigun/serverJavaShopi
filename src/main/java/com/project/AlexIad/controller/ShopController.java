package com.project.AlexIad.controller;

import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.Shop;
import com.project.AlexIad.dao.ShopDAO;
import com.project.AlexIad.models.User;
import com.project.AlexIad.services.UserService;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.net.www.HeaderParser;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop")
@AllArgsConstructor
public class ShopController {

    private final ShopDAO shopDAO;
    private final UserService userService;

//    @Autowired
//    public ShopController(ShopDAO shopDAO) {
//        this.shopDAO = shopDAO;
//    }

    @GetMapping
    public List<Shop> list(
            @RequestParam(name = "name", required = false, defaultValue = "BOSS") String name, Model model) {
        model.addAttribute("name", name);
      //  List<Shop> sortedListByCreationDate =  shopDAO.findAll().stream().sorted(Comparator.comparing(Shop::getCreationDate)).collect(Collectors.toList());
      //  return sortedListByCreationDate;
        return shopDAO.findAll();
    }

    @GetMapping("{id}")
    public Shop getOne(@PathVariable("id") Shop shop) {
        return shop;
    }

    @GetMapping("/p/{id}")

    public ResponseEntity<List<Shop>> getById(@PathVariable("id") int id, @RequestHeader("Authorization") String header ) {
        if (header != null) {
            System.out.println("header From Rec: " + header);
            String clearToken = header.substring(7);
            User userFromDB = userService.findUserByToken(clearToken);
            if(userFromDB.getId() == id) {
                List<Shop> shopsByUserId = shopDAO.findShopsByUserId(id);
                return new ResponseEntity<List<Shop>>(shopsByUserId, HttpStatus.OK);
            }
        }
        return new ResponseEntity<List<Shop>>( HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public Shop create(@RequestBody Shop shop) {
        shop.setCreationDate(LocalDateTime.now());
        return shopDAO.save(shop);
    }

    @PutMapping("{id}")
    public Shop update(@PathVariable("id") Shop shopFromDB,
                       @RequestBody Shop shop) {      // message from user

        BeanUtils.copyProperties(shop, shopFromDB, "id","user");
        return shopDAO.save(shopFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Shop shop) {

        shopDAO.delete(shop);
    }

}
