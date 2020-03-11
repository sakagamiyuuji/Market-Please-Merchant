package com.e.marketpleasemerchant.activity;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.marketpleasemerchant.ConnectionReceiver;
import com.e.marketpleasemerchant.R;
import com.e.marketpleasemerchant.VolleyApp;
import com.e.marketpleasemerchant.fragment.ListProductFragment;
import com.e.marketpleasemerchant.model.AccessToken;
import com.e.marketpleasemerchant.utils.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView botNav;
    private CircleImageView circleAccount;
    ConnectionReceiver connectionReceiver;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        connectionReceiver = new ConnectionReceiver();
        registerReceiver(connectionReceiver,intentFilter);


        fa = this;

        Bundle bundle = getIntent().getExtras();
        String merchant = bundle.getString("merchant");

        Bundle data = new Bundle();
        data.putString("merchantname", merchant);
        ListProductFragment listProductFragment = new ListProductFragment();
        //ListProductFragment listProductFragment = ListProductFragment.newInstance(getApplicationContext());
        listProductFragment.setArguments(data);
        loadFragment(listProductFragment);

        botNav = findViewById(R.id.bot_nav);
        botNav.setOnNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static Activity fa;

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_product_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Bundle bundle = getIntent().getExtras();
        String merchant = bundle.getString("merchant");

        Fragment fragment = null;
        Bundle data = new Bundle();
        data.putString("merchantname", merchant);
        switch (menuItem.getItemId()){
            case R.id.home_menu:
                fragment = new ListProductFragment();
                fragment.setArguments(data);
                loadFragment(fragment);
                break;

            case R.id.other_menu:
                fragment = new ListProductFragment();
                fragment.setArguments(data);
                loadFragment(fragment);
                break;
        }

        return loadFragment(fragment);
    }



    /*RecyclerView recyclerView, rvCategory;
    CardView cvAllProduct;
    AllProductAdapter adapter = new AllProductAdapter(this);
    CategoryListAdapter adapterCat = new CategoryListAdapter(this);

    String url = "http://210.210.154.65:4444/api/products";
    String urlCat = "http://210.210.154.65:4444/api/categories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        initialComponent();
        volleyEffect();
        volleyEffectCat();
    }

    public void initialComponent(){
        recyclerView = findViewById(R.id.rv_all_product);
        rvCategory = findViewById(R.id.rv_category);
        cvAllProduct = findViewById(R.id.cv_allproduct);
    }

    public void generateAdapter(){
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void volleyEffect(){

        JsonObjectRequest listProductReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Product> products = new ArrayList<>();

                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject productJO = data.getJSONObject(i);
                                Gson gson = new Gson();
                                Product product = gson.fromJson(productJO.toString(), Product.class);
                                products.add(product);
                            }



                            generateAdapter();
                            adapter.addData(products);
                            //Toast.makeText(getApplicationContext(), String.valueOf(adapter.getItemCount()), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY NYA ERROR BANG", error.getMessage());
                    }
                });

        VolleyApp.getInstance().addToRequestQueue(listProductReq, "listProductReq");
    }

    public void volleyEffectCat(){

        JsonObjectRequest listCatReq = new JsonObjectRequest(Request.Method.GET, urlCat, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Category> categories = new ArrayList<>();

                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject productJO = data.getJSONObject(i);
                                Gson gson = new Gson();
                                Category category = gson.fromJson(productJO.toString(), Category.class);
                                categories.add(category);
                            }

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                            rvCategory.setLayoutManager(layoutManager);
                            rvCategory.setAdapter(adapterCat);
                            adapterCat.addData(categories);
                            Toast.makeText(getApplicationContext(), String.valueOf(adapterCat.getItemCount()), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY NYA ERROR BANG", error.getMessage());
                    }
                });

        VolleyApp.getInstance().addToRequestQueue(listCatReq, "listCatReq");
    }*/
}