package ru.pascalman.translate.presenter.mappers;

import ru.pascalman.translate.model.dto.TranslateResponseDTO;
import ru.pascalman.translate.presenter.TranslateResponse;

import rx.functions.Func1;

public class TranslateResponseMapper implements Func1<TranslateResponseDTO, TranslateResponse>
{

    @Override
    public TranslateResponse call(TranslateResponseDTO translateResponseDTO)
    {
        if (translateResponseDTO == null)
            return null;

        return new TranslateResponse(translateResponseDTO.getCode(), translateResponseDTO.getLang(), translateResponseDTO.getText());
    }

}
