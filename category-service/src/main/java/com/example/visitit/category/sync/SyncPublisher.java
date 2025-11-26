package com.example.visitit.category.sync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class SyncPublisher {

    private final RestClient client;

    public SyncPublisher(
            @Value("${elements.base-url:http://elements-service:8080}") String baseUrl,
            @Value("${elements.connect-timeout-ms:2000}") int connectTimeoutMs,
            @Value("${elements.read-timeout-ms:3000}") int readTimeoutMs
    ) {
        SimpleClientHttpRequestFactory simple = new SimpleClientHttpRequestFactory();
        simple.setConnectTimeout(connectTimeoutMs);
        simple.setReadTimeout(readTimeoutMs);
        ClientHttpRequestFactory factory = simple;

        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();

        log.info("[SYNC] baseUrl={}, connectTimeoutMs={}, readTimeoutMs={}",
                baseUrl, connectTimeoutMs, readTimeoutMs);
    }

    private void post(String path, Map<String, ?> body) {
        try {
            client.post()
                    .uri(path)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            log.debug("[SYNC] POST {} OK", path);
        } catch (Exception e) {
            // miękkie – brak blokowania CRUD po stronie category-service
            log.warn("[SYNC] POST {} failed: {}", path, e.getMessage());
        }
    }

    public void pushService(UUID id, String name) {
        post("/internal-sync/categories/service", Map.of("id", id, "name", name));
    }

    public void pushClient(UUID id, String displayName) {
        post("/internal-sync/categories/client", Map.of("id", id, "displayName", displayName));
    }

    public void pushEmployee(UUID id, String displayName) {
        post("/internal-sync/categories/employee", Map.of("id", id, "displayName", displayName));
    }

    public void pushRoom(UUID id, String name) {
        post("/internal-sync/categories/room", Map.of("id", id, "name", name));
    }
}
