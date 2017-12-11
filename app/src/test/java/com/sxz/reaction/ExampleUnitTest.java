package com.sxz.reaction;


import com.sxz.reaction.model.Record;
import com.sxz.reaction.network.ReactionWorldRankService;
import com.sxz.reaction.network.WorldRank;
import com.sxz.reaction.network.WorldRankResponse;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private CountDownLatch lock = new CountDownLatch(1);
    @Test
    public void getRecordTest() throws Exception {
        ReactionWorldRankService service = WorldRank.getService(ReactionWorldRankService.class);
        service.getRecords("reaction","auditory").enqueue(new Callback<WorldRankResponse>() {
            @Override
            public void onResponse(Call<WorldRankResponse> call, Response<WorldRankResponse> response) {
                System.out.print(response.body().result);
                lock.countDown();
            }

            @Override
            public void onFailure(Call<WorldRankResponse> call, Throwable t) {

            }
        });
        lock.await(5000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void postRecordTest() throws Exception {
        ReactionWorldRankService service = WorldRank.getService(ReactionWorldRankService.class);
        Record r = new Record();
        r.setUserID("xshxsh");
        r.setTime((float)80.333);
        service.postRecords("reaction","auditory", r).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        lock.await(5000, TimeUnit.MILLISECONDS);
    }
}