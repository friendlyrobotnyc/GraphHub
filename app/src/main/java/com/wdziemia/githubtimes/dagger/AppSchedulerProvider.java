package com.wdziemia.githubtimes.dagger;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

final class AppSchedulerProvider implements SchedulerProvider {
    @Override 
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override 
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override 
    public Scheduler io() {
        return Schedulers.io();
    }
}