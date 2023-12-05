package com.gespeip.render_grafana.utils;

import com.gespeip.render_grafana.confluence_part.Confluence;
import com.gespeip.render_grafana.grafana_part.Grafana;
import com.gespeip.render_grafana.grafana_part.dtos.Dashboard;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@Getter
public class AsyncService {

    @Autowired
    private Confluence confluence;

    @Autowired
    private Grafana grafana;

    private boolean processCompleted = false;
    private String pageId;


    @Async
    public void runProject(String project,
                           String runId,
                           String uid,
                           String testType) throws URISyntaxException, InterruptedException {
        grafana.createImages(uid, runId);
        pageId = confluence.createPage(testType, project, grafana.getDashboard());
        confluence.uploadPictures();
        processCompleted = true;
        System.out.println("completed");
        Thread.sleep(3000);
        processCompleted = false;
    }
}
