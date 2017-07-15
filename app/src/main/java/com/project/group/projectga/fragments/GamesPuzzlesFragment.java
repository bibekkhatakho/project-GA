package com.project.group.projectga.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.group.projectga.R;

public class GamesPuzzlesFragment extends Fragment {
    Toolbar toolbar;
    public GamesPuzzlesFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games_puzzles, container, false);

        // set the background and recolor the menu icon for the toolbar
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackground(getResources().getDrawable(R.drawable.tile_purple));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_purple_24dp));
        // change the icon on the toolbar
        ImageView icon = (ImageView) getActivity().findViewById(R.id.toolbarIcon);
        icon.setImageResource(R.drawable.ic_casino_black_24dp);
        icon.setColorFilter(getResources().getColor(R.color.GamesPuzzles));
        // change the title on the toolbar
        TextView title = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        title.setText(R.string.gamesLabel);

        // enable the option menu
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // add blank icon to the toolbar for integrity of constraints
        menu.add(null).setIcon(R.drawable.ic_android_trans_24dp).setShowAsActionFlags(1);

        super.onCreateOptionsMenu(menu, inflater);
    }

}