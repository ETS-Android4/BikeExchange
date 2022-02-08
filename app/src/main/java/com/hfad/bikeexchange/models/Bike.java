package com.hfad.bikeexchange.models;

public class Bike {

    private String frame, image, category;
    private Integer price;

    public Bike() { }

    public Bike(String frame, String image, String category, Integer price) {
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
