package ru.pascalman.translate.presenter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Languages implements Serializable
{

    private List<String> dirs = null;
    private Map<String, String> langs = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Languages()
	{}

    /**
     * 
     * @param langs
     * @param dirs
     */
    public Languages(List<String> dirs, Map<String, String> langs)
	{
        this.dirs = dirs;
        this.langs = langs;
    }

    public List<String> getDirs() 
	{
        return dirs;
    }

    public void setDirs(List<String> dirs) 
	{
        this.dirs = dirs;
    }

    public Map<String, String> getLangs() 
	{
        return langs;
    }

    public void setLangs(Map<String, String> langs) 
	{
        this.langs = langs;
    }

    @Override
    public int hashCode() 
	{
        return new HashCodeBuilder().append(dirs).append(langs).toHashCode();
    }

    @Override
    public boolean equals(Object other) 
	{
        if (other == this)
            return true;
        
        if (!(other instanceof Languages))
            return false;
        
        Languages rhs = ((Languages) other);
		
        return new EqualsBuilder().append(dirs, rhs.dirs).append(langs, rhs.langs).isEquals();
    }

}