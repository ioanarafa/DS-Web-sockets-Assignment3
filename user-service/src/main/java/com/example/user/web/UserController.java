package com.example.user.web;

import com.example.user.model.User;
import com.example.user.model.SyncEvent;
import com.example.user.repo.UserRepository;
import com.example.user.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;
    private final RabbitTemplate rabbitTemplate;

    public UserController(UserRepository repo, RabbitTemplate rabbitTemplate) {
        this.repo = repo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public List<User> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        User saved = repo.save(user);
        publishUserEvent("CREATED", saved);
        return saved;
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User saved = repo.save(user);
        publishUserEvent("UPDATED", saved);
        return saved;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        User existing = repo.findById(id).orElse(null);
        repo.deleteById(id);
        SyncEvent ev = new SyncEvent();
        ev.setEntityType("USER");
        ev.setEventType("DELETED");
        ev.setUserId(id);
        if (existing != null) {
            ev.setUsername(existing.getUsername());
            ev.setEmail(existing.getEmail());
        }
        rabbitTemplate.convertAndSend(RabbitMQConfig.SYNC_QUEUE, ev);
    }

    private void publishUserEvent(String eventType, User user) {
        SyncEvent ev = new SyncEvent();
        ev.setEntityType("USER");
        ev.setEventType(eventType);
        ev.setUserId(user.getId());
        ev.setUsername(user.getUsername());
        ev.setEmail(user.getEmail());
        rabbitTemplate.convertAndSend(RabbitMQConfig.SYNC_QUEUE, ev);
    }
}
