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

public class leaderBoardadapter extends RecyclerView.Adapter<leaderBoardadapter.leaderBoardView>{


    Context context;
    List<String> jNames;

    public leaderBoardadapter(List<String> jNames, Context context) {
        this.jNames = jNames;
        this.context = context;
    }

    @NonNull
    @Override
    public leaderBoardView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_leaderboard, parent, false);
        return new leaderBoardView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull leaderBoardView holder, int position) {

        try
        {
            String fullName = jNames.get(position);
            final String[] arrOfStr = fullName.split("____", 2);

            holder.text.setText(arrOfStr[0] + " ---> " +arrOfStr[1]);
        }
        catch (Exception e)
        {
            Log.e("CHECK","View already added");
        }

    }

    @Override
    public int getItemCount() {
        return jNames.size();
    }

    public class leaderBoardView extends RecyclerView.ViewHolder {

        TextView text;

        public leaderBoardView(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.leaderboardName);
        }
    }

}
