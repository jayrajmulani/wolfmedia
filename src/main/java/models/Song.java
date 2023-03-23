package models;

import java.sql.Date;

public class Song {
    long id;
    String title;
    String releaseCountry;
    String language;
    float duration;
    float royaltyRate;
    Date releaseDate;
    boolean royaltyPaid;
    public Song(long id, String title, String releaseCountry, String language, float duration, float royaltyRate, Date releaseDate, boolean royaltyPaid) {
        this.id = id;
        this.title = title;
        this.releaseCountry = releaseCountry;
        this.language = language;
        this.duration = duration;
        this.royaltyRate = royaltyRate;
        this.releaseDate = releaseDate;
        this.royaltyPaid = royaltyPaid;
    }
    public Song(String title, String releaseCountry, String language, float duration, float royaltyRate, Date releaseDate, boolean royaltyPaid) {
        this.title = title;
        this.releaseCountry = releaseCountry;
        this.language = language;
        this.duration = duration;
        this.royaltyRate = royaltyRate;
        this.releaseDate = releaseDate;
        this.royaltyPaid = royaltyPaid;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseCountry() {
        return releaseCountry;
    }

    public void setReleaseCountry(String releaseCountry) {
        this.releaseCountry = releaseCountry;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getRoyaltyRate() {
        return royaltyRate;
    }

    public void setRoyaltyRate(float royaltyRate) {
        this.royaltyRate = royaltyRate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isRoyaltyPaid() {
        return royaltyPaid;
    }

    public void setRoyaltyPaid(boolean royaltyPaid) {
        this.royaltyPaid = royaltyPaid;
    }
}
