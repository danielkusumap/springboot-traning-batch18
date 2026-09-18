package com.bootcamp.pokemonservice.repository;

import com.bootcamp.pokemonservice.entity.PokemonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonRepository extends JpaRepository<PokemonEntity, String> {
}
