package com.example.rinaldy.restauranthygienechecker;

import android.content.Context;
import android.media.audiofx.AudioEffect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.PrettyGirlViewHolder> {


    private List<Establishment> prettyGirlList;
    private Context context;

    private OnEstablishmentClickListener listener;

    public ItemAdapter(Context context, List<Establishment> prettyGirlList) {
        this.prettyGirlList = prettyGirlList;
        this.context = context;
    }


    public void setListener(OnEstablishmentClickListener listener) {
        this.listener = listener;
    }

    @Override
    public PrettyGirlViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_view_row, parent, false);
        PrettyGirlViewHolder viewHolder = new PrettyGirlViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PrettyGirlViewHolder holder, int position) {
        Establishment girl = prettyGirlList.get(position);
        holder.bindViewHolder(girl);

    }

    public void clear() {
        if (this.prettyGirlList != null) {
            this.prettyGirlList.clear();
            notifyDataSetChanged();
        }
    }

    public void addGirlList(List<Establishment> girlList) {
        if (this.prettyGirlList != null) {
            this.prettyGirlList.addAll(girlList);
        } else {
            this.prettyGirlList = girlList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return prettyGirlList.size();
    }



    public class PrettyGirlViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTitleTextView;
        TextView addressTextView;

        private Establishment girl;

        public PrettyGirlViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nameTitleTextView = itemView.findViewById(R.id.name);
            addressTextView = itemView.findViewById(R.id.address);
        }

        public void bindViewHolder(Establishment girl) {
            this.girl = girl;
            nameTitleTextView.setText(girl.getBusinessName());
        }

        public void onImageClicked() {
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onEstablishmentClicked(girl);
            }
        }
    }

    public interface OnEstablishmentClickListener {
        void onEstablishmentClicked(Establishment e);
    }
}
