package com.e.marketpleasemerchant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.marketpleasemerchant.R;
import com.e.marketpleasemerchant.VolleyApp;
import com.e.marketpleasemerchant.fragment.ListProductFragment;
import com.e.marketpleasemerchant.model.AccessToken;
import com.e.marketpleasemerchant.utils.TokenManager;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    Animation titleFromUp, titleDescFromSide;
    TextView tvTitle, tvDescTitle;
    private int loadTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initialComponent();
        loadAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                validationToken();
            }
        }, loadTime);
    }

    public void initialComponent(){
        tvTitle = findViewById(R.id.tv_title);
        tvDescTitle = findViewById(R.id.tv_desc_title);
    }

    public void loadAnimation(){
        titleFromUp = AnimationUtils.loadAnimation(this, R.anim.title_splash_fromup);
        titleDescFromSide = AnimationUtils.loadAnimation(this, R.anim.titledesc_splash_fromside);
        tvTitle.setAnimation(titleFromUp);
        tvDescTitle.setAnimation(titleDescFromSide);
    }

    private void validationToken(){
        String url = " http://210.210.154.65:4444/api/auth/getuser";
        AccessToken accessToken = TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).getToken();
        String accestTok = accessToken.getAccessToken();

        if (accessToken.getAccessToken() == null){
            Intent nullToken = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(nullToken);
            Toast.makeText(SplashScreenActivity.this, "Selamat Datang di MarketPlease", Toast.LENGTH_SHORT).show();
            finish();
        }

        else if (TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).getToken() != null){
            StringRequest validateTokenReq = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject user = jsonObject.getJSONObject("user");
                                JSONObject merchant = user.getJSONObject("merchant");
                                String merchantName = merchant.getString("merchantName");
                                //Toast.makeText(SplashScreenActivity.this, merchantName, Toast.LENGTH_SHORT).show();


                                Intent validTokenIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                                validTokenIntent.putExtra("merchant", merchantName);
                                startActivity(validTokenIntent);


                                /*Bundle data = new Bundle();
                                data.putString("merchant", merchantName);*/
                            /*    ListProductFragment listProductFragment = new ListProductFragment();
                                listProductFragment.setArguments(data);
                                FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.list_product_fragment, listProductFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();*/




                                Toast.makeText(SplashScreenActivity.this, "Selamat Datang Kembali", Toast.LENGTH_SHORT).show();
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            /*Intent validTokenIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                            startActivity(validTokenIntent);

                            Toast.makeText(SplashScreenActivity.this, "Selamat Datang Kembali", Toast.LENGTH_SHORT).show();*/
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 401){
                                Intent expiredTokenIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                startActivity(expiredTokenIntent);
                                Toast.makeText(SplashScreenActivity.this, "Token telah Expired. Mohon login ulang", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            if (statusCode == 403){
                                Intent notValidIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                startActivity(notValidIntent);
                                Toast.makeText(SplashScreenActivity.this, "Silahkan Login Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers= new Hashtable<>();
                    headers.put("Accept","application/json");
                    headers.put("Authorization", accessToken.getTokenType() + " " + accestTok);

                    return headers;
                }
            };

            VolleyApp.getInstance().addToRequestQueue(validateTokenReq, "validate_token_req");
        }
    }

    public static String mName(String name){
        return name;
    }
}
