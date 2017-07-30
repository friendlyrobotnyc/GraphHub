package com.wdziemia.githubtimes.ui.views;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Adds extra empty space to bottom of a view.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * Space in pixels
     */
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
    }

}