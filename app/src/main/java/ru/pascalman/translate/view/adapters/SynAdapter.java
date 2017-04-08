package ru.pascalman.translate.view.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.pascalman.translate.R;
import ru.pascalman.translate.databinding.SynListItemBinding;

public class SynAdapter extends BaseAdapter<String, SynAdapter.SynViewHolder>
{

    public SynAdapter()
    {
        super(null);
    }

    @Override
    public SynViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SynListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.syn_list_item, viewGroup, false);

        return new SynViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SynViewHolder viewHolder, int i)
    {
        String text = list.get(i);

        viewHolder.setItem(i, text);
    }

    public class SynViewHolder extends RecyclerView.ViewHolder
    {

        private SynListItemBinding binding;

        public SynViewHolder(SynListItemBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void setItem(int index, String text)
        {
            binding.setText(text);
            binding.setIndex(index);
        }

    }

}