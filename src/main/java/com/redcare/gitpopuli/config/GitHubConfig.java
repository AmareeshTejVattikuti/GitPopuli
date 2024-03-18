package com.redcare.gitpopuli.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class GitHubConfig {

    @Value("${github.url}")
    private String gitUrl;

}
