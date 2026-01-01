package org.example.config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;

public class YamlLoader {

    public static JobConfig loadConfig() throws IOException {
        var yaml = new Yaml(new Constructor(JobConfig.class, new LoaderOptions()));
        try(InputStream is = YamlLoader.class.getClassLoader().getResourceAsStream("local-config.yml")) {
            return yaml.load(is);
        }
    }
}
