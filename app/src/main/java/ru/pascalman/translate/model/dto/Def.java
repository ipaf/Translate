package ru.pascalman.translate.model.dto;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Def
{

    @SerializedName("tr")
    @Expose
    private List<Tr> tr = null;

    public List<Tr> getTr() 
	{
        return tr;
    }

    public void setTr(List<Tr> tr) 
	{
        this.tr = tr;
    }

}