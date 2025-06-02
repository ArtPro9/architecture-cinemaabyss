package ru.yandex.practicum.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.events.kafka.KafkaProducer;

@RestController
@RequestMapping("api/events")
public class EventsController {
    private final ObjectMapper objectMapper = new JsonMapper();
    private final KafkaProducer kafkaProducer;

    public EventsController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("movie")
    public ResponseEntity<String> addMovieEvent(@RequestBody String body) throws Exception {
        kafkaProducer.sendMessage(body, "movie-events");
        return ResponseEntity.status(201).body(objectMapper.writeValueAsString(new Status("success")));

    }

    @PostMapping("payment")
    public ResponseEntity<String> addPaymentEvent(@RequestBody String body) throws Exception {
        kafkaProducer.sendMessage(body, "payment-events");
        return ResponseEntity.status(201).body(objectMapper.writeValueAsString(new Status("success")));
    }

    @PostMapping("user")
    public ResponseEntity<String> addUserEvent(@RequestBody String body) throws Exception {
        kafkaProducer.sendMessage(body, "user-events");
        return ResponseEntity.status(201).body(objectMapper.writeValueAsString(new Status("success")));
    }

    @GetMapping("health")
    public String health() throws Exception {
        return objectMapper.writeValueAsString(new Status(true));
    }

    record Status(Object status) {
    }
}
