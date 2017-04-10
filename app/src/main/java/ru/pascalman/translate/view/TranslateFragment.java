package ru.pascalman.translate.view;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.*;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ru.pascalman.translate.MainActivity;
import ru.pascalman.translate.R;
import ru.pascalman.translate.databinding.TranslateFragmentBinding;
import ru.pascalman.translate.presenter.Syn;
import ru.pascalman.translate.presenter.TranslatePresenter;
import ru.pascalman.translate.view.adapters.SynAdapter;

public class TranslateFragment extends Fragment implements TranslateView, View.OnClickListener, TextView.OnEditorActionListener
{

    private TranslateFragmentBinding binding;
    private TranslatePresenter presenter;
    private SynAdapter adapter;
    private boolean isTranslateFromDialogOpened;
    private AlertDialog changeLanguageDialog;
    private int lookupResponseId = -1;

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.translate_fragment, container, false);
        adapter = new SynAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        binding.rvTranslates.setLayoutManager(layoutManager);
        binding.rvTranslates.setAdapter(adapter);

        binding.btnTranslateFrom.setOnClickListener(this);
        binding.btnSwapLanguages.setOnClickListener(this);
        binding.btnTranslateTo.setOnClickListener(this);
        binding.btnClearTranslateText.setOnClickListener(this);
        binding.btnFavorite.setOnClickListener(this);

        binding.etTranslateText.setOnEditorActionListener(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        presenter = new TranslatePresenter(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser || presenter == null)
            return;

        if (lookupResponseId > -1)
        {
            presenter.showLookupResponseById(lookupResponseId);

            lookupResponseId = -1;
        } else
            presenter.updateTranslateFavorite();
    }

    @Override
    public void initLanguages(List<String> languages)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, languages);

        changeLanguageDialog = new AlertDialog.Builder(getContext())
                .setTitle("Choice language")
                .setSingleChoiceItems(adapter, 0, (dialog, which) -> {
                    String choiceLanguage = adapter.getItem(which);

                    if (isTranslateFromDialogOpened)
                    {
                        presenter.setTranslateFrom(choiceLanguage);
                        binding.btnTranslateFrom.setText(choiceLanguage);
                    }
                    else
                    {
                        presenter.setTranslateTo(choiceLanguage);
                        binding.btnTranslateTo.setText(choiceLanguage);
                    }

                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create();
    }

    @Override
    public void setChoiceLanguages(String translateFrom, String translateTo)
    {
        binding.btnTranslateFrom.setText(translateFrom);
        binding.btnTranslateTo.setText(translateTo);
    }

    @Override
    public void setTranslateFavorite(boolean isFavorite)
    {
        Drawable favoriteDrawable;

        if (isFavorite)
            favoriteDrawable = getResources().getDrawable(android.R.drawable.star_big_on);
        else
            favoriteDrawable = getResources().getDrawable(android.R.drawable.star_big_off);

        binding.btnFavorite.setImageDrawable(favoriteDrawable);
    }

    @Override
    public String getTranslateText()
    {
        return binding.etTranslateText.getText().toString();
    }

    @Override
    public void showTranslatedText(String translatedText, String pos)
    {
        binding.tvTranslatedText.setText(translatedText);
        binding.tvPos.setText(pos);
        binding.rlTranslateResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showList(List<Syn> list)
    {
        adapter.setList(list);
    }

    @Override
    public void showEmptyList()
    {
        makeToast("Not syn");
    }

    private void makeToast(String message)
    {
        Snackbar.make(binding.rvTranslates, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(String error)
    {
        makeToast(error);
    }

    @Override
    public void showOriginalText(String originalText)
    {
        binding.etTranslateText.setText(originalText);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnClearTranslateText:
                presenter.clearLookupResponse();
                binding.etTranslateText.getText().clear();
                binding.rlTranslateResult.setVisibility(View.INVISIBLE);
                binding.btnFavorite.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off));
                break;
            case R.id.btnTranslateFrom:
                isTranslateFromDialogOpened = true;
                changeLanguageDialog.show();
                break;
            case R.id.btnTranslateTo:
                isTranslateFromDialogOpened = false;
                changeLanguageDialog.show();
                break;
            case R.id.btnSwapLanguages:
                swapLanguages();
                break;
            case R.id.btnFavorite:
                updateTranslateFavorite();
                break;
        }
    }

    private void swapLanguages()
    {
        Button btnTranslateFrom = binding.btnTranslateFrom;
        Button btnTranslateTo = binding.btnTranslateTo;
        String translateFrom = btnTranslateFrom.getText().toString();
        String translateTo = btnTranslateTo.getText().toString();
        String temp = translateFrom;

        translateFrom = translateTo;
        translateTo = temp;

        presenter.setTranslateFrom(translateFrom);
        presenter.setTranslateTo(translateTo);

        btnTranslateFrom.setText(translateFrom);
        btnTranslateTo.setText(translateTo);
    }

    private void updateTranslateFavorite()
    {
        boolean isFavorite = binding.btnFavorite.getDrawable().getConstantState().equals(getResources().getDrawable(android.R.drawable.star_big_on).getConstantState());
        boolean isNeedFavorite = !isFavorite;

        setTranslateFavorite(isNeedFavorite);
        presenter.setTranslateFavorite(isNeedFavorite);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if (actionId == EditorInfo.IME_ACTION_SEARCH)
        {
            presenter.onSearchButtonClick();
            ((MainActivity)getActivity()).hideSoftKeyboard();
        }

        return actionId == EditorInfo.IME_ACTION_SEARCH;
    }

    public void setLookupResponseById(int lookupResponseId)
    {
        this.lookupResponseId = lookupResponseId;
    }

}