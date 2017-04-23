package com.ilya.translator.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ilya.translator.BR;
import com.ilya.translator.R;
import com.ilya.translator.databinding.FTranslateBinding;
import com.ilya.translator.models.LanguageType;
import com.ilya.translator.models.TextEntity;
import com.ilya.translator.models.pojo.DictionaryModel;
import com.ilya.translator.service.translation.TranslatorService;
import com.ilya.translator.utils.LanguageDialog;
import com.ilya.translator.utils.adapter.RecyclerBindingAdapter;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Locale;

import static com.ilya.translator.utils.LanguageDialog.FROM;
import static com.ilya.translator.utils.LanguageDialog.TO;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 20:03.
 */
public class TranslateFragment extends Fragment implements TextWatcher {

    public static long DELAY_TIME = 600;
    TranslatorService translatorService;
    RecyclerView recyclerView;
    RecyclerBindingAdapter<DictionaryModel.DefModel.TrModel> meaningAdapter;
    List<DictionaryModel.DefModel.TrModel> trModelList;
    TextEntity textEntity;
    List<LanguageType> languageList;
    TextToSpeech inputTextToSpeech;
    TextToSpeech outputTextToSpeech;

    private Handler handler = new Handler();
    private Runnable runnable;

    private FTranslateBinding binding;

    public static TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translatorService = TranslatorService.getInstance();
        if (textEntity == null) {
            textEntity = translatorService.textEntity;
        }
        inputTextToSpeech = new TextToSpeech(getActivity(), status -> {
            if (status != TextToSpeech.ERROR) {
                inputTextToSpeech.setLanguage(Locale.FRENCH);
            }
        });
        outputTextToSpeech = new TextToSpeech(getActivity(), i -> {
            inputTextToSpeech.setLanguage(Locale.ITALIAN);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_translate, container, false);
        View rootView = binding.getRoot();
        init();
        initToolbar();
        return rootView;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().isEmpty()){
            handler.removeCallbacks(runnable);
            clearTextEntity();
            return;
        }
        String query = charSequence.toString().trim();
        handler.removeCallbacks(runnable);
        runnable = () -> {
            translate(query);
        };
        handler.postDelayed(runnable, DELAY_TIME);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void init() {
        binding.setTranslationTextEntity(textEntity);
        recyclerView = binding.meaningRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        meaningAdapter = new RecyclerBindingAdapter<>(R.layout.item_meaning, BR.trModel, trModelList);
        recyclerView.setAdapter(meaningAdapter);
        binding.textArea.addTextChangedListener(this);
        if(textEntity.inputText!=null && !"".equals(textEntity.inputText)){
            binding.textArea.setText(textEntity.inputText);
        }
        binding.addFavorite.setOnClickListener(view1 -> {
            if(translatorService.changeMark(!textEntity.isMarked)){
                textEntity = translatorService.textEntity;
            }
            else{
                Toast.makeText(getActivity(), "Уже в избранном", Toast.LENGTH_SHORT).show();
            }
            binding.setTranslationTextEntity(textEntity);
        });
        binding.clear.setOnClickListener(view -> {
            clearTextEntity();
        });
        binding.soundInput.setOnClickListener(view -> inputTextToSpeech.speak(textEntity.inputText, TextToSpeech.QUEUE_FLUSH, null));
        binding.soundOutput.setOnClickListener(view -> outputTextToSpeech.speak(textEntity.outputText, TextToSpeech.QUEUE_FLUSH, null));
        languageList = translatorService.getLanguageTypes();
    }

    private void initToolbar() {
        binding.swapLanguage.setOnClickListener(view -> {
            textEntity = translatorService.swapLanguages();
            binding.textArea.setText(textEntity.inputText);
            binding.setTranslationTextEntity(textEntity);
            if (textEntity.inputText != null)
                binding.textArea.setSelection(textEntity.inputText.length());
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
                        if (textEntity.inputText != null) {
                            translate(textEntity.inputText);
                        }
                    }).show();
        });
    }

    private void translate(String query) {
        if (!translatorService.canTranslate()) {
            Toast.makeText(getActivity(), getString(R.string.e_translation), Toast.LENGTH_SHORT).show();
            return;
        }
        translatorService.translate(query).subscribe(defModel -> {
            for (int k = 0; k < defModel.tr.size(); k++) {
                defModel.tr.get(k).number = String.valueOf(k + 1);
            }
            meaningAdapter.setList(defModel.tr);
            binding.setTranslationTextEntity(textEntity);
        }, throwable -> {
            if (throwable instanceof UnknownHostException) {
                Toast.makeText(getActivity(), getString(R.string.e_internet), Toast.LENGTH_SHORT).show();
            }
            meaningAdapter.removeList();
            binding.setTranslationTextEntity(textEntity);
        });
    }
    private void clearTextEntity(){
        textEntity = translatorService.clearEntity();
        binding.setTranslationTextEntity(textEntity);
        meaningAdapter.removeList();
        binding.textArea.removeTextChangedListener(this);
        binding.textArea.setText("");
        binding.textArea.addTextChangedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        translatorService.changeMark(false);
    }
}

