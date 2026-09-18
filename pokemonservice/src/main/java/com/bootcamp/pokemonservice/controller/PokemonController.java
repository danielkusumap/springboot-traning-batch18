package com.bootcamp.pokemonservice.controller;

import com.bootcamp.pokemonservice.dto.reponse.BaseResponse;
import com.bootcamp.pokemonservice.dto.reponse.ResDetailPokemonDto;
import com.bootcamp.pokemonservice.dto.request.ReqCreatePokemonDto;
import com.bootcamp.pokemonservice.service.PokemonService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/rest/v1/pokemons")
public class PokemonController {
    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ResDetailPokemonDto>> createPokemon(
            @RequestBody ReqCreatePokemonDto reqCreatePokemonDto
            ) {
        return ResponseEntity.ok(
               BaseResponse.<ResDetailPokemonDto>builder()
                       .data(pokemonService.createPokemon(reqCreatePokemonDto))
                       .build()
        );
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<ResDetailPokemonDto>>> listPokemon(){
        return ResponseEntity.ok(
                BaseResponse.<List<ResDetailPokemonDto>>builder()
                        .data(pokemonService.getAllPokemon())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ResDetailPokemonDto>> pokemonById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(
                BaseResponse.<ResDetailPokemonDto>builder()
                        .data(pokemonService.getPokemonById(id))
                        .build()
        );
    }

    @PostMapping("/sync")
    public ResponseEntity<BaseResponse<Object>> syncPokemon(){
        pokemonService.createPokemonByThirdParty();
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .data(null)
                        .build()
        );
    }

    @PostMapping("/image/{pokemonId}")
    public ResponseEntity<BaseResponse<String>> uploadImage(
            @PathVariable String pokemonId,
            @RequestParam("file")MultipartFile file
            ){
        pokemonService.uploadPokemonImage(file, pokemonId);
        BaseResponse<String> response = new BaseResponse<>();
        response.setMessage("Image uploaded");
        response.setData(pokemonId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/image/download/{filename}")
    public void downloadImage(
            @PathVariable String filename,
            HttpServletResponse response
    ) {
        pokemonService.downloadPokemonImage(filename, response);
    }
}
