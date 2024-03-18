package com.redcare.gitpopuli.service;

import com.redcare.gitpopuli.client.GitHubApiClient;
import com.redcare.gitpopuli.model.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PopularRepositoriesServiceTest {

    @Mock
    private GitHubApiClient gitHubApiClient;

    @InjectMocks
    private PopularRepositoriesService service;

    @BeforeEach
    public void setup() {
        service = new PopularRepositoriesService(gitHubApiClient);
    }

    @Test
    void testGetPopularRepositories() {
        Repository repository1 = new Repository();
        repository1.setName(Optional.of("repo1"));
        repository1.setUrl(Optional.of(URI.create("https://github.com/repo1")));
        repository1.setStars(Optional.of(100));
        repository1.setLanguage(Optional.of("Java"));
        Repository repository2 = new Repository();
        repository2.setName(Optional.of("repo2"));
        repository2.setUrl(Optional.of(URI.create("https://github.com/repo2")));
        repository2.setStars(Optional.of(200));
        repository2.setLanguage(Optional.of("Go"));
        Flux<Repository> mockResponse = Flux.just(repository1, repository2);
        when(gitHubApiClient.searchRepositories(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(mockResponse);

        Flux<Repository> result = service.getPopularRepositories(Optional.of(10),
                Optional.of("Go"), Optional.of(LocalDate.now()), Optional.of(1));

        StepVerifier.create(result)
                .expectNext(repository1)
                .expectNext(repository2)
                .verifyComplete();
    }
}