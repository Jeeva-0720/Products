package org.productsstore.products.Dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentGatewayRequestDto {
    public Long orderId;
    public Long amount;
}
