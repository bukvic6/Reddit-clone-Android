package com.example.redditadroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditadroid.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyPostViewHolder> {
    Context context;
    String myId;
    ArrayList<Post> list;


    public MyPostAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
        myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_post,parent,false);
        return new MyPostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {
        String title = list.get(position).getTitle();
        String text = list.get(position).getText();
        String reaction = list.get(position).getReaction();
        String pID = list.get(position).getId();
        holder.text.setText(text);
        holder.title.setText(title);
        holder.reaction.setText(reaction);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("id").equalTo(pID);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(context,"Deleted successfully",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyPostViewHolder extends RecyclerView.ViewHolder{
        TextView title,text, reaction;
        Button likeBtn,deleteBtn;

        public MyPostViewHolder(@NonNull View postView){
            super(postView);
            title = postView.findViewById(R.id.postTitle);
            text = postView.findViewById(R.id.postText);
            reaction = postView.findViewById(R.id.reaction);
            likeBtn = postView.findViewById(R.id.btnLike);
            deleteBtn = postView.findViewById(R.id.deletePost);
        }
    }

}
