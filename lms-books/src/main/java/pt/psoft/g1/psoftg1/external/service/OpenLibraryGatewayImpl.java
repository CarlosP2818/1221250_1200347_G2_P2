package pt.psoft.g1.psoftg1.external.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Profile("open")
@Component
public class OpenLibraryGatewayImpl implements BookIsbnGateway {

    private final WebClient webClient;

    public OpenLibraryGatewayImpl(WebClient.Builder builder) {
        webClient = builder.baseUrl("https://openlibrary.org").build();
    }


    @Override
    public Optional<String> getIsbnByTitle(String title) {
        try {
            Mono<Map> responseMono = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search.json")
                            .queryParam("title", title)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class);

            Map<String, Object> response = responseMono.block();
            if (response != null && response.containsKey("docs")) {
                List<Map<String, Object>> docs = (List<Map<String, Object>>) response.get("docs");
                for (Map<String, Object> doc : docs) {
                    if (doc.containsKey("ia")) {
                        List<String> iaList = (List<String>) doc.get("ia");
                        for (String item : iaList) {
                            if (item.startsWith("isbn_")) {
                                return Optional.of(item.substring(5));
                            }
                        }
                    }
                }
            }

            return Optional.empty();

        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error fetching data from Open Library: " + e.getMessage(), e);
        }
    }
}
