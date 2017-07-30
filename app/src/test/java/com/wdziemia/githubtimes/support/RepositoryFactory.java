package com.wdziemia.githubtimes.support;

import com.wdziemia.githubtimes.ui.repository.Repository;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RepositoryFactory extends Factory {

    private static final String[] REPO_NAMES = new String[]{
            "store", "house", "care", "rv", "apartment", "rxjava", "glide", "picasso"
    };

    private static final String[] LANGUAGES = new String[]{
            "Volt", "LSL", "eC", "CoffeeScript", "HTML", "Lex", "API Blueprint", "Swift", "C",
            "AutoHotkey", "Isabelle", "Metal", "Clarion", "JSONiq", "Boo", "AutoIt", "Clojure",
            "Rust", "Prolog"
    };

    private static final String DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
            " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam," +
            " quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute" +
            " irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur." +
            " Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit" +
            " anim id est laborum.";

    public static Repository create() {
        return create(
                UUID.randomUUID().hashCode(),
                getRandomRepoName(),
                getRandomFullName(),
                getRandomPushedAt(),
                getRandomDesc(),
                getRandomStargazersCount(),
                getRandomForksCount(),
                getRandomLanguage(),
                getRandomHtmlUrl());
    }

    public static Repository create(int id, String name, String fullName, Date pushedAt, String description, int stargazersCount, int forksCount, String language, String htmlUrl) {
        Repository repository = new Repository();

        setInternals(repository,
                "id", id,
                "name", name,
                "pushedAt", pushedAt,
                "description", description,
                "stargazersCount", stargazersCount,
                "forksCount", forksCount,
                "fullName", fullName,
                "language", language,
                "htmlUrl", htmlUrl
        );

        return repository;
    }

    private static int getRandomStargazersCount() {
        return new Random().nextInt(1000);
    }

    private static int getRandomForksCount() {
        return new Random().nextInt(1000);
    }

    public static String getRandomRepoName() {
        return REPO_NAMES[new Random().nextInt(REPO_NAMES.length)];
    }

    public static String getRandomFullName() {
        return String.format("%s/%s",
                getRandomOrganizationName(),
                getRandomRepoName());
    }

    public static Date getRandomPushedAt() {
        long startTime = 100000000L + new Random().nextInt(100000);
        return new Date(startTime);
    }

    public static String getRandomDesc() {
        int start = new Random().nextInt(DESCRIPTION.length()/2);
        int end = start + new Random().nextInt(DESCRIPTION.length()/2);
        return DESCRIPTION.substring(start, end);
    }

    public static String getRandomLanguage() {
        return LANGUAGES[new Random().nextInt(LANGUAGES.length)];
    }

    public static String getRandomHtmlUrl() {
        return String.format("https://github.com/%s/%s",
                getRandomOrganizationName(),
                getRandomRepoName());
    }

}