package ru.pascalman.translate.presenter;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import io.realm.Realm;
import ru.pascalman.translate.presenter.mappers.LanguagesMapper;
import ru.pascalman.translate.presenter.mappers.LookupResponseMapper;
import ru.pascalman.translate.presenter.mappers.TranslateResponseMapper;
import ru.pascalman.translate.view.TranslateView;
import ru.pascalman.translate.view.View;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class TranslatePresenter extends BasePresenter implements TextView.OnEditorActionListener
{

    private TranslateView view;
    private LanguagesMapper languagesMapper;
    private TranslateResponseMapper translateResponseMapper;
    private LookupResponseMapper lookupResponseMapper;
    private Languages languages;
    private String translateFrom;
    private String translateTo;
    private LookupResponse lastLookupResponse;
    private int lookupResposeIdForShow = -1;

    public TranslatePresenter(TranslateView view)
    {
        this.view = view;

        languagesMapper = new LanguagesMapper();
        translateResponseMapper = new TranslateResponseMapper();
        lookupResponseMapper = new LookupResponseMapper();

        initLanguages();
    }

    private void initLanguages()
    {
        Subscription subscription = translateModel.getLanguages()
            .map(languagesMapper)
            .subscribe(new Observer<Languages>() {

                @Override
                public void onCompleted()
                {}

                @Override
                public void onError(Throwable e)
                {
                    Log.d(TranslatePresenter.class.getName(), e.toString());
                    showError(e);
                }

                @Override
                public void onNext(Languages languages)
                {
                    TranslatePresenter.this.languages = languages;

                    view.initLanguages(new ArrayList<>(languages.getLangs().values()));

                    if (lookupResposeIdForShow > -1)
                        showLookupResponseById(lookupResposeIdForShow);
                    else
                    {
                        String[] defaultLanguages = getDefaultLanguages();

                        translateFrom = defaultLanguages[0];
                        translateTo = defaultLanguages[1];
                    }

                    view.setChoiceLanguages(translateFrom, translateTo);
                }

            });

        addSubscription(subscription);
    }

    private void showLookupResponseById(int id)
    {
        Realm realm = Realm.getDefaultInstance();

        lastLookupResponse = realm.where(LookupResponse.class).equalTo("id", id).findFirst();
        translateFrom = languages.getLangs().get(lastLookupResponse.getTranslateDirection().substring(0, 1));
        translateTo = languages.getLangs().get(lastLookupResponse.getTranslateDirection().substring(3, 4));

        view.showOriginalText(lastLookupResponse.getOriginalText());
        view.showTranslatedText(lastLookupResponse.getText(), lastLookupResponse.getPos());
        view.setTranslateFavorite(lastLookupResponse.isFavorite());
        view.showList(lastLookupResponse.getSyns());
    }

    private String[] getDefaultLanguages()
    {
        String[] defaultLanguages = new String[2];
        Collection<String> languagesValues = languages.getLangs().values();
        String[] langs = languagesValues.toArray(new String[languagesValues.size()]);

        System.arraycopy(langs, 0, defaultLanguages, 0, defaultLanguages.length);

        return defaultLanguages;
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
        String translateText = view.getTranslateText();

        if (translateText.isEmpty())
            return;

        translate(translateText);
    }

    public void setTranslateFrom(String translateFrom)
    {
        String translateFromClean = getLangAcronimByFullName(translateFrom);

        if (isAllowTranslateDirection(translateFromClean, translateTo))
            this.translateFrom = translateFrom;
        else
            view.showError("Doesn't allow translate direction");
    }

    private String getLangAcronimByFullName(String fullName)
    {
        String langAcronim = "";
        Map<String, String> langs = languages.getLangs();

        for (String key : langs.keySet())
            if (langs.get(key).equals(fullName))
                langAcronim = key;

        return langAcronim;
    }

    private boolean isAllowTranslateDirection(String translateFrom, String translateTo)
    {
        String translateDirection = translateFrom + "-" + translateTo;

        return languages.getDirs().contains(translateDirection);
    }

    public void setTranslateTo(String translateTo)
    {
        String translateToClean = getLangAcronimByFullName(translateTo);

        if (isAllowTranslateDirection(translateFrom, translateToClean))
            this.translateTo = translateTo;
        else
            view.showError("Doesn't allow translate direction");
    }

    private void translate(String translateText)
    {
        String translateDirection = getTranslateDirection();
        Subscription translateSubscription = translateModel.translate(translateDirection, translateText)
                .map(translateResponseMapper)
                .subscribe(new Observer<TranslateResponse>() {

                    @Override
                    public void onCompleted()
                    {}

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.d(TranslatePresenter.class.getName(), e.toString());
                        showError(e);
                    }

                    @Override
                    public void onNext(TranslateResponse translateResponse)
                    {
                        String translatedText = Observable.from(translateResponse.getText())
                                .toBlocking()
                                .first();

                        lookup(translateText, translateDirection, translatedText);
                    }

                });


        addSubscription(translateSubscription);
    }

    private String getTranslateDirection()
    {
        return translateFrom + "-" + translateTo;
    }

    private void lookup(String translateText, String translateDirection, String translatedText)
    {
        Subscription dictionarySubscription = dictionaryModel.lookup(getDictionaryDirection(), translatedText)
                .map(lookupResponseMapper)
                .subscribe(new Observer<LookupResponse>() {

                    @Override
                    public void onCompleted()
                    {}

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.d(TranslatePresenter.class.getName(), e.toString());
                        showError(e);
                    }

                    @Override
                    public void onNext(LookupResponse lookupResponse)
                    {
                        Realm realm = Realm.getDefaultInstance();
                        int lastId = realm.where(LookupResponse.class).max("id").intValue() + 1;

                        lookupResponse.setId(lastId);
                        lookupResponse.setOriginalText(translateText);
                        lookupResponse.setTranslateDirection(translateDirection);

                        lastLookupResponse = realm.copyToRealm(lookupResponse);

                        view.showTranslatedText(lookupResponse.getText(), lookupResponse.getPos());
                        view.showList(lookupResponse.getSyns());
                    }

                });

        addSubscription(dictionarySubscription);
    }

    private String getDictionaryDirection()
    {
        return translateTo + "-" + translateTo;
    }

    public void setTranslateFavorite(boolean isFavorite)
    {
        lastLookupResponse.setFavorite(isFavorite);
    }

    public void openLookupResponseById(int id)
    {
        if (languages != null)
            showLookupResponseById(id);
        else
            lookupResposeIdForShow = id;
    }

    @Override
    protected View getView()
    {
        return view;
    }

}