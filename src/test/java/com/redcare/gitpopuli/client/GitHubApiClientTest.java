package com.redcare.gitpopuli.client;

import com.redcare.gitpopuli.client.impl.GitHubApiClientImpl;
import com.redcare.gitpopuli.config.GitHubConfig;
import com.redcare.gitpopuli.model.GitHubSearchResponse;
import com.redcare.gitpopuli.model.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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

    private static final String GIT_URL = "https://api.github.com/search/repositories";
    private static final String QUERY = "test";
    private static final String SORT = "stars";
    private static final String ORDER = "desc";
    private static final int PER_PAGE = 10;
    private static final int PAGE = 1;
    private static final String API_ERROR = "API Error";

    @BeforeEach
    void setUp() {
        when(gitHubConfig.getGitUrl()).thenReturn(GIT_URL);
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri((Function<UriBuilder, URI>) any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        gitHubApiClient = new GitHubApiClientImpl(webClientBuilder, gitHubConfig);
    }

    @Test
    void testSearchRepositories() {
        GitHubSearchResponse mockResponse = new GitHubSearchResponse(1,Flux.just(new Repository()).collectList().block());
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(GitHubSearchResponse.class)).thenReturn(Mono.just(mockResponse));

        Flux<Repository> result = gitHubApiClient.searchRepositories(QUERY, SORT, ORDER, PER_PAGE, PAGE);

        StepVerifier.create(result)
                .expectNextMatches(repository -> true)
                .verifyComplete();
    }

    @Test
    void testSearchRepositoriesApiError() {
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(GitHubSearchResponse.class)).thenReturn(Mono.error(new RuntimeException(API_ERROR)));

        Flux<Repository> result = gitHubApiClient.searchRepositories(QUERY, SORT, ORDER, PER_PAGE, PAGE);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals(API_ERROR))
                .verify();
    }

}