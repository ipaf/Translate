package ru.pascalman.translate.model.api;

import retrofit2.http.GET;
import retrofit2.http.Query;

import rx.Observable;

public interface TranslateApiInterface
{

    @GET("getLangs")
    Observable<String> getLanguages(@Query("key") String key, @Query("ui") String ui);

    @GET("translate")
    Observable<String> translate(@Query("key") String key, @Query("lang") String lang, @Query("text") String text);

}
