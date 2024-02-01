package idatt2106.group3.backend.Web;

import idatt2106.group3.backend.Model.Chat;
import idatt2106.group3.backend.Model.DTO.SortFilterQueryDTO;
import idatt2106.group3.backend.Model.DTO.Activity.AbsenceDTO;
import idatt2106.group3.backend.Model.DTO.Activity.ActivityDTO;
import idatt2106.group3.backend.Model.DTO.Activity.ActivityRegistrationDTO;
import idatt2106.group3.backend.Model.DTO.User.UserDTO;
import idatt2106.group3.backend.Model.DTO.User.UserNameDTO;
import idatt2106.group3.backend.Service.ActivityService;
import idatt2106.group3.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/activities")
public class ActivityController
{
    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @GetMapping("/{activity_id}")
    public ResponseEntity<ActivityDTO> getActivity(@PathVariable("activity_id") long activityId) {
        ActivityDTO returnActivity = activityService.getActivity(activityId);
        if (returnActivity == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(returnActivity, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ActivityDTO>> getActivities() {
        List<ActivityDTO> activities = activityService.getActivities();
        if (activities == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @PostMapping("/alternatives")
    public ResponseEntity<List<ActivityDTO>> getActivitiesWithFilterAndSorting(@RequestBody SortFilterQueryDTO filter){
        return new ResponseEntity<>(activityService.getActivitiesWithFilterAndSorting(filter),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ActivityDTO> createActivity(@RequestBody ActivityRegistrationDTO activity) {
        ActivityDTO returnActivity = activityService.createActivity(activity);
        if (returnActivity != null) {
            return new ResponseEntity<>(returnActivity, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{activity_id}")
    @PreAuthorize("@activityService.checkIfOrganizerOfActivity(#activityId, principal.userId)")
    public ResponseEntity<ActivityDTO> editActivity(@PathVariable("activity_id") long activityId, @RequestBody ActivityRegistrationDTO activityRegDTO) {
        ActivityDTO returnActivity = activityService.editActivity(activityId, activityRegDTO);
        
        if (returnActivity == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(returnActivity,HttpStatus.OK);
    }

    @DeleteMapping("/{activity_id}")
    @PreAuthorize("@activityService.checkIfOrganizerOfActivity(#activityId, principal.userId)")
    public ResponseEntity<String> deleteActivity(@PathVariable("activity_id") long activityId) {
        if (activityService.deleteActivity(activityId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/{activity_id}/users/{user_id}")
    @PreAuthorize("#userId == principal.userId or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> addUserToActivity(@PathVariable("activity_id") long activityId, @PathVariable("user_id") long userId) {
        if (activityService.addUserToActivity(activityId, userId)) {
            return new ResponseEntity<>(userService.getUser(userId),HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{activity_id}/organizer/{user_id}")
    public ResponseEntity<Boolean> isOrganizerOfActivity(@PathVariable("activity_id") long activityId, @PathVariable("user_id") long userId) {
        ActivityDTO activityDTO = activityService.getActivity(activityId);
        UserDTO userDTO = userService.getUser(userId);

        if(activityDTO == null || userDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (activityDTO.getOrganizerId() == userDTO.getUserId()) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @GetMapping("/{activity_id}/users/{user_id}")
    public ResponseEntity<Boolean> isUserParticipantOfActivity(@PathVariable("activity_id") long activityId, @PathVariable("user_id") long userId){
        return new ResponseEntity<>(activityService.checkIfParticipant(activityId, userId), HttpStatus.OK);
    }

    @GetMapping("/{activity_id}/users")
    public ResponseEntity<List<UserNameDTO>> getUsersOfActivity(@PathVariable("activity_id") long activityId) {
        Set<UserNameDTO> userNameDTOs = activityService.getUsers(activityId);
        if (userNameDTOs != null) {
            return new ResponseEntity<>(new ArrayList<>(userNameDTOs), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{activity_id}/chat")
    public ResponseEntity<Chat> getActivityChat(@PathVariable("activity_id") long activityId) {
        Chat returnChat = activityService.getChat(activityId);
        if (returnChat == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(returnChat, HttpStatus.OK);
    }

    @PostMapping("/{activity_id}/absences")
    @PreAuthorize("@activityService.checkIfOrganizerOfActivity(#activityId, principal.userId)")
    public ResponseEntity<Set<Long>> editAbsence(@PathVariable("activity_id") long activityId, @RequestBody AbsenceDTO absenceDTO) {
        Set<Long> absentUsersId = activityService.markAbsent(activityId, absenceDTO);
        if(absentUsersId == null || absentUsersId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(absentUsersId, HttpStatus.OK);
    }
}
