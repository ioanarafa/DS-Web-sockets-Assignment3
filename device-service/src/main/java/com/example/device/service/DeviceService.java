package com.example.device.service;

import com.example.device.config.RabbitMQConfig;
import com.example.device.model.Device;
import com.example.device.model.SyncEvent;
import com.example.device.repo.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repo;
    private final RabbitTemplate rabbitTemplate;

    public Device create(Device d) {
        Device saved = repo.save(d);

        SyncEvent ev = new SyncEvent();
        ev.setEntityType("DEVICE");
        ev.setEventType("CREATED");
        ev.setDeviceId(saved.getId());
        ev.setName(saved.getName());
        ev.setMaxConsumption(saved.getMaxConsumption());
        ev.setUserId(saved.getUserId());

        rabbitTemplate.convertAndSend("", RabbitMQConfig.SYNC_QUEUE, ev);
        return saved;
    }

    public Device assignUser(Long deviceId, Long userId) {
        Device d = repo.findById(deviceId).orElseThrow();
        d.setUserId(userId);
        Device saved = repo.save(d);

        SyncEvent ev = new SyncEvent();
        ev.setEntityType("DEVICE");
        ev.setEventType("UPDATED");
        ev.setDeviceId(saved.getId());
        ev.setName(saved.getName());
        ev.setMaxConsumption(saved.getMaxConsumption());
        ev.setUserId(saved.getUserId());

        rabbitTemplate.convertAndSend("", RabbitMQConfig.SYNC_QUEUE, ev);
        return saved;
    }

    public void delete(Long id) {
        repo.deleteById(id);

        SyncEvent ev = new SyncEvent();
        ev.setEntityType("DEVICE");
        ev.setEventType("DELETED");
        ev.setDeviceId(id);

        rabbitTemplate.convertAndSend("", RabbitMQConfig.SYNC_QUEUE, ev);
    }
}
