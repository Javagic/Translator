package com.ilya.translator;

import com.ilya.translator.Models.LanguageTranslation;
import com.ilya.translator.Models.PossibleLanguages;

import java.io.UnsupportedEncodingException;
import java.util.List;

import rx.Observable;

import static com.ilya.translator.Const.TRANSLATION_API_KEY;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 08.04.17 15:54.
 */
public class TranslatorManager {
    private static TranslatorManager instance;
    private PossibleLanguages possibleLanguages;
    private List<LanguageType> languageTypes;
    private List<Pair> pairs;
    private LanguageType currentFrom;
    private LanguageType currentTo;
    private Pair currentPair;

    public static TranslatorManager getInstance() {
        if (instance == null) {
            instance = new TranslatorManager();
        }
        return instance;
    }

    private TranslatorManager() {
        setCurrentFrom(new LanguageType("ru","Русский"));
        setCurrentTo(new LanguageType("en","Английский"));
    }

    public Observable<PossibleLanguages> loadLanguageVariations() {
        return RxBackgroundWrapper.doInBackground(
                HttpService.getInstance().getLanguages("en"))
                .doOnNext(possibleLanguages1 -> {
                    TranslatorManager.this.possibleLanguages = possibleLanguages1;
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

    public void setCurrentFrom(LanguageType currentFrom) {
        this.currentFrom = currentFrom;
        if (currentTo != null)
            currentPair = new Pair(currentFrom, currentTo);
    }

    public void setCurrentTo(LanguageType currentTo) {
        this.currentTo = currentTo;
        if (currentFrom != null)
        currentPair = new Pair(currentFrom, currentTo);
    }

    public LanguageType getCurrentFrom() {
        return currentFrom;
    }

    public LanguageType getCurrentTo() {
        return currentTo;
    }

}
