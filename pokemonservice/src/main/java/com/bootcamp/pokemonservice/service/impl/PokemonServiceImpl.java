package com.bootcamp.pokemonservice.service.impl;

import com.bootcamp.pokemonservice.dto.reponse.ResDetailPokemonDto;
import com.bootcamp.pokemonservice.dto.request.ReqCreatePokemonDto;
import com.bootcamp.pokemonservice.entity.PokemonEntity;
import com.bootcamp.pokemonservice.exception.BadRequestException;
import com.bootcamp.pokemonservice.exception.DataNotFoundException;
import com.bootcamp.pokemonservice.repository.PokemonRepository;
import com.bootcamp.pokemonservice.rest.PokemonClient;
import com.bootcamp.pokemonservice.service.PokemonService;
import com.bootcamp.pokemonservice.util.MinioUtil;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PokemonServiceImpl implements PokemonService {

    private final PokemonClient pokemonClient;
    private final PokemonRepository pokemonRepository;

    private final RedisTemplate<String, Object> redisTemplate;
    private final MinioUtil minioUtil;

    public PokemonServiceImpl(PokemonClient pokemonClient, PokemonRepository pokemonRepository, RedisTemplate<String, Object> redisTemplate, MinioUtil minioUtil) {
        this.pokemonClient = pokemonClient;
        this.pokemonRepository = pokemonRepository;
        this.redisTemplate = redisTemplate;
        this.minioUtil = minioUtil;
    }

    @Override
    public ResDetailPokemonDto createPokemon(ReqCreatePokemonDto reqCreatePokemonDto) {
        PokemonEntity pokemonEntity = new PokemonEntity();
        pokemonEntity.setName(reqCreatePokemonDto.getName());
        pokemonEntity.setRarity(reqCreatePokemonDto.getRarity());

        pokemonRepository.save(pokemonEntity);

        return ResDetailPokemonDto.builder()
                .name(pokemonEntity.getName())
                .rarity(pokemonEntity.getRarity())
                .build();
    }

    @Override
    public List<ResDetailPokemonDto> getAllPokemon() {
        List<PokemonEntity> listPokemon = pokemonRepository.findAll();

        // cara 1
//        return listPokemon.stream().map(pokemonEntity ->
//                ResDetailPokemonDto.builder()
//                        .name(pokemonEntity.getName())
//                        .rarity(pokemonEntity.getRarity())
//                        .build()).toList();

        // cara 2
        List<ResDetailPokemonDto> listDetail = new ArrayList<>();
        listPokemon.forEach(pokemonEntity ->
                listDetail.add(ResDetailPokemonDto.builder()
                        .name(pokemonEntity.getName())
                        .rarity(pokemonEntity.getRarity())
                        .build()));
        return listDetail;
    }


//    // dengan cacheable
//    @Override
//    @Cacheable(value = "pokemon", key = "#id")
//    public ResDetailPokemonDto getPokemonById(String id) {
//        log.info("Ini query di db");
//        // get dari redis
//        Optional<PokemonEntity> pokemonOpt = pokemonRepository.findById(id);
//        if (pokemonOpt.isEmpty()) throw new DataNotFoundException("Pokemon tidak ditemukan");
//        PokemonEntity pokemonEntity = pokemonOpt.get();
//        return ResDetailPokemonDto.builder()
//                .name(pokemonEntity.getName())
//                .rarity(pokemonEntity.getRarity())
//                .build();
//    }

    // tanpa @Cacheable
    @Override
    public ResDetailPokemonDto getPokemonById(String id) {
        String key = "pokemon:" + id;
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            log.info("Get pokemon from redis");
            return (ResDetailPokemonDto) cached;
        }
        log.info("Ini query di db");
        // get dari redis
        Optional<PokemonEntity> pokemonOpt = pokemonRepository.findById(id);
        if (pokemonOpt.isEmpty()) throw new DataNotFoundException("Pokemon tidak ditemukan");
        PokemonEntity pokemonEntity = pokemonOpt.get();
        ResDetailPokemonDto result = ResDetailPokemonDto.builder()
                .name(pokemonEntity.getName())
                .rarity(pokemonEntity.getRarity())
                .build();

        redisTemplate.opsForValue().set(key, result, 2, TimeUnit.MINUTES);
        return result;
    }

    @Override
    public void createPokemonByThirdParty() {
        try{
            JsonNode responsePokemon = pokemonClient.searchCards(null);
            JsonNode dataList = responsePokemon.get("data");

            if (dataList == null){
                throw new DataNotFoundException("Pokemon not found");
            }

            List<PokemonEntity> allPokemon = new ArrayList<>();
            dataList.forEach(pokemonNode ->
                    allPokemon.add(mapToEntity(pokemonNode))
            );
            pokemonRepository.saveAll(allPokemon);
        } catch (FeignException.InternalServerError e){
            throw new BadRequestException("Bad Request Feign");
        }
    }

    @Override
    public void uploadPokemonImage(MultipartFile file, String pokemonId) {
        try{
            minioUtil.uploadMultipart(file, pokemonId);
        } catch (Exception e){
            throw new BadRequestException("Failed to upload image: " + e.getMessage());
        }
    }

    @Override
    public void downloadPokemonImage(String pokemonId, HttpServletResponse response) {
        try{
            minioUtil.downloadFile(pokemonId, response);
        } catch (Exception e){
            throw new BadRequestException("Failed to download image: " + e.getMessage());
        }
    }

    private PokemonEntity mapToEntity(JsonNode node){
        PokemonEntity pokemonEntity = new PokemonEntity();
        pokemonEntity.setId(node.get("id").asText());
        pokemonEntity.setName(node.get("name").asText());
        pokemonEntity.setRarity(node.path("rarity").asText("-"));
        pokemonEntity.setRawData(node);
        return pokemonEntity;
    }
}
