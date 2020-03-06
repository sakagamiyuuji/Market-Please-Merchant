package com.e.marketpleasemerchant.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.e.marketpleasemerchant.R;
import com.e.marketpleasemerchant.fragment.ListProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView botNav;
    private CircleImageView circleAccount;
    private TextView headUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Fragment fragment = new ListProductFragment();
        loadFragment(fragment);

        botNav = findViewById(R.id.bot_nav);
        botNav.setOnNavigationItemSelectedListener(this);

    }

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
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.home_menu:
                fragment = new ListProductFragment();
                break;

            case R.id.other_menu:
                fragment = new ListProductFragment();
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