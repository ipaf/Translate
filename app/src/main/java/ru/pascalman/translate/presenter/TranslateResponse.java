package ru.pascalman.translate.presenter;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TranslateResponse implements Serializable
{

    private int code;
    private String lang;
    private List<String> text = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public TranslateResponse()
	{}

    /**
     * 
     * @param text
     * @param code
     * @param lang
     */
    public TranslateResponse(int code, String lang, List<String> text)
	{
        this.code = code;
        this.lang = lang;
        this.text = text;
    }

    public int getCode() 
	{
        return code;
    }

    public void setCode(int code) 
	{
        this.code = code;
    }

    public String getLang() 
	{
        return lang;
    }

    public void setLang(String lang) 
	{
        this.lang = lang;
    }

    public List<String> getText() 
	{
        return text;
    }

    public void setText(List<String> text) 
	{
        this.text = text;
    }

    @Override
    public int hashCode() 
	{
        return new HashCodeBuilder().append(code).append(lang).append(text).toHashCode();
    }

    @Override
    public boolean equals(Object other) 
	{
        if (other == this) 
            return true;
        
        if (!(other instanceof TranslateResponse))
            return false;
        
        TranslateResponse rhs = ((TranslateResponse) other);
		
        return new EqualsBuilder().append(code, rhs.code).append(lang, rhs.lang).append(text, rhs.text).isEquals();
    }

}