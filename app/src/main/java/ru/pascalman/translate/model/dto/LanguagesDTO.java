package ru.pascalman.translate.model.dto;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguagesDTO
{

    @SerializedName("dirs")
    @Expose
    private List<String> dirs = null;
    @SerializedName("langs")
    @Expose
    private Map<String, String> langs = null;

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

}