package com.wdziemia.githubtimes.ui.repository;

import java.util.Date;

public class Repository {
    private int id;

    private String login;

    private String name;

    private String fullName;

    private String description;

    private Date pushedAt;

    private int stargazersCount;

    private int forksCount;

    private String language;

    private String htmlUrl;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getPushedAt() {
        return pushedAt;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public String getLanguage() {
        return language;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getFullName() {
        return fullName;
    }
}
