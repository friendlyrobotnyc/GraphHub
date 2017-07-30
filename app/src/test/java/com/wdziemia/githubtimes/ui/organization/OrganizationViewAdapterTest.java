package com.wdziemia.githubtimes.ui.organization;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.TestGithubTimesApplication;
import com.wdziemia.githubtimes.support.OrganizationFactory;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = TestGithubTimesApplication.class)
public class OrganizationViewAdapterTest {

    static Picasso picasso;
    OrganizationViewAdapter subject;

    @Before
    public void setUp() throws Exception {
        picasso = Mockito.mock(Picasso.class);
        Whitebox.setInternalState(picasso, "singleton", picasso);

        subject = new OrganizationViewAdapter(RuntimeEnvironment.application);

        RequestCreator creator = Mockito.mock(RequestCreator.class);
        Mockito.when(picasso.load(anyString())).thenReturn(creator);
        Mockito.when(creator.placeholder(anyInt())).thenReturn(creator);
    }

    @Test
    public void onCreateViewHolder_returnsInflatedView(){
        RecyclerView parent = new RecyclerView(RuntimeEnvironment.application);
        parent.setLayoutManager(new LinearLayoutManager(RuntimeEnvironment.application));
        OrganizationViewAdapter.OrganizationViewHolder holder =
                subject.onCreateViewHolder(parent, 0);
        Assert.assertNotNull(holder);
        Assert.assertNotNull(holder.itemView);
        Assert.assertNotNull(holder.image);
        Assert.assertNotNull(holder.name);
    }

    @Test
    public void onBindViewHolder_setsCorrectData(){
        final Organization organization = OrganizationFactory.create();
        List<Organization> items = Collections.singletonList(organization);
        subject.setItems(items);

        RecyclerView parent = new RecyclerView(RuntimeEnvironment.application);
        parent.setLayoutManager(new LinearLayoutManager(RuntimeEnvironment.application));
        OrganizationViewAdapter.OrganizationViewHolder holder =
                subject.onCreateViewHolder(parent, 0);

        subject.onBindViewHolder(holder, 0);

        Assert.assertEquals(holder.name.getText(), organization.getLogin());
        Mockito.verify(picasso).load(organization.getAvatarUrl());
    }

    @Test
    public void onViewRecycled_cleansImageLoad(){
        RecyclerView parent = new RecyclerView(RuntimeEnvironment.application);
        parent.setLayoutManager(new LinearLayoutManager(RuntimeEnvironment.application));
        OrganizationViewAdapter.OrganizationViewHolder holder =
                subject.onCreateViewHolder(parent, 0);

        holder.image = Mockito.mock(ImageView.class);
        subject.onViewRecycled(holder);
        Mockito.verify(holder.image).setImageDrawable(null);
        Mockito.verify(picasso).cancelRequest(holder.image);
    }

    @Test
    public void onBindViewHolder_onClick_callsItemClickSubject() {
        final Organization org = OrganizationFactory.create();
        List<Organization> items = Collections.singletonList(org);
        subject.setItems(items);

        RecyclerView parent = new RecyclerView(RuntimeEnvironment.application);
        parent.setLayoutManager(new LinearLayoutManager(RuntimeEnvironment.application));
        OrganizationViewAdapter.OrganizationViewHolder holder =
                subject.onCreateViewHolder(parent, 0);

        List<Organization> emittedItems = new ArrayList<>();
        subject.onBindViewHolder(holder, 0);
        subject.getItemClickSubject().subscribe(clickedHolder ->{
            emittedItems.add(clickedHolder.item);
        });
        holder.itemView.performClick();

        Assert.assertEquals(emittedItems.get(0), org);
    }
}