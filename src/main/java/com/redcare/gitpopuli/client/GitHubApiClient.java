package com.redcare.gitpopuli.client;

import com.redcare.gitpopuli.model.Repository;
import reactor.core.publisher.Flux;

public interface GitHubApiClient {

    Flux<Repository> searchRepositories(String query, String sort, String order, int perPage, int page);
}