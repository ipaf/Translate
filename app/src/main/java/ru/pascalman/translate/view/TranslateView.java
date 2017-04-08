package ru.pascalman.translate.view;

import java.util.List;

public interface TranslateView extends ListView<String>
{

    void setChoiceLanguages(String translateFrom, String translateTo);
    void setTranslateFavorite(boolean isFavorite);
    void initLanguages(List<String> languages);
    String getTranslateText();
    void showTranslatedText(String translatedText, String pos);
    void showOriginalText(String originalText);

}