package com.gespeip.render_grafana.confluence_part.services;

import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Component;

@Component
public class ExtractorService {

    public String getBody(String templatePage) {
        String path = "$.body.storage.value";
        return JsonPath.read(templatePage, path);
    }

    public String getTitle(String templatePage) {
        String path = "$.title";
        return JsonPath.read(templatePage, path);
    }

    public String getPageID(String templatePage) {String path = "$.id";
        return JsonPath.read(templatePage, path);
    }

}
