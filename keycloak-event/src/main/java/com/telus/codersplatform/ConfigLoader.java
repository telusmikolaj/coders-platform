package com.telus.codersplatform;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoader {
    public RabbitMQConfig loadConfig(String path) {
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(Paths.get(path))) {
            return yaml.loadAs(in, RabbitMQConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

