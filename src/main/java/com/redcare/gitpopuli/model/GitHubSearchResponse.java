package com.redcare.gitpopuli.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GitHubSearchResponse(@JsonProperty("total_count") int totalCount, List<Repository> items) {
}