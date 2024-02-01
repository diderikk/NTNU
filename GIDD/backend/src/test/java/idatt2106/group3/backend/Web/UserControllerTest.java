package idatt2106.group3.backend.Web;

import com.fasterxml.jackson.databind.ObjectMapper;

import idatt2106.group3.backend.Enum.Difficulty;
import idatt2106.group3.backend.Model.Activity;
import idatt2106.group3.backend.Model.User;
import idatt2106.group3.backend.Model.DTO.User.UserEditDTO;
import idatt2106.group3.backend.Model.DTO.User.UserWithPasswordDTO;
import idatt2106.group3.backend.Repository.ActivityRepository;
import idatt2106.group3.backend.Repository.UserRepository;
import idatt2106.group3.backend.Service.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup()
    {
        Activity activity1 = new Activity("Football", "Type", "Playing", "A football", Difficulty.EASY.value, "Trondheim", "Dal", 50.30, 50.50, LocalDateTime.now().plusMinutes(20), 60, false, 10, true, null);
        User user1 = new User("1Forename", "1Surname", "test1@test.com", LocalDate.of(2005, 1, 1), Difficulty.EASY, "test hash", "test salt", 4, "Organizer", 2, null);
        User user2 = new User("1Forename", "1Surname", "test2@test.com", LocalDate.of(2005, 1, 1), Difficulty.MEDIUM, "test hash", "test salt", 4, "Organizer", 2, null);
        User user3 = new User("1Forename", "1Surname", "test3@test.com", LocalDate.of(2005, 1, 1), Difficulty.HARD, "test hash", "test salt", 4, "Organizer", 2, null);
        activityRepository.save(activity1);
        // Needed for testing the editing user
        user2.setHash(passwordEncoder.encode(("test hash")));
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    @AfterEach
    public void teardown() {
        userService.removeUserFromActivity(1, 1);
        userRepository.deleteAll();
        activityRepository.deleteAll();
    }

    @Test
    public void getUser_ReturnFirstUserAndStatus_StatusOk() throws Exception
    {
        long id = userRepository.findAll().get(0).getUserId();
        this.mockMvc.perform(get("/api/v1/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forename", containsStringIgnoringCase("Forename")))
                .andExpect(jsonPath("$.surname", containsStringIgnoringCase("Surname")))
                .andExpect(jsonPath("$.email", containsStringIgnoringCase("test1@test.com")))
                .andExpect(jsonPath("$.dateOfBirth", containsStringIgnoringCase("2005-01-01")))
                .andExpect(jsonPath("$.trainingLevel", is("EASY")))
                .andExpect(jsonPath("$.role", containsStringIgnoringCase("Organizer")))               
                .andReturn();
    }

    @Test
    public void createUser_PostUserGetResponse_StatusCreated() throws Exception
    {

        UserWithPasswordDTO userPasswordDTO = new UserWithPasswordDTO("Forename", "Surname", "test@test.com", LocalDate.of(2005, 1, 1), Difficulty.MEDIUM, "test hash", "test");

        String userJson = objectMapper.writeValueAsString(userPasswordDTO);

        this.mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", isA(String.class)))
                .andExpect(jsonPath("$.user.forename", containsStringIgnoringCase("Forename")))
                .andExpect(jsonPath("$.user.surname", containsStringIgnoringCase("Surname")))
                .andExpect(jsonPath("$.user.email", containsStringIgnoringCase("test@test.com")))
                .andExpect(jsonPath("$.user.dateOfBirth", containsStringIgnoringCase("2005-01-01")))
                .andExpect(jsonPath("$.user.trainingLevel", is("MEDIUM")))
                .andReturn();
    }

    @Test
    public void editUser_UpdateUserGetResponse_StatusOk() throws Exception
    {
        UserEditDTO userEditDTO = new UserEditDTO("Forename1", "Surname1", "test1@test1.com", null, Difficulty.HARD, "test hash", "test hash", "test");
        String userJson = objectMapper.writeValueAsString(userEditDTO);

        long id = userRepository.findAll().get(1).getUserId();

        this.mockMvc.perform(get("/api/v1/users/" + id))
                .andExpect(jsonPath("$.forename", containsStringIgnoringCase("1Forename")))
                .andExpect(jsonPath("$.surname", containsStringIgnoringCase("1Surname")))
                .andExpect(jsonPath("$.email", containsStringIgnoringCase("test2@test.com")))
                .andExpect(jsonPath("$.dateOfBirth", containsStringIgnoringCase("2005-01-01")))
                .andExpect(jsonPath("$.trainingLevel", is("MEDIUM")))
                .andExpect(jsonPath("$.role", containsStringIgnoringCase("Organizer")))
                .andReturn();

        this.mockMvc.perform(post("/api/v1/users/" + id)
                .contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forename", containsStringIgnoringCase("Forename1")))
                .andExpect(jsonPath("$.surname", containsStringIgnoringCase("Surname1")))
                .andExpect(jsonPath("$.email", containsStringIgnoringCase("test1@test1.com")))
                .andExpect(jsonPath("$.dateOfBirth", containsStringIgnoringCase("2005-01-01")))
                .andExpect(jsonPath("$.trainingLevel", is("HARD")))
                .andExpect(jsonPath("$.role", containsStringIgnoringCase("Organizer")))
                .andReturn();

        this.mockMvc.perform(get("/api/v1/users/" + id))
                .andExpect(jsonPath("$.forename", containsStringIgnoringCase("Forename1")))
                .andExpect(jsonPath("$.surname", containsStringIgnoringCase("Surname1")))
                .andExpect(jsonPath("$.email", containsStringIgnoringCase("test1@test1.com")))
                .andExpect(jsonPath("$.dateOfBirth", containsStringIgnoringCase("2005-01-01")))
                .andExpect(jsonPath("$.trainingLevel", is("HARD")))
                .andExpect(jsonPath("$.role", containsStringIgnoringCase("Organizer")))
                .andReturn();
    }

    @Test
    public void deleteUser_ShouldDeleteUser_StatusOk() throws Exception
    {
        long id = userRepository.findAll().get(2).getUserId();
        this.mockMvc.perform(delete("/api/v1/users/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserActivities_getActivitiesAndRespons_StatusOk() throws Exception
    {
        long userId = userRepository.findAll().get(0).getUserId();
        long activityId = activityRepository.findAll().get(0).getActivityId();
        userService.addUserToActivity(userId, activityId);
        this.mockMvc.perform(get("/api/v1/users/" + userId + "/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title", containsStringIgnoringCase("Football")))
                .andExpect(jsonPath("$.[0].type", containsStringIgnoringCase("Type")))
                .andExpect(jsonPath("$.[0].description", containsStringIgnoringCase("Playing")))
                .andExpect(jsonPath("$.[0].equipment", containsStringIgnoringCase("A football")))
                .andExpect(jsonPath("$.[0].difficulty", is(Difficulty.EASY.value)))
                .andExpect(jsonPath("$.[0].city", containsStringIgnoringCase("Trondheim")))
                .andExpect(jsonPath("$.[0].place", containsStringIgnoringCase("Dal")))
                .andExpect(jsonPath("$.[0].longitude", is(50.30)))
                .andExpect(jsonPath("$.[0].latitude", is(50.50)))
                .andExpect(jsonPath("$.[0].durationMinutes", is(60)))
                .andExpect(jsonPath("$.[0].privateActivity", is(false)))
                .andExpect(jsonPath("$.[0].maxParticipants", is(10)))
                .andReturn();
    }

    @Test
    public void removeUserFromActivity_ShouldRemoveUserAndGetResponse_StatusOk() throws Exception
    {
        long userId = userRepository.findAll().get(0).getUserId();
        long activityId = activityRepository.findAll().get(0).getActivityId();
        userService.addUserToActivity(userId, activityId);
        this.mockMvc.perform(delete("/api/v1/users/" + userId + "/activities/" + activityId))
                .andExpect(status().isOk());
    }

    @Test
    public void getFutureActivities_ShouldReturnFutureActivities_StatusOk() throws Exception
    {
        long userId = userRepository.findAll().get(0).getUserId();
        long activityId = activityRepository.findAll().get(0).getActivityId();
        userService.addUserToActivity(userId, activityId);
        this.mockMvc.perform(get("/api/v1/users/" + userId + "/my-activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title", containsStringIgnoringCase("Football")));
    }

    @Test
    public void getOrganizedActivities_ShouldReturnOrganizedActivities_StatusOk() throws Exception
    {
        long userId = userRepository.findAll().get(0).getUserId();
        this.mockMvc.perform(get("/api/v1/users/" + userId + "/organized-activities"))
                .andExpect(status().isOk());
    }
}
