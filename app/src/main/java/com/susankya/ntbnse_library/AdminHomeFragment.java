package com.susankya.ntbnse_library;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.susankya.model.OfflineTransactionDetail;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;


public class AdminHomeFragment extends Fragment  implements View.OnClickListener{
      private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<OfflineTransactionDetail> offlineTransactionDetails;

    private String mParam1;
    private String mParam2;
    private OfflineTransactionAdapter adapter;
    @BindView(R.id.rvOffline)RecyclerView rvOffline;
    @BindView(R.id.pendingTran)TextView pendingTran;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

       public static AdminHomeFragment newInstance(String param1, String param2) {
        AdminHomeFragment fragment = new AdminHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setOfflineAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        offlineTransactionDetails=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_admin_home, container, false);
        ButterKnife.bind(this,v);
        setOfflineAdapter();
        initUI(v);
       return v;

    }

private void setOfflineAdapter(){

    try {
        Log.d(TAG, "onCreateView: "+LocalStorage.getText());
        JSONArray ja=new JSONArray(LocalStorage.getText());
        Type listType = new TypeToken<List<OfflineTransactionDetail>>() {}.getType();
        offlineTransactionDetails = new Gson().fromJson(LocalStorage.getText(), listType);
        Log.d(TAG, "onCreateView: "+offlineTransactionDetails.size());
        if(offlineTransactionDetails.size()==0)
            pendingTran.setVisibility(View.GONE);
        else
            pendingTran.setVisibility(View.VISIBLE);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    adapter=new OfflineTransactionAdapter(offlineTransactionDetails,getActivity());
    LinearLayoutManager llm=new LinearLayoutManager(getActivity());

    rvOffline.setLayoutManager(llm);
    rvOffline.setItemAnimator(new DefaultItemAnimator());
    rvOffline.setAdapter(adapter);
}
    private void initUI(View v) {
        final View btnHorizontalNtb = v.findViewById(R.id.btn_issue_book);
        btnHorizontalNtb.setOnClickListener(this);
        final View btnHorizontalCoordinatorNtb = v.findViewById(R.id.btn_return_book);
        btnHorizontalCoordinatorNtb.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        ViewCompat.animate(v)
                .setDuration(200)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setInterpolator(new CycleInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {

                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        switch (v.getId()) {
                            case R.id.btn_issue_book:
                               startActivity(
                                        new Intent(getActivity(), IssueActivity.class)
                                );
                                break;
                            case R.id.btn_return_book:
                                startActivity(
                                        new Intent(getActivity(), ReturnActivity.class)
                                );
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onAnimationCancel(final View view) {

                    }
                })
                .withLayer()
                .start();
    }

    private class CycleInterpolator implements android.view.animation.Interpolator {

        private final float mCycles = 0.5f;

        @Override
        public float getInterpolation(final float input) {
            return (float) Math.sin(2.0f * mCycles * Math.PI * input);
        }
    }
}
