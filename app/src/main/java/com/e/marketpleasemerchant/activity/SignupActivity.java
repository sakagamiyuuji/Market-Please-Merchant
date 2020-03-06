package com.e.marketpleasemerchant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.marketpleasemerchant.R;
import com.e.marketpleasemerchant.VolleyApp;
import com.e.marketpleasemerchant.error.RegisterErrorResponse;
import com.e.marketpleasemerchant.model.AccessToken;
import com.e.marketpleasemerchant.utils.TokenManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {
    @BindView(R.id.edt_firstname) EditText edtFirstName;
    @BindView(R.id.edt_lastname) EditText edtLastName;
    @BindView(R.id.edt_email) EditText edtEmail;
    @BindView(R.id.edt_password) EditText edtPassword;
    @BindView(R.id.edt_confirmpassword) EditText edtConfirmPassword;
    @BindView(R.id.edt_merchantname) EditText edtMerchantName;

    @BindView(R.id.checkbox_as_merchant) CheckBox checkBoxMerchat;
    @BindView(R.id.signup)
    CardView signUp;
    @BindView(R.id.linear_merchant) LinearLayout linearMerchant;

    final String FIRST_NAME = "first_name";
    final String LAST_NAME = "last_name";
    final String EMAIL = "email";
    final String PASSWORD = "password";
    final String CPASSWORD = "confirm_password";
    final String IS_MERCHANT = "is_merchant";
    final String MERCHANT_NAME = "merchant_name";

    String firstName, lastName, email, password, confirmPassword, merchantName;
    int isMerchant = 0; // set 1 for true, set 1 in merchant app, and 0 in customer app

    AccessToken accesToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

    }

    @OnCheckedChanged(R.id.checkbox_as_merchant)
    public void checkAsMerchant(){
        if (checkBoxMerchat.isChecked()){
            linearMerchant.setVisibility(View.VISIBLE);
            isMerchant = 1;
            Toast.makeText(this, String.valueOf(isMerchant), Toast.LENGTH_SHORT).show();
        }
        else{
            linearMerchant.setVisibility(View.INVISIBLE);
            isMerchant = 0;
            Toast.makeText(this, String.valueOf(isMerchant), Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.signup)
    public void register(){
        if (isValidInput()==true){
            postDataRegister();
            Toast.makeText(this, "Mohon Tunggu Sebentar", Toast.LENGTH_SHORT).show();
        }
    }

    private void postDataRegister(){
        firstName = edtFirstName.getText().toString();
        lastName =  edtLastName.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        confirmPassword = edtConfirmPassword.getText().toString();
        merchantName = edtMerchantName.getText().toString();

        String url = "http://210.210.154.65:4444/api/auth/signup";
        StringRequest requestReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        accesToken = new Gson().fromJson(response, AccessToken.class);

                        TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).saveToken(accesToken);
                        Toast.makeText(getApplicationContext(), "Berhasil di daftarkan", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        String body = "";
                        Toast.makeText(SignupActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject res = new JSONObject(body);
                            RegisterErrorResponse errorResponse = new Gson().fromJson(res.getJSONObject("error").toString(), RegisterErrorResponse.class);
                            if(errorResponse.getEmailError().size() > 0){
                                if(errorResponse.getEmailError().get(0) != null){
                                    edtEmail.setError(errorResponse.getEmailError().get(0));
                                }
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new Hashtable<>();

                headers.put("Accept","application/json");
                headers.put("Content-Type","application/x-www-form-urlencoded");

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();

                params.put(FIRST_NAME,firstName);
                params.put(LAST_NAME,lastName);
                params.put(EMAIL,email);
                params.put(PASSWORD,password);
                params.put(CPASSWORD,confirmPassword);
                params.put(IS_MERCHANT,String.valueOf(isMerchant));
                params.put(MERCHANT_NAME,merchantName);
                if (isMerchant == 1){
                    params.put(IS_MERCHANT, String.valueOf(isMerchant));
                    params.put(MERCHANT_NAME, merchantName);
                }

                return params;
            }

        };

        VolleyApp.getInstance().addToRequestQueue(requestReq, "register_req");
    }

    private boolean isValidInput(){

        boolean isValid = true;

        if (edtFirstName.getText().toString().isEmpty()){
            edtFirstName.setError("First name cannot be empty");
            isValid = false;
        }

        if (edtLastName.getText().toString().isEmpty()){
            edtLastName.setError("Last name cannot be empty");
            isValid = false;
        }

        if (edtEmail.getText().toString().isEmpty()){
            edtEmail.setError("Email name cannot be empty");
            isValid = false;
        }else if(!edtEmail.getText().toString().contains("@")){
            edtEmail.setError("Must be a valid email");
        }

        if (edtPassword.getText().toString().isEmpty()){
            edtPassword.setError("Password cannot be empty");
            isValid = false;
        } else if (edtPassword.getText().toString().length() < 8) {
            edtPassword.setError("Password must be 8 or more character");
        }

        if(edtConfirmPassword.getText().toString().isEmpty()){
            edtConfirmPassword.setError("Confirm password cannot be empty");
            isValid = false;
        }
        else if(!(edtConfirmPassword.getText().toString().equals(edtPassword.getText().toString()))){
            edtConfirmPassword.setError("Password did not match");
            isValid = false;
        }

        if (isMerchant == 1){
            if (edtMerchantName.getText().toString().isEmpty()) {
                edtMerchantName.setError("Merchant Name cannot be empty");
                isValid = false;
            }
        }

        return isValid;
    }
}