package com.wdziemia.githubtimes.ui.repository;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.TestGithubTimesApplication;
import com.wdziemia.githubtimes.support.RepositoryFactory;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = TestGithubTimesApplication.class)
public class RepositoryViewAdapterTest {

    RepositoryViewAdapter subject;
    NumberFormat numFormatter;

    @Before
    public void setUp() throws Exception {
        subject = new RepositoryViewAdapter(RuntimeEnvironment.application);
        numFormatter = NumberFormat.getIntegerInstance();
    }

    @Test
    public void onCreateViewHolder_returnsInflatedView() {
        RecyclerView parent = new RecyclerView(RuntimeEnvironment.application);
        parent.setLayoutManager(new LinearLayoutManager(RuntimeEnvironment.application));
        RepositoryViewAdapter.RepositoryViewHolder holder =
                subject.onCreateViewHolder(parent, 0);
        Assert.assertNotNull(holder);
        Assert.assertNotNull(holder.itemView);
        Assert.assertNotNull(holder.name);
        Assert.assertNotNull(holder.desc);
        Assert.assertNotNull(holder.forks);
        Assert.assertNotNull(holder.language);
        Assert.assertNotNull(holder.stars);
        Assert.assertNotNull(holder.updated);
    }

    @Test
    public void onBindViewHolder_setsCorrectData() {
        final Repository repo = RepositoryFactory.create();
        List<Repository> items = Collections.singletonList(repo);
        subject.setItems(items);

        RecyclerView parent = new RecyclerView(RuntimeEnvironment.application);
        parent.setLayoutManager(new LinearLayoutManager(RuntimeEnvironment.application));
        RepositoryViewAdapter.RepositoryViewHolder holder =
                subject.onCreateViewHolder(parent, 0);

        subject.onBindViewHolder(holder, 0);

        Assert.assertEquals(holder.name.getText(), repo.getName());
        Assert.assertEquals(holder.desc.getText(), repo.getDescription());
        Assert.assertEquals(holder.language.getText(), repo.getLanguage());
        Assert.assertEquals(holder.stars.getText(), numFormatter.format(repo.getStargazersCount()));
        Assert.assertEquals(holder.forks.getText(), numFormatter.format(repo.getForksCount()));
    }

    @Test
    public void onBindViewHolder_onClick_callsItemClickSubject() {
        final Repository repo = RepositoryFactory.create();
        List<Repository> items = Collections.singletonList(repo);
        subject.setItems(items);

        RecyclerView parent = new RecyclerView(RuntimeEnvironment.application);
        parent.setLayoutManager(new LinearLayoutManager(RuntimeEnvironment.application));
        RepositoryViewAdapter.RepositoryViewHolder holder =
                subject.onCreateViewHolder(parent, 0);

        List<Repository> emittedItems = new ArrayList<>();
        subject.onBindViewHolder(holder, 0);
        subject.getItemClickSubject().subscribe(emittedItems::add);
        holder.itemView.performClick();

        Assert.assertEquals(emittedItems.get(0), repo);
    }
}