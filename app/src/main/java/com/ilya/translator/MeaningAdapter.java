package com.ilya.translator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.ilya.translator.Models.DictionaryModel.DefModel.TrModel;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 09.04.17 15:54.
 */
public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.MeaningViewHolder> {

    private List<TrModel> meaningList;

    public MeaningAdapter(){
        meaningList = new ArrayList<>();
    }
    public MeaningAdapter(List<TrModel> list) {
        this.meaningList = list;
    }

    @Override
    public MeaningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meaning, parent, false);
        return new MeaningViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MeaningViewHolder holder, int position) {
        holder.tvMeaning.setVisibility(View.GONE);
        holder.bind(meaningList.get(position));
    }

    @Override
    public int getItemCount() {
        return meaningList.size();
    }

    public void setMeaningList(List<TrModel> meaningList){
        this.meaningList = meaningList;
        this.notifyDataSetChanged();
    }
    class MeaningViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        TextView tvTranslatedMeaning;
        TextView tvMeaning;

        MeaningViewHolder(View itemView) {
            super(itemView);
            tvNumber = (TextView) itemView.findViewById(R.id.number);
            tvMeaning = (TextView) itemView.findViewById(R.id.meaning);
            tvTranslatedMeaning = (TextView) itemView.findViewById(R.id.tr_meaning);
        }

        void bind(TrModel trModel) {
            tvNumber.setText(String.valueOf(getAdapterPosition() + 1));
            String translatedMeaning = trModel.text+ ", " + buildMeaning(trModel.similar);
                tvTranslatedMeaning.setText(translatedMeaning);
            if (trModel.mean != null) {
                tvMeaning.setVisibility(View.VISIBLE);
                tvMeaning.setText("(" + buildMeaning(trModel.mean) + ")");
            }
        }


        private String buildMeaning(ArrayList<TrModel> list) {
            if (list == null) return "";
            StringBuilder stringBuilder = new StringBuilder();
            for (TrModel trModel : list) {
                stringBuilder.append(trModel.text).append(", ");
            }
            return stringBuilder.toString().substring(0, stringBuilder.length() - 2);
        }
    }

}
