package com.wdziemia.githubtimes.ui.views;

import android.graphics.Rect;

import com.wdziemia.githubtimes.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SpaceItemDecorationTest {

    @Test
    public void getItemOffsets_spaceSetTo16_modifiesTo16() throws Exception {
        SpaceItemDecoration subject = new SpaceItemDecoration(16);

        Rect outRect = new Rect(4, 4, 4, 4);
        subject.getItemOffsets(outRect, null, null, null);

        assertThat(outRect.left).isEqualTo(4);
        assertThat(outRect.top).isEqualTo(4);
        assertThat(outRect.right).isEqualTo(4);
        assertThat(outRect.bottom).isEqualTo(16);
    }

}