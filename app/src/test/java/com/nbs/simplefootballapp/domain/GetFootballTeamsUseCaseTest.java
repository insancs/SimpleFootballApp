package com.nbs.simplefootballapp.domain;

import com.nbs.simplefootballapp.BuildConfig;
import com.nbs.simplefootballapp.data.libs.ApiClient;
import com.nbs.simplefootballapp.data.libs.ApiManager;
import com.nbs.simplefootballapp.data.libs.ApiService;
import com.nbs.simplefootballapp.data.libs.OkHttpClientFactory;
import com.nbs.simplefootballapp.data.libs.ParameterInterceptor;
import com.nbs.simplefootballapp.data.model.entity.FootballTeam;
import com.nbs.simplefootballapp.data.model.request.GetFootballTeamRequest;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class GetFootballTeamsUseCaseTest {

    private List<FootballTeam> result;

    private ApiManager apiManager;

    private GetFootballTeamsUseCase getFootballTeamsUseCase;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Before
    public void setUp() {
        apiManager = new ApiManager(ApiService.createService(ApiClient.class,
                OkHttpClientFactory.create(new ParameterInterceptor(new HashMap<String, String>())),
                BuildConfig.BASE_URL));

        getFootballTeamsUseCase = new GetFootballTeamsUseCase(apiManager);
    }

    @Test
    public void shouldGetFootballTeams() throws InterruptedException {
        getFootballTeamsUseCase.setRequestModel(new GetFootballTeamRequest("English Premier League"));
        getFootballTeamsUseCase.setOnGetFootballTeamsCallback(new GetFootballTeamsUseCase.OnGetFootballTeamsCallback() {
            @Override
            public void onGetFootballTeamsSuccess(List<FootballTeam> footballTeams) {
                result = footballTeams;
                countDownLatch.countDown();
            }

            @Override
            public void onGetFootballTeamsFailed(String message) {
                countDownLatch.countDown();
            }
        });
        getFootballTeamsUseCase.execute();

        countDownLatch.await();
        assertTrue(result.size() > 0);
    }

    @Test
    public void shouldGetErrorFootballTeams() throws InterruptedException {
        getFootballTeamsUseCase.setRequestModel(new GetFootballTeamRequest("English Premier Leagues"));
        getFootballTeamsUseCase.setOnGetFootballTeamsCallback(new GetFootballTeamsUseCase.OnGetFootballTeamsCallback() {
            @Override
            public void onGetFootballTeamsSuccess(List<FootballTeam> footballTeams) {
                result = footballTeams;
                countDownLatch.countDown();
            }

            @Override
            public void onGetFootballTeamsFailed(String message) {
                countDownLatch.countDown();
            }
        });
        getFootballTeamsUseCase.execute();

        countDownLatch.await();
        assertTrue(result == null);
    }

}