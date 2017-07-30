package com.wdziemia.githubtimes.ui.views;

import android.app.Application;

import com.wdziemia.githubtimes.BuildConfig;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboAttributeSet;
import org.robolectric.res.Attribute;
import org.robolectric.shadows.ShadowToast;

import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml", packageName = "com.wdziemia.githubtimes")
public class RangeEditTextPreferenceTest {

    Application application;

    @Before
    public void setUp() throws Exception {
        application = spy(RuntimeEnvironment.application);
    }

    @Test
    public void init_defaultAttrs_setsHintText() {
        RangeEditTextPreference subject = new RangeEditTextPreference(application);
        Assert.assertEquals(subject.getEditText().getHint(), "Range between 0 and 100");
    }

    @Test
    public void init_whenShowMinMaxHint_setsHintText() {
        RangeEditTextPreference subject = new RangeEditTextPreference(application,
                RoboAttributeSet.create(application,
                        new Attribute("com.wdziemia.githubtimes:attr/minValue", "1", "com.wdziemia.githubtimes"),
                        new Attribute("com.wdziemia.githubtimes:attr/maxValue", "100", "com.wdziemia.githubtimes"),
                        new Attribute("com.wdziemia.githubtimes:attr/showMinMaxHint", "true", "com.wdziemia.githubtimes")
                ));

        Assert.assertEquals(subject.getEditText().getHint(), "Range between 1 and 100");
    }

    @Test
    public void init_whenDontShowMinMaxHint_doesNotSetHintText() {
        RangeEditTextPreference subject = new RangeEditTextPreference(application,
                RoboAttributeSet.create(application,
                        new Attribute("com.wdziemia.githubtimes:attr/minValue", "1", "com.wdziemia.githubtimes"),
                        new Attribute("com.wdziemia.githubtimes:attr/maxValue", "100", "com.wdziemia.githubtimes"),
                        new Attribute("com.wdziemia.githubtimes:attr/showMinMaxHint", "false", "com.wdziemia.githubtimes")
                ));

        Assert.assertEquals(subject.getEditText().getHint(), null);
    }

    @Test
    public void onDialogClosed_whenValidInput_setsPreference() {
        RangeEditTextPreference subject = new RangeEditTextPreference(application,
                RoboAttributeSet.create(application,
                        new Attribute("com.wdziemia.githubtimes:attr/minValue", "1", "com.wdziemia.githubtimes"),
                        new Attribute("com.wdziemia.githubtimes:attr/maxValue", "100", "com.wdziemia.githubtimes")
                ));

        subject.getEditText().setText("100");
        subject.onDialogClosed(true);

        Assert.assertEquals(subject.getText(), "100");
    }

    @Test
    public void onDialogClosed_whenInvalidInput_showsToast() {
        RangeEditTextPreference subject = new RangeEditTextPreference(application,
                RoboAttributeSet.create(application,
                        new Attribute("com.wdziemia.githubtimes:attr/minValue", "1", "com.wdziemia.githubtimes"),
                        new Attribute("com.wdziemia.githubtimes:attr/maxValue", "100", "com.wdziemia.githubtimes")
                ));

        subject.getEditText().setText("101");
        subject.onDialogClosed(true);

        Assert.assertEquals(ShadowToast.getTextOfLatestToast(), "Value must fall in range of 1 and 100");
    }
}