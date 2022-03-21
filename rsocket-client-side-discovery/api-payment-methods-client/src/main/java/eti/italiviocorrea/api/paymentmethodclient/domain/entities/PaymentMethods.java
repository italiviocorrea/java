package eti.italiviocorrea.api.paymentmethodclient.domain.entities;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethods implements Serializable {

    int id;
    int type;
    String description;
    LocalDate effectiveDate;
    LocalDate expirationDate;

}
