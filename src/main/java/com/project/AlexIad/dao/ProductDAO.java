package com.project.AlexIad.dao;
/**
 *
 * @author Alex Iadvigun
 * @version 1.0
 */
import com.project.AlexIad.models.Product;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@EnableJdbcRepositories
public interface ProductDAO extends JpaRepository <Product, Long> {   // Integer
    List<Product> findProductsByUserId(int id);

}
