package com.example.wheneverapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wheneverapp.Layout.MainActivity;
import com.example.wheneverapp.Model.LoginResponse;
import com.example.wheneverapp.Model.User;
import com.example.wheneverapp.api.SERVER;
import com.example.wheneverapp.api.UserService;
import com.example.wheneverapp.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private Retrofit retrofit;
    private User user;
    private String BASE_URL = SERVER.BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // sự kiện login
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        binding.btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        boolean isValid = true;

        // Kiem tra email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Email không hợp lệ!");
            isValid = false;
        }else {
            binding.tilEmail.setError(null);
        }

        // kiem tra password
        if (password.length() < 6) {
            binding.tilPassword.setError("Mật khẩu phải có ít nhất 6 kí tự");
            isValid = false;
        }else {
            binding.tilPassword.setError(null);
        }

        if (isValid) {
            Log.d("LOGIN", "email: " + email + " -Password: " + password);
            user = new User();
            user.setUsername(email);
            user.setPassword(password);
            UserService userService = retrofit.create(UserService.class);
            Call<LoginResponse> call = userService.login(user);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.code() == 200 && response.body() != null) {
                        Log.d("LOGIN", "Đăng nhập thành công!" + response.body().getToken());
                        saveToken(response.body().getToken(), response.body().getUser().get_id());
                        finish();
                    } else {
                        Log.e("API_LOG", "Response failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e("API_LOG", "API Error: " + t.getMessage());
                }
            });
        }
    }

    // Lưu token
    private void saveToken(String token,String customerId) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        editor.putString("customerId", customerId);
        editor.apply();
    }
}