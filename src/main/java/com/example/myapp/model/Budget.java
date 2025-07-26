package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ToString(exclude = "user")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long budgetMonth;

    private Long budgetYear;

    private Double allocatedBudget;

    private Double remainingBudget;

    private Double usedBudget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scope_id")
    @JsonIgnore
    private Scope scope;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
