package com.susankya.ntbnse_library;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;


public class LibraryInchargeLoginFragment extends Fragment {
     private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.input_username)EditText username;
    @BindView(R.id.input_password)EditText password;
    @BindView(R.id.btn_login)Button loginButton;


    public LibraryInchargeLoginFragment() {
        // Required empty public constructor
    }


    public static LibraryInchargeLoginFragment newInstance(String param1, String param2) {
        LibraryInchargeLoginFragment fragment = new LibraryInchargeLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v= inflater.inflate(R.layout.fragment_library_incharge_login, container, false);
        ButterKnife.bind(this,v);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(username.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
                    Snackbar.make(v,"Please fill all the fields.", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    ApiClient.m.api.adminLogin(username.getText().toString().trim(),password.getText().toString().trim()).enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                            ServerResponse rs=response.body();
                            MySharedPreferences myPref=new MySharedPreferences(getActivity(),Constants.BASIC_SHAREDPREF);
                            Log.d(TAG, "onResponse: adminlogin"+response.body());
                            if (rs == null) {
                                Snackbar.make(view,"Wrong username or password",Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                            if(rs.isSuccessful()){
                                Log.d("TAG", "onResponse: "+ rs.getResult());

                                myPref.setAdminSignedIn(true);
                                if(rs.hasToken())
                                myPref.setToken(rs.getToken());
                                startActivity(new Intent(getActivity(),MainActivity.class));
                                getActivity().finish();
                            }else {
                                Snackbar.make(view,rs.getResult(),Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            Toast.makeText(getActivity(),"Cannot login with provided credentials",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        return v;
    }


}
