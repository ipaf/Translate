package ru.pascalman.translate.model.dto;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LookupResponseDTO
{

    @SerializedName("def")
    @Expose
    private List<Def> def = null;

    public List<Def> getDef() 
	{
        return def;
    }

    public void setDef(List<Def> def) 
	{
        this.def = def;
    }

}