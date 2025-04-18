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
import android.widget.Button;

import com.example.wheneverapp.Activity.LoginActivity;
import com.example.wheneverapp.Model.User;
import com.example.wheneverapp.api.SERVER;
import com.example.wheneverapp.api.UserService;
import com.example.wheneverapp.databinding.FragmentProfileBinding;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private Retrofit retrofit;
    private User user;
    private String BASE_URL = SERVER.BASE_URL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Mở form đăng nhập
        binding.signInFrmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // Mở form đăng ký

        // sự kiện click logout
        binding.logOutBtn.setOnClickListener(v -> logout());

        // check login
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        checkLoginStatus();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLoginStatus(); // Kiểm tra lại trạng thái đăng nhập khi quay lại ProfileFragment
    }

    //Kiểm tra đã login chưa
    private void checkLoginStatus() {
        String token = getSavedToken();
        if (token == null || token.isEmpty()) {
            binding.signInFrmBtn.setVisibility(View.VISIBLE);
            binding.signUpFrmBtn.setVisibility(View.VISIBLE);
            binding.logOutBtn.setVisibility(View.GONE);
        } else {
            Log.d("TOKEN: ", token);
            binding.signInFrmBtn.setVisibility(View.GONE);
            binding.signUpFrmBtn.setVisibility(View.GONE);
            binding.logOutBtn.setVisibility(View.VISIBLE);
        }
    }

    private void logout() {
        UserService userService = retrofit.create(UserService.class);
        Call<User> call = userService.logout();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200 && response.body() != null) {
                    binding.signInFrmBtn.setVisibility(View.VISIBLE);
                    binding.signUpFrmBtn.setVisibility(View.VISIBLE);
                    binding.logOutBtn.setVisibility(View.GONE);
                    clearToken();
                } else {
                    Log.e("API_LOG", "Response failed: " + response.code());
                    binding.signInFrmBtn.setVisibility(View.GONE);
                    binding.signUpFrmBtn.setVisibility(View.GONE);
                    binding.logOutBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API_LOG", "API Error: " + t.getMessage());
            }
        });
    }

    private String getSavedToken() {
        SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getString("token", ""); // Trả về token hoặc chuỗi rỗng nếu không có
    }

    private void clearToken() {
        SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("token");
        editor.apply();
    }
}