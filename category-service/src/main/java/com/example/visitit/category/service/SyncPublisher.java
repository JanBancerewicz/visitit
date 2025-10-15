package com.example.visitit.category.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map; import java.util.UUID;

@Service @RequiredArgsConstructor
public class SyncPublisher {
    private static final Logger log = LoggerFactory.getLogger(SyncPublisher.class);
    private final WebClient elementsClient;

    public void upsertClient(UUID id, String displayName){
        post("client", Map.of("id", id.toString(), "displayName", displayName));
    }
    public void upsertEmployee(UUID id, String displayName){
        post("employee", Map.of("id", id.toString(), "displayName", displayName));
    }
    public void upsertService(UUID id, String name){
        post("service", Map.of("id", id.toString(), "name", name));
    }
    public void upsertRoom(UUID id, String name){
        post("room", Map.of("id", id.toString(), "name", name));
    }
    public void delete(String type, UUID id){
        try { elementsClient.delete().uri("/internal-sync/categories/{t}/{id}", type, id).retrieve()
                .toBodilessEntity().block(); }
        catch (Exception e){ log.warn("SYNC delete {} {} failed: {}", type, id, e.toString()); }
    }

    private void post(String type, Map<String,Object> body){
        try { elementsClient.post().uri("/internal-sync/categories/{t}", type).bodyValue(body)
                .retrieve().toBodilessEntity().block(); }
        catch (Exception e){ log.warn("SYNC upsert {} {} failed: {}", type, body.get("id"), e.toString()); }
    }
}
