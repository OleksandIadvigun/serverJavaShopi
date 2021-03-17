package com.project.AlexIad.controller;
/**
 *
 * @author Alex Iadvigun
 * @version 1.0
 */
import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.Views;
import com.project.AlexIad.dao.ProductDAO;
import com.fasterxml.jackson.annotation.JsonView;
import com.project.AlexIad.services.AlarmService;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alarms")
@AllArgsConstructor
@CrossOrigin
public class AlarmController {
    private final AlarmService alarmService;


    @GetMapping
    @Test
    @JsonView(Views.IdName.class)
    public ResponseEntity<List<Product>> getAlarmsCurrentUser
            (@RequestHeader("Authorization") String header){
        if(header!=null) {
            try {
                List<Product> productsList = alarmService.getAlarms(header);
                return new ResponseEntity<List<Product>>(productsList, HttpStatus.OK);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return new ResponseEntity<List<Product>>(HttpStatus.NOT_FOUND);
    }


}

