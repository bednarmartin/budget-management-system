package com.bednarmartin.budgetmanagementsystem.db.model;

import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    @Column(name = "transaction_type")
    private TransactionType transactionType;
    @NonNull
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @NonNull
    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;
}
