package ru.pascalman.translate.model;

import ru.pascalman.translate.model.api.TranslateApiInterface;
import ru.pascalman.translate.model.dto.LanguagesDTO;
import ru.pascalman.translate.model.dto.TranslateResponseDTO;
import ru.pascalman.translate.other.Const;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TranslateModelImpl implements TranslateModel
{

    private final Observable.Transformer schedulersTransformer;

    private TranslateApiInterface apiInterface;
    private Scheduler uiThread;
    private Scheduler ioThread;

    public TranslateModelImpl(TranslateApiInterface apiInterface)
    {
        this.apiInterface = apiInterface;

        uiThread = AndroidSchedulers.mainThread();
        ioThread = Schedulers.io();
        schedulersTransformer = o -> ((Observable) o).subscribeOn(ioThread)
            .observeOn(uiThread)
            .unsubscribeOn(ioThread);
    }

    @Override
    public Observable<LanguagesDTO> getLanguages()
    {
        return apiInterface
            .getLanguages(Const.TRANSLATE_KEY, "ru")
            .compose(applySchedulers());
    }

    @Override
    public Observable<TranslateResponseDTO> translate(String lang, String text)
    {
        return apiInterface
            .translate(Const.TRANSLATE_KEY, lang, text)
            .compose(applySchedulers());
    }

    @SuppressWarnings("unchecked")
    private <T> Observable.Transformer<T, T> applySchedulers()
    {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }

}
