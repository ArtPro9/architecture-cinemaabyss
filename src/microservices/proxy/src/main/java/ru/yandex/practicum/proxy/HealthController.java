package ru.yandex.practicum.proxy;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements HealthIndicator {
    @Override
    @GetMapping("health")
    public Health health() {
        return new Health.Builder().up().build();
    }
}
