package com.bootcamp18.training.rest;

import com.bootcamp18.training.dto.response.BaseResponse;
import com.bootcamp18.training.dto.response.ResFeignDetailPokemonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pokemonservice")
public interface PokemonClient {
    @GetMapping("/pokemon/rest/v1/pokemons/{id}")
    BaseResponse<ResFeignDetailPokemonDto> getPokemonById(
            @PathVariable String id
    );
}
