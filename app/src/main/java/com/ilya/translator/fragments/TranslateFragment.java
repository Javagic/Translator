package com.ilya.translator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilya.translator.R;
import com.ilya.translator.TextEntity;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 20:03.
 */
public class TranslateFragment extends Fragment {

  TextEntity textEntity;
FTransla
  //private FTranslateFragment binding;

  public static TranslateFragment newInstance() {
    TranslateFragment fragment = new TranslateFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    //binding = DataBindingUtil.inflate(inflater, R.layout.f_slide_page, container, false);
    //View view = binding.getRoot();
    return view;
  }
}
