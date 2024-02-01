package idatt2106.group3.backend.Service;

import idatt2106.group3.backend.Model.Activity;
import idatt2106.group3.backend.Model.Chat;
import idatt2106.group3.backend.Model.User;
import idatt2106.group3.backend.Model.UserSecurityDetails;
import idatt2106.group3.backend.Enum.Difficulty;
import idatt2106.group3.backend.Model.DTO.Activity.AbsenceDTO;
import idatt2106.group3.backend.Model.DTO.Activity.ActivityDTO;
import idatt2106.group3.backend.Model.DTO.Activity.ActivityRegistrationDTO;
import idatt2106.group3.backend.Repository.ActivityRepository;
import idatt2106.group3.backend.Repository.ChatRepository;
import idatt2106.group3.backend.Repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ActivityServiceTest
{
    @InjectMocks
    private ActivityService activityService;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatRepository chatRepository;

    @BeforeEach
    public void setup() {
        Activity activity3 = new Activity("Activity3", "Type", "Description", "Equipment", Difficulty.HARD.value, "Oslo", "Oslo", 3.2, 3.2, LocalDateTime.now().plusDays(1), 100, false, 0, false, null);
        Activity activity2 = new Activity("Activity2", "Type", "Description", "Equipment", Difficulty.MEDIUM.value, "Bergen", "Bergen", 2.2, 2.2, LocalDateTime.of(2000, 1, 1, 1, 1, 1), 100, false, 10, false, null);
        Activity activity1 = new Activity("Activity1", "Type", "Description", "Equipment", Difficulty.EASY.value, "Trondheim", "Trondheim", 1.2, 1.2, LocalDateTime.of(2000, 1, 1, 1, 1, 1), 100, false, 10, false, null);
        User user = new User("Forename", "Surname", "Email", LocalDate.of(2000, 1, 1), Difficulty.MEDIUM, "Hash", "Salt", 1, "Role", 1, null);

        activity3.setActivityId(2);
        activity2.setActivityId(1);
        activity1.setActivityId(0);
        user.setUserId(0);

        activity1.setUsers(new HashSet<User>());
        activity2.setUsers(new HashSet<User>());
        activity3.setUsers(new HashSet<User>());

        activity1.setChat(new Chat());
        activity1.setOrganizer(user);

        Mockito.lenient()
        .when(activityRepository.findById(activity3.getActivityId()))
        .thenReturn(java.util.Optional.of(activity3));
        Mockito.lenient()
        .when(activityRepository.findById(activity2.getActivityId()))
        .thenReturn(java.util.Optional.of(activity2));
        Mockito.lenient()
        .when(activityRepository.findById(activity1.getActivityId()))
        .thenReturn(java.util.Optional.of(activity1));

        Mockito.lenient()
        .when(userRepository.findById(user.getUserId()))
        .thenReturn(java.util.Optional.of(user));

        Mockito.lenient()
        .when(activityRepository.existsById(activity3.getActivityId()))
        .thenReturn(false);
        Mockito.lenient()
        .when(activityRepository.existsById(activity2.getActivityId()))
        .thenReturn(true);
        Mockito.lenient()
        .when(activityRepository.existsById(activity1.getActivityId()))
        .thenReturn(true);

        List<Activity> activities = new ArrayList<>();
        activities.add(activity1);
        activities.add(activity2);
        activities.add(activity3);
        Mockito.lenient()
        .when(activityRepository.findAllFromNow())
        .thenReturn(activities);
    }

    @Test
    public void getActivity_IdExists_ActivityIsCorrect()
    {
        long activityId = 0l;
        ActivityDTO activityDTO = activityService.getActivity(activityId);
        assertThat(activityDTO.getTitle()).isEqualTo("Activity1");
        assertThat(activityDTO.getType()).isEqualTo("Type");
        assertThat(activityDTO.getDescription()).isEqualTo("Description");
        assertThat(activityDTO.getEquipment()).isEqualTo("Equipment");
        assertThat(activityDTO.getDifficulty()).isEqualTo(Difficulty.EASY.value);
        assertThat(activityDTO.getCity()).isEqualTo("Trondheim");
        assertThat(activityDTO.getPlace()).isEqualTo("Trondheim");
        assertThat(activityDTO.getLongitude()).isEqualTo(1.2);
        assertThat(activityDTO.getLatitude()).isEqualTo(1.2);
        assertThat(activityDTO.getStartTime()).isEqualTo(LocalDateTime.of(2000, 1, 1, 1, 1, 1));
        assertThat(activityDTO.getDurationMinutes()).isEqualTo(100);
        assertThat(activityDTO.isPrivateActivity()).isFalse();
        assertThat(activityDTO.getMaxParticipants()).isEqualTo(10);
        assertThat(activityDTO.getActivityPicture()).isNull();
    }

    @Test
    public void getActivity_IdDoesNotExist_ReturnNull() {
        long activityId = -1l;
        ActivityDTO activityDTO = activityService.getActivity(activityId);
        assertNull(activityDTO);
    }

    @Test
    public void getActivities_ReturnListAndStatus_ListNotNull() {
        List<ActivityDTO> activities = activityService.getActivities();
        assertNotNull(activities);

        assertThat(activities.get(0).getTitle()).isEqualTo("Activity1");
        assertThat(activities.get(0).getType()).isEqualTo("Type");
        assertThat(activities.get(0).getDescription()).isEqualTo("Description");
        assertThat(activities.get(0).getEquipment()).isEqualTo("Equipment");
        assertThat(activities.get(0).getDifficulty()).isEqualTo(Difficulty.EASY.value);
        assertThat(activities.get(0).getCity()).isEqualTo("Trondheim");
        assertThat(activities.get(0).getPlace()).isEqualTo("Trondheim");
        assertThat(activities.get(0).getLongitude()).isEqualTo(1.2);
        assertThat(activities.get(0).getLatitude()).isEqualTo(1.2);
        assertThat(activities.get(0).getStartTime()).isEqualTo(LocalDateTime.of(2000, 1, 1, 1, 1, 1));

        assertThat(activities.get(1).getTitle()).isEqualTo("Activity2");
        assertThat(activities.get(1).getType()).isEqualTo("Type");
        assertThat(activities.get(1).getDescription()).isEqualTo("Description");
        assertThat(activities.get(1).getEquipment()).isEqualTo("Equipment");
        assertThat(activities.get(1).getDifficulty()).isEqualTo(Difficulty.MEDIUM.value);
        assertThat(activities.get(1).getCity()).isEqualTo("Bergen");
        assertThat(activities.get(1).getPlace()).isEqualTo("Bergen");
        assertThat(activities.get(1).getLongitude()).isEqualTo(2.2);
        assertThat(activities.get(1).getLatitude()).isEqualTo(2.2);
        assertThat(activities.get(1).getStartTime()).isEqualTo(LocalDateTime.of(2000, 1, 1, 1, 1, 1));
    }

    @Test
    public void createActivity_ReturnActivityDTOOfCreatedActivity_ActivityIsCorrect() {
        User user = new User("Forename", "Surname", "test@test.com", LocalDate.of(2005, 1, 1), Difficulty.HARD, "test hash", "test salt", 100, "Organizer", 2, null);
        Mockito.lenient()
        .when(userRepository.findById(any()))
        .thenReturn(java.util.Optional.of(user));

        User user1 = userRepository.findById(0l).get();
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.lenient().when(authentication.getPrincipal())
        .thenReturn(new UserSecurityDetails("test hash", "test123@test.com", user1.getUserId(), null));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.lenient().when(securityContext.getAuthentication())
        .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        ActivityRegistrationDTO activityRegistrationDTO = new ActivityRegistrationDTO("Activity3", "Type", "Description", "Equipment", Difficulty.HARD.value, "Oslo", "Oslo", 3.2, 3.2, LocalDateTime.of(2000, 1, 1, 1, 1, 1), 100, false, 10, null);
        Mockito.lenient().when(activityRepository.save(any())).thenReturn(new Activity("Activity3", "Type", "Description", "Equipment", Difficulty.HARD.value, "Oslo", "Oslo", 3.2, 3.2, LocalDateTime.of(2000, 1, 1, 1, 1, 1), 100, false, 10, false, null));
        ActivityDTO activityDTO = activityService.createActivity(activityRegistrationDTO);
        assertNotNull(activityDTO);
        assertThat(activityDTO.getTitle()).isEqualTo("Activity3");
        assertThat(activityDTO.getType()).isEqualTo("Type");
        assertThat(activityDTO.getDescription()).isEqualTo("Description");
        assertThat(activityDTO.getEquipment()).isEqualTo("Equipment");
        assertThat(activityDTO.getDifficulty()).isEqualTo(Difficulty.HARD.value);
        assertThat(activityDTO.getCity()).isEqualTo("Oslo");
        assertThat(activityDTO.getPlace()).isEqualTo("Oslo");
        assertThat(activityDTO.getLongitude()).isEqualTo(3.2);
        assertThat(activityDTO.getLatitude()).isEqualTo(3.2);
        assertThat(activityDTO.getStartTime()).isEqualTo(LocalDateTime.of(2000, 1, 1, 1, 1, 1));
    }

    @Test
    public void editActivity_ReturnEditedActivity_ActivityIsCorrectlyEdited() {
        long activityId = 0l;
        ActivityRegistrationDTO activityRegDTO = new ActivityRegistrationDTO("NewTitle", "NewType", "NewDescription", "NewEquipment", Difficulty.HARD.value, "NewCity", "NewPlace", 10l, 10l, LocalDateTime.of(2021, 2, 2, 2, 2, 2), 60, false, 60, null);
        Mockito.lenient().when(activityRepository.save(any())).thenReturn(new Activity("NewTitle", "NewType", "NewDescription", "NewEquipment", Difficulty.HARD.value, "NewCity", "NewPlace", 10l, 10l, LocalDateTime.of(2021, 2, 2, 2, 2, 2), 60, false, 60, false, null));
        ActivityDTO activityDTO = activityService.editActivity(activityId, activityRegDTO);
        assertNotNull(activityDTO);
        assertThat(activityDTO.getTitle()).isEqualTo("NewTitle");
        assertThat(activityDTO.getType()).isEqualTo("NewType");
        assertThat(activityDTO.getDescription()).isEqualTo("NewDescription");
        assertThat(activityDTO.getEquipment()).isEqualTo("NewEquipment");
        assertThat(activityDTO.getDifficulty()).isEqualTo(Difficulty.HARD.value);
        assertThat(activityDTO.getCity()).isEqualTo("NewCity");
        assertThat(activityDTO.getPlace()).isEqualTo("NewPlace");
        assertThat(activityDTO.getLongitude()).isEqualTo(10l);
        assertThat(activityDTO.getLatitude()).isEqualTo(10l);
        assertThat(activityDTO.getStartTime()).isEqualTo(LocalDateTime.of(2021, 2, 2, 2, 2, 2));
        assertThat(activityDTO.getDurationMinutes()).isEqualTo(60);
        assertThat(activityDTO.isPrivateActivity()).isFalse();
        assertThat(activityDTO.getMaxParticipants()).isEqualTo(60);
        assertThat(activityDTO.getActivityPicture()).isNull();
    }

    @Test
    public void editActivity_ActivityDoesNotExist_ReturnsNull() {
        long activityId = -1l;
        ActivityRegistrationDTO activityRegDTO = new ActivityRegistrationDTO("NewTitle", "NewType", "NewDescription", "NewEquipment", Difficulty.HARD.value, "NewCity", "NewPlace", 10l, 10l, LocalDateTime.of(2021, 2, 2, 2, 2, 2), 60, false, 60, null);
        Mockito.lenient().when(activityRepository.save(any())).thenReturn(new Activity("NewTitle", "NewType", "NewDescription", "NewEquipment", Difficulty.HARD.value, "NewCity", "NewPlace", 10l, 10l, LocalDateTime.of(2021, 2, 2, 2, 2, 2), 60, false, 60, false, null));
        ActivityDTO activityDTO = activityService.editActivity(activityId, activityRegDTO);
        assertNull(activityDTO);
    }

    @Test
    public void deleteUser_ActivityDoesNotExist_ReturnsFalse() {
        long activityId = -1l;
        assertFalse(activityService.deleteActivity(activityId));
    }

    @Test
    public void deleteUser_ActivityExistsButIsFinished_ReturnsFalse() {
        long activityId = 0l;
        assertFalse(activityService.deleteActivity(activityId));
    }

    @Test
    public void deleteUser_ActivityExistsAndIsInFuture_ReturnsTrue() {
        long activityId = 2l;
        assertTrue(activityService.deleteActivity(activityId));
    }

    @Test
    public void addUserToActivity_ActivityDoesNotExist_ReturnsFalse() {
        long activityId = -1l;
        assertFalse(activityService.addUserToActivity(activityId, 1));
    }

    @Test
    public void addUserToActivity_ActivityIsFull_ReturnsFalse() {
        long activityId = 2l;
        long userId = 0l;
        assertFalse(activityService.addUserToActivity(activityId, userId));
    }

    @Test
    public void addUserToActivity_UserDoesNotExist_ReturnsFalse() {
        long activityId = 0l;
        long userId = -1l;
        assertFalse(activityService.addUserToActivity(activityId, userId));
    }

    @Test
    public void addUserToActivity_UserActivitiesIsNull_ReturnsFalse() {
        long activityId = 0l;
        long userId = 0l;
        userRepository.findById(userId).get().setActivities(null);
        assertFalse(activityService.addUserToActivity(activityId, userId));
    }

    @Test
    public void addUserToActivity_UserAddedToActivity_ReturnsTrue() {
        long activityId = 0l;
        long userId = 0l;
        userRepository.findById(userId).get().setActivities(new HashSet<Activity>());
        assertTrue(activityService.addUserToActivity(activityId, userId));
    }

    @Test
    public void getChat_ActivityDoesNotExist_ReturnsNull() {
        long activityId = -1l;
        assertNull(activityService.getChat(activityId));
    }

    @Test
    public void getChat_ActivityExists_ReturnsChat() {
        long activityId = 0l;
        assertNotNull(activityService.getChat(activityId));
        assertThat(activityService.getChat(activityId).getChatId()).isZero();
        assertThat(activityService.getChat(activityId).getActivity()).isNull();
        assertThat(activityService.getChat(activityId).getMessages()).isNull();
    }

    @Test
    public void checkIfOrganizerOfActivity_ActivityDoesNotExist_ReturnsFalse() {
        long activityId = -1l;
        long userId = 0l;
        assertFalse(activityService.checkIfOrganizerOfActivity(activityId, userId));
    }

    @Test
    public void checkIfOrganizerOfActivity_NotOrganizerOfActivity_ReturnsFalse() {
        long activityId = 0l;
        long userId = 1l;
        assertFalse(activityService.checkIfOrganizerOfActivity(activityId, userId));
    }

    @Test
    public void checkIfOrganizerOfActivity_OrganizerOfActivity_ReturnsTrue() {
        long activityId = 0l;
        long userId = 0l;
        assertTrue(activityService.checkIfOrganizerOfActivity(activityId, userId));
    }

    @Test
    public void checkIfParticipant_NotParticipantOfActivity_ReturnsFalse() {
        long activityId = 0l;
        long userId = 1l;

        Mockito.lenient().when(activityRepository.findIfUserIsParticipantOfActivity(1l, 0l)).thenReturn(Optional.of(0));
        Mockito.lenient().when(activityRepository.findIfUserIsParticipantOfActivity(0l, 0l)).thenReturn(Optional.of(0));

        assertFalse(activityService.checkIfOrganizerOfActivity(activityId, userId));
    }

    @Test
    public void checkIfParticipant_ParticipantOfActivity_ReturnsTrue() {
        long activityId = 0l;
        long userId = 0l;

        Mockito.lenient().when(activityRepository.findIfUserIsParticipantOfActivity(1l, 0l)).thenReturn(Optional.of(0));
        Mockito.lenient().when(activityRepository.findIfUserIsParticipantOfActivity(0l, 0l)).thenReturn(Optional.of(0));

        assertTrue(activityService.checkIfOrganizerOfActivity(activityId, userId));
    }

    @Test
    public void markAbsent_ActivityDoesNotExist_ReturnsEmptySet() {
        long activityId = -1l;
        assertThat(activityService.markAbsent(activityId, new AbsenceDTO())).isEmpty();
    }

    @Test
    public void markAbsent_ActivityExists_ReturnsUserIdsOfAbsentUsers() {
        long activityId = 0l;
        Set<Long> ids = new HashSet<>();
        ids.add(0l);
        assertThat(activityService.markAbsent(activityId, new AbsenceDTO(ids))).isNotEmpty();
        assertThat(activityService.markAbsent(activityId, new AbsenceDTO(ids)).size()).isEqualTo(1);
    }

    @Test
    public void getUsers_ActivityDoesNotExist_ReturnsEmptySet() {
        long activityId = -1l;
        assertThat(activityService.getUsers(activityId)).isEmpty();
    }

    @Test
    public void getUsers_ActivityExists_ReturnsSetOfUserNameDTOs() {
        long activityId = 0l;
        activityRepository.findById(activityId).get().getUsers().add(new User());
        assertThat(activityService.getUsers(activityId)).isNotEmpty();
    }
}
