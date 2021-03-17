package com.project.AlexIad.services;

import com.project.AlexIad.dao.ProductDAO;
import com.project.AlexIad.dao.UserDAO;
import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AlarmService {
    private final ProductService productService;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;

    public List<Product> getAlarms(String header){
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Product> productsCurrentUser = productService.getProductsCurrentUser(header);
        if(productsCurrentUser!=null) {
            List<Product> alarmsList = productsCurrentUser.stream().sorted(Comparator.comparing(Product::getOverdueDate))
                    .filter(a -> a.getOverdueDate().isBefore(localDateTime) ||
                            a.getOverdueDate().isEqual(localDateTime))
                    .collect(Collectors.toList());
            if (alarmsList != null) {
                return alarmsList;
            }
        }
        return null;
    }

    public Product upDateOverdueDateInProduct(Product product, String header){
        if(header!=null) {
            String clearToken = header.substring(7);
            User userFromDB = userDAO.findUserByToken(clearToken);
            if(userFromDB!=null) {
                if (product != null) {
                    System.out.println(product + " product from request");
                    Product productDAOById = productDAO.getOne(product.getId());
                    System.out.println(productDAOById + " productFromDB");
                    if (productDAOById != null && productDAOById.getUser().getId() == userFromDB.getId()) {
                        System.out.println(productDAOById + " inside");
                        productDAOById.setUpdateDate(LocalDateTime.now());
                        productDAOById.setOverdueDate(productDAOById.getUpdateDate().plusDays(productDAOById.getExpiration()));
//                        BeanUtils.copyProperties(product, productDAOById, "id", "user", "creationDate","expiration","amount","name");
                        productDAO.save(productDAOById);
                        System.out.println(productDAOById + " after save");
                        return productDAOById;
                    }
                }
            }
        }
        return null;
    }
}
