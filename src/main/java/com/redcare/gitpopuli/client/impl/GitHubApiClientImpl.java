package com.redcare.gitpopuli.client.impl;

import com.redcare.gitpopuli.client.GitHubApiClient;
import com.redcare.gitpopuli.config.GitHubConfig;
import com.redcare.gitpopuli.exception.GitHubApiException;
import com.redcare.gitpopuli.model.GitHubSearchResponse;
import com.redcare.gitpopuli.model.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GitHubApiClientImpl implements GitHubApiClient {

    private final WebClient webClient;

    public GitHubApiClientImpl(WebClient.Builder webClientBuilder, GitHubConfig gitHubConfig) {
        this.webClient = webClientBuilder.baseUrl(gitHubConfig.getGitUrl()).build();
    }

    @Override
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
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), this::handleClientResponseError)
                .bodyToMono(GitHubSearchResponse.class)
                .flatMapMany(response -> {
                    log.info("Received {} repositories", response.totalCount());
                    return Flux.fromIterable(response.items());
                })
                .share();
    }

    private Mono<? extends Throwable> handleClientResponseError(ClientResponse clientResponse) {
        return clientResponse.createException()
                .flatMap(error -> {
                    log.error("Error calling GitHub API: {}", clientResponse.statusCode());
                    return Mono.error(new GitHubApiException("Error calling GitHub API: " + error.getMessage() + ", please try again after sometime"));
                });
    }
}