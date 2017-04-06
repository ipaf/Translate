package ru.pascalman.translate.view;

public interface TranslateView extends ListView<String>
{

    void setDefaultLanguages(String translateFrom, String translateTo);
    String getTranslateText();
    void showTranslatedText(String translatedText, String pos);

}