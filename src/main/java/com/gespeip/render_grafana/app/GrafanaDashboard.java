package com.gespeip.render_grafana.app;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class GrafanaDashboard {
    private Map<String,String> panelIDAndGraphicName;
    private String from;
    private String to;
    private Map<String, String> variables;
    private String dashboardUID;
    private String dashboardName;
    private String json;
    private String token;
    private String host;
    private String uid;
    private String PATH;

    public String getPATH() {
        return PATH;
    }

    public GrafanaDashboard(Grafana_Auth grafana_auth) throws ParseException, IOException {;
        token = grafana_auth.token;
        host = grafana_auth.host;
        uid = grafana_auth.uid;
        json = getJSON();
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
        variables = getVariables(jsonObject);
        dashboardUID = getDashboardUID(jsonObject);
        panelIDAndGraphicName = getPanelID(jsonObject);
        from = getTimeFrom(jsonObject);
        to = getTimeTo(jsonObject);
        dashboardName = getDashboardName(jsonObject);
        PATH = "src/main/resources/pictures/";
    }
    private static String getDashboardUID(JSONObject jsonObject) {
        JSONObject dashboard = (JSONObject) jsonObject.get("dashboard");
        return (String) dashboard.get("uid");
    }
    private static String getDashboardName(JSONObject jsonObject) {
        JSONObject dashboard = (JSONObject) jsonObject.get("meta");
        return (String) dashboard.get("slug");
    }
    private static Map<String, String> getVariables(JSONObject jsonObject) {
        Map<String, String> map = new HashMap<>();
        JSONObject dashboard = (JSONObject) jsonObject.get("dashboard");
        JSONObject templating = (JSONObject) dashboard.get("templating");
        JSONArray list = (JSONArray) templating.get("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject temp = (JSONObject) list.get(i);
            String name = (String) temp.get("name");
            JSONObject current = (JSONObject) temp.get("current");
            Object value = current.get("value");
            String objectOrArray = value.getClass().toString();
            if (objectOrArray.equals("class java.lang.String")) {
                map.put(value.toString(), name);
            } else {
                JSONArray values = (JSONArray) value;
                for (int j = 0; j < values.size(); j++) {
                    map.put((String) values.get(j), name);
                }
            }
        }
        return map;
    }
    private static String getTimeFrom(JSONObject jsonObject) {
        JSONObject dashboard = (JSONObject) jsonObject.get("dashboard");
        JSONObject time = (JSONObject) dashboard.get("time");
        return (String) time.get("from");
    }
    private static String getTimeTo(JSONObject jsonObject) {
        JSONObject dashboard = (JSONObject) jsonObject.get("dashboard");
        JSONObject time = (JSONObject) dashboard.get("time");
        return (String) time.get("to");
    }
    private static Map<String,String > getPanelID(JSONObject jsonObject) {
        Map<String, String> map = new HashMap<>();
        JSONObject dashboard = (JSONObject) jsonObject.get("dashboard");
        JSONArray panels = (JSONArray) dashboard.get("panels"); //jsonObject.get("id").toString();
        for (int i = 0; i < panels.size(); i++) {
            JSONObject temp = (JSONObject) panels.get(i);
            JSONArray arrays= (JSONArray) temp.get("panels");
            if (arrays != null) {
                for (Object o : arrays) {
                    JSONObject jo = (JSONObject) o;
                    if (jo.get("type").toString().equals("graph"))
                        map.put(jo.get("id").toString(), jo.get("title").toString());
                }
            } else {
                if (temp.get("type").toString().equals("graph"))
                    map.put(temp.get("id").toString(), temp.get("title").toString());
            }
        }
        return map;
    }

    private String getJSON() throws IOException {
        URL rssURL = new URL("http://" + host + "/api/dashboards/uid/" + uid);
        URLConnection yc = rssURL.openConnection();
        yc.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(yc.getInputStream()));

        StringBuilder response = new StringBuilder();
        String line = bufReader.readLine();

        while (line != null) {
            response.append(line).append("\n");
            line = bufReader.readLine();
        }
        bufReader.close();

        return response.toString();
    }


    public void print() throws IOException {

        //добавление переменных
        for (Map.Entry<String,String> panel : panelIDAndGraphicName.entrySet()) {
            Map<String, String> params = new HashMap<>();
            params.put("from", from);
            params.put("to", to);
            params.put("panelId", panel.getKey());
            params.put("width", "1000");
            params.put("height", "500");
            params.put("tz", "Europe%2FMoscow");

            //добавление переменных var в path-часть
            for (Map.Entry<String, String> vars : variables.entrySet()) {
                params.put("var-" + vars.getValue(), vars.getKey());
            }
            //соединение в одну строку с помощью & и =
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(param.getKey());
                postData.append('=');
                postData.append(param.getValue());
            }


            //отправка запроса на рендер
            URL url = new URL("http://" + host + "/render/d-solo/" +
                    dashboardUID + "/" +
                    dashboardName + "?" + postData.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //добавление Headers
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setConnectTimeout(3600000);

            //рендер картинок
            Image image = ImageIO.read(new BufferedInputStream(connection.getInputStream()));
            ImageIO.write((RenderedImage) image, "png",
                    new File( PATH + "/" +
                            panel.getKey() + ".png"));
        }
    }
}
