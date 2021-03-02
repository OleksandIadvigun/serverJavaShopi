package com.project.AlexIad;

import com.project.AlexIad.controller.AlarmController;
import com.project.AlexIad.models.Product;
import com.project.AlexIad.dao.ProductDAO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@SpringBootTest
class DemoIadApplicationTests {
    AlarmController alarmController;

    @Test
    public void IsAlarmCorrect() {
        ProductDAO productDAO = mock(ProductDAO.class);

        String str = "2016-03-04 11:30:40";
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        List<Product> productList = new ArrayList<>();
        Product product1 = new Product("a", 10, 4);
        product1.setCreationDate(LocalDateTime.parse("2020-05-28 10:30:30", formatter));
        productList.add(product1);
        Product product2 = new Product("b", 10, 14);
        product2.setCreationDate(LocalDateTime.parse("2020-05-28 10:30:30", formatter));
        productList.add(product2);
        Product product3 = new Product("c", 5, 15);
        product3.setCreationDate(LocalDateTime.parse("2020-05-28 10:30:30", formatter));
        productList.add(product3);
        Product product4 = new Product("d", 5, 1);
        product4.setCreationDate(LocalDateTime.parse("2020-06-05 10:30:30", formatter));
        productList.add(product4);

        doReturn((Iterable<Product>) productList).when(productDAO).findAll();
        alarmController = new AlarmController(productDAO);
        List<Product> expected = new ArrayList<>();
        expected.add(product1);
        expected.add(product4);

        List<Product> actual = alarmController.findExpiratedProducts();
        Assert.assertEquals(expected, actual);
    }
}
