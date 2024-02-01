package idatt2106.group3.backend.Model.DTO.Activity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Superclass for activity DTO classes
 */
public class ActivitySuperclassDTO {
    private String title;
    private String type;
    private String description;
    private String equipment;
    private int difficulty;
    private String city;
    private String place;
    private double longitude;
    private double latitude;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;
    private int durationMinutes;
    private boolean isPrivateActivity;
    private int maxParticipants;
    private String activityPicture;

    public ActivitySuperclassDTO(String title, String type, String description, String equipment, int difficulty, String city, String place, double longitude, double latitude, LocalDateTime startTime, int durationMinutes, boolean isPrivateActivity, int maxParticipants, String activityPicture) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.equipment = equipment;
        this.difficulty = difficulty;
        this.city = city;
        this.place = place;
        this.longitude = longitude;
        this.latitude = latitude;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.isPrivateActivity = isPrivateActivity;
        this.maxParticipants = maxParticipants;
        this.activityPicture = activityPicture;
    }

    public ActivitySuperclassDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public boolean isPrivateActivity() {
        return isPrivateActivity;
    }

    public void setPrivateActivity(boolean privateActivity) {
        isPrivateActivity = privateActivity;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getActivityPicture() {
        return activityPicture;
    }

    public void setActivityPicture(String activityPicture) {
        this.activityPicture = activityPicture;
    }

    @Override
    public String toString()
    {
        return "Activity{" +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", equipment='" + equipment + '\'' +
                ", difficulty=" + difficulty +
                ", city='" + city + '\'' +
                ", place='" + place + '\'' +
                ", longitude=" + longitude + '\'' +
                ", latitude=" + latitude + '\'' +
                ", startTime=" + startTime + '\'' +
                ", durationMinutes=" + durationMinutes + '\'' +
                ", isPrivateActivity=" + isPrivateActivity + '\'' +
                ", maxParticipants=" + maxParticipants;
    }
}
