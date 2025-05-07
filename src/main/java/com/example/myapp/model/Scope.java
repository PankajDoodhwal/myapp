package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Getter
@Setter
@ToString(exclude = "user")
public class Scope {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scopeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "scope", cascade = CascadeType.ALL)
    private List<Category> categories;
}
