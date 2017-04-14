package com.ilya.translator.service;

import com.ilya.translator.models.pojo.LanguageTranslation;
import com.ilya.translator.models.LanguageType;
import com.ilya.translator.models.Pair;
import com.ilya.translator.models.pojo.PossibleLanguages;
import com.ilya.translator.service.http.HttpService;
import com.ilya.translator.utils.RxBackgroundWrapper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
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
    private PossibleLanguages possibleLanguages;
    private List<LanguageType> languageTypes;
    private List<Pair> pairs;
    private LanguageType currentInput;
    private LanguageType currentOutput;
    private Pair currentPair;

    public static TranslatorService getInstance() {
        if (instance == null) {
            instance = new TranslatorService();
        }
        return instance;
    }

    private TranslatorService() {
        setCurrentInput(new LanguageType("ru","Русский"));
        setCurrentOutput(new LanguageType("en","Английский"));
    }

    public Observable<PossibleLanguages> loadLanguageVariations() {
        return RxBackgroundWrapper.doInBackground(
                HttpService.getInstance().getLanguages("ru"))
                .doOnNext(possibleLanguages1 -> {
                    TranslatorService.this.possibleLanguages = possibleLanguages1;
                    languageTypes = LanguageType.getList(possibleLanguages1.langs);
                    pairs = Pair.asList(possibleLanguages1.dirs);
                }).doOnError(throwable -> {

                });
    }

    public List<LanguageType> getLanguageTypes() {
        return languageTypes;
    }

    public Observable<LanguageTranslation> translate(CharSequence text) throws UnsupportedEncodingException {
        return RxBackgroundWrapper.doInBackground(
                HttpService.getInstance().translate( text.toString(), currentPair.toString(), "plain", "1"));
    }

    public void setCurrentInput(LanguageType currentInput) {
        this.currentInput = currentInput;
        if (currentOutput != null)
            currentPair = new Pair(currentInput, currentOutput);
    }

    public void setCurrentOutput(LanguageType currentOutput) {
        this.currentOutput = currentOutput;
        if (currentInput != null)
        currentPair = new Pair(currentInput, currentOutput);
    }

    public LanguageType getCurrentInput() {
        return currentInput;
    }

    public LanguageType getCurrentOutput() {
        return currentOutput;
    }

    public void swapLanguages(){
        LanguageType languageType = new LanguageType();
        languageType.longName = currentInput.longName;
        languageType.shortName = currentInput.shortName;
        currentInput = currentOutput;
        currentOutput = languageType;
    }
}
