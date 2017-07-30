package com.wdziemia.githubtimes.dagger;

import rx.Scheduler;

/**
 * Helper interface to provide {@link Scheduler} for Rx calls. Extracting scheduler in a interace
 * allows us to inject {@link Scheduler}s for testing.
 */
public interface SchedulerProvider {
    Scheduler ui();
    Scheduler computation();
    Scheduler io();
}