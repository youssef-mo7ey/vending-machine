package com.task.vendingmachine.Models.DTO;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private String name;
    private Integer price;
    private Integer amount;
}
