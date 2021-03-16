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
@ToString(of = {"name", "amount","expiration","user"})
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Table (name="products")
@EntityListeners(AuditingEntityListener.class)
public class Product extends BaseEntity{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonView(Views.IdName.class)
//    @JsonView(Views.IdName.class)
//    private Long id;
    @JsonView(Views.IdName.class)
    private String name;
    @JsonView(Views.IdName.class)
    private int amount;
    @JsonView(Views.IdName.class)
    private int expiration ;
    //@Column(updatable = true )
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonView(Views.IdName.class)
//    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonView(Views.IdName.class)
    private LocalDateTime overdueDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonView(Views.IdName.class)
//    private LocalDateTime updateDate;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private User user;

    public Product() {
    }

    public Product(String name, int amount, int expiration) {
//        this.id = super.getId();
        this.name = name;
        this.amount = amount;
        this.expiration = expiration;
    }
}
