package ru.pascalman.translate.model.dto;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tr 
{

    @SerializedName("syn")
    @Expose
    private List<Syn> syn = null;

    public List<Syn> getSyn() 
	{
        return syn;
    }

    public void setSyn(List<Syn> syn) 
	{
        this.syn = syn;
    }

}