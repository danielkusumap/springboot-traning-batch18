package com.bootcamp.pokemonservice.controller;

import com.bootcamp.pokemonservice.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseController {
    private final SseService sseService;

    @GetMapping("/subscribe/{eventKey}")
    public SseEmitter subscribe(
            @PathVariable("eventKey") String eventKey
    ){
        // membuat SSE connection dengan timeout 8 jam.
        SseEmitter emitter = new SseEmitter(8 * 60 * 60 * 1000L);
        sseService.addEmitter(eventKey, emitter);

        return emitter;
    }
}