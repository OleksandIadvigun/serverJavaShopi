package com.project.AlexIad.dao;
/**
 *
 * @author Alex Iadvigun
 * @version 1.0
 */
import com.project.AlexIad.models.Shop;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableJdbcRepositories
public interface ShopDAO extends JpaRepository<Shop, Long> {   // Integer

    @Query(name = "get_shops",                                                 // <-------------- Solved Lazy exception!
            value = "SELECT s FROM Shop s left JOIN fetch s.user")
      List<Shop> getAll();

   // List<Shop> findFullList();

    List<Shop> findShopsByUserId(int id);

    @Query(name = "get_shop",                                                 // <-------------- Solved Lazy exception!
            value = "SELECT s FROM Shop s left JOIN fetch s.user where s.id= :id")
    Shop getOne(@Param("id") Long id);
}
