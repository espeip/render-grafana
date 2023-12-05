package com.gespeip.render_grafana;


import com.gespeip.render_grafana.confluence_part.Confluence;
import com.gespeip.render_grafana.grafana_part.Grafana;
import com.gespeip.render_grafana.utils.AsyncService;
import com.gespeip.render_grafana.utils.FilesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.net.URISyntaxException;


@Controller
public class MainController {

    @Autowired
    private Confluence confluence;

    @Autowired
    private Grafana grafana;
    @Autowired
    private FilesUtils filesUtils;
    @Autowired
    private AsyncService asyncService;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "Render-Grafana");
        return "home";
    }


    @GetMapping("/run")
    @ResponseBody
    public String testPrint(
            @RequestParam String project,
            @RequestParam String runId,
            @RequestParam String uid,
            @RequestParam String testType
    ) throws URISyntaxException, InterruptedException {

        asyncService.runProject(project,runId,uid,testType);
        return "ok";
    }


    @GetMapping("/checkStatus")
    @ResponseBody
    public String checkProcessStatus() {
        if (asyncService.isProcessCompleted()) {
            return "completed";
        } else {
            return "in_progress";
        }
    }

    @GetMapping("/getPageId")
    @ResponseBody
    public String finalPage(Model model){
        return  asyncService.getPageId();
    }

}
