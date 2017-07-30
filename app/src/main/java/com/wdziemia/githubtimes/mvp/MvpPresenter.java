package com.wdziemia.githubtimes.mvp;
/**
 * Base class for Presenters. Used to define the required methods our presenters must implement, and
 * also allows us to easily cache presenters.
 */
public interface MvpPresenter <T extends MvpView>
{
    /**
     * Attach the view to this Presenter in order for the Presenter to communicate with. Should be a
     * 1-1 mapping between View:Presenter.
     *
     * @param view - The {@link MvpView} subclass to be attached. The view needs to be casted to
     *             correct type in each presenter
     */
    void attach(T view);

    /**
     * Use this to detach presenter from the view. Re-attach view later with {@link
     * #attach(MvpView)}
     */
    void detach();

    /**
     * Use when the presenter is finished; the presenter instance should not be used again after
     * calling this. Need to create a new one.
     */
    void destroy();

}
