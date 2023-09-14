package com.gespeip.render_grafana;

public class Model {
    private String login;
    private String pass;
    private String url;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    private String FileName;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Model(String login, String pass, String url) {
        this.login = login;
        this.pass = pass;
        this.url = url;
    }
}
