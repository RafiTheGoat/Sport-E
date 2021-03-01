package com.example.sport_e;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingView> {

    Context context;
    List<String> jNames;

    public PendingAdapter(List<String> jNames, Context context) {
        this.jNames = jNames;
        this.context = context;
    }


    @NonNull
    @Override
    public PendingView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_row, parent, false);
        return new PendingView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingView holder, int position) {

        try
        {
            String fullName = jNames.get(position);
            final String[] arrOfStr = fullName.split("____", 3);

            holder.name.setText(arrOfStr[0] + " has sent you a Friend Request");

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptRequest(arrOfStr[1],arrOfStr[2]);

                }
            });

            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rejectRequest(arrOfStr[1],arrOfStr[2]);
                }
            });

        }
        catch (Exception e)
        {
            Log.e("CHECK","View already added");
        }


    }

    private void rejectRequest(String uid, String FriendUID) {

        FirebaseDatabase.getInstance().getReference().child("Pending").child(uid).child(FriendUID).removeValue();

        Toast.makeText(context,"Friend Request Rejected",Toast.LENGTH_SHORT).show();

    }

    private void acceptRequest(String uid, String FriendUID) {

        HashMap<String, String> hp = new HashMap<>();
        hp.put("Friend UID", FriendUID);

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Friends").child(FriendUID).setValue(hp);

        HashMap<String, String> hp1 = new HashMap<>();
        hp1.put("Friend UID", uid);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FriendUID).child("Friends").child(uid).setValue(hp1);

        Toast.makeText(context,"Friend Request Accepted",Toast.LENGTH_SHORT).show();

        FirebaseDatabase.getInstance().getReference().child("Pending").child(uid).child(FriendUID).removeValue();


    }

    @Override
    public int getItemCount() {
        return jNames.size();
    }

    public class PendingView extends RecyclerView.ViewHolder {

        TextView name;
        Button accept;
        Button reject;

        public PendingView(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.PendingName);
            accept = itemView.findViewById(R.id.AcceptButton);
            reject = itemView.findViewById(R.id.RejectButton);
        }
    }
}
