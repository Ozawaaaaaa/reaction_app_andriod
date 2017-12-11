package com.sxz.reaction.network;

import com.sxz.reaction.model.Record;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Path;

/**
 * Created by Shihao on 12/11/17.
 */

public class WorldRank {
    private static Retrofit worldRankClient = null;
    private static final String BASE_URL = "http://localhost:3030/";
    private static Retrofit getClient(String baseUrl) {
        if (worldRankClient==null) {
            worldRankClient = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return worldRankClient;
    }


    public static <T> T getService(Class<T> service){
        return getClient(BASE_URL).create(service);
    }

}

