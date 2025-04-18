package com.example.wheneverapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wheneverapp.Model.Model;
import com.example.wheneverapp.R;

import java.util.List;

public class ModelAdapter extends BaseAdapter {
    private Context context;
    private List<Model> modelList;

    public ModelAdapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_model_card, parent, false);
            holder = new ViewHolder();
            holder.modelCardName = convertView.findViewById(R.id.modelCardName);
            holder.modelCardImage = convertView.findViewById(R.id.modelCardImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Model model = modelList.get(position);
        holder.modelCardName.setText(model.getName());
        String imageUrl = model.getImage().get(0);  // Get first image
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.modelCardImage);
        return convertView;
    }

    private static class ViewHolder {
        TextView modelCardName;
        ImageView modelCardImage;
    }
}

