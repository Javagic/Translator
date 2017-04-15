package com.ilya.translator.service.http;

import com.ilya.translator.models.pojo.DictionaryModel;
import com.ilya.translator.models.pojo.LanguageTranslation;
import com.ilya.translator.models.pojo.PossibleLanguages;
import com.ilya.translator.utils.Const;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 03.04.17 19:38.
 */
public class HttpService {
    private static HttpService instance;

    private TranslationApi translationApi;
    private DictionaryApi dictionaryApi;

    public static HttpService getInstance() {
        if (instance == null) {
            instance = new HttpService();
        }
        return instance;
    }

    private HttpService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.TRANSLATION_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        translationApi = retrofit.create(TranslationApi.class);

        Retrofit retrofit1 = new Retrofit.Builder()//можно убрать?
                .baseUrl(Const.DICTIONARY_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        dictionaryApi = retrofit1.create(DictionaryApi.class);
    }


    public Observable<LanguageTranslation> translate(String text, String lang, String plain, String options) {
        return translationApi.translate(Const.TRANSLATION_API_KEY, text, lang, plain, options);
    }

    public Observable<PossibleLanguages> getLanguages(String langCode) {
        return translationApi.getLanguages(Const.TRANSLATION_API_KEY, langCode);
    }

    public Observable<DictionaryModel.DefModel> lookup(String text, String lang) {
        return dictionaryApi.lookup(Const.DICTIONARY_API_KEY, text, lang).flatMap(new Func1<DictionaryModel, Observable<DictionaryModel.DefModel>>() {
            @Override
            public Observable<DictionaryModel.DefModel> call(DictionaryModel dictionaryModel) {
                if(dictionaryModel.def.isEmpty()) return Observable.error(new Throwable("нет перевода"));
                return Observable.just(dictionaryModel.def.get(0));
            }
        });
    }
}