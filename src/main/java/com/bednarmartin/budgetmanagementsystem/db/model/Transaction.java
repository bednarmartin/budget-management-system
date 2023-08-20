package com.bednarmartin.budgetmanagementsystem.db.model;

import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String description;
    @NonNull
    private TransactionType type;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    @NonNull
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @NonNull
    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

}
