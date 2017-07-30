package com.wdziemia.githubtimes.mvp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A basic "cache" for presenters. Store presenters here during configuration changes and
 * retrieve them by ID.
 */
public class PresenterCache {
    private static PresenterCache instance = new PresenterCache();
    private Map<String, MvpPresenter> presenters = new HashMap<>();

    private PresenterCache() {
    }

    public static PresenterCache getInstance() {
        return instance;
    }

    public String cachePresenter(MvpPresenter presenter) {
        String presenterId = generateId();
        presenters.put(presenterId, presenter);

        return presenterId;
    }

    public MvpPresenter getPresenterById(String id) {
        return presenters.get(id);
    }

    public void removePresenterWithId(String id) {
        presenters.remove(id);
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

}