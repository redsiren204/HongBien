package com.goshu.hongbien;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pro.alexzaitsev.freepager.library.view.infinite.InfiniteVerticalPager;
import pro.alexzaitsev.freepager.library.view.infinite.ViewFactory;

public class InfiniteVerticalFragment extends Fragment implements ViewFactory {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        InfiniteVerticalPager verticalPaper = (InfiniteVerticalPager) inflater.inflate(R.layout.fragment_infinite_vertical, container, false);
        verticalPaper.setFactory(this);
        verticalPaper.instantiate();
        return verticalPaper;
    }

    @Override
    public View makeView(int vertical, int horizontal) {
        Button btn = new Button(getActivity());
        btn.setText("Vertical " + vertical);
        btn.setBackgroundColor(getResources().getColor(R.color.sample_bg));
        View view = new View(getContext());
        return btn;
    }
}
