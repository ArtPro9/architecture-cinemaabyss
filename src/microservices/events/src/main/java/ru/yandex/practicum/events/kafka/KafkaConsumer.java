package ru.yandex.practicum.events.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @KafkaListener(topics = {"movie-events", "payment-events", "user-events"})
    public void listen(String message) {
        logger.info("Consumed message: {}", message);
    }
}
