package com.bootcamp18.training.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResFeignDetailPokemonDto {
    private String name;
    private String rarity;
}
