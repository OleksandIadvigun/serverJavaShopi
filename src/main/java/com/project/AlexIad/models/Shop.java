package com.project.AlexIad.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString(of = {"name", "longitude", "latitude"})
@EqualsAndHashCode(callSuper = true)
@Table(name = "shops")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Shop extends BaseEntity{

    @JsonView(Views.ShopView.class)
    private String name;

    @JsonView(Views.ShopView.class)
    private float latitude;
    @JsonView(Views.ShopView.class)
    private float longitude;
    @JsonView(Views.ShopView.class)
    private int areaSize;


//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private User user;


//    @Column(updatable = false)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonView(Views.ShopView.class)
//    private LocalDateTime creationDate = LocalDateTime.now();

    public Shop(String name, float latitude, float longitude, int areaSize) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.areaSize = areaSize;
    }
    public Shop(String name) {
        this.name = name;
    }

    public Shop() {
    }

}
