package com.redcare.gitpopuli.service;

import com.redcare.gitpopuli.model.Repository;
import reactor.core.publisher.Flux;
import java.time.LocalDate;
import java.util.Optional;

public interface PopularRepositoriesService {

    Flux<Repository> getPopularRepositories(Optional<Integer> top, Optional<String> language, Optional<LocalDate> since, Optional<Integer> page);

}