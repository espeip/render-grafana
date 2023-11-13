package com.gespeip.render_grafana.utils;

import com.gespeip.render_grafana.confluence_part.Confluence;
import com.gespeip.render_grafana.grafana_part.Grafana;
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
    public CompletableFuture<String> getWaitingPage() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            System.out.println("wait");
        }
        return CompletableFuture.completedFuture("waiting page");
    }

    @Async
    public void runProject(String project,
                           String namePage,
                           String uid,
                           String testType) throws URISyntaxException, InterruptedException {
        grafana.createImages(uid);
        //Dashboard dashboard = grafana.createDashboard("QoxmTt27z");
        pageId = confluence.createPage(testType, project, namePage, grafana.getDashboard());
        confluence.uploadPictures();
        processCompleted = true;
        Thread.sleep(3000);
        processCompleted = false;
    }
}
