package com.paytm.core.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserModel {
    @Id
    private String username;
    private String password;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @ElementCollection(targetClass = CustomAuthorityEnum.class, fetch = FetchType.EAGER)
    @Column(name = "authorities")
    @Enumerated(EnumType.STRING)
    private Set<CustomAuthorityEnum> Authorities;

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserModel(String username, String password, Set<CustomAuthorityEnum> authorities) {
        this.username = username;
        this.password = password;
        Authorities = authorities;
    }
}
