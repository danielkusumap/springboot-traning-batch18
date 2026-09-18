package com.bootcamp18.training.service.impl;

import com.bootcamp18.training.dto.response.BaseResponse;
import com.bootcamp18.training.dto.response.ResFeignDetailPokemonDto;
import com.bootcamp18.training.rest.PokemonClient;
import com.bootcamp18.training.service.PokemonService;
import org.springframework.stereotype.Service;

@Service
public class PokemonServiceImpl implements PokemonService {
    private final PokemonClient pokemonClient;

    public PokemonServiceImpl(PokemonClient pokemonClient) {
        this.pokemonClient = pokemonClient;
    }

    @Override
    public ResFeignDetailPokemonDto getPokemonById(String id) {
        BaseResponse<ResFeignDetailPokemonDto> response = pokemonClient.getPokemonById(id);
        ResFeignDetailPokemonDto output = new ResFeignDetailPokemonDto();
        output.setName(response.getData().getName());
        output.setRarity(response.getData().getRarity());
        return output;
    }
}
