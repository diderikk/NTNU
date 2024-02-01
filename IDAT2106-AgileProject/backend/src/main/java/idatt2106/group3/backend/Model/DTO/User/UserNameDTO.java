package idatt2106.group3.backend.Model.DTO.User;

import idatt2106.group3.backend.Model.User;

/**
 * DTO class that only sends minimal information
 * about a user to frontend
 */
public class UserNameDTO {
    private long userId;
    private String forename;
    private String surname;

    public UserNameDTO(long userId, String forename, String surname) {
        this.userId = userId;
        this.forename = forename;
        this.surname = surname;
    }

    public UserNameDTO(User user) {
        this.userId = user.getUserId();
        this.forename = user.getForename();
        this.surname = user.getSurname();
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

    @Override
    public String toString() {
        return "User{" +
                ", userId='" + userId + '\'' +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
    
}
