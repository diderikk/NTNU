package idatt2106.group3.backend.Model.DTO.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import idatt2106.group3.backend.Enum.Difficulty;
import idatt2106.group3.backend.Model.Activity;
import idatt2106.group3.backend.Model.User;

/**
 * DTO class for sending User information to frontend
 */
public class UserDTO extends UserSuperclassDTO {
    private long userId;
    private String role;
    private String profilePicture;
    private boolean trusted;


    public UserDTO(long userId, String forename, String surname, String email, LocalDate dateOfBirth, Difficulty trainingLevel, String role, String profilePicture, boolean trusted) {
        super(forename, surname, email, dateOfBirth, trainingLevel);
        this.userId = userId;
        this.role = role;
        this.profilePicture = profilePicture;
        this.trusted = trusted;
    }

    public UserDTO() {
        super();
    }

    public UserDTO(User user){
        super(user.getForename(), user.getSurname(), user.getEmail(), user.getDateOfBirth(), user.getTrainingLevel());
        this.userId = user.getUserId();
        this.role = user.getRole();
        if(user.getProfilePicture()!= null)this.profilePicture = new String(user.getProfilePicture(), StandardCharsets.UTF_8);
        this.trusted = isTrusted(user);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    /**
     * Checks if user is verified to have "Trusted"-badge
     * @param user
     * @return true if user is trusted, false otherwise
     */
    private boolean isTrusted(User user) {
        if(user == null) return false;

        int absence = user.getAbsence();
        Set<Activity> allActivities = user.getActivities();

        if(allActivities == null) return false;

        // Filter out activities which already happened using start time of the activities + duration
        List<Activity> activities = allActivities.stream().filter(act -> act.getStartTime().plusMinutes(act.getDurationMinutes()).isBefore(LocalDateTime.now())).collect(Collectors.toList());
        int numOfActivities = activities.size();

        // User is trusted if user has participated in min. 3 activities,
        // and has shown up to more than 75% of all activities user has registered
        if((numOfActivities - absence) >= 3 && (numOfActivities * 0.25) >= absence) {
            return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return super.toString() + '\'' +
                ", userId='" + userId + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
