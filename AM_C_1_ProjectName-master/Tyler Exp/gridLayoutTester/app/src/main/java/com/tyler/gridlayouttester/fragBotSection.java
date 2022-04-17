package com.tyler.gridlayouttester;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class fragBotSection extends Fragment{

    private static TextView topMemeText;
    private static TextView botMemeText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bottom_section, container, false);
        topMemeText = view.findViewById(R.id.topMemeText);
        botMemeText = view.findViewById(R.id.botMemeText);
        return view;
    }

    public void setMemeText(String top, String bot){
        topMemeText.setText(top);
        botMemeText.setText(bot);
    }
}