package app.my_group.com.myapplication.network;

import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.services.params.Geocode;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpHelper {

    @GET("/1.1/search/tweets.json?" +
            "tweet_mode=extended&include_cards=true&cards_platform=TwitterKit-13")
    Observable<Search> tweets(@Query("q") String query,
                              //EncodedQuery protects commas from encode
                              @Query(value = "geocode", encoded = true) Geocode geocode,
                              @Query("lang") String lang,
                              @Query("locale") String locale,
                              @Query("result_type") String resultType,
                              @Query("count") Integer count,
                              @Query("until") String until,
                              @Query("since_id") Long sinceId,
                              @Query("max_id") Long maxId,
                              @Query("include_entities") Boolean includeEntities);
}