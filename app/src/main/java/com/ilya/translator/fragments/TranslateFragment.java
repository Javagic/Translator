package com.ilya.translator.fragments;

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

import com.ilya.translator.BR;
import com.ilya.translator.databinding.FTranslateBinding;
import com.ilya.translator.models.LanguageType;
import com.ilya.translator.models.TextEntity;
import com.ilya.translator.models.pojo.DictionaryModel;
import com.ilya.translator.R;
import com.ilya.translator.utils.CRUDService;
import com.ilya.translator.utils.LanguageDialog;
import com.ilya.translator.utils.adapter.RecyclerBindingAdapter;
import com.ilya.translator.service.TranslatorService;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.ilya.translator.utils.LanguageDialog.FROM;
import static com.ilya.translator.utils.LanguageDialog.TO;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 20:03.
 */
public class TranslateFragment extends Fragment implements TextWatcher {
    TranslatorService translatorService;
    RecyclerView recyclerView;
    RecyclerBindingAdapter<DictionaryModel.DefModel.TrModel> meaningAdapter;
    FTranslateBinding binding;
    List<DictionaryModel.DefModel.TrModel> trModelList;
    CRUDService crudService;
    TextEntity textEntity;
    List<LanguageType> languageList;

    public static TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translatorService = TranslatorService.getInstance();
        crudService = CRUDService.getInstance(getActivity());
        if(textEntity == null){
            textEntity = translatorService.textEntity;
        }
    }


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args.containsKey("textEntity")) {
            textEntity = args.getParcelable("textEntity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_translate, container, false);
        View rootView = binding.getRoot();
        binding.setTranslationTextEntity(textEntity);
        recyclerView = binding.meaningRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        meaningAdapter = new RecyclerBindingAdapter<>(R.layout.item_meaning, BR.trModel, trModelList);
        recyclerView.setAdapter(meaningAdapter);
        binding.textArea.setText(textEntity.inputText);
        binding.addFavorite.setOnClickListener(view1 -> {
            textEntity.isMarked = !textEntity.isMarked;
            crudService.updateTextEntity(textEntity);
            binding.setTranslationTextEntity(textEntity);
        });
        binding.textArea.addTextChangedListener(this);
        binding.clear.setOnClickListener(view -> {
            textEntity = translatorService.clearEntity();
            binding.setTranslationTextEntity(textEntity);
            meaningAdapter.removeList();
            binding.textArea.setText("");
        });
        languageList = translatorService.getLanguageTypes();
        initToolbar();
        return rootView;
    }

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
                translatorService.translate(query).subscribe(languageTranslation -> {//вынести куда нибудь отсюда
                    binding.setTranslationTextEntity(textEntity);
                }, throwable -> {
                });

                translatorService.lookup(query).subscribe(defModel -> {
                    for (int k = 0; k < defModel.tr.size(); k++) {
                        defModel.tr.get(k).number = String.valueOf(k + 1);
                    }
                    textEntity.pos = defModel.pos;
                    meaningAdapter.setList(defModel.tr);
                    binding.setTranslationTextEntity(textEntity);
                }, throwable -> {
                    meaningAdapter.removeList();
                    textEntity.pos = "";
                    binding.setTranslationTextEntity(textEntity);
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void initToolbar() {
        binding.swapLanguage.setOnClickListener(view -> {
            translatorService.swapLanguages();
            binding.setTranslationTextEntity(textEntity);
        });

        binding.lang1.setOnClickListener(view -> {
            (new LanguageDialog(getContext()))
                    .setLanguageList(languageList)
                    .setTitle(FROM)
                    .setLanguageClickListener((position, item) -> {
                        binding.lang1.setText(item.longName);
                        translatorService.setCurrentInput(item);
                    }).show();
        });

        binding.lang2.setOnClickListener(view -> {
            (new LanguageDialog(getContext()))
                    .setLanguageList(languageList)
                    .setTitle(TO)
                    .setLanguageClickListener((position, item) -> {
                        binding.lang2.setText(item.longName);
                        translatorService.setCurrentOutput(item);
                    }).show();
        });
    }
}
