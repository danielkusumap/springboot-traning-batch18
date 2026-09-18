package com.bootcamp.pokemonservice.dto.request;

import lombok.Data;

@Data
public class ReqCreatePokemonDto {
    private String name;
    private String rarity;
}
