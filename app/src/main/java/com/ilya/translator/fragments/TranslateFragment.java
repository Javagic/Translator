package com.ilya.translator.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilya.translator.HttpService;
import com.ilya.translator.Models.LanguageTranslation;
import com.ilya.translator.R;
import com.ilya.translator.RxBackgroundWrapper;
import com.ilya.translator.TranslatorManager;
import com.ilya.translator.databinding.FragmentTranslateBinding;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import rx.functions.Action1;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 20:03.
 */
public class TranslateFragment extends Fragment {
    TranslatorManager translatorManager;

    FragmentTranslateBinding binding;




    Date date;

    //private FTranslateFragment binding;

    public static TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translatorManager = TranslatorManager.getInstance();
    }


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        //textEntity = args.getParcelable("textEntity");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_translate, container, false);

        View view = binding.getRoot();

        date = new Date();
        binding.textArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((new Date()).getTime() > date.getTime() + 200) {
                    date = new Date();
                    try {
                        translatorManager.translate(charSequence).subscribe(languageTranslation -> {
                            binding.result.setText(languageTranslation.text.get(0));

                        }, throwable -> {

                        });
                        RxBackgroundWrapper.doInBackground(HttpService.getInstance().lookup("Hello","en-en")).subscribe(languageTranslation1 -> {

                        });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
