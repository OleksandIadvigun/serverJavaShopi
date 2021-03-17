package com.project.AlexIad.services;

import com.project.AlexIad.models.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AlarmService {
    private final ProductService productService;
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
}
