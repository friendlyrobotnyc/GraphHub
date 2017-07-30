package com.wdziemia.githubtimes;

import com.wdziemia.githubtimes.support.shadow.ShadowTransition;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.bytecode.ShadowMap;

import java.lang.reflect.Method;

public class GithubTimesTestRunner extends RobolectricGradleTestRunner {

    public GithubTimesTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected ShadowMap createShadowMap() {
        return super.createShadowMap()
                .newBuilder().addShadowClass(ShadowTransition.class).build();
    }

    @Override
    public Config getConfig(Method method) {
        Config config = super.getConfig(method);
        config.shadows();
        return config;
    }

}