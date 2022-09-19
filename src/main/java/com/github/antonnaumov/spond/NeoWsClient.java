package com.github.antonnaumov.spond;

import com.github.antonnaumov.spond.domain.Asteroid;
import com.github.antonnaumov.spond.encoding.NeoResponseDeserializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@ApplicationScoped
public final class NeoWsClient {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
    @ConfigProperty(name = "neo-ws.api-url")
    String url;
    @ConfigProperty(name = "neo-ws.api-key")
    String apiKey;
    public Collection<Asteroid> asteroids(LocalDate start, LocalDate end) throws Exception {
        final var client = HttpClient.newHttpClient();
        final var uri = String.format("%s/feed?start_date=%s&end_date=%s&api_key=%s",
                url, start.format(dateFormatter), end.format(dateFormatter), apiKey);
        final var req = HttpRequest.
                newBuilder(URI.create(uri)).
                GET().
                build();
        final var resp = client.send(req, HttpResponse.BodyHandlers.ofInputStream());
        return new NeoResponseDeserializer().deserialize(resp.body());
    }
}
