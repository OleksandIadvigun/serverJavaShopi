package com.project.AlexIad.controller;


import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.Views;
import com.project.AlexIad.dao.ProductDAO;
import com.fasterxml.jackson.annotation.JsonView;
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


@RestController
@RequestMapping("/product")
//@CrossOrigin("http://localhost:3000")
public class ProductController {

    private final ProductDAO productDAO;

    @Autowired
    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }


    @GetMapping
    @JsonView(Views.IdName.class)
//    public List<Product> list
    public ResponseEntity<List<Product>> list
            (
            @RequestParam(name = "name", required = false, defaultValue = "BOSS") String name, Model model) {
        model.addAttribute("name", name);
        List<Product> sortedListByCreationDate = productDAO.findAll().stream().sorted(Comparator.comparing(Product::getCreationDate)).collect(Collectors.toList());
       // return sortedListByCreationDate;
       // List<Product> productDAOAll = productDAO.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "http://localhost:3000");
        return new ResponseEntity<List<Product>>(sortedListByCreationDate , HttpStatus.OK);
    }

    @GetMapping("{id}")
    public Product getOne(@PathVariable("id") long id) {
        Optional<Product> optional = productDAO.findById(id);
       return optional.get();
//        Product productDAOById = productDAO.getOne(id);
//        return productDAOById;
    }


    @PostMapping
    public Product create(@RequestBody Product product) {
        product.setCreationDate(LocalDateTime.now());
        product.setOverdueDate(product.getCreationDate().plusDays(product.getExpiration()));
        return productDAO.save(product);
    }

    @PutMapping("{id}")
    public Product update(@PathVariable("id") long id,
                          @RequestBody Product product) {      // message from user

        Product productDAOById = productDAO.getOne(id);
        BeanUtils.copyProperties(product, productDAOById, "id");
        productDAOById.setCreationDate(LocalDateTime.now());
        productDAOById.setOverdueDate(productDAOById.getCreationDate().plusDays(productDAOById.getExpiration()));
        return productDAO.save(productDAOById);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Product product) {

        productDAO.delete(product);
    }

}
