package ru.pascalman.translate.presenter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import ru.pascalman.translate.view.ListWithFindView;
import ru.pascalman.translate.view.View;
import rx.Observable;

public class ListWithFindPresenter extends BasePresenter implements TextView.OnEditorActionListener
{

    private ListWithFindView view;
    private List<LookupResponse> responses;

    public TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}

        @Override
        public void afterTextChanged(Editable s)
        {
            onSearchButtonClick();
        }

    };

    public ListWithFindPresenter(ListWithFindView view, boolean isOnlyFavorite)
    {
        this.view = view;

        init(isOnlyFavorite);
    }

    private void init(boolean isOnlyFavorite)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LookupResponse> realmResults = realm.where(LookupResponse.class).findAll();

        if (isOnlyFavorite)
            realmResults = realmResults.where().equalTo("isFavorite", true).findAll();

        responses = realmResults;

        if (responses.size() > 0)
            view.showList(responses);
        else
            view.showEmptyList();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if (actionId == EditorInfo.IME_ACTION_SEARCH)
            onSearchButtonClick();

        return actionId == EditorInfo.IME_ACTION_SEARCH;
    }

    private void onSearchButtonClick()
    {
        String findString = view.getFindString();

        if (findString.isEmpty())
            return;

        List<LookupResponse> findResult = Observable.from(responses)
                .filter(response -> response.getOriginalText().contains(findString))
                .toList()
                .toBlocking()
                .first();

        if (findResult.size() > 0)
            view.showList(findResult);
        else
            view.showEmptyList();
    }

    @Override
    protected View getView()
    {
        return view;
    }

}