package ru.pascalman.translate.model;

import ru.pascalman.translate.model.api.DictionaryApiInterface;
import ru.pascalman.translate.model.dto.LookupResponseDTO;
import ru.pascalman.translate.other.Const;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DictionaryModelImpl implements DictionaryModel
{

    private final Observable.Transformer schedulersTransformer;

    private DictionaryApiInterface apiInterface;
    private Scheduler uiThread;
    private Scheduler ioThread;

    public DictionaryModelImpl(DictionaryApiInterface apiInterface)
    {
        this.apiInterface = apiInterface;

        uiThread = AndroidSchedulers.mainThread();
        ioThread = Schedulers.io();
        schedulersTransformer = o -> ((Observable) o).subscribeOn(ioThread)
                .observeOn(uiThread)
                .unsubscribeOn(ioThread);
    }

    @Override
    public Observable<LookupResponseDTO> lookup(String lang, String text)
    {
        return apiInterface
            .lookup(Const.DICTIONARY_KEY, lang, text, "ru", 0x0002)
            .compose(applySchedulers());
    }

    @SuppressWarnings("unchecked")
    private <T> Observable.Transformer<T, T> applySchedulers()
    {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }

}
