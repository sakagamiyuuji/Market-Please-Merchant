package com.e.marketpleasemerchant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.e.marketpleasemerchant.VolleyApp;
import com.e.marketpleasemerchant.model.AccessToken;
import com.e.marketpleasemerchant.utils.TokenManager;
import com.e.marketpleasemerchant.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Hashtable;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout floatUsername, floatPw;
    CardView cvLogin;

    ConnectivityManager connectivityManager;
    int succes;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    EditText edtEmail, edtPassword;
    String email, password;
    AccessToken accessToken;


    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialComponent();
        conectivityCheck();
        loginClick();
        signUpClick();


    }

    public void conectivityCheck(){
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);{
            if (connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected()){
                Toast.makeText(this, "Internet Detected!", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void loginClick(){
        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);{
                    if (connectivityManager.getActiveNetworkInfo() != null
                            && connectivityManager.getActiveNetworkInfo().isAvailable()
                            && connectivityManager.getActiveNetworkInfo().isConnected()){
                        if (validate() == true){
                            email = edtEmail.getText().toString();
                            password = edtPassword.getText().toString();
                            login();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    public void signUpClick(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean validate(){
        boolean isValid = true;
        if (edtEmail.getText().toString().isEmpty()){
            edtEmail.setError("Email tidak boleh kosong");
            isValid = false;
        }
        else if(edtPassword.getText().toString().isEmpty()){
            edtPassword.setError("Password tidak boleh kosong");
            isValid = false;
        }
        else{
            email = edtEmail.getText().toString();
            password = edtPassword.getText().toString();
            isValid = true;
        }


        return isValid;
    }



    /*String validasi (EditText edt){
        String text = null;
        if(edt.getText().toString().isEmpty()){
            edt.setError("Mohon isi email/password dengan bernar");
        }else{
            text = edt.getText().toString();
        }
        return text;
    }*/

    public void initialComponent(){
        signUp = findViewById(R.id.signup);
        cvLogin = findViewById(R.id.cv_login);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
    }

    private void setupFloatingLabelError(){
        floatUsername = findViewById(R.id.username_til);



    }

    public void login(){
        String url = " http://210.210.154.65:4444/api/auth/login";

        StringRequest loginReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        accessToken = new Gson().fromJson(response, AccessToken.class);
                        TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).saveToken(accessToken);


                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String statusCode = String.valueOf(error.networkResponse.statusCode);

                        Toast.makeText(LoginActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, "Email/Password yang anda masukan salah", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        VolleyApp.getInstance().addToRequestQueue(loginReq, "login_req");
    }
}