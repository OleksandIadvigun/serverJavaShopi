package com.project.AlexIad.controller;


import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.Views;
import com.project.AlexIad.dao.ProductDAO;
import com.fasterxml.jackson.annotation.JsonView;
import com.project.AlexIad.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    private ProductService productService;

    @GetMapping
    @JsonView(Views.IdName.class)
    public ResponseEntity<List<Product>> getProductsCurrentUser
            (@RequestHeader("Authorization") String header){
        System.out.println("in metod " + header);
        if(header!=null) {
            try {
                List<Product> productsList = productService.getProductsCurrentUser(header);
                return new ResponseEntity<List<Product>>(productsList, HttpStatus.OK);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return new ResponseEntity<List<Product>>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product,
                          @RequestHeader("Authorization") String header  ) {
        Product addedProduct = productService.addProductInCurrentUser(product, header);
        if(addedProduct!=null){
        return  new ResponseEntity<Product>(addedProduct, HttpStatus.OK);
        }
        return new ResponseEntity<Product>( HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PutMapping("/edit")
    public ResponseEntity<Product> update(@RequestBody Product product,
                          @RequestHeader("Authorization") String header ) {      // message from user
        Product editedProduct = productService.editProductInCurrentUser(product, header);
        if(editedProduct!=null){
            return  new ResponseEntity<Product>(editedProduct, HttpStatus.OK);
        }
        return new ResponseEntity<Product>( HttpStatus.METHOD_NOT_ALLOWED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody Product product,
                       @RequestHeader("Authorization") String header  ) {
        System.out.println("IN delete method");
       if( productService.deleteProductInCurrentUser(product, header)!=null){
           System.out.println("deleted resp from server");
           return new ResponseEntity<String>("Deleted!", HttpStatus.OK);
       }
        return new ResponseEntity<String>( HttpStatus.METHOD_NOT_ALLOWED);
    }
}
