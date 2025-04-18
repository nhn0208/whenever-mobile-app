package com.example.wheneverapp.Model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Model implements Parcelable {
    private String _id;
    private String name;
    private String description;
    private List<String> image;
    private String category;
    private List<Product> products;
    private double price;
    private String slug;

    public Model(String _id, String name, String description, List<String> image, String category, List<Product> products, double price, String slug) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.category = category;
        this.products = products;
        this.price = price;
        this.slug = slug;
    }

    protected Model(Parcel in) {
        _id = in.readString();
        name = in.readString();
        description = in.readString();
        image = in.createStringArrayList();
        category = in.readString();
        price = in.readDouble();
        slug = in.readString();
        products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeStringList(image);
        dest.writeString(category);
        dest.writeDouble(price);
        dest.writeString(slug);
        dest.writeTypedList(products);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getPrice() {
        return price;
    }

    public String getSlug() {
        return slug;
    }
}
