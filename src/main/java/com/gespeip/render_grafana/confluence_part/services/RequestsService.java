package com.gespeip.render_grafana.confluence_part.services;

import com.gespeip.render_grafana.confluence_part.configs.ConfluenceUnirestConfig;
import com.gespeip.render_grafana.confluence_part.dtos.Page;
import com.gespeip.render_grafana.utils.ResourceReader;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

@Service
@AllArgsConstructor
public class RequestsService {
    @Autowired
    private ConfluenceUnirestConfig cuc;
    @Autowired
    private TemplateService templateService;

    public String getTemplateReport(String pageId) {
        return cuc.getUnirest().get("/rest/api/content/" + pageId)
                .queryString("expand", "body.storage")
                .asString()
                .getBody();
    }

    public String createPage(Page page) {
        return cuc.getUnirest().post("/rest/api/content/")
                .contentType("application/json")
                .body(templateService.getPageBody(page))
                .asString()
                .getBody();
    }

    public void numberedSetTrue(String pageId) {
        cuc.getUnirest()
                .put("/rest/numberedheadings/1.0/page-configuration/" + pageId)
                .body(templateService.getNumberedBody());
    }

    public void uploadPicture(String pageId, File file) {
        String path = String.format("/rest/api/content/%s/child/attachment", pageId);
        cuc.getUnirest()
                .post(path)
                .field("file", file)
                .asEmpty()
        ;
    }


}
