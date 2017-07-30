package com.wdziemia.githubtimes.ui.repository;

import java.util.List;

public class RepositoryResponse {

    private int totalCount;

    private boolean incompleteResults;

    private List<Repository> items;

    public List<Repository> getItems() {
        return items;
    }
}
