package com.example.wheneverapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.wheneverapp.Model.Cart;
import com.example.wheneverapp.Model.CartRequest;
import com.example.wheneverapp.Model.Model;
import com.example.wheneverapp.Model.Product;
import com.example.wheneverapp.R;
import com.example.wheneverapp.api.CartService;
import com.example.wheneverapp.api.SERVER;
import com.example.wheneverapp.databinding.FragmentAddToCartBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddToCartBottomSheet extends BottomSheetDialogFragment {

    private FragmentAddToCartBinding binding;
    private Model model;
    private List<Product> productList;
    private Product selectedProduct;
    private int selectedStock = 0;
    private int quantity = 1;
    private String BASE_URL = SERVER.BASE_URL;

    public AddToCartBottomSheet(Model model) {
        this.model = model;
        this.productList = model.getProducts(); // Lấy danh sách sản phẩm theo size
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddToCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (model != null) {
            binding.tvPrice.setText(String.format("%s VND",(int) model.getPrice()));
            binding.tvQuantity.setText(String.format("%s", quantity));

            // Load hình ảnh đầu tiên của sản phẩm
            Glide.with(requireContext())
                    .load(model.getImage().get(0))
                    .placeholder(R.drawable.placeholder_image)
                    .into(binding.ivProductImage);

            setupSizeButton();
            setupQuantityButton();
        }

        // Xử lý thêm vào giỏ hàng
        binding.btnAddToCart.setOnClickListener(v -> addToCart());

        // Đóng popup
        binding.btnClose.setOnClickListener(v -> dismiss());
    }

    // Thiết lập chọn size
    private void setupSizeButton() {
        //binding.layoutSizeButtons.removeAllViews();

        for (Product product : productList) {
            TextView sizeBtn = new TextView(getContext());
            sizeBtn.setText(product.getSize());
            sizeBtn.setBackgroundResource(R.drawable.button_rectangle);
            sizeBtn.setGravity(17);
            sizeBtn.setPadding(32,16,32,16);
            sizeBtn.setTextSize(16);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, // width
                    ViewGroup.LayoutParams.WRAP_CONTENT  // height
            );
            params.setMargins(0, 0, 32, 0);
            sizeBtn.setLayoutParams(params);
            sizeBtn.setOnClickListener(v -> selectSize(product, sizeBtn));

            binding.layoutSizeButtons.addView(sizeBtn);
        }
    }

    // Xử lý sự kiện khi chọn size
    private void selectSize(Product product, TextView selectedBtn) {
        selectedProduct = product;
        selectedStock = product.getInstock();
        binding.tvInStock.setText(String.format("Kho: %s", selectedStock));
        //Log.d("Selected Product:", "Id: " + product.get_id() + " Size: " + product.getSize());

        // Đặt lại màu cho tất cả các nút size
        for (int i = 0; i < binding.layoutSizeButtons.getChildCount(); i++) {
            TextView tv = (TextView) binding.layoutSizeButtons.getChildAt(i);
            tv.setBackgroundResource(R.drawable.button_rectangle);
            tv.setTextColor(getResources().getColor(R.color.black));
        }

        selectedBtn.setBackgroundResource(R.drawable.button_custom);
        selectedBtn.setTextColor(getResources().getColor(R.color.primary));
    }

    // setup tăng giảm số lượng
    private void setupQuantityButton() {
        binding.btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                binding.tvQuantity.setText(String.valueOf(quantity));
            }
        });

        binding.btnIncrease.setOnClickListener(v -> {
            if (quantity < selectedStock) {
                quantity++;
                binding.tvQuantity.setText(String.valueOf(quantity));
            }
        });

    }

    // Gửi request thêm vào giỏ hàng
    private void addToCart() {
        if (selectedProduct.getSize() == null) {
            Toast.makeText(getContext(), "Vui lòng chọn size!", Toast.LENGTH_SHORT).show();
            return;
        }
        int quantity = Integer.parseInt(binding.tvQuantity.getText().toString());
        if (quantity <= 0 || quantity > selectedStock) {
            Toast.makeText(getContext(), "Số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        String customerId = getCustomerId();
        //Log.d("Selected Product:", "Id: " + selectedProduct+ "  - Quantity: " + quantity + "  -CustomerId: "+ customerId);

        // Gửi request đến server (addToCart)
        CartRequest cartRequest = new CartRequest(customerId, selectedProduct.get_id(), quantity);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CartService cartService = retrofit.create(CartService.class);
        Call<Cart> call = cartService.addProductToCart(cartRequest);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("ADD CART", "ADD CART SUCCESSFULLy");
                    dismiss();
                }else {
                    Log.e("ADD ERROR", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Log.e("API_LOG", "API Error: " + t.getMessage());
            }
        });
    }

    private String getCustomerId() {
        SharedPreferences prefs = getContext().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getString("customerId","");
    }
}
