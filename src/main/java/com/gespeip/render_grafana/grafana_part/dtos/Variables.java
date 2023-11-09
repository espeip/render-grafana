package com.gespeip.render_grafana.grafana_part.dtos;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Variables {
    private List<Variable> variables;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Variable variable : variables)
            sb.append(variable.toString()).append("&");
        return sb.toString();
    }
}
