package com.gespeip.render_grafana.grafana_part.configs;

import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Getter
public class GrafanaUnirestConfig {

    private UnirestInstance unirest;

    GrafanaUnirestConfig(@Value("${grafana.host}") String host, @Value("${grafana.token}") String token) {
        unirest = Unirest.spawnInstance();
        unirest
                .config()
                .defaultBaseUrl("https://" + host)
                .addDefaultHeader("Authorization", "Bearer " + token)
        ;
    }
}
