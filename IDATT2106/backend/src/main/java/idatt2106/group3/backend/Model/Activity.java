package idatt2106.group3.backend.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import idatt2106.group3.backend.Model.DTO.Activity.ActivityRegistrationDTO;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Entity class for storing information about an activity
 * with activityId as primary key
 */
@Entity(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long activityId;
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
    private boolean markedAbsence;

    @JsonIgnore
    @ManyToMany(mappedBy = "activities", fetch = FetchType.EAGER)
    private Set<User> users;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User organizer;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "chatId", referencedColumnName = "chatId")
    private Chat chat;

    @Lob
    private byte[] activityPicture;

    public Activity(String title, String type, String description, String equipment, int difficulty, String city, String place, double longitude, double latitude, LocalDateTime startTime, int durationMinutes, boolean isPrivateActivity, Set<User> users, User organizer, Chat chat, int maxParticipants, boolean markedAbsence, byte[] activityPicture) {
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
        this.users = users;
        this.organizer = organizer;
        this.chat = chat;
        this.maxParticipants = maxParticipants;
        this.markedAbsence = markedAbsence;
        this.activityPicture = activityPicture;
    }

    public Activity(String title, String type, String description, String equipment, int difficulty, String city, String place, double longitude, double latitude, LocalDateTime startTime, int durationMinutes, boolean isPrivateActivity, int maxParticipants, boolean markedAbsence, byte[] activityPicture) {
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
        this.markedAbsence = markedAbsence;
        this.activityPicture = activityPicture;
    }

    /**
     * Activity constructor used for creating a new activity based on
     * information from ActivityRegistration DTO and organizer.
     * @param activityRegistrationDTO
     * @param organizeUser
     */
    public Activity(ActivityRegistrationDTO activityRegistrationDTO, User organizeUser){
        this.title = activityRegistrationDTO.getTitle();
        this.type = activityRegistrationDTO.getType();
        this.description = activityRegistrationDTO.getDescription();
        this.equipment = activityRegistrationDTO.getEquipment();
        this.difficulty = activityRegistrationDTO.getDifficulty();
        this.city = activityRegistrationDTO.getCity();
        this.place = activityRegistrationDTO.getPlace();
        this.longitude = activityRegistrationDTO.getLongitude();
        this.latitude = activityRegistrationDTO.getLatitude();
        this.startTime = activityRegistrationDTO.getStartTime();
        this.durationMinutes = activityRegistrationDTO.getDurationMinutes();
        this.isPrivateActivity = activityRegistrationDTO.isPrivateActivity();
        if(activityRegistrationDTO.getActivityPicture() != null)this.activityPicture = activityRegistrationDTO.getActivityPicture().getBytes();
        this.maxParticipants = activityRegistrationDTO.getMaxParticipants();
        this.markedAbsence = false;
        this.organizer = organizeUser;
    }

    public Activity() {
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public byte[] getActivityPicture() {
        return activityPicture;
    }

    public void setActivityPicture(byte[] activityPicture) {
        this.activityPicture = activityPicture;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public boolean isMarkedAbsence() {
        return markedAbsence;
    }

    public void setMarkedAbsence(boolean markedAbsence) {
        this.markedAbsence = markedAbsence;
    }

    @Override
    public String toString()
    {
        return "Activity{" +
                "activityId=" + activityId +
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
                ", maxParticipants='" + maxParticipants + '\'' +
                ", markedAbsence='" + markedAbsence + '\'' +
                ", users=" + users + '\'' +
                ", organizer=" + organizer + '\'' +
                ", chat=" + chat + '\'' +
                '}';
    }
}
