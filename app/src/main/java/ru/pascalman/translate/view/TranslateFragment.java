package ru.pascalman.translate.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;

import ru.pascalman.translate.R;
import ru.pascalman.translate.databinding.TranslateFragmentBinding;
import ru.pascalman.translate.presenter.TranslatePresenter;
import ru.pascalman.translate.view.adapters.SynAdapter;

public class TranslateFragment extends Fragment implements TranslateView
{

    private TranslateFragmentBinding binding;
    private TranslatePresenter presenter;
    private SynAdapter adapter;
    private boolean isTranslateFromDialogOpened;
    private AlertDialog changeLanguageDialog;

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.translate_fragment, container, false);
        adapter = new SynAdapter();
        presenter = new TranslatePresenter(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        binding.rvTranslates.setLayoutManager(layoutManager);
        binding.rvTranslates.setAdapter(adapter);
        binding.etTranslateText.setOnEditorActionListener(presenter);

        return binding.getRoot();
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
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create();
    }

    @Override
    public void setDefaultLanguages(String translateFrom, String translateTo)
    {
        binding.btnTranslateFrom.setText(translateFrom);
        binding.btnTranslateTo.setText(translateTo);
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
        binding.llTranslateResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showList(List<String> list)
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

    public void onClearClicked(View view)
    {
        binding.etTranslateText.getText().clear();
        binding.llTranslateResult.setVisibility(View.INVISIBLE);
    }

    public void onChangeLanguageClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.btnTranslateFrom:
                isTranslateFromDialogOpened = true;
                break;
            case R.id.btnTranslateTo:
                isTranslateFromDialogOpened = false;
                break;
        }

        changeLanguageDialog.show();
    }

    public void onSwapLanguagesClicked(View view)
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

    public void onTranslateFavoriteClicked(View view)
    {
        ImageButton imageButton = (ImageButton) view;
        boolean isFavorite = imageButton.getDrawable().equals(getResources().getDrawable(android.R.drawable.star_big_on));
        boolean isNeedFavorite = !isFavorite;

        if (isNeedFavorite)
            imageButton.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
        else
            imageButton.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off));

        presenter.setTranslateFavorite(isNeedFavorite);
    }

}
