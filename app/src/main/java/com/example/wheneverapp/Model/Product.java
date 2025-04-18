package com.example.wheneverapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String _id;
    private String size;
    private int instock;
    private String modelId;

    public Product(Parcel in) {
        _id = in.readString();
        size = in.readString();
        instock = in.readInt();
        modelId = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(size);
        dest.writeInt(instock);
        dest.writeString(modelId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String get_id() {
        return _id;
    }

    public String getSize() {
        return size;
    }

    public int getInstock() {
        return instock;
    }

    public String getModelId() {
        return modelId;
    }
}
