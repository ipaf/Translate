package ru.pascalman.translate.presenter.mappers;

import java.util.List;

import ru.pascalman.translate.model.dto.Def;
import ru.pascalman.translate.model.dto.LookupResponseDTO;
import ru.pascalman.translate.model.dto.Syn;
import ru.pascalman.translate.presenter.LookupResponse;

import rx.Observable;
import rx.functions.Func1;

public class LookupResponseMapper implements Func1<LookupResponseDTO, LookupResponse>
{

    @Override
    public LookupResponse call(LookupResponseDTO lookupResponseDTO)
    {
        if (lookupResponseDTO == null)
            return null;

        Def def = lookupResponseDTO.getDef().get(0);
        String text = def.getText();
        String pos = def.getPos();
        List<String> syns = Observable.from(def.getTr().get(0).getSyn())
                .map(Syn::getText)
                .toList()
                .toBlocking()
                .first();

        return new LookupResponse(text, pos, syns);
    }

}
