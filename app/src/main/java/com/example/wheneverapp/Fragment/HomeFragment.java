package com.example.wheneverapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.wheneverapp.Activity.ModelActivity;
import com.example.wheneverapp.Adapter.ModelAdapter;
import com.example.wheneverapp.Model.Model;
import com.example.wheneverapp.api.ModelService;
import com.example.wheneverapp.api.SERVER;
import com.example.wheneverapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private String BASE_URL = SERVER.BASE_URL;
    private Retrofit retrofit;
    private GridView modelGridView;
    private List<Model> modelList;
    private ModelAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        modelGridView = binding.modelView;

        modelList = new ArrayList<>();
        adapter = new ModelAdapter(requireContext(), modelList);
        modelGridView.setAdapter(adapter);
        modelGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Model model = modelList.get(i);
                Intent intent = new Intent(binding.getRoot().getContext(), ModelActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("model",model);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // lay data
        fetchModels();
        return root;
    }

    private void fetchModels() {
        ModelService modelService = retrofit.create(ModelService.class);
        Call<List<Model>> call = modelService.getAllModels();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if (response.code() == 200 && response.body() != null) {
                    modelList.clear();
                    modelList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Log.e("API_LOG", "API ERROR: " + t.getMessage());
            }
        });
    }
}
