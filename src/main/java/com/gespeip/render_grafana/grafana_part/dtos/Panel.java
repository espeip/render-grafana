package com.gespeip.render_grafana.grafana_part.dtos;

import com.gespeip.render_grafana.grafana_part.services.DashboardService;
import com.gespeip.render_grafana.utils.StringGen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class Panel {
    private int id;
    private String title;
    private Variables variables;
    private String unique;

    public Panel(int id, String title, Variables variables) {
        this.id = id;
        this.title = title;
        this.variables = variables;
        unique = StringGen.genString(48,57,7);
    }

    @Override
    public String toString() {
        return unique + ".png";
    }

    public String printerPath() {
        return new StringBuilder()
                .append(variables.toString())
                .append("panelId=").append(id)
                .toString();
    }
}
