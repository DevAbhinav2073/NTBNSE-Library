package com.susankya.ntbnse_library;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserLoginFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    @BindView(R.id.input_roll)EditText rollNumber;
    @BindView(R.id.input_passcode)EditText passcode;
    @BindView(R.id.btn_user_login)Button loginButton;
    @BindView(R.id.link_admin_login)TextView adminLogin;
    private static final String TAG="TAG";


    public UserLoginFragment() {
        // Required empty public constructor
    }

    public static UserLoginFragment newInstance(String param1, String param2) {
        UserLoginFragment fragment = new UserLoginFragment();
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
       final View v= inflater.inflate(R.layout.fragment_initial_screen, container, false);
        ButterKnife.bind(this,v);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(rollNumber.getText().toString().isEmpty()||passcode.getText().toString().isEmpty()){
                    Snackbar.make(v,"Please fill all the fields.",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    ApiClient.m.api.userLogin(rollNumber.getText().toString().trim(),passcode.getText().toString().trim()).enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            ServerResponse rs=response.body();
                            Log.d(TAG, "onResponse: "+response.body().getResult());
                            MySharedPreferences myPref=new MySharedPreferences(getActivity(),Constants.BASIC_SHAREDPREF);

                            if(rs.isSuccessful()){
                                Snackbar.make(view,rs.getResult(),Snackbar.LENGTH_SHORT).show();
                                Log.d(TAG, "onResponse: "+ rs.getResult());
                                myPref.setUserSignedIn(true);
                                myPref.setRollNumber(rollNumber.getText().toString().trim());
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

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,new LibraryInchargeLoginFragment()).addToBackStack(null).commit();
            }
        });


        return v;
    }


}
