package ru.pascalman.translate.presenter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class SynItem implements Serializable
{

    private int number;
    private String text;

    public SynItem()
    {}

    public SynItem(int number, String text)
    {
        this.number = number;
        this.text = text;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(number).append(text).toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
            return true;

        if (!(other instanceof SynItem))
            return false;

        SynItem rhs = ((SynItem) other);

        return new EqualsBuilder().append(number, rhs.number).append(text, rhs.text).isEquals();
    }

}