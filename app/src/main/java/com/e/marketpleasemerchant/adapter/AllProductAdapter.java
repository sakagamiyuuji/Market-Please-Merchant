package com.e.marketpleasemerchant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.e.marketpleasemerchant.R;
import com.e.marketpleasemerchant.activity.ProductDeskripsiActivity;
import com.e.marketpleasemerchant.model.Product;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Product> product = new ArrayList<>();
    private final String DETAIL_PRODUCT = "detail_product";

    public AllProductAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.all_product_layout, parent, false);
        return new MyViewHolder(view);
    }

    public void addData (ArrayList<Product> products){
        this.product = products;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvProductName.setText(product.get(position).getProductName());
        holder.tvMerchantName.setText(String.valueOf(product.get(position).getMerchant().getMerchantName()));
        holder.tvCategoryName.setText(product.get(position).getCategory().getCategoryName());

        String baseURL = "http://210.210.154.65:4444/storage/";
        String url = baseURL+product.get(position).getProductImage();

        if (product.get(position).getProductImage() != null){
            Glide.with(context)
                    .load(url)
                    .into(holder.productImg);
        }
        else{
            Glide.with(context).load(R.drawable.error404).into(holder.productImg);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDeskripsiActivity.class);
                String json = new Gson().toJson(product.get(position));
                intent.putExtra(DETAIL_PRODUCT, json);
                context.startActivity(intent);
            }
        });

        String urlId = "http://210.210.154.65:4444/api/product-by-id/"+product.get(position).getProductId();
        if (urlId == null){
            product.remove(position);
        }


    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvProductName, tvMerchantName, tvCategoryName;
        ImageView productImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvMerchantName = itemView.findViewById(R.id.tv_merchant_name);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            productImg = itemView.findViewById(R.id.product_img);
        }
    }
}
