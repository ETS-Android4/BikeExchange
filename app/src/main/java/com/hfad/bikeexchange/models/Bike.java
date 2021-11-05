package com.hfad.bikeexchange.models;

public class Bike {

    private String frame, image, category;
    private Double price;

    public Bike() { }

    public Bike(String frame, String image, String category, Double price) {
        this.frame = frame;
        this.image = image;
        this.category = category;
        this.price = price;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
