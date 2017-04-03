package ru.pascalman.translate.presenter.mappers;

import ru.pascalman.translate.model.dto.LanguagesDTO;
import ru.pascalman.translate.presenter.Languages;

import rx.functions.Func1;

public class LanguagesMapper implements Func1<LanguagesDTO, Languages>
{


    @Override
    public Languages call(LanguagesDTO languagesDTO)
    {
        if (languagesDTO == null)
            return null;

        return new Languages(languagesDTO.getDirs(), languagesDTO.getLangs());
    }

}
