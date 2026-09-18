package com.bootcamp18.training.service;

import com.bootcamp18.training.dto.response.ResFeignDetailPokemonDto;

public interface PokemonService {
    ResFeignDetailPokemonDto getPokemonById(String id);
}
