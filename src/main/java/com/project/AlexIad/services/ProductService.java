package com.project.AlexIad.services;
/**
 *
 * @author Alex Iadvigun
 * @version 1.0
 */
import com.project.AlexIad.dao.ProductDAO;
import com.project.AlexIad.dao.UserDAO;
import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.Status;
import com.project.AlexIad.models.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProductService {
    private ProductDAO productDAO;
    private UserDAO userDAO;

    public List<Product> getProductsCurrentUser(String header){
        if(header!=null) {
            String clearToken = header.substring(7);
            User userFromDB = userDAO.findUserByToken(clearToken);
            if(userFromDB!=null) {
                List<Product> prodList = productDAO.findProductsByUserId(userFromDB.getId());
                List<Product> filteredListByStatus = prodList.stream().filter(product -> product.getStatus().equals(Status.ACTIVE)).collect(Collectors.toList());
                if (filteredListByStatus != null) {
                    return filteredListByStatus;
                }
            }
        }
        return null;
    }

    public Product addProductInCurrentUser(Product product, String header){
        if(header!=null) {
            String clearToken = header.substring(7);
            User userFromDB = userDAO.findUserByToken(clearToken);
            if(userFromDB!=null) {
                if (product != null) {
                    product.setUser(userFromDB);
                    product.setCreationDate(LocalDateTime.now());
                    product.setOverdueDate(product.getCreationDate().plusDays(product.getExpiration()));
                    productDAO.save(product);
                    return product;
                }
            }
        }
        return null;
    }

    public Product editProductInCurrentUser(Product product, String header){
        if(header!=null) {
            String clearToken = header.substring(7);
            User userFromDB = userDAO.findUserByToken(clearToken);
            if(userFromDB!=null) {
                if (product != null) {
                    System.out.println(product + " product from request");
                    Product productDAOById = productDAO.getOne(product.getId());
                    System.out.println(productDAOById + " productFromDB");
                    if (productDAOById != null && productDAOById.getUser().getId() == userFromDB.getId()) {
                        product.setUpdateDate(LocalDateTime.now());
                        product.setOverdueDate(product.getUpdateDate().plusDays(product.getExpiration()));
                        BeanUtils.copyProperties(product, productDAOById, "id", "user", "creationDate");
                        productDAO.save(productDAOById);
                        return productDAOById;
                    }
                }
            }
        }
        return null;
    }

    public Product deleteProductInCurrentUser(Product product, String header){
        if(header!=null) {
            String clearToken = header.substring(7);
            User userFromDB = userDAO.findUserByToken(clearToken);
            Product byId = productDAO.getOne(product.getId());
            if(byId!=null && userFromDB!=null ){
                if(byId.getUser().getId()==userFromDB.getId()) {
                    System.out.println(byId + " prod from db before del");
                    byId.setStatus(Status.DELETED);
                    productDAO.save(byId);
                    System.out.println("deleted from db");
                    return byId;
                }
            }
        }
        return null;
    }
}
