package com.redcare.gitpopuli.service;

import com.redcare.gitpopuli.client.GitHubApiClient;
import com.redcare.gitpopuli.model.Repository;
import com.redcare.gitpopuli.service.impl.PopularRepositoriesServiceImpl;
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
class PopularRepositoriesServiceImplTest {

    @Mock
    private GitHubApiClient gitHubApiClient;

    @InjectMocks
    private PopularRepositoriesServiceImpl service;

    @BeforeEach
    public void setup() {
        service = new PopularRepositoriesServiceImpl(gitHubApiClient);
    }

    @Test
    void testGetPopularRepositories() {
        Repository repository1 = prepareRepository("repo1", "https://github.com/repo1", 100, "Java");
        Repository repository2 = prepareRepository("repo2", "https://github.com/repo2", 200, "Go");
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

    private Repository prepareRepository(String name, String url, int stars, String language) {
        Repository repository = new Repository();
        repository.setName(Optional.of(name));
        repository.setUrl(Optional.of(URI.create(url)));
        repository.setStars(Optional.of(stars));
        repository.setLanguage(Optional.of(language));
        return repository;
    }
}