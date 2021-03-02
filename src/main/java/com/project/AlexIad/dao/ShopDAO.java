package com.project.AlexIad.dao;

import com.project.AlexIad.models.Shop;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@EnableJdbcRepositories
public interface ShopDAO extends JpaRepository<Shop, Long> {   // Integer

    @Query(name = "get_shop",                                                 // <-------------- Solved Lazy exception!
            value = "SELECT s FROM Shop s left JOIN fetch s.user")
      List<Shop> getAll();

   // List<Shop> findFullList();

    List<Shop> findShopsByUserId(int id);
}
