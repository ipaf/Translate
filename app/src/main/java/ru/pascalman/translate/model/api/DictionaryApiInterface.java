package ru.pascalman.translate.model.api;

import retrofit2.http.GET;
import retrofit2.http.Query;

import ru.pascalman.translate.model.dto.LookupResponseDTO;
import rx.Observable;

public interface DictionaryApiInterface
{

    @GET("lookup")
    Observable<LookupResponseDTO> lookup(@Query("key") String key, @Query("lang") String lang, @Query("text") String text, @Query("ui") String ui, @Query("flags") int flags);

}
