package com.gespeip.render_grafana.grafana_part.dtos;

import com.gespeip.render_grafana.grafana_part.services.DashboardService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
public class Variable {
    String title;
    List<String> current_values;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String value : current_values)
            sb.append("var-").append(title).append("=").append(value).append("&");
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
