package ru.pascalman.translate.presenter;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LookupResponse implements Serializable
{

    private String text;
    private String pos;
    private List<String> syns = null;

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

    public List<String> getSyns() 
	{
        return syns;
    }

    public void setSyns(List<String> syns) 
	{
        this.syns = syns;
    }

    @Override
    public int hashCode() 
	{
        return new HashCodeBuilder().append(text).append(pos).append(syns).toHashCode();
    }

    @Override
    public boolean equals(Object other) 
	{
        if (other == this) 
            return true;
        
        if (!(other instanceof LookupResponse))
            return false;
        
        LookupResponse rhs = ((LookupResponse) other);
		
        return new EqualsBuilder().append(text, rhs.text).append(pos, rhs.pos).append(syns, rhs.syns).isEquals();
    }

}