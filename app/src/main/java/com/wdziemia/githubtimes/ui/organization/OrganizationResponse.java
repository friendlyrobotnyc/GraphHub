package com.wdziemia.githubtimes.ui.organization;

import java.util.List;

public class OrganizationResponse {

    private int totalCount;

    private boolean incompleteResults;

    private List<Organization> items;

    public List<Organization> getItems() {
        return items;
    }

}
