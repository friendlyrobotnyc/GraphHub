package com.wdziemia.githubtimes.support.shadow;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.internal.Shadow;

import java.util.ArrayList;
import java.util.List;

@Implements(Transition.class)
public class ShadowTransition {

    @RealObject
    private Transition transition;

    private ArrayList<Transition.TransitionListener> listeners;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Implementation
    public Transition addListener(Transition.TransitionListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
        return Shadow.directlyOn(transition, Transition.class).addListener(listener);
    }

    public List<Transition.TransitionListener> getListeners() {
      return listeners;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void invokeEnd() {
        for (Transition.TransitionListener listener : listeners) {
            listener.onTransitionEnd(transition);
        }
    }
}