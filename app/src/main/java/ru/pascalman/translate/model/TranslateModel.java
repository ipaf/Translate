package ru.pascalman.translate.model;

import ru.pascalman.translate.model.dto.LanguagesDTO;
import ru.pascalman.translate.model.dto.TranslateResponseDTO;
import rx.Observable;

public interface TranslateModel
{

    Observable<LanguagesDTO> getLanguages();
    Observable<TranslateResponseDTO> translate(String lang, String text);

}
