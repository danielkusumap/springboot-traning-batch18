package com.bootcamp.pokemonservice.service.impl;

import com.bootcamp.pokemonservice.service.SseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class SseServiceImpl implements SseService {
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

    @Override
    public void addEmitter(String key, SseEmitter emitter) {
        List<SseEmitter> sseEmitters = this.emitters.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>());
        setupCallbacks(key, emitter, sseEmitters);

        try {
            sseEmitters.add(emitter);

            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .name("connection-ready")
                    .data("Connected to " + key + " stream");

            emitter.send(event);
            log.info("Client connected to stream: {}", key);
        } catch (IOException e) {
            log.warn("Failed to send initial event for key: {}, removing emitter", key);
            sseEmitters.remove(emitter);
        } catch (Exception e) {
            log.error("Unexpected error adding emitter for key: {}", key, e);
            sseEmitters.remove(emitter);
            throw e;
        }
    }

    private void setupCallbacks(String key, SseEmitter emitter, List<SseEmitter> sseEmitters) {
        Runnable cleanup = () -> {
            sseEmitters.remove(emitter);
            log.debug("Emitter removed for key: {}", key);
        };

        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError((ex) -> cleanup.run());
    }

    @Override
    public void sendEvent(String key,String eventName, Object eventData) {
        List<SseEmitter> sseEmitters = emitters.get(key);

        if (sseEmitters == null || sseEmitters.isEmpty()) {
            return;
        }

        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : sseEmitters) {
            try {
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .name(eventName)
                        .data(eventData);

                emitter.send(event);
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }

        if (!deadEmitters.isEmpty()) {
            sseEmitters.removeAll(deadEmitters);
            log.info("Cleaned up {} dead emitters for key: {}", deadEmitters.size(), key);
        }
    }
}