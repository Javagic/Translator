package com.ilya.translator.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilya.translator.BR;
import com.ilya.translator.models.TextEntity;
import com.ilya.translator.service.http.HttpService;
import com.ilya.translator.models.pojo.DictionaryModel;
import com.ilya.translator.models.Pair;
import com.ilya.translator.R;
import com.ilya.translator.utils.CRUDService;
import com.ilya.translator.utils.RecyclerBindingAdapter;
import com.ilya.translator.utils.RxBackgroundWrapper;
import com.ilya.translator.service.TranslatorService;
import com.ilya.translator.databinding.FragmentTranslateBinding;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 20:03.
 */
public class TranslateFragment extends Fragment {
    TranslatorService translatorService;
    RecyclerView recyclerView;
    RecyclerBindingAdapter<DictionaryModel.DefModel.TrModel> meaningAdapter;
    FragmentTranslateBinding binding;
    List<DictionaryModel.DefModel.TrModel> trModelList;
    CRUDService crudService;
    TextEntity textEntity;

    public static TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        textEntity = new TextEntity();
        super.onCreate(savedInstanceState);
        translatorService = TranslatorService.getInstance();
        crudService = CRUDService.getInstance(getActivity());
    }


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_translate, container, false);
        View view = binding.getRoot();

        recyclerView = binding.meaningRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        meaningAdapter  = new RecyclerBindingAdapter<>(R.layout.item_meaning, BR.trModel, trModelList);
        recyclerView.setAdapter(meaningAdapter);

        binding.textArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) return;
                String lastChar = charSequence.toString().substring(charSequence.length() - 1);
                String query = charSequence.toString().trim();
                if (" ,.;".contains(lastChar)) {
                    try {
                        translatorService.translate(query).subscribe(languageTranslation -> {
                            TextEntity textEntity = new TextEntity();
                            textEntity.outputText = languageTranslation.text.get(0);
                            textEntity.inputText = query;
                            textEntity.inputLanguage = translatorService.getCurrentInput().shortName;
                            textEntity.outputLanguage = translatorService.getCurrentOutput().shortName;
                            binding.result.setText(languageTranslation.text.get(0));
                            binding.defWord.setText(query);
                            crudService.addTextEntity(textEntity);
                        }, throwable -> {
                        });

                        String pair = Pair.pairFrom(translatorService.getCurrentInput(), translatorService
                            .getCurrentOutput());
                        RxBackgroundWrapper.doInBackground(HttpService.getInstance().lookup(query, pair)).subscribe(defModel -> {
                            for (int k = 0; k < defModel.tr.size(); k++) {
                                defModel.tr.get(k).number = String.valueOf(k+1);
                            }
                            binding.pos.setText(defModel.pos);
                            meaningAdapter.setList(defModel.tr);
                        }, throwable -> {
                            meaningAdapter.removeList();
                            binding.pos.setText("");
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
