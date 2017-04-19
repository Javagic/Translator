package com.ilya.translator.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilya.translator.BR;
import com.ilya.translator.main.MainActivity;
import com.ilya.translator.R;
import com.ilya.translator.models.TextEntity;
import com.ilya.translator.utils.CRUDService;
import com.ilya.translator.utils.adapter.ItemDecorator;
import com.ilya.translator.utils.adapter.RecyclerBindingAdapter;
import com.ilya.translator.databinding.FHistoryBinding;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 10.04.17 19:50.
 */
public class HistoryFragment extends Fragment implements SearchView.OnQueryTextListener {
    FHistoryBinding binding;
    CRUDService crudService;
    RecyclerView recyclerView;
    RecyclerBindingAdapter<TextEntity> textEntitiesAdapter;
    SearchView searchView;

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
        textEntitiesAdapter.setOnLongItemClickListener((position, item) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setPositiveButton("Удалить перевод?", (dialogInterface, i) -> {
                        textEntitiesAdapter.notifyItemRemoved(position);
                        //доделать
                    }
            );
            builder.setOnCancelListener(dialogInterface -> {
            });
            builder.show();
        });
        searchView = binding.searchView;
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        textEntitiesAdapter.setList(CRUDService.getInstance(getContext()).searchHistory(newText.trim()));
        return false;
    }


}
