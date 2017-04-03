package ru.pascalman.translate.presenter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class TranslateItem implements Serializable
{

    private boolean isFavorite;
    private String originalText;
    private String translatedText;
    private String translateDirection;

    public TranslateItem()
    {}

    public TranslateItem(boolean isFavorite, String originalText, String translatedText, String translateDirection)
    {
        this.isFavorite = isFavorite;
        this.originalText = originalText;
        this.translatedText = translatedText;
        this.translateDirection = translateDirection;
    }

    public boolean isFavorite()
    {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite)
    {
        this.isFavorite = isFavorite;
    }

    public String getOriginalText()
    {
        return originalText;
    }

    public void setOriginalText(String originalText)
    {
        this.originalText = originalText;
    }

    public String getTranslatedText()
    {
        return translatedText;
    }

    public void setTranslatedText(String translatedText)
    {
        this.translatedText = translatedText;
    }

    public String getTranslateDirection()
    {
        return translateDirection;
    }

    public void setTranslateDirection(String translateDirection)
    {
        this.translateDirection = translateDirection;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(isFavorite).append(originalText).append(translatedText).append(translateDirection).toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
            return true;

        if (!(other instanceof TranslateItem))
            return false;

        TranslateItem rhs = ((TranslateItem) other);

        return new EqualsBuilder().append(isFavorite, rhs.isFavorite).append(originalText, rhs.originalText).append(translatedText, rhs.translatedText).append(translateDirection, rhs.translateDirection).isEquals();
    }

}