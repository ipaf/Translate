package ru.pascalman.translate.presenter.mappers;

import java.util.List;

import io.realm.RealmList;
import ru.pascalman.translate.model.dto.Def;
import ru.pascalman.translate.model.dto.LookupResponseDTO;
import ru.pascalman.translate.model.dto.Syn;
import ru.pascalman.translate.presenter.LookupResponse;

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
        List<Syn> syns = def.getTr().get(0).getSyn();
        RealmList<ru.pascalman.translate.presenter.Syn> realmSyns = new RealmList<>();
        
        for (Syn syn : syns)
            realmSyns.add(new ru.pascalman.translate.presenter.Syn(syn.getText(), syn.getPos()));

        return new LookupResponse(text, pos, realmSyns);
    }

}