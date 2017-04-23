package com.ilya.translator.utils.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 11.04.17 20:52.
 */

/**
 * универсальный адаптер для простых списков
 * @param <T> класс передаваемой в биндинг модели
 */
public class RecyclerBindingAdapter<T> extends RecyclerView.Adapter<RecyclerBindingAdapter.BindingHolder> {
    private int holderLayout, variableId;
    private List<T> items = new ArrayList<>();
    private OnItemClickListener<T> onItemClickListener;
    private OnLongItemClickListener<T> onLongItemClickListener;

    public RecyclerBindingAdapter(int holderLayout, int variableId, List<T> items) {
        this.holderLayout = holderLayout;
        this.variableId = variableId;
        this.items = items;
    }

    @Override
    public RecyclerBindingAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(holderLayout, parent, false);
        return new BindingHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerBindingAdapter.BindingHolder holder, int position) {
        final T item = items.get(position);
        holder.getBinding().getRoot().setOnClickListener(v -> {
            if (onItemClickListener != null) onItemClickListener.onItemClick(holder.getAdapterPosition(), item);
        });
        holder.getBinding().getRoot().setOnLongClickListener(v -> {
            if (onLongItemClickListener != null) onLongItemClickListener.onItemClick(holder.getAdapterPosition(), item);
            return false;
        });
        holder.getBinding().setVariable(variableId, item);
    }

    public void setList(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void removeList() {
        if (this.items != null) {
            this.items.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else return 0;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener<T> onLongItemClickListener) {
        this.onLongItemClickListener = onLongItemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T item);
    }

    public interface OnLongItemClickListener<T> {
        void onItemClick(int position, T item);
    }

    public void removeItem(int position){
        notifyItemRemoved(position);
        items.remove(position);
    }

    static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        BindingHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}