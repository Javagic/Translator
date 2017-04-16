package com.ilya.translator.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilya.translator.R;
import com.ilya.translator.models.LanguageType;
import com.ilya.translator.models.Pair;
import com.ilya.translator.models.TextEntity;
import com.ilya.translator.models.pojo.DictionaryModel;
import com.ilya.translator.service.TranslatorService;

import java.util.ArrayList;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 12.04.17 19:07.
 */
public class BindingUtils {

    @BindingAdapter("translatedMeaning")
    public static void translatedMeaning(TextView textView,
                                         DictionaryModel.DefModel.TrModel trModel) {
        StringBuilder stringBuilder = new StringBuilder(trModel.text);
        if (trModel.similar != null) {
            stringBuilder.append(", ");
            stringBuilder.append(buildMeaning(trModel.similar));
        }
        textView.setText(stringBuilder.toString());
    }

    @BindingAdapter("meaning")
    public static void meaning(TextView textView,
                               DictionaryModel.DefModel.TrModel trModel) {
        if (trModel.mean != null) {
            textView.setText("(" + buildMeaning(trModel.mean) + ")");
        } else textView.setVisibility(View.GONE);
    }

    private static String buildMeaning(ArrayList<DictionaryModel.DefModel.TrModel> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (DictionaryModel.DefModel.TrModel trModel : list) {
            stringBuilder.append(trModel.text).append(", ");
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - 2);
    }

    @BindingAdapter("longLanguage")
    public static void longLanguage(TextView textView, String language) {
        for (LanguageType languageType : TranslatorService.getInstance().getLanguageTypes()) {
            if (language.equals(languageType.shortName)) {
                textView.setText(languageType.longName);
            }
        }
    }
}


