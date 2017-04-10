package ru.pascalman.translate.presenter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

import io.realm.RealmObject;

public class Syn extends RealmObject implements Serializable
{

    private String text;
    private String pos;

    /**
     * No args constructor for use in serialization
     *
     */
    public Syn()
    {}

    /**
     *
     * @param text
     * @param pos
     */
    public Syn(String text, String pos)
    {
        this.text = text;
        this.pos = pos;
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

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(text).append(pos).toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
            return true;

        if (!(other instanceof Syn))
            return false;

        Syn rhs = ((Syn) other);

        return new EqualsBuilder().append(text, rhs.text).append(pos, rhs.pos).isEquals();
    }

}
