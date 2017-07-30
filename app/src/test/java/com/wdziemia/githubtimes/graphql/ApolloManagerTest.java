package com.wdziemia.githubtimes.graphql;

import com.apollographql.apollo.api.Response;
import com.wdziemia.githubtimes.RepoQuery;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 7/30/17.
 */
public class ApolloManagerTest {
    @Test
    public void repositories() throws Exception {
        Response<RepoQuery.Data> response = new ApolloManager().repositories().execute();
       assertNotNull(response.data().organization().repositories());
    }

}