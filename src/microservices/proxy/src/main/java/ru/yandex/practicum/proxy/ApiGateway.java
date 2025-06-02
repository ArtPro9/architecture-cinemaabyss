package ru.yandex.practicum.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("api")
public class ApiGateway {
    private final RestTemplate restTemplate = new RestTemplate();

    private final String monolithUrl;
    private final String moviesServiceUrl;
    private final String eventsServiceUrl;
    private final Boolean gradualMigration;
    private final Integer moviesMigrationPercent;

    public ApiGateway(@Value("${MONOLITH_URL:http://monolith:8080}") String monolithUrl,
                      @Value("${MOVIES_SERVICE_URL:http://movies-service:8081}") String moviesServiceUrl,
                      @Value("${EVENTS_SERVICE_URL:http://events-service:8082}") String eventsServiceUrl,
                      @Value("${GRADUAL_MIGRATION:true}") Boolean gradualMigration,
                      @Value("${MOVIES_MIGRATION_PERCENT:50}") Integer moviesMigrationPercent
    ) {
        this.monolithUrl = monolithUrl;
        this.moviesServiceUrl = moviesServiceUrl;
        this.eventsServiceUrl = eventsServiceUrl;
        this.gradualMigration = gradualMigration;
        this.moviesMigrationPercent = moviesMigrationPercent;
    }

    @GetMapping("movies")
    public ResponseEntity<String> getMovies(@RequestParam("id") Optional<String> id) {
        String url = monolithUrl;
        if (gradualMigration && useNewService()) {
            url = moviesServiceUrl;
        }
        url = url + "/api/movies";
        if (id.isPresent()) {
            url = url + "?id=" + id.get();
        }
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

    @PostMapping("movies")
    public ResponseEntity<String> createMovie(@RequestBody String movieData) {
        String url = monolithUrl;
        if (gradualMigration && useNewService()) {
            url = moviesServiceUrl;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(movieData, headers);
        return restTemplate.exchange(url + "/api/movies", HttpMethod.POST, request, String.class);
    }

    @GetMapping("users")
    public ResponseEntity<String> getUsers(@RequestParam("id") Optional<String> id) {
        String url = monolithUrl + "/api/users";
        if (id.isPresent()) {
            url = url + "?id=" + id.get();
        }
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

    @PostMapping("users")
    public ResponseEntity<String> createUser(@RequestBody String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        return restTemplate.exchange(monolithUrl + "/api/users", HttpMethod.POST, request, String.class);
    }

    @GetMapping("payments")
    public ResponseEntity<String> getPayments(@RequestParam("id") Optional<String> id) {
        String url = monolithUrl + "/api/payments";
        if (id.isPresent()) {
            url = url + "?id=" + id.get();
        }
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

    @PostMapping("payments")
    public ResponseEntity<String> createPayment(@RequestBody String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        return restTemplate.exchange(monolithUrl + "/api/payments", HttpMethod.POST, request, String.class);
    }

    @GetMapping("subscriptions")
    public ResponseEntity<String> getSubscriptions(@RequestParam("id") Optional<String> id) {
        String url = monolithUrl + "/api/subscriptions";
        if (id.isPresent()) {
            url = url + "?id=" + id.get();
        }
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

    @PostMapping("subscriptions")
    public ResponseEntity<String> createSubscription(@RequestBody String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        return restTemplate.exchange(monolithUrl + "/api/subscriptions", HttpMethod.POST, request, String.class);
    }

    private boolean useNewService() {
        return Math.random() < (double) moviesMigrationPercent / 100;
    }
}