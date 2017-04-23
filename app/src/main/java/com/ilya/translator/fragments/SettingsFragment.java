package com.ilya.translator.fragments;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ilya.translator.R;
import com.ilya.translator.databinding.FSettingsBinding;
import com.ilya.translator.service.translation.TranslatorManager;
import com.ilya.translator.utils.CRUDService;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 20:07.
 */

/**
 * Фрагмент для управления настройками приложения :
 * -Обновление списка доступных языков
 * -Очистка базы данных приложения
 */
public class SettingsFragment extends Fragment {
  FSettingsBinding binding;
  CRUDService crudService;
  TranslatorManager translatorManager;

  public static SettingsFragment newInstance() {
    SettingsFragment fragment = new SettingsFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.f_settings, container, false);
    View rootView = binding.getRoot();
    crudService = CRUDService.getInstance();
    translatorManager = TranslatorManager.getInstance();
    binding.clearHistory.setOnClickListener(view -> {
      crudService.removeAllHistory();
      Toast.makeText(getActivity(), R.string.history_cleared, Toast.LENGTH_SHORT).show();
    });

    binding.refresh.setOnClickListener(view -> {
      ProgressDialog loading = ProgressDialog.show(getActivity(), getString(R.string.loading), getString(R.string.please_wait), true);//set content
      translatorManager.loadLanguageVariations().subscribe(possibleLanguages -> {
        crudService.removeAllLanguages();
        crudService.removeAllPairs();
        crudService.addLanguageTypes(translatorManager.getLanguageTypes());
        crudService.addPairs(translatorManager.getPairs());
        loading.dismiss();
        Toast.makeText(getActivity(),  getString(R.string.data_refreshed), Toast.LENGTH_SHORT).show();
      }, throwable -> {
        loading.dismiss();
        Toast.makeText(getActivity(), getActivity().getString(R.string.e_internet), Toast.LENGTH_SHORT).show();
      });
    });


    return rootView;
  }

}
