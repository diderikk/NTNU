package idatt2106.group3.backend.Web;

import com.fasterxml.jackson.databind.ObjectMapper;

import idatt2106.group3.backend.Enum.Difficulty;
import idatt2106.group3.backend.Model.Activity;
import idatt2106.group3.backend.Model.User;
import idatt2106.group3.backend.Model.UserSecurityDetails;
import idatt2106.group3.backend.Model.DTO.Activity.AbsenceDTO;
import idatt2106.group3.backend.Repository.ActivityRepository;
import idatt2106.group3.backend.Repository.UserRepository;
import idatt2106.group3.backend.Service.ActivityService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ActivityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup(){
        User user1 = new User("Forename", "Surname", "test@test.com", LocalDate.of(2005, 1, 1), Difficulty.HARD, "test hash", "test salt", 100, "Organizer", 2, null);
        user1 = userRepository.save(user1);
        Activity activity = new Activity("Playing", "Type", "Football", "A football", Difficulty.EASY.value, "Trondheim", "Dal", 50.30, 50.50, LocalDateTime.now(), 60, false, 10, true, null);
        Activity activity1 = new Activity("Playing", "Type", "Football", "A football", Difficulty.EASY.value, "Trondheim", "Dal", 50.30, 50.50, LocalDateTime.now(), 60, false, 10, true, null);
        Activity activity2 = new Activity("Playing", "Type", "Football", "A football", Difficulty.EASY.value, "Trondheim", "Dal", 50.30, 50.50, LocalDateTime.now(), 60, false, 10, true, null);
        activity2.setOrganizer(user1);
        activity1.setOrganizer(user1);
        activity.setOrganizer(user1);
        activityRepository.save(activity2);
        activityRepository.save(activity1);
        activityRepository.save(activity);
        
    }

    @AfterEach
    public void teardown() {
        activityRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getActivity_ReturnFirstActivityAndStatus_StatusOk() throws Exception {
        long id = activityRepository.findAll().get(0).getActivityId();
        this.mockMvc.perform(get("/api/v1/activities/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", containsStringIgnoringCase("Playing")))
                .andExpect(jsonPath("$.type", containsStringIgnoringCase("Type")))
                .andExpect(jsonPath("$.description", containsStringIgnoringCase("Football")))
                .andExpect(jsonPath("$.equipment", containsStringIgnoringCase("A football")))
                .andExpect(jsonPath("$.difficulty", is(Difficulty.EASY.value)))
                .andExpect(jsonPath("$.city", containsStringIgnoringCase("Trondheim")))
                .andExpect(jsonPath("$.place", containsStringIgnoringCase("Dal")))
                .andExpect(jsonPath("$.longitude", is(50.30)))
                .andExpect(jsonPath("$.latitude", is(50.50)))
                //.andExpect(jsonPath("$.startTime", is(null)))
                .andExpect(jsonPath("$.durationMinutes", is(60)))
                .andExpect(jsonPath("$.privateActivity", is(false)))
                .andExpect(jsonPath("$.maxParticipants", is(10)))
                .andReturn();
    }

    @Test
    public void getActivities_ReturnListAndStatus_StatusOk() throws Exception {
        this.mockMvc.perform(get("/api/v1/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))))
                .andReturn();
    }

    @Test
    public void createActivity_PostActivity_StatusCreated() throws Exception
    {
        User user1 = new User("Forename", "Surname", "test123@test.com", LocalDate.of(2005, 1, 1), Difficulty.MEDIUM, "test hash", "test salt", 100, "Organizer", 2, null);
        user1 = userRepository.save(user1);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.lenient().when(authentication.getPrincipal())
        .thenReturn(new UserSecurityDetails("test hash", "test123@test.com", user1.getUserId(), null));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.lenient().when(securityContext.getAuthentication())
        .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Activity activity = new Activity("Playing", "Type", "Football", "A football", Difficulty.EASY.value, "Trondheim", "Dal", 50.30, 50.50, LocalDateTime.now(), 60, false, 10, true, null);
        String activityJson = objectMapper.writeValueAsString(activity);

        this.mockMvc.perform(post("/api/v1/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", containsStringIgnoringCase("Playing")))
                .andExpect(jsonPath("$.type", containsStringIgnoringCase("Type")))
                .andExpect(jsonPath("$.description", containsStringIgnoringCase("Football")))
                .andExpect(jsonPath("$.equipment", containsStringIgnoringCase("A football")))
                .andExpect(jsonPath("$.difficulty", is(Difficulty.EASY.value)))
                .andExpect(jsonPath("$.city", containsStringIgnoringCase("Trondheim")))
                .andExpect(jsonPath("$.place", containsStringIgnoringCase("Dal")))
                .andExpect(jsonPath("$.longitude", is(50.30)))
                .andExpect(jsonPath("$.latitude", is(50.50)))
                .andExpect(jsonPath("$.durationMinutes", is(60)))
                .andExpect(jsonPath("$.privateActivity", is(false)))
                .andExpect(jsonPath("$.maxParticipants", is(10)));

        activityRepository.deleteAll();
    }

    @Test
    public void editActivity_UpdateActivity_StatusOk() throws Exception
    {
        Activity activity = new Activity("Title", "Type", "Football and games1", "Two footballs", Difficulty.EASY.value, "Trondheim", "Dal", 50.30, 50.50, null, 60, false, 10, true, null);
        String activityJson = objectMapper.writeValueAsString(activity);

        long id = activityRepository.findAll().get(2).getActivityId();
        this.mockMvc.perform(put("/api/v1/activities/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", containsStringIgnoringCase("title")))
                .andExpect(jsonPath("$.type", containsStringIgnoringCase("Type")))
                .andExpect(jsonPath("$.description", containsStringIgnoringCase("Football and games1")))
                .andExpect(jsonPath("$.equipment", containsStringIgnoringCase("Two footballs")))
                .andExpect(jsonPath("$.difficulty", is(Difficulty.EASY.value)))
                .andExpect(jsonPath("$.city", containsStringIgnoringCase("Trondheim")))
                .andExpect(jsonPath("$.place", containsStringIgnoringCase("Dal")))
                .andExpect(jsonPath("$.longitude", is(50.30)))
                .andExpect(jsonPath("$.latitude", is(50.50)))
                .andExpect(jsonPath("$.durationMinutes", is(60)))
                .andExpect(jsonPath("$.privateActivity", is(false)))
                .andExpect(jsonPath("$.maxParticipants", is(10)))
                .andReturn();

        this.mockMvc.perform(get("/api/v1/activities/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", containsStringIgnoringCase("title")))
                .andExpect(jsonPath("$.type", containsStringIgnoringCase("Type")))
                .andExpect(jsonPath("$.description", containsStringIgnoringCase("Football and games1")))
                .andExpect(jsonPath("$.equipment", containsStringIgnoringCase("Two footballs")))
                .andExpect(jsonPath("$.difficulty", is(Difficulty.EASY.value)))
                .andExpect(jsonPath("$.city", containsStringIgnoringCase("Trondheim")))
                .andExpect(jsonPath("$.place", containsStringIgnoringCase("Dal")))
                .andExpect(jsonPath("$.longitude", is(50.30)))
                .andExpect(jsonPath("$.latitude", is(50.50)))
                .andExpect(jsonPath("$.durationMinutes", is(60)))
                .andExpect(jsonPath("$.privateActivity", is(false)))
                .andExpect(jsonPath("$.maxParticipants", is(10)))
                .andReturn();
    }

    @Test
    public void deleteActivity_ShouldDeleteActivity_StatusOk() throws Exception //Brude fungere, men får ikke testet da ingen entitet med id 0 eksisterer
    {
        long id = activityRepository.findAll().get(1).getActivityId();
        this.mockMvc.perform(delete("/api/v1/activities/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void addUserToActivity_ExistingUserAdded_StatusCreated() throws Exception
    {
        long userId = userRepository.findAll().get(0).getUserId();
        long activityId = activityRepository.findAll().get(0).getActivityId();
        this.mockMvc.perform(post("/api/v1/activities/" + activityId + "/users/" + userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.forename", containsStringIgnoringCase("Forename")))
                .andExpect(jsonPath("$.surname", containsStringIgnoringCase("Surname")))
                .andExpect(jsonPath("$.email", containsStringIgnoringCase("test@test.com")))
                .andExpect(jsonPath("$.dateOfBirth", containsStringIgnoringCase("2005-01-01")))
                .andExpect(jsonPath("$.trainingLevel", is("HARD")))
                .andExpect(jsonPath("$.role", containsStringIgnoringCase("Organizer")));
        activityService.deleteActivity(activityId);
    }

    @Test
    public void markAbsent_ShouldMarkAbsentUsers_StatusOk() throws Exception {
        long id = activityRepository.findAll().get(0).getActivityId();

        Set<Long> absentUsersId = new HashSet<>();
        absentUsersId.add(id);
        AbsenceDTO absenceDTO = new AbsenceDTO(absentUsersId);
        String absenceJson = objectMapper.writeValueAsString(absenceDTO);
        this.mockMvc.perform(post("/api/v1/activities/" + id + "/absences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(absenceJson))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void isOrganizerOfActivity_ShouldCheckIfOrganizerOfActivity_StatusOk() throws Exception {
        Activity activity = activityRepository.findAll().get(0);

        long activityId = activity.getActivityId();
        long organizerId = activity.getOrganizer().getUserId();

        this.mockMvc.perform(get("/api/v1/activities/" + activityId + "/users/" + organizerId))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getUsers_ShouldReturnAllUsersRegisteredInActivity_StatusOk() throws Exception {
        long activityId = activityRepository.findAll().get(0).getActivityId();
        this.mockMvc.perform(get("/api/v1/activities/" + activityId + "/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))))
                .andReturn();
    }
}