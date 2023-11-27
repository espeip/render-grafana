package com.gespeip.render_grafana.postges_part.repositories;

import com.gespeip.render_grafana.postges_part.models.Result;
import org.springframework.data.repository.CrudRepository;

public interface ResultRepository extends CrudRepository<Result, Long> {
    Result findByRunId(Integer runId);
}
