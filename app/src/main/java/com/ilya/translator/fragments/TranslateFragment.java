package com.ilya.translator.fragments;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilya.translator.HttpService;
import com.ilya.translator.LanguageAdapter;
import com.ilya.translator.LanguageType;
import com.ilya.translator.Models.LanguageTranslation;
import com.ilya.translator.R;
import com.ilya.translator.RxBackgroundWrapper;
import com.ilya.translator.TextEntity;
import com.ilya.translator.databinding.FragmentTranslateBinding;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;

import rx.Subscriber;

import static com.ilya.translator.Const.API_KEY;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 20:03.
 */
public class TranslateFragment extends Fragment {
    FragmentTranslateBinding binding;
    TextEntity textEntity;
    ArrayList<LanguageType> languages;
     Dialog dialog;
    Date date;
    //private FTranslateFragment binding;

    public static TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dialog = new Dialog(getActivity());
    }


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        //textEntity = args.getParcelable("textEntity");
        languages = args.getParcelableArrayList("languages");
        textEntity = new TextEntity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        date = new Date();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_translate, container, false);
        View view = binding.getRoot();
        //binding.setTextEntity(textEntity);
        LanguageAdapter.LanguageAdapterCallback listener = position -> {
            textEntity.currentLang = languages.get(position);
            binding.lang1.setText(languages.get(position).ui);
            dialog.dismiss();
        };
        binding.lang1.setOnClickListener(view1 -> {
            dialog.setContentView(R.layout.dialog_language_layout);
            RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.language_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new LanguageAdapter(languages, listener));
            dialog.show();
        });
        LanguageAdapter.LanguageAdapterCallback listener2 = position -> {
            textEntity.currentLang = languages.get(position);
            binding.lang2.setText(languages.get(position).ui);
            dialog.dismiss();
        };
        binding.lang2.setOnClickListener(view1 -> {
            dialog.setContentView(R.layout.dialog_language_layout);
            RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.language_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new LanguageAdapter(languages, listener2));
            dialog.show();
        });
        binding.textArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String b = "en-ru";
                if(binding.lang1.getText().equals("Russian")){
                    b = "ru-en";
                }
                if((new Date()).getTime()>date.getTime() + 200){
                    date = new Date();

                    RxBackgroundWrapper.doInBackground(
                        HttpService.getInstance().getHttpApi().translate(API_KEY,charSequence.toString().trim(),b,"plain","1")).subscribe(
                        new Subscriber<LanguageTranslation>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(LanguageTranslation o) {
                                binding.result.setText(o.text.get(0));
                            }
                        });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }


}
