package com.example.redditadroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditadroid.model.Community;

import java.util.ArrayList;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    private final RecViewInterface recViewInterface;
    Context context;
    ArrayList<Community> list;

    public CommunityAdapter(Context context,ArrayList<Community> list,RecViewInterface recViewInterface){

        this.context = context;
        this.list = list;
        this.recViewInterface = recViewInterface;

    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.community,parent,false);
        return new CommunityViewHolder(v, recViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {

        Community community = list.get(position);
        holder.name.setText(community.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public CommunityViewHolder(@NonNull View itemView, RecViewInterface recViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.communityName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}
