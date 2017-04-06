package ru.pascalman.translate.presenter;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.Collection;

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

                    String[] defaultLanguages = getDefaultLanguages();

                    translateFrom = defaultLanguages[0];
                    translateTo = defaultLanguages[1];

                    view.setDefaultLanguages(translateFrom, translateTo);
                }

            });

        addSubscription(subscription);
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
                        long lastId = realm.where(LookupResponse.class).max("id").longValue() + 1;

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

    @Override
    protected View getView()
    {
        return view;
    }

}