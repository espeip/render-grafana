package com.gespeip.render_grafana.grafana_part.services;

import com.gespeip.render_grafana.grafana_part.configs.GrafanaUnirestConfig;
import com.gespeip.render_grafana.grafana_part.dtos.*;
import com.gespeip.render_grafana.postges_part.repositories.ResultRepository;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class DashboardService {

    @Autowired
    private GrafanaUnirestConfig guc;
    @Autowired
    private ResultRepository repository;

    private String json;
    private String uid;
    private String runId;

    // получение JSON по UID дашборда и помещение в переменную json
    private void setJson(String uid) {
        json = guc.getUnirest().get("/api/dashboards/uid/" + uid)
                .asString()
                .getBody();
    }

    //Получение основного JSON и добавление его в переменную
    public void init(String uid, String runId) {
        this.runId = runId;
        this.uid = uid;
        setJson(uid);
    }


    //Получение основного JSON и добавление его в переменную
    public void init(String uid) {
        this.uid = uid;
        setJson(uid);
    }


    //получения уникального id Дашборда в Grafana
    public String getUid() {
        return uid;
    }


    //получение названия Дашборда
    public String getDashboardTitle() {
        String path = "$.dashboard.title";
        return JsonPath.read(json, path);
    }

    //получение списка Row
    public List<Row> getRows() {
        String pathTitle = "$.dashboard.panels[?(@.type == 'row')].title";
        List<Row> rows = new ArrayList<>();

        for (String title : (List<String>) JsonPath.read(json, pathTitle)) {
            String repeat = getRepeatOption(title);
            String s = "";
            if (repeat.isEmpty()) {
                rows.add(
                        new Row(
                                title,
                                getPanelsPerRowTitle(title, new Variables(getVars())),
                                getRepeatOption(title)))
                ;
            } else {
                for (String value : getCurrentVarsPerVarName(repeat))
                    rows.add(
                            new Row(
                                    title.replace("$" + repeat, value),
                                    getPanelsPerRowTitle(title, new Variables(getVarsForRepeatOptions(repeat, value))),
                                    getRepeatOption(title)))
                            ;
            }
        }
        return rows;
    }

    //получение переменной для repeatOption по названию панели
    private String getRepeatOption(String title) {
        String path = String.format("$.dashboard.panels[?(@.title == '%s')].repeat", title);
        return JsonPath.read(json, path)
                .toString()
                .replaceAll("[^A-Za-zА-Яа-я]", "");
    }

    //получение переменной для repeatOption по id панели
    private String getRepeatOption(int id) {
        String path = String.format("$.dashboard.panels[?(@.id == '%d')].repeat", id);
        return JsonPath.read(json, path)
                .toString()
                .replaceAll("[^A-Za-zА-Яа-я]", "");
    }


    //получение панелей по названию Row
    public List<Panel> getPanelsPerRowTitle(String title, Variables variables) {
        String path = String.format("$.dashboard.panels[?(@.title == '%s')].panels[*].id", title);
        List<Panel> panels = new ArrayList<>();
        List<Integer> response = (List<Integer>) JsonPath.read(json, path);
        for(int panelId : response)
            panels.add(new Panel(panelId, getPanelTitlePerPanelId(panelId), variables));
        return panels;
    }

    //получение названия панели по id
    public String getPanelTitlePerPanelId(int panelId) {
        String path = String.format("$.dashboard..panels[?(@.id == %d)].title", panelId);
        return JsonPath.read(json,path).toString();
    }

    //получение Панелей из json
    public List<Panel> getPanels() {
        String path = "$.dashboard.panels[?(@.type != 'row')].id";
        List<Panel> panels = new ArrayList<>();
        for (int id : (List<Integer>) JsonPath.read(json, path)) {
            String repeat = getRepeatOption(id);
            String s = "";
            if (repeat.isEmpty()) {
                panels.add(
                        new Panel(
                                id,
                                getPanelTitlePerPanelId(id),
                                new Variables(getVars())))
                ;
            } else {
                for (String value : getCurrentVarsPerVarName(repeat))
                    panels.add(
                            new Panel(
                                    id,
                                    getPanelTitlePerPanelId(id),
                                    new Variables(getVarsForRepeatOptions(repeat,value))))
                            ;
            }
        }
        return panels;
    }

    //получение списка с переменными
    public List<Variable> getVars() {
        String path = "$.dashboard.templating.list[*].name";
        List<Variable> variables = new ArrayList<>();
        for (String varName : (List<String>) JsonPath.read(json, path))
            variables.add(new Variable(varName,getCurrentVarsPerVarName(varName)));
        return variables;
    }

    //получение списка переменных для ситуаций с repeatOptions
    public List<Variable> getVarsForRepeatOptions(String key, String value) {
        String path = String.format("$.dashboard.templating.list[?(@.name != '%s')].name", key);
        List<Variable> variables = new ArrayList<>();
        for (String varName : (List<String>) JsonPath.read(json, path))
            variables.add(new Variable(varName,getCurrentVarsPerVarName(varName)));
        variables.add(new Variable(key, Arrays.asList(value)));
        return variables;
    }

    //получение значения переменной по ее названию
    public List<String> getCurrentVarsPerVarName(String varName) {
        String path = String.format("$.dashboard.templating.list[?(@.name=='%s')].current.value", varName);

        List<Object> resultList = JsonPath.read(json, path);

            if (resultList.get(0) instanceof String)
                return Arrays.asList((String) resultList.get(0));
            else
                return  (List<String>) resultList.get(0);
    }

    //получение времени начала из grafana
    public String getTimeFrom() {
        String path = "$.dashboard.time.from";

        if (runId.isEmpty())
            return JsonPath.read(json, path);
        else
            return getTimeFrom(Integer.parseInt(runId));
    }

    //получение времени конца из grafana
    public String getTimeTo() {
        String path = "$.dashboard.time.to";

        if (runId.isEmpty())
            return JsonPath.read(json, path);
        else
            return getTimeTo(Integer.parseInt(runId));
    }

    //получение времени начала теста по runId
    public String getTimeFrom(int runId) {
        return String.valueOf(repository.findByRunId(runId).getStartTime());
    }

    //получение времени конца теста по runId
    public String getTimeTo(int runId) {
        return String.valueOf(repository.findByRunId(runId).getEndTime());
    }

}
