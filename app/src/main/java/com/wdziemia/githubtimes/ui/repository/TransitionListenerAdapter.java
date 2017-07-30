package com.wdziemia.githubtimes.ui.repository;


import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;

/**
 * Helper Implementation to avoid having to override every method of {@link Transition.TransitionListener}
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class TransitionListenerAdapter implements Transition.TransitionListener {

    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {

    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }
}