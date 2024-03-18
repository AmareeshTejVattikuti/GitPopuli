package com.redcare.gitpopuli.service;

import com.redcare.gitpopuli.client.GitHubApiClient;
import com.redcare.gitpopuli.model.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PopularRepositoriesService {

    private final GitHubApiClient gitHubApiClient;

    private static final int DEFAULT_TOP = 10;
    private static final int DEFAULT_PAGE = 1;

    @Cacheable(cacheNames = "popularRepositories", key = "{#top, #language, #since, #page}")
    public Flux<Repository> getPopularRepositories(Optional<Integer> top, Optional<String> language, Optional<LocalDate> since, Optional<Integer> page) {
        String query = buildQuery(language, since);

        return gitHubApiClient.searchRepositories(query, "stars", "desc", top.orElse(DEFAULT_TOP), page.orElse(DEFAULT_PAGE));
    }

    private String buildQuery(Optional<String> language, Optional<LocalDate> since) {
        StringBuilder queryBuilder = new StringBuilder();
        language.ifPresent(lang -> queryBuilder.append("language:").append(lang).append("+"));
        since.ifPresent(date -> queryBuilder.append("created:").append(date).append("+"));

        return queryBuilder.toString();
    }
}