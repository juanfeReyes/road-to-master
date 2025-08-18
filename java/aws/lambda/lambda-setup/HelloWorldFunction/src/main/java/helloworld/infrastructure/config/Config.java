package helloworld.infrastructure.config;

import lombok.Data;

@Data
public class Config {
    private String environmentName;
    private String imagePullEndpoint;
}
