package com.bootcamp.pokemonservice.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductViewMessage {
    private String pokemonName;
    private String viewedAt;
}
