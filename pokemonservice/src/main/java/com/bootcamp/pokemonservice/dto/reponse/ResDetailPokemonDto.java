package com.bootcamp.pokemonservice.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResDetailPokemonDto {
    private String name;
    private String rarity;
}
