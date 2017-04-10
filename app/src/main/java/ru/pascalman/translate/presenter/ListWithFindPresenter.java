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
    private boolean isOnlyFavorite;
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
        this.isOnlyFavorite = isOnlyFavorite;
    }

    public void init()
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LookupResponse> realmResults = realm.where(LookupResponse.class).findAll();

        if (isOnlyFavorite)
            realmResults = realmResults.where().equalTo("isFavorite", true).findAll();

        responses = Observable.from(realmResults)
                .toList()
                .toBlocking()
                .first();

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
            view.showError("Nothing is found");
    }

    public void clearResponses()
    {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        if (isOnlyFavorite)
            for (LookupResponse lookupResponse : realm.where(LookupResponse.class).equalTo("isFavorite", true).findAll())
                lookupResponse.setFavorite(false);
        else
            realm.delete(LookupResponse.class);

        realm.commitTransaction();

        responses.clear();
        view.showList(responses);
        view.showError((isOnlyFavorite ? "Favorite" : "History") + " cleared");
    }

    public void removeResponseById(int id)
    {
        Realm realm = Realm.getDefaultInstance();
        LookupResponse response = realm.where(LookupResponse.class).equalTo("id", id).findFirst();

        for (int i = responses.size() - 1; i >= 0; i--)
        {
            LookupResponse responseForDelete = responses.get(i);

            if (responseForDelete.getId() == id)
            {
                responses.remove(responseForDelete);

                break;
            }
        }

        realm.beginTransaction();
        response.deleteFromRealm();
        realm.commitTransaction();

        if (responses.size() > 0)
            view.showList(responses);
        else
            view.showEmptyList();
    }

    @Override
    protected View getView()
    {
        return view;
    }

}