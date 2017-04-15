package com.ilya.translator.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilya.translator.BR;
import com.ilya.translator.MainActivity;
import com.ilya.translator.R;
import com.ilya.translator.models.TextEntity;
import com.ilya.translator.utils.CRUDService;
import com.ilya.translator.utils.ItemDecorator;
import com.ilya.translator.utils.RecyclerBindingAdapter;
import com.ilya.translator.databinding.FHistoryBinding;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 10.04.17 19:50.
 */
public class HistoryFragment extends Fragment {
    FHistoryBinding binding;
    CRUDService crudService;
    RecyclerView recyclerView;
    RecyclerBindingAdapter<TextEntity> textEntitiesAdapter;

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crudService = CRUDService.getInstance(getActivity());
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
        textEntitiesAdapter = new RecyclerBindingAdapter<>(R.layout.item_text_entity, BR.textEntity, crudService.getHistory());
        recyclerView.setAdapter(textEntitiesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Drawable divider = ContextCompat.getDrawable(getActivity(), R.drawable.item_divider_bottom);
        recyclerView.addItemDecoration(new ItemDecorator(divider));
        textEntitiesAdapter.setOnItemClickListener((position, item) -> {
            ((MainActivity) getActivity()).setTranslateFragment(item);
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
