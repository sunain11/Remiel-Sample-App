package com.sunain.sampleapp.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sunain.sampleapp.R;


public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView= inflater.inflate(R.layout.fragment_home, container, false);
        TextView tv=rootView.findViewById(R.id.tv_welcome);
        tv.setText("Welcome "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        return  rootView;
    }




}
