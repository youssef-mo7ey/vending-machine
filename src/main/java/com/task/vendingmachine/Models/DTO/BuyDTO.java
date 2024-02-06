package com.task.vendingmachine.Models.DTO;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyDTO {
    private Integer id;
    private Integer amount;
}
