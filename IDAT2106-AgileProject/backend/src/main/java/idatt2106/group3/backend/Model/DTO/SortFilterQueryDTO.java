package idatt2106.group3.backend.Model.DTO;

import idatt2106.group3.backend.Enum.SortingType;

/**
 * DTO used for sorting and filtering of activities
 */
public class SortFilterQueryDTO {
    private SortingType sortingType;
    private String searchQuery;
    private Integer difficulty;
    private int amount;
    private double userLongitude;
    private double userLatitude;

    public SortFilterQueryDTO(){
        // Must be empty constructor
    }

    public SortingType getSortingType() {
        return sortingType;
    }

    public void setSortingType(SortingType sortingType) {
        this.sortingType = sortingType;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }
}
