package ru.pascalman.translate.presenter.mappers;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

        return new Languages(languagesDTO.getDirs(), sortByValue(languagesDTO.getLangs()));
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map )
    {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());

        Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        Map<K, V> result = new LinkedHashMap<>();

        for (Map.Entry<K, V> entry : list)
            result.put(entry.getKey(), entry.getValue());

        return result;
    }

}