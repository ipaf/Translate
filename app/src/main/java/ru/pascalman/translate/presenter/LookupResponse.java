package ru.pascalman.translate.presenter;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LookupResponse extends RealmObject implements Serializable
{

    @PrimaryKey
    private int id;
    private String originalText;
    private String text;
    private String pos;
    private String translateFrom;
    private String translateTo;
    private String translateDirection;
    private RealmList<Syn> syns = null;
    private boolean isFavorite;

    /**
     * No args constructor for use in serialization
     * 
     */
    public LookupResponse() 
	{}

    /**
     *
     * @param text
     * @param pos
     * @param syns
     */
    public LookupResponse(String text, String pos, RealmList<Syn> syns)
	{
        this.text = text;
        this.pos = pos;
        this.syns = syns;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getOriginalText()
    {
        return originalText;
    }

    public void setOriginalText(String originalText)
    {
        this.originalText = originalText;
    }

    public String getText() 
	{
        return text;
    }

    public void setText(String text) 
	{
        this.text = text;
    }

    public String getPos() 
	{
        return pos;
    }

    public void setPos(String pos) 
	{
        this.pos = pos;
    }

    public String getTranslateFrom()
    {
        return translateFrom;
    }

    public void setTranslateFrom(String translateFrom)
    {
        this.translateFrom = translateFrom;
    }

    public String getTranslateTo()
    {
        return translateTo;
    }

    public void setTranslateTo(String translateTo)
    {
        this.translateTo = translateTo;
    }

    public String getTranslateDirection()
    {
        return translateDirection;
    }

    public void setTranslateDirection(String translateDirection)
    {
        this.translateDirection = translateDirection;
    }

    public RealmList<Syn> getSyns()
	{
        return syns;
    }

    public void setSyns(RealmList<Syn> syns)
	{
        this.syns = syns;
    }

    public boolean isFavorite()
    {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite)
    {
        this.isFavorite = isFavorite;
    }

    @Override
    public int hashCode() 
	{
        return new HashCodeBuilder()
                .append(id)
                .append(originalText)
                .append(text)
                .append(pos)
                .append(translateFrom)
                .append(translateTo)
                .append(translateDirection)
                .append(syns)
                .append(isFavorite)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) 
	{
        if (other == this) 
            return true;
        
        if (!(other instanceof LookupResponse))
            return false;
        
        LookupResponse rhs = ((LookupResponse) other);
		
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(originalText, rhs.originalText)
                .append(text, rhs.text)
                .append(pos, rhs.pos)
                .append(translateFrom, rhs.translateFrom)
                .append(translateTo, rhs.translateTo)
                .append(translateDirection, rhs.translateDirection)
                .append(syns, rhs.syns)
                .append(isFavorite, rhs.isFavorite)
                .isEquals();
    }

}