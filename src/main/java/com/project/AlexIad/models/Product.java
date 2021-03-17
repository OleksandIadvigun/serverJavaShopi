package com.project.AlexIad.models;
/**
 *
 * @author Alex Iadvigun
 * @version 1.0
 */
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonView(Views.IdName.class)
    private String name;
    @JsonView(Views.IdName.class)
    private int amount;
    @JsonView(Views.IdName.class)
    private int expiration ;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonView(Views.IdName.class)
    private LocalDateTime overdueDate;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private User user;

    public Product() {
    }

    public Product(String name, int amount, int expiration) {
        this.name = name;
        this.amount = amount;
        this.expiration = expiration;
    }
}
