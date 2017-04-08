package ru.pascalman.translate.presenter;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LookupResponse extends RealmObject implements Serializable
{

    @PrimaryKey
    private int id;
    private String originalText;
    private String text;
    private String pos;
    private String translateDirection;
    private List<String> syns = null;
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
    public LookupResponse(String text, String pos, List<String> syns)
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

    public String getTranslateDirection()
    {
        return translateDirection;
    }

    public void setTranslateDirection(String translateDirection)
    {
        this.translateDirection = translateDirection;
    }

    public List<String> getSyns() 
	{
        return syns;
    }

    public void setSyns(List<String> syns) 
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
        return new HashCodeBuilder().append(text).append(pos).append(syns).append(isFavorite).toHashCode();
    }

    @Override
    public boolean equals(Object other) 
	{
        if (other == this) 
            return true;
        
        if (!(other instanceof LookupResponse))
            return false;
        
        LookupResponse rhs = ((LookupResponse) other);
		
        return new EqualsBuilder().append(text, rhs.text).append(pos, rhs.pos).append(syns, rhs.syns).append(isFavorite, rhs.isFavorite).isEquals();
    }

}