package com.bootcamp18.training.controller;

import com.bootcamp18.training.dto.response.BaseResponse;
import com.bootcamp18.training.dto.response.ResFeignDetailPokemonDto;
import com.bootcamp18.training.service.PokemonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/v1/pokemon")
public class PokemonController {
    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ResFeignDetailPokemonDto>> pokemonById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(
                BaseResponse.<ResFeignDetailPokemonDto>builder()
                        .data(pokemonService.getPokemonById(id))
                        .build()
        );
    }
}
