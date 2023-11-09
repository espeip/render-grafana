package com.gespeip.render_grafana.confluence_part.configs;

import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfluenceUnirestConfig {

    private UnirestInstance unirest;

    ConfluenceUnirestConfig(@Value("${confluence.host}") String host, @Value("${confluence.token}") String token) {
        unirest = Unirest.spawnInstance();
        unirest.config().defaultBaseUrl("https://" + host)
                .addDefaultHeader("Authorization", "Bearer " + token)
                .addDefaultHeader("X-Atlassian-Token", "no-check")
//                .proxy("localhost", 8888)
//                .verifySsl(false)
        ;
    }
}
