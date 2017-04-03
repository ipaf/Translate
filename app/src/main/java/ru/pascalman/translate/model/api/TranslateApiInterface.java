package ru.pascalman.translate.model.api;

import retrofit2.http.GET;
import retrofit2.http.Query;

import ru.pascalman.translate.model.dto.LanguagesDTO;
import ru.pascalman.translate.model.dto.TranslateResponseDTO;
import rx.Observable;

public interface TranslateApiInterface
{

    @GET("getLangs")
    Observable<LanguagesDTO> getLanguages(@Query("key") String key, @Query("ui") String ui);

    @GET("translate")
    Observable<TranslateResponseDTO> translate(@Query("key") String key, @Query("lang") String lang, @Query("text") String text);

}
