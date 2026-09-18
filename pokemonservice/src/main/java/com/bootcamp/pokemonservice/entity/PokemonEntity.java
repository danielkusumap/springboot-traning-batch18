package com.bootcamp.pokemonservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Table(name = "mst_pokemon")
@Data
public class PokemonEntity {
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "rarity")
    private String rarity;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode rawData;

}
