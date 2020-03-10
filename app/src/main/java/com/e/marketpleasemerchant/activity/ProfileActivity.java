package com.e.marketpleasemerchant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.e.marketpleasemerchant.R;
import com.e.marketpleasemerchant.VolleyApp;
import com.e.marketpleasemerchant.model.AccessToken;
import com.e.marketpleasemerchant.model.Register;
import com.e.marketpleasemerchant.utils.TokenManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.profile_name) TextView profilName;
    @BindView(R.id.profile_email) TextView profilEmail;
    @BindView(R.id.profile_merchant_name) TextView profileMerchantName;
    @BindView(R.id.profile_create_at) TextView profilCreateAt;
    @BindView(R.id.btn_logout) Button btnLogout;

    Register register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        getDataUser();

    }

    @OnClick(R.id.btn_logout)
    public void logout(){
        TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).deleteToken();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        /*intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
        HomeActivity.fa.finish();
        finish();

    }


    private void getDataUser(){
        String url = " http://210.210.154.65:4444/api/auth/getuser/";
        AccessToken accessToken = TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).getToken();
        String accesTok = accessToken.getAccessToken();
        StringRequest userProfileReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject user = jsonObject.getJSONObject("user");
                            String firstname = user.getString("first_name");
                            String lastname = user.getString("last_name");
                            String fullname = firstname + " " + lastname;
                            profilName.setText(fullname);
                            profilEmail.setText(user.getString("email"));
                            profilCreateAt.setText(user.getString("created_at"));

                            JSONObject merchant = user.getJSONObject("merchant");
                            profileMerchantName.setText(merchant.getString("merchantName"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
        /*JsonObjectRequest userProfileReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            JSONObject user = response.getJSONObject("user");
                            String firstname = user.getString("first_name");
                            //String lastname = String.valueOf(user.getString("last_name"));

                            profilName.setText(firstname);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },*/
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        Toast.makeText(ProfileActivity.this, statusCode, Toast.LENGTH_SHORT).show();

                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new Hashtable<>();
                headers.put("Accept","application/json");
                headers.put("Authorization", accessToken.getTokenType() +" "+ accesTok);

                return headers;
            }
        };

        VolleyApp.getInstance().addToRequestQueue(userProfileReq, "user_profil_req");
    }
}
