package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.util.List;

@Entity
@Data // includes @Getter, @Setter, @ToString, etc.
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@ToString(exclude = {"scopeList", "accountList"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String role = "USER";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Scope> scopeList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Account> accountList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Transaction> transactionList;
}
