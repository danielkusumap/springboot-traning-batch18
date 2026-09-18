package com.bootcamp.pokemonservice.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "pokemonClient", url = "https://api.pokemontcg.io/v2")
public interface PokemonClient {
    @GetMapping("/cards")
    JsonNode searchCards(@RequestParam("q") String query);
}
