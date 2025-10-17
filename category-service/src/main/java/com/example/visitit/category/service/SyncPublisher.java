package com.example.visitit.category.service;

import com.example.visitit.category.dto.sync.ClientSyncDTO;
import com.example.visitit.category.dto.sync.EmployeeSyncDTO;
import com.example.visitit.category.dto.sync.RoomSyncDTO;
import com.example.visitit.category.dto.sync.ServiceSyncDTO;
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

    // fragment: opcjonalne przeciążenia
    public void upsertClient(ClientSyncDTO d){ upsertClient(d.id(), d.displayName()); }
    public void upsertEmployee(EmployeeSyncDTO d){ upsertEmployee(d.id(), d.displayName()); }
    public void upsertService(ServiceSyncDTO d){ upsertService(d.id(), d.name()); }
    public void upsertRoom(RoomSyncDTO d){ upsertRoom(d.id(), d.name()); }

}
