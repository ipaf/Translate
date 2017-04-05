package ru.pascalman.translate.view;

import java.util.List;

import ru.pascalman.translate.presenter.SynItem;

public interface TranslateView extends ListView<SynItem>
{

    void setDefaultLanguages(String translateFrom, String translateTo);
    String getTranslateText();
    void showTranslatedText(String translatedText, String pos, List<String> syns);

}