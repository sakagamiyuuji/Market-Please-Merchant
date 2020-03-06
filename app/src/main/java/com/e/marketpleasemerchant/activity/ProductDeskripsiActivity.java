package com.e.marketpleasemerchant.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.e.marketpleasemerchant.R;
import com.e.marketpleasemerchant.VolleyApp;
import com.e.marketpleasemerchant.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDeskripsiActivity extends AppCompatActivity {

    private final String DETAIL_PRODUCT = "detail_product";

    TextView productName, productSlug, productQty, categoryName, merchantName, merchantSlug, productDes, productPrice;
    ImageView productImage;
    Button btnAddToCart;
    FloatingActionButton fabEdt;
    Product product = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_deskripsi);

        showDeskripsi();
        btnClickAddToCard();

        fabEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDeskripsiActivity.this, EditActivity.class);
                intent.putExtra("id", product.getProductId());
                Toast.makeText(ProductDeskripsiActivity.this, String.valueOf(product.getProductId()), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }

    public void btnClickAddToCard(){
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDeskripsiActivity.this, R.style.AlertDialogTheme);
                View view = View.inflate(ProductDeskripsiActivity.this, R.layout.layout_delete_task, null);
                Button btnYes = view.findViewById(R.id.buttonYes);
                Button btnNo = view.findViewById(R.id.buttonNo);

                final AlertDialog alert = builder.create();
                alert.setView(view);
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteProduct();

                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ProductDeskripsiActivity.this, "BATAL DI HAPUS", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.show();

            }
        });
    }

    public void showDeskripsi(){

        Bundle bundle = getIntent().getExtras();
        String json = bundle.getString(DETAIL_PRODUCT);

        product = new Gson().fromJson(json, Product.class);
        initialComponent();

        String baseUrl = "http://210.210.154.65:4444/storage/";
        String url = baseUrl+product.getProductImage();

        /*Glide.with(this)
                .load(url)
                .into(productImage);
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();*/


        if (product.getProductImage() != null){
            Glide.with(getApplicationContext())
                    .load(url)
                    //.placeholder(R.drawable.hourglass)
                    .override(productImage.getWidth(), 200)
                    .fallback(R.drawable.notfound)
                    //.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE).error(R.drawable.notfound))
                    .transition(DrawableTransitionOptions.withCrossFade(2000))
                    .into(productImage);
            //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        }
        else{
            Glide.with(this).load(R.drawable.notfound).into(productImage);
        }

        int price = product.getProductPrice();
        Locale localID = new Locale("in", "ID");
        NumberFormat indoKurs = NumberFormat.getCurrencyInstance(localID);
        String currency = indoKurs.format((double) price);

        productName.setText(product.getProductName());
        productSlug.setText(product.getProductSlug());
        //productQty.setText(String.valueOf(product.getProductQty()));
        productQty.setText(String.valueOf(product.getProductQty()));
        categoryName.setText(product.getCategory().getCategoryName());
        merchantName.setText(product.getMerchant().getMerchantName());
        merchantSlug.setText(product.getMerchant().getMerchantSlug());


        if (product.getProductDesc() != null){
            productDes.setText(product.getProductDesc());
        }
        productPrice.setText(currency);
    }

    public void initialComponent(){
        productName = findViewById(R.id.tv_product_name);
        productSlug = findViewById(R.id.tv_product_slug);
        productQty = findViewById(R.id.tv_product_qty);
        productImage = findViewById(R.id.product_images);
        categoryName = findViewById(R.id.tv_category_name);
        merchantName = findViewById(R.id.tv_merchant_name);
        merchantSlug = findViewById(R.id.tv_merchant_slug);
        productDes = findViewById(R.id.tv_deskripsi_product);
        productPrice = findViewById(R.id.tv_product_price);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        fabEdt = findViewById(R.id.fab_edit);
    }

    public void deleteProduct(){
        String url = "http://210.210.154.65:4444/api/product/"+ product.getProductId()+"/delete";
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                        Toast.makeText(ProductDeskripsiActivity.this, "DATA TELAH DI HAPUS", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductDeskripsiActivity.this, "DATA GAGAL DI HAPUS", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyApp.getInstance().addToRequestQueue(req, "delete_product");
    }
}