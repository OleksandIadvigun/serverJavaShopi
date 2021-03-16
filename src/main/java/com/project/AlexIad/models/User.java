package com.project.AlexIad.models;
/**
 *
 * @author Alex Iadvigun
 * @version 1.0
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table (name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private int id;
    @Expose
    private String username;
    @Expose
    private String firstname;
    @Expose
    private String lastname;
    private String password;
    @Expose
    private int age;
    @Expose
    private String mobile;
    @Expose
    private String sex;
    private String role = "ROLE_USER";
    @Expose
    private String token;
    @Expose
    private String email;
    private String activationCode;
    @Expose
    private String logo;
    private boolean isActivated = false;

    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @Expose
    private Address address;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn (name="user_id")
    private List<Product> products;


    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn (name="user_id")
    private List<Shop> shops = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(this.role));
        return roles;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return this.isActivated;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", mobile='" + mobile + '\'' +
                ", sex='" + sex + '\'' +
                ", role='" + role + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", activationCode='" + activationCode + '\'' +
                ", logo='" + logo + '\'' +
                ", isActivated=" + isActivated +
                ", address=" + address +
                '}';
    }
}
