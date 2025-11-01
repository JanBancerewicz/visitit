package com.example.visitit.category.sync;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Component
public class SyncPublisher {

    private final RestClient client;

    public SyncPublisher() {
        // Fabryka z timeoutami (wspierane settery, bez deprecations)
        SimpleClientHttpRequestFactory simple = new SimpleClientHttpRequestFactory();
        simple.setConnectTimeout((int) Duration.ofSeconds(2).toMillis());
        simple.setReadTimeout((int) Duration.ofSeconds(3).toMillis());
        ClientHttpRequestFactory factory = simple;

        this.client = RestClient.builder()
                .baseUrl("http://localhost:8082") // albo "http://localhost:8080" przez gateway
                .requestFactory(factory)
                .build();
    }

    private void post(String path, Map<String, ?> body) {
        try {
            client.post().uri(path).body(body).retrieve().toBodilessEntity();
        } catch (Exception e) {
            System.err.println("[SYNC] " + path + " failed: " + e.getMessage());
        }
    }

    public void pushService(UUID id, String name)   { post("/internal-sync/categories/service",  Map.of("id", id, "name", name)); }
    public void pushClient(UUID id, String dn)      { post("/internal-sync/categories/client",   Map.of("id", id, "displayName", dn)); }
    public void pushEmployee(UUID id, String dn)    { post("/internal-sync/categories/employee", Map.of("id", id, "displayName", dn)); }
    public void pushRoom(UUID id, String name)      { post("/internal-sync/categories/room",     Map.of("id", id, "name", name)); }
}
