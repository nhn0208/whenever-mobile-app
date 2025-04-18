package com.example.wheneverapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ContentView;

import com.bumptech.glide.Glide;
import com.example.wheneverapp.Model.Cart;
import com.example.wheneverapp.Model.Model;
import com.example.wheneverapp.R;
import com.example.wheneverapp.api.ModelService;
import com.example.wheneverapp.api.SERVER;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private  Retrofit retrofit;
    private String BASE_URL = SERVER.BASE_URL;
    private List<Cart> cartList;
    private Model model;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int i) {
        return cartList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_cart_item, parent, false);
            holder = new CartAdapter.ViewHolder();
            holder.btnEditCartItem = convertView.findViewById(R.id.btnEditCartItem);
            holder.btnDeleteCartItem = convertView.findViewById(R.id.btnDeleteCartItem);
            holder.nameCartItem = convertView.findViewById(R.id.nameCartItem);
            holder.sizeCartItem = convertView.findViewById(R.id.sizeCartItem);
            holder.priceCartItem = convertView.findViewById(R.id.priceCartItem);
            holder.btnDecreaseCartItem = convertView.findViewById(R.id.btnDecreaseCartItem);
            holder.btnIncreaseCartItem = convertView.findViewById(R.id.btnIncreaseCartItem);
            holder.quantityCartItem = convertView.findViewById(R.id.quantityCartItem);
            holder.imgCartItem = convertView.findViewById(R.id.imgCartItem);
            convertView.setTag(holder);
        } else {
            holder = (CartAdapter.ViewHolder) convertView.getTag();
        }
        Cart cart = cartList.get(position);

        // get model information
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        holder.nameCartItem.setText(cart.getProductId().getModelId().getName());
        holder.sizeCartItem.setText(cart.getProductId().getSize());
        holder.priceCartItem.setText(String.format("%s VND",cart.getProductId().getModelId().getPrice()));
        holder.quantityCartItem.setText(String.format("%s",cart.getQuantity()));
        String imageUrl = cart.getProductId().getModelId().getImage().get(0);
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.imgCartItem);


        // Sự kiện khi bấm nút "Sửa"
        holder.btnEditCartItem.setOnClickListener(v -> {
            String s = (String) holder.btnEditCartItem.getText();
            if (Objects.equals(s, "Sửa")) {
                holder.btnDeleteCartItem.setVisibility(View.VISIBLE);
                Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                holder.btnDeleteCartItem.startAnimation(slideIn);
                holder.btnEditCartItem.setText("Xong");
            }else {
                Animation slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out);
                holder.btnDeleteCartItem.startAnimation(slideOut);
                holder.btnDeleteCartItem.setVisibility(View.GONE);
                holder.btnEditCartItem.setText("Sửa");
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView nameCartItem, sizeCartItem, priceCartItem, btnEditCartItem, btnDeleteCartItem, btnDecreaseCartItem, btnIncreaseCartItem, quantityCartItem;
        ImageView imgCartItem;
    }
}
