package ru.pascalman.translate.model;

import ru.pascalman.translate.model.dto.LookupResponseDTO;
import rx.Observable;

public interface DictionaryModel
{

    Observable<LookupResponseDTO> lookup(String lang, String text);

}
