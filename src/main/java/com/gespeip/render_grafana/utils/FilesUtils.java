package com.gespeip.render_grafana.utils;

import com.google.common.io.Resources;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

@Component
public class FilesUtils {

    @Value("${directory.pictures}")
    private String directoryPictures;

    public File[] getPictures() throws URISyntaxException {
        return new File(
                Resources.getResource(directoryPictures)
                        .toURI())
                .listFiles();
    }

    public void deletePictures() throws URISyntaxException {
        Arrays.stream(getPictures())
                .filter(File::isFile)
                .forEach(File::delete);
    }

    public String getPicturesDirectory() {
        return Resources.getResource(directoryPictures).toString();
    }

}
