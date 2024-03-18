package com.redcare.gitpopuli.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GitHubSearchResponse {
    private int totalCount;
    private boolean incompleteResults;
    private List<Repository> items;

}