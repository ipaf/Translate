package ru.pascalman.translate.presenter;

import ru.pascalman.translate.model.DictionaryModel;
import ru.pascalman.translate.model.DictionaryModelImpl;
import ru.pascalman.translate.model.TranslateModel;
import ru.pascalman.translate.model.TranslateModelImpl;
import ru.pascalman.translate.model.api.ApiModule;
import ru.pascalman.translate.model.api.DictionaryApiInterface;
import ru.pascalman.translate.model.api.TranslateApiInterface;
import ru.pascalman.translate.other.Const;
import ru.pascalman.translate.view.View;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter implements Presenter
{

    protected TranslateModel translateModel;
    protected DictionaryModel dictionaryModel;
    protected CompositeSubscription compositeSubscription;

    public BasePresenter()
    {
        TranslateApiInterface translateApiInterface = ApiModule.getTranslateApiInterface(Const.TRANSLATE_URL);
        DictionaryApiInterface dictionaryApiInterface = ApiModule.getDictionaryApiInterface(Const.DICTIONARY_URL);

        translateModel = new TranslateModelImpl(translateApiInterface);
        dictionaryModel = new DictionaryModelImpl(dictionaryApiInterface);
        compositeSubscription = new CompositeSubscription();
    }

    protected void addSubscription(Subscription subscription)
    {
        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop()
    {
        compositeSubscription.clear();
    }

    protected abstract View getView();

    protected void showError(Throwable e)
    {
        getView().showError(e.getMessage());
    }

}