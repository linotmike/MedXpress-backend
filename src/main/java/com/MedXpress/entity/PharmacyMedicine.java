package com.MedXpress.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "pharmacy_medicine",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_pharmacy_medicine_unique",
                        columnNames = {"pharmacy_id", "medicine_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyMedicine {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;

    @Column(name = "last_updated_at", nullable = false)
    private OffsetDateTime lastUpdatedAt;

    @PrePersist
    void onCreate() {
        lastUpdatedAt = OffsetDateTime.now();
        if (stockQuantity < 0) stockQuantity = 0;
    }

    @PreUpdate
    void onUpdate() {
        lastUpdatedAt = OffsetDateTime.now();
    }
}
