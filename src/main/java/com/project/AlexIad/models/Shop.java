package com.project.AlexIad.models;
/**
 *
 * @author Alex Iadvigun
 * @version 1.0
 */
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@ToString(of = {"name", "longitude", "latitude","areaSize"})
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
