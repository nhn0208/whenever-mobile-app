package com.example.wheneverapp.Activity;

import static java.lang.String.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.wheneverapp.Adapter.ImageAdapter;
import com.example.wheneverapp.Fragment.AddToCartBottomSheet;
import com.example.wheneverapp.Model.Model;
import com.example.wheneverapp.R;
import com.example.wheneverapp.databinding.ActivityModelBinding;

import java.util.List;

public class ModelActivity extends AppCompatActivity {
    private ActivityModelBinding binding;
    private boolean isDescriptionExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        EdgeToEdge.enable(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        Model model = (Model) bundle.getParcelable("model");
        if (model!=null) {
            // Get name
            binding.modelName.setText(model.getName());

            // get price
            binding.modelPrice.setText(format("%s VND",(int) model.getPrice()));

            //Get image list
            List<String> imageUrls = model.getImage();
            if (imageUrls != null && !imageUrls.isEmpty()) {
                ImageAdapter adapter = new ImageAdapter(this, imageUrls);
                binding.viewPager.setAdapter(adapter);
                binding.lengthOfImageList.setText(String.valueOf(imageUrls.size()));

                // Listen for page changes
                binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        binding.indexOfImage.setText(String.valueOf(position + 1));
                    }
                });
            }

            // get description
            binding.modelDescription.setText(model.getDescription());
            // Setup description toggle
            binding.btnToggleDescription.setOnClickListener(v -> {
                isDescriptionExpanded = !isDescriptionExpanded;

                if (isDescriptionExpanded) {
                    // Mở rộng description
                    binding.modelDescription.setMaxLines(Integer.MAX_VALUE);
                    binding.modelDescription.setEllipsize(null);
                    binding.btnToggleDescription.setText("Thu gọn");
                } else {
                    // Thu gọn description
                    binding.modelDescription.setMaxLines(4);
                    binding.modelDescription.setEllipsize(TextUtils.TruncateAt.END);
                    binding.btnToggleDescription.setText("Xem thêm");
                }
            });
        }
        // button back
        binding.btnBack.setOnClickListener(v -> finish());

        //button add to cart popup
        binding.btnAddToCartPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = getSavedToken();
                Log.d("TOKEN", token);
                if (token.isEmpty()) {
                    Intent intent = new Intent(ModelActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    AddToCartBottomSheet bottomSheet = new AddToCartBottomSheet(model);
                    bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private String getSavedToken() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getString("token", ""); // Trả về token hoặc chuỗi rỗng nếu không có
    }
}