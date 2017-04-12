package com.ilya.translator.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilya.translator.R;
import com.ilya.translator.utils.RecyclerBindingAdapter;
import com.ilya.translator.models.TextEntity;
import com.ilya.translator.databinding.FHistoryBinding;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 10.04.17 19:50.
 */
public class HistoryFragment extends Fragment {
FHistoryBinding binding;
  RecyclerView recyclerView;
  RecyclerBindingAdapter textEntitiesAdapter;
  public static HistoryFragment newInstance() {
    HistoryFragment fragment = new HistoryFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
    binding = DataBindingUtil.inflate(inflater, R.layout.f_history, container, false);
    View view = binding.getRoot();

    recyclerView = binding.historyRecycler;
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setAdapter(textEntitiesAdapter);
    return view;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }


}
