package com.redcare.gitpopuli.integrationtest;

import com.redcare.gitpopuli.config.GitHubConfig;
import com.redcare.gitpopuli.client.impl.GitHubApiClientImpl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

class GitHubApiClientMockIntegrationTest {

    private MockWebServer mockWebServer;
    private GitHubApiClientImpl gitHubApiClient;

    @BeforeEach
    void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();

        WebClient.Builder webClientBuilder = WebClient.builder();
        GitHubConfig gitHubConfig = new GitHubConfig();
        gitHubConfig.setGitUrl(mockWebServer.url("/").toString());

        this.gitHubApiClient = new GitHubApiClientImpl(webClientBuilder, gitHubConfig);
    }

    @AfterEach
    void tearDown() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void testSearchRepositoriesIntegration() {
        mockWebServer.enqueue(createMockResponse());
        StepVerifier.create(gitHubApiClient.searchRepositories("Spring Boot", "stars", "desc", 10, 1))
                .expectNextMatches(repository -> {
                    return true;
                })
                .verifyComplete();
    }

    private MockResponse createMockResponse() {
        String mockResponseJson = """
                {
                    "total_count": 1,
                    "incomplete_results": false,
                    "items": [
                        {
                            "id": 326038947,
                            "node_id": "MDEwOlJlcG9zaXRvcnkzMjYwMzg5NDc=",
                            "name": "DefaultCreds-cheat-sheet",
                            "full_name": "ihebski/DefaultCreds-cheat-sheet",
                            "private": false,
                            "owner": {
                                "login": "ihebski",
                                "id": 13177580,
                                "url": "https://api.github.com/users/ihebski",
                                "html_url": "https://github.com/ihebski",
                                "repos_url": "https://api.github.com/users/ihebski/repos",
                                "events_url": "https://api.github.com/users/ihebski/events{/privacy}",
                                "received_events_url": "https://api.github.com/users/ihebski/received_events",
                                "type": "User",
                                "site_admin": false
                            }
                          }
                        ],
                        "visibility": "public",
                        "forks": 657,
                        "open_issues": 0,
                        "watchers": 5163,
                        "default_branch": "main",
                        "score": 1.0
                }
                """;

        return new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(mockResponseJson);
    }
}