package idatt2106.group3.backend.Model.DTO.User;

import java.time.LocalDate;

import idatt2106.group3.backend.Enum.Difficulty;

/**
 * DTO class for User POST-requests when registering a new user
 */
public class UserWithPasswordDTO extends UserSuperclassDTO{
    private String password;
    private String profilePicture;


    public UserWithPasswordDTO(String forename, String surname, String email, LocalDate dateOfBirth, Difficulty trainingLevel, String password, String profilePicture) {
        super(forename, surname, email, dateOfBirth, trainingLevel);
        this.password = password;
        this.profilePicture = profilePicture;
    }

    public UserWithPasswordDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString()
    {
        return super.toString() + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
