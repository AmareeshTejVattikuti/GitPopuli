package com.redcare.gitpopuli.controller;

import com.redcare.gitpopuli.model.Repository;
import com.redcare.gitpopuli.service.PopularRepositoriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PopularRepositoriesControllerTest {

    @Mock
    private PopularRepositoriesService popularRepositoriesService;

    @InjectMocks
    private PopularRepositoriesController popularRepositoriesController;

    private Repository sampleRepository;

    @BeforeEach
    void setUp() {
        sampleRepository = new Repository();
    }

    @Test
    void getPopularRepositories() {
        LocalDate sinceDate = LocalDate.of(2019, 1, 1);
        Flux<Repository> expectedFlux = Flux.just(sampleRepository);

        when(popularRepositoriesService.getPopularRepositories(Optional.of(10), Optional.empty(), Optional.of(sinceDate), Optional.of(1)))
                .thenReturn(expectedFlux);

        Flux<Repository> result = popularRepositoriesController.getPopularRepositories(Optional.of(10), Optional.empty(), Optional.of(sinceDate), Optional.of(1), null);

        StepVerifier.create(result)
                .expectNext(sampleRepository)
                .verifyComplete();

        verify(popularRepositoriesService).getPopularRepositories(Optional.of(10), Optional.empty(), Optional.of(sinceDate), Optional.of(1));
    }
}