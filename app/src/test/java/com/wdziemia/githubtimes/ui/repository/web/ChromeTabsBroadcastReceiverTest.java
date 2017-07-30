package com.wdziemia.githubtimes.ui.repository.web;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;

import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml", packageName = "com.wdziemia.githubtimes")
public class ChromeTabsBroadcastReceiverTest {

    private ChromeTabsBroadcastReceiver subject;
    private final String url = "http://example.com";
    private final String titleValue = "test title";

    @Before
    public void setUp() throws Exception {
        subject = new ChromeTabsBroadcastReceiver();
    }

    @Test
    public void onReceive_withDataUrl_showsChooser(){
        Intent intent = Mockito.mock(Intent.class);
        when(intent.getDataString()).thenReturn(url);

        Application application = spy(RuntimeEnvironment.application);
        Resources spiedResources = spy(application.getResources());
        when(application.getResources()).thenReturn(spiedResources);
        when(spiedResources.getString(R.string.share_title))
                .thenReturn(titleValue);

        ArgumentCaptor<Intent> argumentCaptor = ArgumentCaptor.forClass(Intent.class);
        doNothing().when(application).startActivity(argumentCaptor.capture());

        subject.onReceive(application, intent);

        Intent chooserIntent = argumentCaptor.getValue();
        Assert.assertEquals((chooserIntent.getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK) == Intent.FLAG_ACTIVITY_NEW_TASK, true);
        Assert.assertEquals(chooserIntent.getAction(), Intent.ACTION_CHOOSER);
        Assert.assertEquals(chooserIntent.getStringExtra(Intent.EXTRA_TITLE), titleValue);

        Intent dataIntent = chooserIntent.getParcelableExtra(Intent.EXTRA_INTENT);
        Assert.assertEquals(dataIntent.getAction(), Intent.ACTION_SEND);
        Assert.assertEquals(dataIntent.getType(), "text/plain");
        Assert.assertEquals(dataIntent.getStringExtra(Intent.EXTRA_TEXT), url);
    }

    @Test
    public void onReceive_withNoDataUrl_doesNothing(){
        Intent intent = Mockito.mock(Intent.class);
        when(intent.getDataString()).thenReturn(null);

        Application application = spy(RuntimeEnvironment.application);
        Resources spiedResources = spy(application.getResources());
        when(application.getResources()).thenReturn(spiedResources);
        when(spiedResources.getString(R.string.share_title))
                .thenReturn(titleValue);

        ArgumentCaptor<Intent> argumentCaptor = ArgumentCaptor.forClass(Intent.class);
        doNothing().when(application).startActivity(argumentCaptor.capture());

        subject.onReceive(application, intent);

        Mockito.verify(application, never()).startActivity(any(Intent.class));
    }

}