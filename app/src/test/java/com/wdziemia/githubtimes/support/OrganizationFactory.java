package com.wdziemia.githubtimes.support;

import com.wdziemia.githubtimes.ui.organization.Organization;

import java.util.UUID;

public class OrganizationFactory extends Factory {

    public static Organization create() {
        return create(
                UUID.randomUUID().hashCode(),
                getRandomOrganizationName(),
                getRandomAvatarHtmlUrl());
    }

    public static Organization create(int id, String login, String avatarUrl) {
        Organization organization = new Organization();
        setInternals(organization,
                "id", id,
                "login", login,
                "avatarUrl", avatarUrl
        );
        return organization;
    }

    public static String getRandomAvatarHtmlUrl() {
        return String.format("https://imgur.com/%s_avatar.png",
                getRandomOrganizationName());
    }

}