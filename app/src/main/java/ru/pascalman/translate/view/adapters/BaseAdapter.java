package ru.pascalman.translate.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T1, T2 extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T2>
{

    protected List<T1> list;
    protected View.OnClickListener listener;

    public BaseAdapter(View.OnClickListener listener)
    {
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void setList(List<T1> list)
    {
        this.list = list;

        notifyDataSetChanged();
    }

    public List<T1> getList()
    {
        return list;
    }

    public void clearList()
    {
        list.clear();
        notifyDataSetChanged();
    }

}