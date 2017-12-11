package com.sxz.reaction.network;

import com.sxz.reaction.model.Record;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Shihao on 12/11/17.
 */

public interface ReactionWorldRankService {
    @HTTP(method = "GET", path = "/{category}/{type}")
    Call<WorldRankResponse> getRecords(@Path("category") String category, @Path("type") String type);

    @HTTP(method = "POST", path = "/{category}/{type}", hasBody = true)
    Call<Void> postRecords(@Path("category") String category, @Path("type") String type,@Body Record record);
}
