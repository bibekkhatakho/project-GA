package com.project.group.projectga.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.group.projectga.R;
import com.project.group.projectga.activities.ImportantPeoplesActivity;
import com.project.group.projectga.models.ImportantPeople;

import java.util.ArrayList;


/**
 * Created by ramjiseetharaman on 7/13/17.
 */

public class ImportantPeopleAdapter extends RecyclerView.Adapter<ImportantPeopleAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ImportantPeople> mImportantPeoplesList;
    Voice play;

    public ImportantPeopleAdapter(Context context, ArrayList<ImportantPeople> importantPeoplesList) {
        mContext = context;
        mImportantPeoplesList = importantPeoplesList;
        play = new Voice(mContext);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_important_people, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ImportantPeople importantPeople = mImportantPeoplesList.get(position);

        holder.personName.setText(importantPeople.getName());
        holder.personRelation.setText(importantPeople.getRelation());
        holder.shortDescription.setText(importantPeople.getShortDescription());
        holder.longDescription.setText(importantPeople.getLongDescription());
        holder.importantPeoplesKey.setText(importantPeople.getKey());
        holder.play.setOnClickListener(new View.OnClickListener() {
            String myString  = importantPeople.getShortDescription().replaceAll("\\.","") + importantPeople.getLongDescription().replaceAll("\\.","               ");

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,myString,Toast.LENGTH_SHORT).show();
                play.say(myString);
            }
        });
        Glide.with(mContext).load(importantPeople.getProfile()).into(holder.profilePicture);


    }

    @Override
    public int getItemCount() {
        if (mImportantPeoplesList == null) {
            return 0;
        } else {
            return mImportantPeoplesList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView personName, personRelation, shortDescription, longDescription, importantPeoplesKey;
        CircularImageView profilePicture;
        ImageView play;


        ViewHolder(View v) {
            super(v);
            v.setOnCreateContextMenuListener(this);
            profilePicture = (CircularImageView) v.findViewById(R.id.profilePicture);
            personName = (TextView) v.findViewById(R.id.personName);
            personRelation = (TextView) v.findViewById(R.id.personRelation);
            shortDescription = (TextView) v.findViewById(R.id.shortDescription);
            longDescription = (TextView) v.findViewById(R.id.longDescription);
            importantPeoplesKey = (TextView) v.findViewById(R.id.people_key);
            play = (ImageView)v.findViewById(R.id.playInfoView);

        }

        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Important Peoples Options");
            MenuItem edit = menu.add(0, v.getId(), 0, "Edit");//groupId, itemId, order, title
            MenuItem delete = menu.add(0, v.getId(), 0, "Delete");
            Log.d("check", "view " + v);
            edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(mContext, ImportantPeoplesActivity.class);
                    intent.putExtra("Key", importantPeoplesKey.getText().toString());
                    mContext.startActivity(intent);
                    return false;
                }
            });
            delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    deletePersonFromListByKey(importantPeoplesKey.getText().toString());
                    Toast.makeText(mContext, "Person from list Deleted", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });


        }

        private void deletePersonFromListByKey(String key) {
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Important Peoples").child(key);
            databaseReference.removeValue();
            mImportantPeoplesList.remove(getAdapterPosition());
            notifyDataSetChanged();

        }

    }

}