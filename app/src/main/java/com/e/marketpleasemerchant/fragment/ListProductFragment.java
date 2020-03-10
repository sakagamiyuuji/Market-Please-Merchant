package com.e.marketpleasemerchant.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.e.marketpleasemerchant.R;
import com.e.marketpleasemerchant.VolleyApp;
import com.e.marketpleasemerchant.activity.AddProductActivity;
import com.e.marketpleasemerchant.activity.ProfileActivity;
import com.e.marketpleasemerchant.activity.SplashScreenActivity;
import com.e.marketpleasemerchant.adapter.AllProductAdapter;
import com.e.marketpleasemerchant.adapter.CategoryListAdapter;
import com.e.marketpleasemerchant.model.AccessToken;
import com.e.marketpleasemerchant.model.Category;
import com.e.marketpleasemerchant.model.Product;
import com.e.marketpleasemerchant.utils.TokenManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListProductFragment extends Fragment {

    private FloatingActionButton fabAdd;

    private RecyclerView recyclerView, rvCategory;
    private CardView cvAllProduct;
    private AllProductAdapter adapter = new AllProductAdapter(getActivity());
    private CategoryListAdapter adapterCat = new CategoryListAdapter(getActivity());
    private static Context context;
    private CircleImageView circleAccount;
    public static TextView headUserName;


    //String url = "http://210.210.154.65:4444/api/products";
    String url = "http://210.210.154.65:4444/api/merchant/products";
    String urlCat = "http://210.210.154.65:4444/api/categories";

    public static ListProductFragment newInstance(Context contexts){
        ListProductFragment listProductFragment = new ListProductFragment();
        context = contexts;
        return listProductFragment;
    }


    public ListProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_list_product, container, false);

        recyclerView = root.findViewById(R.id.rv_all_product);
        rvCategory = root.findViewById(R.id.rv_category);
        cvAllProduct = root.findViewById(R.id.cv_allproduct);
        fabAdd = root.findViewById(R.id.fab_add);
        circleAccount = root.findViewById(R.id.accountprofile);
        headUserName = root.findViewById(R.id.head_username);

        String merchantName = getArguments().getString("merchantname");
        //Toast.makeText(context, merchantName, Toast.LENGTH_SHORT).show();
        headUserName.setText("Welcome "+merchantName);

        /*try {
            String merchantName = getArguments().getString("merchant");
            if (merchantName != null){
                headUserName.setText("Welcome "+merchantName);
            }
            else {
                Toast.makeText(context, "NAMA KOSONG", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/


        circleAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        fabClick();


        volleyEffect();
        volleyEffectCat();
        return root;
    }


    public void fabClick(){
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ADD PRODUCT", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                startActivity(intent);
            }
        });
    }


    public void generateAdapter(){
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void volleyEffect(){

        AccessToken accessToken = TokenManager.getInstance(this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)).getToken();
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
                        //Log.e("VOLLEY NYA ERROR BANG", error.getMessage());
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new Hashtable<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", accessToken.getTokenType() +" "+ accessToken.getAccessToken());
                return headers;
            }
        };

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

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            rvCategory.setLayoutManager(layoutManager);
                            rvCategory.setAdapter(adapterCat);
                            adapterCat.addData(categories);
                            //Toast.makeText(context.getApplicationContext(), String.valueOf(adapterCat.getItemCount()), Toast.LENGTH_SHORT).show();
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
    }

}
