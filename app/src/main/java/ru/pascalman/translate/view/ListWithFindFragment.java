package ru.pascalman.translate.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.*;
import android.view.View;

import java.util.List;

import ru.pascalman.translate.MainActivity;
import ru.pascalman.translate.R;
import ru.pascalman.translate.databinding.ListWithFindFragmentBinding;
import ru.pascalman.translate.presenter.ListWithFindPresenter;
import ru.pascalman.translate.presenter.LookupResponse;
import ru.pascalman.translate.view.adapters.TranslateAdapter;

public class ListWithFindFragment extends Fragment implements ListWithFindView, View.OnClickListener, View.OnLongClickListener
{

    private boolean isOnlyFavorite;
    private String type;
    private ListWithFindFragmentBinding binding;
    private TranslateAdapter adapter;
    private ListWithFindPresenter presenter;
    private int idLongClickedResponse;
    private AlertDialog removeResponseDialog;
    private AlertDialog clearResponses;

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        isOnlyFavorite = getArguments().getBoolean("isOnlyFavorite");
        type = isOnlyFavorite ? "Favorite" : "History";
        binding = DataBindingUtil.inflate(inflater, R.layout.list_with_find_fragment,container, false);
        adapter = new TranslateAdapter(this, this);
        removeResponseDialog = new AlertDialog.Builder(getContext())
                .setTitle("Remove search result")
                .setMessage("Do you want to remove search result?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> presenter.removeResponseById(idLongClickedResponse))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                .create();
        clearResponses = new AlertDialog.Builder(getContext())
                .setTitle("Clear " + type)
                .setMessage("Do you really want to clear " + type + "?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    presenter.clearResponses();
                    binding.rlSearchBar.setVisibility(View.GONE);
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                .create();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        binding.rvItems.setLayoutManager(layoutManager);
        binding.rvItems.setAdapter(adapter);

        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        presenter = new ListWithFindPresenter(this, isOnlyFavorite);

        binding.etFind.addTextChangedListener(presenter.textWatcher);
        binding.etFind.setOnEditorActionListener(presenter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser)
            presenter.init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.search_remove_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_search:
                if (binding.rlSearchBar.getVisibility() == View.VISIBLE)
                    binding.rlSearchBar.setVisibility(View.GONE);
                else
                    binding.rlSearchBar.setVisibility(View.VISIBLE);
                break;
            case R.id.action_clear:
                clearResponses.show();
                break;
        }

        return true;
    }

    @Override
    public String getFindString()
    {
        return binding.etFind.getText().toString();
    }

    @Override
    public void showList(List<LookupResponse> list)
    {
        adapter.setList(list);
    }

    @Override
    public void showEmptyList()
    {
        adapter.clearList();
        makeToast(type + " is empty");
    }

    private void makeToast(String message)
    {
        Snackbar.make(binding.rvItems, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(String error)
    {
        makeToast(error);
    }

    @Override
    public void onClick(View v)
    {
        ((MainActivity)getActivity()).openTranslationWithResponseId(v.getId());
    }

    @Override
    public boolean onLongClick(View v)
    {
        idLongClickedResponse = v.getId();

        removeResponseDialog.show();

        return true;
    }

}