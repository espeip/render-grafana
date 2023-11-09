package com.gespeip.render_grafana.grafana_part.dtos;

import com.gespeip.render_grafana.grafana_part.services.DashboardService;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@AllArgsConstructor
public class Row {
    private String title;
    private List<Panel> panels;
    private String repeat;

}
