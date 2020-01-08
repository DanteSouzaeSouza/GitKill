package app.com.gitkill.apiutils;

import java.util.ArrayList;

import app.com.gitkill.models.languages.TrendingLanguage;
import app.com.gitkill.models.repositories.TrendingRepositories;
import app.com.gitkill.models.users.TrendingDevelopers;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface AllApiService {
    //Call for trending repositories
    @GET
    Call<ArrayList<TrendingRepositories>> getTrendingRepos(@Url String url);
    //Call for trending languages
    @GET
    Call<ArrayList<TrendingLanguage>> getTrendingLanguages(@Url String url);
    //Call for trending users
    @GET
    Call<ArrayList<TrendingDevelopers>> getTrendingUsers(@Url String url);
}
