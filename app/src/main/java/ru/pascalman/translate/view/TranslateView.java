package ru.pascalman.translate.view;

import java.util.List;

public interface TranslateView extends ListView<String>
{

    void setDefaultLanguages(String translateFrom, String translateTo);
    void initLanguages(List<String> languages);
    String getTranslateText();
    void showTranslatedText(String translatedText, String pos);

}