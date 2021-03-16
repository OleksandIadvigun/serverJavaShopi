package com.project.AlexIad.models;


import com.google.gson.annotations.Expose;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Address extends BaseEntity{
    private Long id = super.getId();
    @Expose
    private String country;
    @Expose
    private String city;

    @OneToOne(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private User user;

}
