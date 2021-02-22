package com.example.sport_e;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentView> {

    List<String> jNames;
    Context context;

    public CommentAdapter(List<String> jNames, Context context) {
        this.jNames = jNames;
        this.context = context;
    }


    @NonNull
    @Override
    public CommentAdapter.CommentView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        return new CommentView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentView holder, int position) {
        try
        {
            String fullName = jNames.get(position);
            String[] arrOfStr = fullName.split("____", 2);
            holder.name.setText(arrOfStr[0]);
            holder.comment.setText(arrOfStr[1]);
        }
        catch (Exception e)
        {
            Log.e("CHECK", "View already exists");
        }

    }

    @Override
    public int getItemCount() {
        return jNames.size();
    }

    public class CommentView extends RecyclerView.ViewHolder{

        TextView name;
        TextView comment;

        public CommentView(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.CommentName);
            comment = itemView.findViewById(R.id.CommentText);
        }
    }
}
