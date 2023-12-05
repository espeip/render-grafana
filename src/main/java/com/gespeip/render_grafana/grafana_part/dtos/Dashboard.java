package com.gespeip.render_grafana.grafana_part.dtos;

import com.gespeip.render_grafana.grafana_part.services.DashboardService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Dashboard {


    private String title;
    private String uid;
    private List<Row> rows;
    private List<Panel> panels;
    private String timeFrom;
    private String timeTo;

    public Dashboard(DashboardService dashboardService) {
        title = dashboardService.getDashboardTitle();
        uid = dashboardService.getUid();
        timeFrom = dashboardService.getTimeFrom();
        timeTo = dashboardService.getTimeTo();
        panels = dashboardService.getPanels();
        rows = dashboardService.getRows();
    }


    public String printerPath(Panel panel) {
        return new StringBuilder()
                .append(uid)
                .append("/").append(title)
                .append("%20")
                .append("?")
                .append("orgId=1").append("&")
                .append("from=").append(timeFrom).append("&")
                .append("to=").append(timeTo).append("&")
                .append("width=1500").append("&")
                .append("height=750").append("&")
                .append("frameborder=0").append("&")
                .append(panel.printerPath())
                .toString();
    }
}
