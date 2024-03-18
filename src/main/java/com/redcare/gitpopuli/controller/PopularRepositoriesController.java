package com.redcare.gitpopuli.controller;

import com.redcare.gitpopuli.PopularReposApi;
import com.redcare.gitpopuli.model.Repository;
import com.redcare.gitpopuli.service.PopularRepositoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PopularRepositoriesController implements PopularReposApi {

    private final PopularRepositoriesService popularRepositoriesService;

    @Override
    public Flux<Repository> getPopularRepositories(
            Optional<Integer> top,
            Optional<String> language,
            Optional<LocalDate> since,
            Optional<Integer> page,
            ServerWebExchange exchange) {
        return Objects.requireNonNull(popularRepositoriesService.getPopularRepositories(top, language, since, page));
    }
}