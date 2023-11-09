package com.gespeip.render_grafana.confluence_part.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Page {
    private String title;
    private String ancestor;
    private String body;

}
