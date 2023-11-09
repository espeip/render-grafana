package com.gespeip.render_grafana.confluence_part.services;

import com.gespeip.render_grafana.confluence_part.dtos.Page;
import com.gespeip.render_grafana.grafana_part.dtos.Dashboard;
import com.gespeip.render_grafana.grafana_part.dtos.Panel;
import com.gespeip.render_grafana.grafana_part.dtos.Row;
import com.gespeip.render_grafana.utils.ResourceReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TemplateService {
    @Autowired
    private ResourceReader reader;
    @Value("${confluence.space}")
    private String space;
    @Value("${confluence.templatePath}")
    private String templatePath;
    @Value("${confluence.numberedPath}")
    private String numberedPath;
    @Value("${confluence.graphicsPath}")
    private String graphicsPath;

    public String getPageBody(Page page) {
        return reader
                .readResource(templatePath)
                .replace("${title}", page.getTitle())
                .replace("${ancestors}", page.getAncestor())
                .replace("${space}", space)
                .replace("${body}", page.getBody());
    }

    public String getNumberedBody() {
        return reader
                .readResource(numberedPath);
    }

    private String getGraphic(Panel panel) {
        return reader
                .readResource(graphicsPath)
                .replace("${image}",panel.toString());
    }
    private String getRow(Row row) {
        StringBuilder sb = new StringBuilder();
        sb.append("<p class=\\\"auto-cursor-target\\\"><br /></p><ac:structured-macro ac:name=\\\"expand\\\" ac:schema-version=\\\"1\\\" ac:macro-id=\\\"a933e70a-76f5-4d26-99ce-5d1d3e05e79b\\\"><ac:parameter ac:name=\\\"title\\\">\\\"")
                .append(row.getTitle())
                .append("\\\"</ac:parameter><ac:rich-text-body>");
        for (Panel panel : row.getPanels())
            sb.append(getGraphic(panel));
        sb.append("</ac:rich-text-body></ac:structured-macro><p class=\\\"auto-cursor-target\\\"><br /></p>");
        return sb.toString();
    }
    private String getPanel(Panel panel) {
        return
                new StringBuilder()
                        .append("<p>")
                        .append(panel.toString())
                        .append("</p>")
                        .append(getGraphic(panel))
                        .toString();
    }

    public String getAllGraphics(Dashboard dashboard) {
        StringBuilder sb = new StringBuilder();
        for (Panel panel : dashboard.getPanels())
            sb.append(getGraphic(panel));
        for (Row row : dashboard.getRows())
            sb.append(getRow(row));
        return sb.toString();
    }
}
