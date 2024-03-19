package com.redcare.gitpopuli.integrationtest;

import com.redcare.gitpopuli.client.GitHubApiClient;
import com.redcare.gitpopuli.model.Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("test")
class GitHubApiClientIntegrationTest {

    @Autowired
    private GitHubApiClient gitHubApiClient;

    @Test
    void testSearchRepositoriesIntegration() {
        String query = "language:java";
        String sort = "stars";
        String order = "desc";
        int perPage = 5;
        int page = 1;

        Flux<Repository> repositoryFlux = gitHubApiClient.searchRepositories(query, sort, order, perPage, page);

        StepVerifier.create(repositoryFlux)
                .expectNextCount(5)
                .verifyComplete();
    }
}