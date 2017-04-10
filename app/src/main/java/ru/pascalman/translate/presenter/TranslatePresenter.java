package ru.pascalman.translate.presenter;

import android.util.Log;

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

public class TranslatePresenter extends BasePresenter
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

                    view.initLanguages(new ArrayList<>(languages.getLangs().values()));

                    String[] defaultLanguages = getDefaultLanguages();

                    translateFrom = defaultLanguages[0];
                    translateTo = defaultLanguages[1];

                    view.setChoiceLanguages(translateFrom, translateTo);
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

    public void onSearchButtonClick()
    {
        String translateText = view.getTranslateText();

        if (translateText.isEmpty())
            return;

        translate(translateText);
    }

    public void setTranslateFrom(String translateFrom)
    {
        this.translateFrom = translateFrom;
    }

    public void setTranslateTo(String translateTo)
    {
        this.translateTo = translateTo;
    }

    private void translate(String translateText)
    {
        if (!isAllowTranslateDirection(translateFrom, translateTo))
        {
            view.showError("Doesn't allow translate direction");

            return;
        }

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

    private boolean isAllowTranslateDirection(String translateFrom, String translateTo)
    {
        String translateFromClean = getLangAcronimByFullName(translateFrom);
        String translateToClean = getLangAcronimByFullName(translateTo);
        String translateDirection = translateFromClean + "-" + translateToClean;

        return languages.getDirs().contains(translateDirection);
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

    private String getTranslateDirection()
    {
        String translateFromClean = getLangAcronimByFullName(translateFrom);
        String translateToClean = getLangAcronimByFullName(translateTo);

        return translateFromClean + "-" + translateToClean;
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
                        Number lastId = realm.where(LookupResponse.class).max("id");
                        int nextId = lastId == null ? 0 : lastId.intValue() + 1;

                        lookupResponse.setId(nextId);
                        lookupResponse.setOriginalText(translateText);
                        lookupResponse.setTranslateFrom(translateFrom);
                        lookupResponse.setTranslateTo(translateTo);
                        lookupResponse.setTranslateDirection(translateDirection);

                        realm.beginTransaction();

                        lastLookupResponse = realm.copyToRealm(lookupResponse);

                        realm.commitTransaction();

                        view.showTranslatedText(lookupResponse.getText(), lookupResponse.getPos());
                        view.showList(lookupResponse.getSyns());
                    }

                });

        addSubscription(dictionarySubscription);
    }

    private String getDictionaryDirection()
    {
        String translateToClean = getLangAcronimByFullName(translateTo);

        return translateToClean + "-" + translateToClean;
    }

    public void setTranslateFavorite(boolean isFavorite)
    {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        lastLookupResponse.setFavorite(isFavorite);
        realm.commitTransaction();
    }

    public void updateTranslateFavorite()
    {
        if (lastLookupResponse != null)
            view.setTranslateFavorite(lastLookupResponse.isFavorite());
    }

    public void clearLookupResponse()
    {
        lastLookupResponse = null;
    }

    public void showLookupResponseById(int id)
    {
        Realm realm = Realm.getDefaultInstance();

        lastLookupResponse = realm.where(LookupResponse.class).equalTo("id", id).findFirst();
        translateFrom = lastLookupResponse.getTranslateFrom();
        translateTo = lastLookupResponse.getTranslateTo();

        view.setChoiceLanguages(translateFrom, translateTo);
        view.showOriginalText(lastLookupResponse.getOriginalText());
        view.showTranslatedText(lastLookupResponse.getText(), lastLookupResponse.getPos());
        view.setTranslateFavorite(lastLookupResponse.isFavorite());
        view.showList(lastLookupResponse.getSyns());
    }

    @Override
    protected View getView()
    {
        return view;
    }

}