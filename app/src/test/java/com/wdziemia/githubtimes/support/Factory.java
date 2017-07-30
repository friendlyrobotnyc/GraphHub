package com.wdziemia.githubtimes.support;

import org.mockito.internal.util.reflection.Whitebox;

import java.util.Random;

public abstract class Factory {

    static final String[] ORG_NAMES = new String[]{
            "nytimes", "google", "square", "usa", "poland", "newyork", "california", "dog"
    };

    public static String getRandomOrganizationName() {
        return ORG_NAMES[new Random().nextInt(ORG_NAMES.length)];
    }

    protected static Object setInternals(Object object, Object... attributes) {
        if (attributes.length % 2 != 0) {
            throw new RuntimeException("setInternals must be called with an even number of attributes");
        }

        for (int i = 0; i < attributes.length; i += 2) {
            if (!(attributes[i] instanceof String)) {
                throw new RuntimeException("attribute was not passed in as string: " + attributes[i]);
            }

            try {
                Whitebox.setInternalState(object, (String) attributes[i], attributes[i + 1]);
            } catch (RuntimeException e) {
                throw new RuntimeException(String.format("Could not set %s attribute %s with value %s", object.getClass().getSimpleName(), attributes[i], attributes[i + 1]), e);
            }
        }

        return object;
    }

}