package com.example.myapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Getter
@Setter
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scope_id")
    private Scope scope;
}
