package com.redcare.gitpopuli.client;

import com.redcare.gitpopuli.config.GitHubConfig;
import com.redcare.gitpopuli.model.GitHubSearchResponse;
import com.redcare.gitpopuli.model.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubApiClientTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private GitHubConfig gitHubConfig;

    @Captor
    private ArgumentCaptor<String> uriCaptor;

    private GitHubApiClient gitHubApiClient;

    @BeforeEach
    void setUp() {
        when(gitHubConfig.getGitUrl()).thenReturn("https://api.github.com/search/repositories");
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri((Function<UriBuilder, URI>) any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        gitHubApiClient = new GitHubApiClient(webClientBuilder, gitHubConfig);
    }

    @Test
    void testSearchRepositories() {
        GitHubSearchResponse mockResponse = new GitHubSearchResponse();
        mockResponse.setItems(Flux.just(new Repository()).collectList().block());

        when(responseSpec.bodyToMono(GitHubSearchResponse.class)).thenReturn(Mono.just(mockResponse));

        Flux<Repository> result = gitHubApiClient.searchRepositories("test", "stars", "desc", 10, 1);

        StepVerifier.create(result)
                .expectNextMatches(repository -> true)
                .verifyComplete();
    }

    @Test
    void testSearchRepositoriesApiError() {
        when(responseSpec.bodyToMono(GitHubSearchResponse.class)).thenReturn(Mono.error(new RuntimeException("API Error")));

        Flux<Repository> result = gitHubApiClient.searchRepositories("test", "stars", "desc", 10, 1);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("API Error"))
                .verify();
    }
}