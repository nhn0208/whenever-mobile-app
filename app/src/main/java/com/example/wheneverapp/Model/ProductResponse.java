package com.example.wheneverapp.Model;

public class ProductResponse {
    private String _id;
    private String size;
    private int instock;
    private Model modelId;

    public String get_id() {
        return _id;
    }

    public String getSize() {
        return size;
    }

    public int getInstock() {
        return instock;
    }

    public Model getModelId() {
        return modelId;
    }
}
