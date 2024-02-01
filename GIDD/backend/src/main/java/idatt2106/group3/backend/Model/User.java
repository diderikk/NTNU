package idatt2106.group3.backend.Model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import idatt2106.group3.backend.Enum.Difficulty;

/**
 * Entity class for storing information about a user
 * with userId as primary key
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String forename;
    private String surname;
    @Column(unique = true)
    private String email;
    private LocalDate dateOfBirth;
    private String hash;
    private String salt;
    private int rating;
    private String role;
    private int absence;
    private Difficulty trainingLevel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_Activity",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "activityId")
    )
    Set<Activity> activities;

    @OneToMany(mappedBy = "organizer")
    Set<Activity> organizedActivities;

    @OneToMany(mappedBy = "reportWriter", targetEntity = Report.class)
    Set<Report> reportsSent;

    @OneToMany(mappedBy = "reportedUser", targetEntity = Report.class)
    Set<Report> reportsReceived;

    @OneToMany(mappedBy = "user", targetEntity = Message.class)
    Set<Message> messages;

    @Lob
    private byte[] profilePicture;

    public User(String forename, String surname, String email, LocalDate dateOfBirth, Difficulty trainingLevel, String hash, String salt, int rating, String role, int absence, Set<Activity> activities, Set<Activity> organizedActivities, Set<Report> reportsSent, Set<Report> reportsReceived, Set<Message> messages, byte[] profilePicture) {
        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.trainingLevel = trainingLevel;
        this.hash = hash;
        this.salt = salt;
        this.rating = rating;
        this.role = role;
        this.absence = absence;
        this.activities = activities;
        this.organizedActivities = organizedActivities;
        this.reportsSent = reportsSent;
        this.reportsReceived = reportsReceived;
        this.messages = messages;
        this.profilePicture = profilePicture;
    }

    public User(String forename, String surname, String email, LocalDate dateOfBirth, Difficulty trainingLevel, String hash, String salt, int rating, String role, int absence, byte[] profilePicture) {
        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.trainingLevel = trainingLevel;
        this.hash = hash;
        this.salt = salt;
        this.rating = rating;
        this.role = role;
        this.absence = absence;
        this.profilePicture = profilePicture;
    }

    public User() {
    }

    public Set<Report> getReportsSent() {
        return reportsSent;
    }

    public void setReportsSent(Set<Report> reportsSent) {
        this.reportsSent = reportsSent;
    }

    public Set<Report> getReportsReceived() {
        return reportsReceived;
    }

    public void setReportsReceived(Set<Report> reportsReceived) {
        this.reportsReceived = reportsReceived;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public Set<Activity> getOrganizedActivities() {
        return organizedActivities;
    }

    public void setOrganizedActivities(Set<Activity> organizedActivities) {
        this.organizedActivities = organizedActivities;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAbsence() {
        return absence;
    }

    public void setAbsence(int absence) {
        this.absence = absence;
    }

    public Difficulty getTrainingLevel() {
        return trainingLevel;
    }

    public void setTrainingLevel(Difficulty trainingLevel) {
        this.trainingLevel = trainingLevel;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "userId=" + userId +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", hash='" + hash + '\'' +
                ", salt='" + salt + '\'' +
                ", rating=" + rating + '\'' +
                ", role='" + role + '\'' +
                ", absence=" + absence + '\'' +
                ", trainingLevel=" + trainingLevel + '\'' +
                ", activities=" + activities + '\'' +
                ", organizedActivities=" + organizedActivities + '\'' +
                ", reportsSent=" + reportsSent + '\'' +
                ", reportsReceived=" + reportsReceived + '\'' +
                ", messages=" + messages + '\'' +
                '}';
    }
}
