package com.gespeip.render_grafana.utils;

import com.google.common.io.Resources;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;

import java.nio.charset.StandardCharsets;

@Component
public class ResourceReader {
    @SneakyThrows
    public String readResource(String fileName){
        return FileUtils.readFileToString(
                new File(Resources.getResource(fileName)
                        .toURI()),
                StandardCharsets.UTF_8
        );
    }
}
