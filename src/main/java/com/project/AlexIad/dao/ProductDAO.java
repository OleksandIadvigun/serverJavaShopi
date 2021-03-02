package com.project.AlexIad.dao;

import com.project.AlexIad.models.Product;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.JpaRepository;

@EnableJdbcRepositories
public interface ProductDAO extends JpaRepository <Product, Long> {   // Integer

}
