package com.gespeip.render_grafana;


import com.gespeip.render_grafana.app.GrafanaDashboard;
import com.gespeip.render_grafana.app.Grafana_Auth;
import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class MainController {

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "Render-Grafana");
        return "home";
    }

    public static void zipFile(String path) throws IOException {
        File [] files = new File(path).listFiles();
        ZipOutputStream os = new ZipOutputStream(new FileOutputStream("zipout.zip"));
        for(File f : files) {
            FileInputStream fileInputStream =
                    new FileInputStream(path + f.getName());
            ZipEntry zipEntry = new ZipEntry(f.getName());
            os.putNextEntry(zipEntry);
            os.write(IOUtils.toByteArray(fileInputStream));
        }
        os.close();
    }

    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download(@RequestParam String login, @RequestParam String pass, @RequestParam String url) throws IOException, ParseException {
        Grafana_Auth grafana_auth;
        if (login == "")
            grafana_auth = new Grafana_Auth(url);
        else
            grafana_auth = new Grafana_Auth(url, login, pass);
        GrafanaDashboard grafanaDashboard = new GrafanaDashboard(grafana_auth);
        grafanaDashboard.print();
        grafana_auth.delete_token();


        zipFile(grafanaDashboard.getPATH());
        File file = new File("zipout.zip");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment;filename=zipout.zip");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
