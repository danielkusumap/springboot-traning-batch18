package com.bootcamp.pokemonservice.service;

import com.bootcamp.pokemonservice.dto.reponse.ResDetailPokemonDto;
import com.bootcamp.pokemonservice.dto.request.ReqCreatePokemonDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PokemonService {
    ResDetailPokemonDto createPokemon(ReqCreatePokemonDto reqCreatePokemonDto);
    List<ResDetailPokemonDto> getAllPokemon();
    ResDetailPokemonDto getPokemonById(String id);
    void createPokemonByThirdParty();
    void uploadPokemonImage(MultipartFile file, String pokemonId);
    void downloadPokemonImage(String pokemonId, HttpServletResponse response);
}
