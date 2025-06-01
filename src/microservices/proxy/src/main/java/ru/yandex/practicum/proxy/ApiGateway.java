package ru.yandex.practicum.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    public ResponseEntity<String> getMovies() {
        String url = monolithUrl;
        if (gradualMigration && useNewService()) {
            url = moviesServiceUrl;
        }
        return restTemplate.exchange(url + "/api/movies", HttpMethod.GET, null, String.class);
    }

    @GetMapping("users")
    public ResponseEntity<String> getUsers() {
        return restTemplate.exchange(monolithUrl + "/api/users", HttpMethod.GET, null, String.class);
    }

    private boolean useNewService() {
        return Math.random() < (double) moviesMigrationPercent / 100;
    }
}
