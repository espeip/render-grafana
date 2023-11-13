package com.gespeip.render_grafana.confluence_part;

import com.gespeip.render_grafana.confluence_part.dtos.Page;
import com.gespeip.render_grafana.confluence_part.services.ExtractorService;
import com.gespeip.render_grafana.confluence_part.services.RequestsService;
import com.gespeip.render_grafana.confluence_part.services.TemplateService;
import com.gespeip.render_grafana.grafana_part.dtos.Dashboard;
import com.gespeip.render_grafana.utils.FilesUtils;
import com.google.common.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URISyntaxException;

@Component
public class Confluence {
    @Autowired
    private RequestsService requestsService;
    @Autowired
    private ExtractorService extractorService;
    @Autowired
    private FilesUtils filesUtils;
    @Autowired
    private TemplateService templateService;

    private String pageId;

    public String createPage(String templatePageId, String ancestors, String title, Dashboard dashboard) {
        String templateBody = extractorService
                .getBody(requestsService
                        .getTemplateReport(templatePageId))
                .replace("\"","\\\"")
                .replace("${graphics}", templateService.getAllGraphics(dashboard))
        ;

        Page page = new Page(title, ancestors, templateBody);

        String newPageResponse = requestsService.createPage(page);
        pageId = extractorService.getPageID(newPageResponse);
        requestsService.numberedSetTrue(pageId);
        return pageId;
    }
    public void uploadPictures() throws URISyntaxException {
        for(File file : filesUtils.getPictures())
            requestsService.uploadPicture(pageId, file);
        filesUtils.deletePictures();
    }
}
