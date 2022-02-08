package com.hfad.bikeexchange.models;

public class Item {
    public Item() { }

    private String name, image, category;
    private Integer id, commonPrice;

    public Item(Integer id, String name, String image, String category, Integer commonPrice) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.category = category;
        this.commonPrice = commonPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String frame) {
        this.name = frame;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCommonPrice() {
        return commonPrice;
    }

    public void setCommonPrice(Integer commonPrice) {
        this.commonPrice = commonPrice;
    }
}
