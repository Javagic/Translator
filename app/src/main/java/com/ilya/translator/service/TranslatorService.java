package com.ilya.translator.service;

import android.util.Log;

import com.ilya.translator.models.TextEntity;
import com.ilya.translator.models.pojo.DictionaryModel;
import com.ilya.translator.models.pojo.LanguageTranslation;
import com.ilya.translator.models.LanguageType;
import com.ilya.translator.models.Pair;
import com.ilya.translator.models.pojo.PossibleLanguages;
import com.ilya.translator.service.http.HttpService;
import com.ilya.translator.utils.CRUDService;
import com.ilya.translator.utils.Const;
import com.ilya.translator.utils.RxBackgroundWrapper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 08.04.17 15:54.
 */
public class TranslatorService {
    private static TranslatorService instance;
    private List<LanguageType> languageTypes;
    private List<Pair> pairs;
    private LanguageType currentInput;
    private LanguageType currentOutput;
    private Pair currentPair;
    public TextEntity textEntity;

    public static TranslatorService getInstance() {
        if (instance == null) {
            instance = new TranslatorService();
        }
        return instance;
    }

    private TranslatorService() {
        textEntity = new TextEntity();
        setCurrentInput(new LanguageType("ru", "Русский"));
        setCurrentOutput(new LanguageType("en", "Английский"));
        textEntity.inputLanguage = currentInput.shortName;
        textEntity.outputLanguage = currentOutput.shortName;
    }

    public Observable<PossibleLanguages> loadLanguageVariations() {
        return RxBackgroundWrapper.doInBackground(
                HttpService.getInstance().getLanguages("ru"))
                .doOnNext(possibleLanguages1 -> {
                    languageTypes = LanguageType.getList(possibleLanguages1.langs);
                    pairs = Pair.asList(possibleLanguages1.dirs);
                }).doOnError(throwable -> {
                    Log.i(Const.MY_LOG,"error getlangs internet " + throwable.getMessage() + throwable.getClass());
                });
    }

    public List<LanguageType> getLanguageTypes() {
        return languageTypes;
    }

    public Observable<DictionaryModel.DefModel> lookup(String query) {
        String pair = Pair.pairFrom(getCurrentInput(), getCurrentOutput());
        Log.i(Const.MY_LOG, "lookup: " + query + " " + pair, null);
        return RxBackgroundWrapper.doInBackground(HttpService.getInstance().lookup(query, pair));
    }

    public Observable<LanguageTranslation> translate(CharSequence text) throws UnsupportedEncodingException {
        Log.i(Const.MY_LOG, "translate: " + text.toString() + " " + currentPair.toString(), null);
        return RxBackgroundWrapper.doInBackground(
                HttpService.getInstance().translate(text.toString(), currentPair.toString(), "plain", "1"));
    }

    public void setCurrentInput(LanguageType currentInput) {
        this.currentInput = currentInput;
        if (currentOutput != null)
            makePair();
    }

    public void setCurrentOutput(LanguageType currentOutput) {
        this.currentOutput = currentOutput;
        if (currentInput != null)
            makePair();
    }

    private void makePair() {
        currentPair = new Pair(currentInput, currentOutput);
    }

    public LanguageType getCurrentInput() {
        return currentInput;
    }

    public LanguageType getCurrentOutput() {
        return currentOutput;
    }

    public void swapLanguages() {//TODO:поменять также сущность
        LanguageType languageType = new LanguageType();
        languageType.longName = currentInput.longName;
        languageType.shortName = currentInput.shortName;
        currentInput = currentOutput;
        currentOutput = languageType;
        makePair();
        textEntity.inputLanguage = currentInput.shortName;
        textEntity.outputLanguage = currentOutput.shortName;
        String inputText = textEntity.inputText;
        textEntity.inputText = textEntity.outputText;
        textEntity.outputText = inputText;
    }

    public void setLanguageTypes(List<LanguageType> languageTypes) {
        this.languageTypes = languageTypes;
    }

}
