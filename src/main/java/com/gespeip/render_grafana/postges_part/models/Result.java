package com.gespeip.render_grafana.postges_part.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "results")
@Entity
public class Result {

    @Id
    private int runId;

    @Column(name = "run_start")
    private int startTime;

    @Column(name = "run_end")
    private int endTime;
}
