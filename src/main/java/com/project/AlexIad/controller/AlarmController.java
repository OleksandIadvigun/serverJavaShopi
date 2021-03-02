package com.project.AlexIad.controller;

import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.Views;
import com.project.AlexIad.dao.ProductDAO;
import com.fasterxml.jackson.annotation.JsonView;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alarms")
public class AlarmController {
    private ProductDAO productDAO;

    @Autowired
    public AlarmController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @GetMapping
    @Test
    @JsonView(Views.IdName.class)
    public List<Product> findExpiratedProducts() {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Product> all = (List<Product>) productDAO.findAll();
        List<Product> sortedList = all.stream().sorted(Comparator.comparing(a ->
                a.getOverdueDate()))
                .filter(a -> a.getOverdueDate().isBefore(localDateTime) ||
                        a.getOverdueDate().isEqual(localDateTime))
                .collect(Collectors.toList());
        return sortedList;
    }

    @GetMapping("{id}")
    public Product getOne(@PathVariable("id") Product product) {
        return product;
    }


    @PostMapping
    public Product create(@RequestBody Product product) {
        product.setCreationDate(LocalDateTime.now());
        product.setOverdueDate(product.getCreationDate().plusDays(product.getExpiration()));
        return productDAO.save(product);
    }

    @PutMapping("{id}")
    public Product update(@PathVariable("id") Product productFromDB,
                          @RequestBody Product product) {      // message from user

        BeanUtils.copyProperties(product, productFromDB, "id");
        productFromDB.setCreationDate(LocalDateTime.now());
        productFromDB.setOverdueDate(productFromDB.getCreationDate().plusDays(productFromDB.getExpiration()));
        return productDAO.save(productFromDB);
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Product product) {

        productDAO.delete(product);
    }

}

