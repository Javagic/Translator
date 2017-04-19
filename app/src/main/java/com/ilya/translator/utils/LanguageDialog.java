package com.ilya.translator.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Window;

import com.ilya.translator.BR;
import com.ilya.translator.R;
import com.ilya.translator.databinding.DialogLanguageLayoutBinding;
import com.ilya.translator.models.LanguageType;
import com.ilya.translator.utils.adapter.RecyclerBindingAdapter;

import java.util.List;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 16.04.17 16:58.
 */
public class LanguageDialog extends Dialog {
    public static String FROM = "from";
    public static String TO = "to";

    private DialogLanguageLayoutBinding binding;
    RecyclerBindingAdapter<LanguageType> languageAdapter;
    String title;

    RecyclerView languageRecycler;

    public LanguageDialog(Context context) {
        super(context);
        languageAdapter = new RecyclerBindingAdapter<>(R.layout.item_language, BR.languageName, null);
    }

    public LanguageDialog setLanguageList(List<LanguageType> languageList) {
        languageAdapter.setList(languageList);
        return this;
    }

    public LanguageDialog setLanguageClickListener(RecyclerBindingAdapter.OnItemClickListener<LanguageType> onItemClickListener) {
        languageAdapter.setOnItemClickListener((position, item) -> {
            onItemClickListener.onItemClick(position,item);
            dismiss();
        });
        return this;
    }

    public LanguageDialog setTitle(String text) {
        if(text.equals(FROM)){
            title = getContext().getString(R.string.translate_from);
        }
        else {
            title = getContext().getString(R.string.translate_to);
        }
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_language_layout, null, false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());
        binding.text.setText(title);
        languageRecycler = binding.languageRecycler;
        languageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        languageRecycler.setAdapter(languageAdapter);
    }
}
