package com.alias.smartparty.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alias.smartparty.R;
import com.alias.smartparty.entity.Knowledge;

import java.util.List;

public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeAdapter.ViewHolder> {

    private List<Knowledge> knowledgeList;

    public KnowledgeAdapter(List<Knowledge> knowledgeList) {
        this.knowledgeList = knowledgeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_knowledge, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Knowledge knowledge = knowledgeList.get(position);
        holder.titleTextView.setText(knowledge.getTitle());
        holder.contentTextView.setText(knowledge.getContent());
//        Bitmap bitmap = BitmapFactory.decodeFile(knowledge.getPicture());
//        holder.pictureImageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return knowledgeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }
}