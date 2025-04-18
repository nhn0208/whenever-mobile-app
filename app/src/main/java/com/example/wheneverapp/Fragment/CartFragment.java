package com.example.wheneverapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.wheneverapp.Activity.LoginActivity;
import com.example.wheneverapp.Adapter.CartAdapter;
import com.example.wheneverapp.Model.Cart;
import com.example.wheneverapp.R;
import com.example.wheneverapp.api.CartService;
import com.example.wheneverapp.api.SERVER;
import com.example.wheneverapp.databinding.FragmentCartBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private Retrofit retrofit;
    private String BASE_URL = SERVER.BASE_URL;
    private List<Cart> cartList;
    private CartAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        cartList = new ArrayList<>();
        adapter = new CartAdapter(requireContext(), cartList);
        binding.cartListView.setAdapter(adapter);

        // get cart list
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fetchCart();
        return root;
    }

    // hàm fetch cart
    private void fetchCart() {
        String customerId = getSavedCustomerId();
        CartService cartService = retrofit.create(CartService.class);
        Call<List<Cart>> call = cartService.getAllCartByCustomerId(customerId);
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.code() == 200 && response.body() != null) {
                    cartList.clear();
                    cartList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    double total = 0;
                    for (Cart cart : response.body()) {
                        total += cart.getProductId().getModelId().getPrice();
                    }
                    binding.totalCartTV.setText(String.format("Tổng tiền: %s VND",(int) total));
                }else {
                    Log.e("API_LOG", "Response failed: " + response.code());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.e("API_LOG", "API ERROR: " + t.getMessage());
            }
        });
    }
    private String getSavedCustomerId() {
        SharedPreferences prefs = getContext().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getString("customerId","");
    }
}