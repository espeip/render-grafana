package com.gespeip.render_grafana.grafana_part;

import com.gespeip.render_grafana.grafana_part.dtos.Dashboard;
import com.gespeip.render_grafana.grafana_part.services.DashboardService;
import com.gespeip.render_grafana.grafana_part.services.PrintService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Getter
public class Grafana {
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private PrintService printService;
    private Dashboard dashboard;

    public void createImages(String uid, String runId) {
        dashboard = createDashboard(uid, runId);
        printService.init(dashboard);
        printService.print();
    }
    public Dashboard createDashboard(String uid, String runId) {
        dashboardService.init(uid, runId);
        return new Dashboard(dashboardService);
    }
}
