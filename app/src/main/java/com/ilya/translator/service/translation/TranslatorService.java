package com.ilya.translator.service.translation;

import android.util.Log;

import com.ilya.translator.models.LanguageType;
import com.ilya.translator.models.Pair;
import com.ilya.translator.models.TextEntity;
import com.ilya.translator.models.pojo.DictionaryModel;
import com.ilya.translator.models.pojo.LanguageTranslation;
import com.ilya.translator.models.pojo.PossibleLanguages;
import com.ilya.translator.service.http.HttpService;
import com.ilya.translator.utils.CRUDService;
import com.ilya.translator.utils.Const;
import com.ilya.translator.utils.RxBackgroundWrapper;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

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
    private boolean canTranslate;

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
        textEntity.id = CRUDService.getInstance().addTextEntity(textEntity);
    }

    public Observable<PossibleLanguages> loadLanguageVariations() {
        return HttpService.getInstance().getLanguages("ru").compose(RxBackgroundWrapper.applySchedulers())
                .doOnNext(possibleLanguages1 -> {
                    languageTypes = LanguageType.getList(possibleLanguages1.langs);
                    pairs = Pair.asList(possibleLanguages1.dirs);
                    makePair();
                }).doOnError(throwable -> {
                    Log.i(Const.MY_LOG, "error getlangs internet " + throwable.getMessage() + throwable.getClass());
                });
    }

    public List<LanguageType> getLanguageTypes() {
        return languageTypes;
    }

    private boolean checkPair() {
        for (Pair pair : pairs) {
            if (pair.toString().equals(currentPair.toString())) {
                return true;
            }
        }
        return false;
    }

    public Observable<DictionaryModel.DefModel> translate(CharSequence text) {
        Log.i(Const.MY_LOG, "translate: " + text.toString() + " " + currentPair.toString(), null);
        return HttpService.getInstance().translate(text.toString(), currentPair.toString()).compose(RxBackgroundWrapper.applySchedulers()).flatMap(new Func1<LanguageTranslation, Observable<DictionaryModel.DefModel>>() {
            @Override
            public Observable<DictionaryModel.DefModel> call(LanguageTranslation languageTranslation) {
                textEntity.outputText = languageTranslation.text.get(0);
                textEntity.inputText = text.toString();
                textEntity.inputLanguage = currentInput.shortName;
                textEntity.outputLanguage = currentOutput.shortName;
                textEntity.isMarked = false;
                return HttpService.getInstance().lookup(text.toString(), currentPair.toString()).compose(RxBackgroundWrapper.applySchedulers()).doOnNext(defModel -> {
                    textEntity.pos = defModel != null ? defModel.pos : "";
                    CRUDService.getInstance().updateTextEntity(textEntity);
                }).doOnError(throwable -> {
                    textEntity.pos = "";
                    CRUDService.getInstance().updateTextEntity(textEntity);
                });
            }
        });
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
        canTranslate = pairs != null && checkPair();
    }

    public LanguageType getCurrentInput() {
        return currentInput;
    }

    public LanguageType getCurrentOutput() {
        return currentOutput;
    }

    public TextEntity swapLanguages() {
        LanguageType languageType = new LanguageType();
        languageType.longName = currentInput.longName;
        languageType.shortName = currentInput.shortName;
        currentInput = currentOutput;
        currentOutput = languageType;
        makePair();
        String inputText = textEntity.outputText;
        String outputText = textEntity.inputText;
        textEntity = new TextEntity();
        textEntity.inputLanguage = currentInput.shortName;
        textEntity.outputLanguage = currentOutput.shortName;
        textEntity.inputText = inputText;
        textEntity.outputText = outputText;
        textEntity.id =CRUDService.getInstance().addTextEntity(textEntity);
        return textEntity;
    }

    public void setLanguageTypes(List<LanguageType> languageTypes) {
        this.languageTypes = languageTypes;
    }

    public TextEntity clearEntity() {
        textEntity = new TextEntity();
        textEntity.inputLanguage = currentInput.shortName;
        textEntity.outputLanguage = currentOutput.shortName;
        textEntity.id = CRUDService.getInstance().addTextEntity(textEntity);
        return textEntity;
    }

    public void setTextEntity(TextEntity textEntity) {
        this.textEntity = new TextEntity(textEntity);
        this.textEntity.id = CRUDService.getInstance().addTextEntity(textEntity);
        for (LanguageType languageType : languageTypes) {
            if (languageType.shortName.equals(textEntity.inputLanguage)) {
                currentInput = new LanguageType(languageType);
            } else if (languageType.shortName.equals(textEntity.outputLanguage)) {
                currentOutput = new LanguageType(languageType);
            }
        }
        makePair();
    }

    public boolean changeMark(boolean flag) {
        textEntity.isMarked = !textEntity.isMarked;
        if (flag) {
            for (TextEntity entity : CRUDService.getInstance().getFavorites()) {
                if (textEntity.equals(entity)) {
                    textEntity.isMarked = !textEntity.isMarked;
                    return false;
                }
            }
            CRUDService.getInstance().updateTextEntity(textEntity);
            textEntity = new TextEntity(textEntity);
            textEntity.id = CRUDService.getInstance().addTextEntity(textEntity);
            return true;
        }
        CRUDService.getInstance().updateTextEntity(textEntity);
        return true;
    }

    public List<Pair> getPairs() {
        return pairs;
    }

    public boolean canTranslate() {
        return canTranslate;
    }

    public void setPairs(List<Pair> pairs) {
        this.pairs = pairs;
        makePair();
    }
}
