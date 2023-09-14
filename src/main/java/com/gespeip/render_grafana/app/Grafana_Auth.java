package com.gespeip.render_grafana.app;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class Grafana_Auth {
    String token;
    String host;
    String uid;
    private String login;
    private String password;
    private String apiID;

    public Grafana_Auth(String grafanaLink, String login, String password) throws IOException, ParseException {
        host = grafanaLink.split("http://")[1].split("/d")[0];
        uid = grafanaLink.split("d/")[1].split("/")[0];
        this.login = login;
        this.password = password;
        create_token();
    }
    public Grafana_Auth(String grafanaLink) throws IOException, ParseException {
        host = grafanaLink.split("http://")[1].split("/d")[0];
        uid = grafanaLink.split("d/")[1].split("/")[0];
        this.login = "admin";
        this.password = "admin";
        create_token();
    }

    private void create_token() throws IOException, ParseException {
        URL url = new URL(" http://" + host +"/api/auth/keys");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode((login + ":" + password).getBytes())));
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        osw.write("{\"name\":\"" + StringGen.genString(48,57,7) + "\", \"role\": \"Admin\"}");
        osw.flush();
        osw.close();
        connection.connect();
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String line = bufReader.readLine();

        while (line != null) {
            response.append(line).append("\n");
            line = bufReader.readLine();
        }
        bufReader.close();
        //System.out.println(response);
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.toString());

        token = (String) jsonObject.get("key");
        apiID = jsonObject.get("id").toString();
    }
    public void delete_token() throws IOException {
        URL url = new URL(" http://" + host + "/api/auth/keys/" + apiID);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);
        connection.connect();
    }
}
