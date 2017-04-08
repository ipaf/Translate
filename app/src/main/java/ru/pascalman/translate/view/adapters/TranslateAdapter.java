package ru.pascalman.translate.view.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.pascalman.translate.R;
import ru.pascalman.translate.databinding.TranslateListItemBinding;
import ru.pascalman.translate.presenter.LookupResponse;

public class TranslateAdapter extends BaseAdapter<LookupResponse, TranslateAdapter.TranslateViewHolder>
{

    private View.OnLongClickListener longClickListener;

    public TranslateAdapter(View.OnClickListener listener, View.OnLongClickListener longClickListener)
    {
        super(listener);

        this.longClickListener = longClickListener;
    }

    @Override
    public TranslateViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TranslateListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.translate_list_item, viewGroup, false);
        View bindingRoot = binding.getRoot();

        bindingRoot.setOnClickListener(listener);
        bindingRoot.setOnLongClickListener(longClickListener);

        return new TranslateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TranslateViewHolder viewHolder, int i)
    {
        LookupResponse lookupResponse = list.get(i);

        viewHolder.setLookupResponse(lookupResponse);
    }

    public class TranslateViewHolder extends RecyclerView.ViewHolder
    {

        private TranslateListItemBinding binding;

        public TranslateViewHolder(TranslateListItemBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void setLookupResponse(LookupResponse lookupResponse)
        {
            binding.setLookupResponse(lookupResponse);
            binding.getRoot().setId(lookupResponse.getId());
        }

    }

}