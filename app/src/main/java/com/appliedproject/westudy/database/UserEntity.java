package com.appliedproject.westudy.database;

public final class UserEntity {
    private String id;
    private String username;
    private String email;
    private String photourl;
    private int coins;

    public UserEntity(String id, String username, String email, String photourl, int coins) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.photourl = photourl;
        this.coins = coins;
    }

    public UserEntity() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
