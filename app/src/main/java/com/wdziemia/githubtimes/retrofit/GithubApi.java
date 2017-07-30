package com.wdziemia.githubtimes.retrofit;

import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.ui.organization.OrganizationResponse;
import com.wdziemia.githubtimes.ui.repository.RepositoryResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GithubApi {

    @GET(BuildConfig.SEARCH_ORG_ENDPOINT)
    Observable<OrganizationResponse> searchOrganizations(@Query(value = "q", encoded = true) String orgName);

    @GET(BuildConfig.SEARCH_REPO_ENDPOINT)
    Observable<RepositoryResponse> searchRepositories(@Query(value = "q", encoded = true) String orgName,
                                                      @Query("sort") String sort,
                                                      @Query("order") String order,
                                                      @Query("per_page") int perPage);

}
