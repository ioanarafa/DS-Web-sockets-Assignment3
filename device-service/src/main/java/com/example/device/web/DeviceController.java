package com.example.device.web;

import com.example.device.model.Device;
import com.example.device.model.SyncEvent;
import com.example.device.repo.DeviceRepository;
import com.example.device.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    private final DeviceRepository repo;
    private final RabbitTemplate rabbitTemplate;

    public DeviceController(DeviceRepository repo, RabbitTemplate rabbitTemplate) {
        this.repo = repo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public List<Device> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Device getById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PostMapping
    public Device create(@RequestBody Device d) {
        Device saved = repo.save(d);
        publishDeviceEvent("CREATED", saved);
        return saved;
    }

    @PutMapping("/{id}")
    public Device update(@PathVariable Long id, @RequestBody Device d) {
        d.setId(id);
        Device saved = repo.save(d);
        publishDeviceEvent("UPDATED", saved);
        return saved;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        Device existing = repo.findById(id).orElse(null);
        repo.deleteById(id);
        SyncEvent ev = new SyncEvent();
        ev.setEntityType("DEVICE");
        ev.setEventType("DELETED");
        ev.setDeviceId(id);
        if (existing != null) {
            ev.setName(existing.getName());
            ev.setMaxConsumption(existing.getMaxConsumption());
        }
        rabbitTemplate.convertAndSend(RabbitMQConfig.SYNC_QUEUE, ev);
    }

    private void publishDeviceEvent(String eventType, Device device) {
        SyncEvent ev = new SyncEvent();
        ev.setEntityType("DEVICE");
        ev.setEventType(eventType);
        ev.setDeviceId(device.getId());
        ev.setName(device.getName());
        ev.setMaxConsumption(device.getMaxConsumption());
        rabbitTemplate.convertAndSend(RabbitMQConfig.SYNC_QUEUE, ev);
    }

    @PostMapping("/{deviceId}/assign/{userId}")
    public Device assignUser(@PathVariable Long deviceId, @PathVariable Long userId) {
        Device d = repo.findById(deviceId).orElseThrow();
        d.setUserId(userId);
        return repo.save(d);
    }

    @GetMapping("/user/{userId}")
    public List<Device> byUser(@PathVariable Long userId) {
        return repo.findByUserId(userId);
    }
}