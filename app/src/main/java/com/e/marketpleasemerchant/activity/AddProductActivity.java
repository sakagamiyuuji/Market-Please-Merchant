package com.e.marketpleasemerchant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.e.marketpleasemerchant.error.ProductErrorResponse;
import com.e.marketpleasemerchant.R;
import com.e.marketpleasemerchant.VolleyApp;
import com.e.marketpleasemerchant.adapter.CategorySpinnerAdapter;
import com.e.marketpleasemerchant.model.AccessToken;
import com.e.marketpleasemerchant.model.Category;
import com.e.marketpleasemerchant.utils.TokenManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner categoryDropDown;
    ArrayList<Category> categories = new ArrayList<>();
    CategorySpinnerAdapter catSpinAdapter = new CategorySpinnerAdapter();
    ImageView edtProductImg;
    Button btnAddProduct;
    EditText edtProductName, edtProductPrice, edtProductQty, edtProductDesc;

    private int PICK_IMAGE_REQUEST = 1;
    private String productImage = null; // image string yang akan dikirim  ke server (bukan dalam bentuk gambar tapi dalam bentuk string base64.
    private String productName, productDesc,productQty, productPrice, categoryId, merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initialComponent();
        setAdapter();
        getAllCategory();

        edtProductImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChoose();

            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = edtProductName.getText().toString();
                productDesc = edtProductDesc.getText().toString();
                productPrice = edtProductPrice.getText().toString();
                productQty = edtProductQty.getText().toString();
                merchantId = "1";

                if(productImage == null){ // jika kosong,
                    productImage = null;     // isi dengan null
                }

                postProductToServer();
                //finish();
                Toast.makeText(getApplicationContext(), "SEDANG DI PROSES", Toast.LENGTH_SHORT).show();
            }

        });


    }

    public void setAdapter(){
        categoryDropDown.setAdapter(catSpinAdapter);
        categoryDropDown.setOnItemSelectedListener(this);
    }
    public void initialComponent(){
        categoryDropDown = findViewById(R.id.category_dropdown);
        edtProductImg = findViewById(R.id.edt_product_img);
        btnAddProduct = findViewById(R.id.btn_add_newproduct);
        edtProductName = findViewById(R.id.edt_product_name);
        edtProductPrice = findViewById(R.id.edt_product_price);
        edtProductQty = findViewById(R.id.edt_product_qty);
        edtProductDesc = findViewById(R.id.edt_product_desc);
    }

    public void getAllCategory(){
        String url = "http://210.210.154.65:4444/api/categories";

        JsonObjectRequest listCatReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0;i<data.length();i++){
                                Gson gson = new Gson();
                                Category category = gson.fromJson(data.getJSONObject(i).toString(),Category.class);
                                categories.add(category);
                            }

                            catSpinAdapter.addData(categories);
                            catSpinAdapter.notifyDataSetChanged();
                            //Toast.makeText(getApplicationContext(),String.valueOf(catSpinAdapter.getCount()),Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        VolleyApp.getInstance().addToRequestQueue(listCatReq, "category_dropdown");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.categoryId = String.valueOf(catSpinAdapter.getItemId(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showImageChoose(){
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp) {

        /*
        1. First convert the image into bitmap.
           Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        2. Then compress bitmap to ByteArrayOutputStream
        3. Convert ByteArrayOutputStream to byte array.
        4. Finally convert byte array to base64 string.*/

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //encoding image to string
                productImage = getStringImage(bitmap);
                Log.d("image",productImage);

                Glide.with(getApplicationContext())
                        .load(bitmap)
                        .override(edtProductImg.getWidth())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(edtProductImg);
                System.out.println("image : "+productImage);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void postProductToServer() {
        AccessToken accessToken = TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).getToken();
        String accesTok = accessToken.getAccessToken();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://210.210.154.65:4444/api/merchant/products",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response :", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code=jsonObject.getInt("code");
                            if(code ==200){
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                            else{
                                ProductErrorResponse errorResponse = new Gson().fromJson(jsonObject.getString("message"),ProductErrorResponse.class);
                                if(errorResponse.getProductNameError().size() != 0) {
                                    if (errorResponse.getProductNameError().get(0) != null) {
                                        edtProductName.setError(errorResponse.getProductNameError().get(0));
                                        Toast.makeText(getApplicationContext(), errorResponse.getProductNameError().get(0), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if(errorResponse.getProductQtyError().size() != 0) {
                                    if (errorResponse.getProductQtyError().get(0) != null) {
                                        edtProductQty.setError(errorResponse.getProductQtyError().get(0));
                                        Toast.makeText(getApplicationContext(), errorResponse.getProductQtyError().get(0), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if(errorResponse.getProductPriceError().size() != 0) {
                                    if (errorResponse.getProductPriceError().get(0) != null) {
                                        edtProductPrice.setError(errorResponse.getProductPriceError().get(0));
                                        Toast.makeText(getApplicationContext(), errorResponse.getProductPriceError().get(0), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            Log.i("response :", response);

                        } catch (Exception ex) {
                            ex.printStackTrace();

                        }
                        Toast.makeText(getApplicationContext(), "Success Added product",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        try {
                            String body = new String(error.networkResponse.data, "UTF-8");
                            Toast.makeText(AddProductActivity.this, body, Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        if(error.networkResponse.statusCode == 400){
                            Toast.makeText(getApplicationContext(), String.valueOf(error.networkResponse), Toast.LENGTH_LONG).show();
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();

                params.put("productName", productName);
                params.put("productDesc", productDesc);
                params.put("productQty", productQty);

                if (productImage != null){
                    params.put("productImage", productImage);
                }

                params.put("productPrice", productPrice);
                params.put("categoryId", categoryId);
                //params.put("merchantId", merchantId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new Hashtable<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", accessToken.getTokenType() +" "+ accesTok);
                return headers;
            }
        };
        {
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}
