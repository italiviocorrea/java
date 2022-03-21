package eti.italiviocorrea.api.paymentmethodsserver.domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(value = "payment_methods")
public class PaymentMethods implements Serializable {

    @Id
    @Column("id")
    int id;

    @Column("type")
    int type;

    @Column("description")
    String description;

    @Column("effective_date")
    LocalDate effectiveDate;

    @Column("expiration_date")
    LocalDate expirationDate;

}
