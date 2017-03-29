package ru.pascalman.translate.model.api;

import retrofit2.http.GET;
import retrofit2.http.Query;

import rx.Observable;

public interface DictionaryApiInterface
{

    @GET("lookup")
    Observable<String> lookup(@Query("key") String key, @Query("lang") String lang, @Query("text") String text, @Query("ui") String ui, @Query("flags") int flags);

}
