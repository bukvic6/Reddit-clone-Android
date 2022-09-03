package com.example.redditadroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditadroid.model.Community;
import com.example.redditadroid.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCommunityAdapter extends RecyclerView.Adapter<MyCommunityAdapter.MyCommunityViewHolder> {
    Context context;
    ArrayList<Community> list;

    public MyCommunityAdapter(Context context,ArrayList<Community> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyCommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_community,parent,false);
        return new MyCommunityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommunityViewHolder holder, int position) {
        String name = list.get(position).getName();
        String description = list.get(position).getDescription();
        String cId = list.get(position).getId();
        holder.name.setText(name);
        holder.description.setText(description);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("communityId").equalTo(cId);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Query query2 = FirebaseDatabase.getInstance().getReference("Communities").orderByChild("id").equalTo(cId);
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds: snapshot.getChildren()){
                                    ds.getRef().removeValue();
                                }
                                Toast.makeText(context,"Community deleted successfully",Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateCommunity.class);
                intent.putExtra("communityId", cId);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    class MyCommunityViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        Button deleteBtn, updateBtn;

        public MyCommunityViewHolder(@NonNull View postView) {
            super(postView);
            name = postView.findViewById(R.id.myCommunityName);
            description = postView.findViewById(R.id.myCommunityDescription);
            deleteBtn = postView.findViewById(R.id.deleteCommunity);
            updateBtn = postView.findViewById(R.id.updateCommunity);
        }
    }
}
