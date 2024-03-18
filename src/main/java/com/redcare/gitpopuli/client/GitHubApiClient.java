package com.redcare.gitpopuli.client;

import com.redcare.gitpopuli.config.GitHubConfig;
import com.redcare.gitpopuli.model.GitHubSearchResponse;
import com.redcare.gitpopuli.model.Repository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Component
public class GitHubApiClient {

    private final WebClient webClient;

    public GitHubApiClient(WebClient.Builder webClientBuilder, GitHubConfig gitHubConfig) {
        this.webClient = webClientBuilder.baseUrl(gitHubConfig.getGitUrl()).build();
    }

    public Flux<Repository> searchRepositories(String query, String sort, String order, int perPage, int page) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", query)
                        .queryParam("sort", sort)
                        .queryParam("order", order)
                        .queryParam("per_page", perPage)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(GitHubSearchResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.getItems()));
    }
}