package com.example.paw.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paw.myapplication.R;
import com.example.paw.myapplication.recycleview.MyRecycleViewAdapter;

public class RecFragment extends Fragment {


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.rec_fragment, container, false);
        RecyclerView recyclerView = myview.findViewById(R.id.recycler_fragment1);

        recyclerView.setAdapter(new MyRecycleViewAdapter());

        return myview;
    }
}