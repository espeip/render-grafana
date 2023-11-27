package com.gespeip.render_grafana.grafana_part.services;

import com.gespeip.render_grafana.grafana_part.configs.GrafanaUnirestConfig;
import com.gespeip.render_grafana.grafana_part.dtos.Dashboard;
import com.gespeip.render_grafana.grafana_part.dtos.Panel;
import com.gespeip.render_grafana.grafana_part.dtos.Row;

import com.gespeip.render_grafana.utils.FilesUtils;
import kong.unirest.HttpResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class PrintService {
    @Autowired
    private FilesUtils filesUtils;
    @Autowired
    private GrafanaUnirestConfig guc;
    private Dashboard dashboard;


    public void init(Dashboard dashboard) {
        this.dashboard = dashboard;
    }


    private void print(Panel panel) {
        String dir = filesUtils.getPicturesDirectory();
        HttpResponse<File> response = guc.getUnirest()
                .get("/render/d-solo/" + dashboard.printerPath(panel))
                .asFile( "/Users/sergeitarasov/IdeaProjects/render-grafana/target/classes/pictures/" + panel.toString());
        File file = response.getBody();
//        guc.getUnirest().get("/render/d-solo/" + dashboard.printerPath(panel))
//                .asFile( dir + "/" + panel.getId() + panel.getTitle() +".png")
//                .getBody();
        System.out.println(guc.getUnirest().get("/render/d-solo/" + dashboard.printerPath(panel)).getUrl());
    }

    private void print(Row row) {
        for (Panel panel : row.getPanels())
            print(panel);
    }

    private void print(List<Panel> panels) {
        for (Panel panel : panels)
            print(panel);
    }

    public void print() {
        print(dashboard.getPanels());
        for (Row row : dashboard.getRows())
                print(row);
    }
}
