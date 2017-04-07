package com.ilya.translator;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 21:40.
 */
public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private LanguageAdapterCallback languageAdapterCallback;
    private ArrayList<LanguageType> languageList;

    public LanguageAdapter(ArrayList<LanguageType> list, LanguageAdapterCallback languageAdapterCallback) {
        this.languageAdapterCallback = languageAdapterCallback;
        this.languageList = list;
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
        return new LanguageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {
        holder.textView.setText(languageList.get(position).ui);
        holder.cardView.setOnClickListener(
                view -> languageAdapterCallback.onClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView cardView;

        LanguageViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.lang_name);
            cardView = (CardView) itemView.findViewById(R.id.lang_wrapper);
        }
    }

    public interface LanguageAdapterCallback {
        void onClick(int position);
    }
}
