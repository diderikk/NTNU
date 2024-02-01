package idatt2106.group3.backend.Web;

import idatt2106.group3.backend.Model.Activity;
import idatt2106.group3.backend.Model.DTO.User.UserDTO;
import idatt2106.group3.backend.Model.DTO.User.UserEditDTO;
import idatt2106.group3.backend.Model.DTO.User.UserRegistrationCallbackDTO;
import idatt2106.group3.backend.Model.DTO.User.UserWithPasswordDTO;
import idatt2106.group3.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/users")
public class UserController
{
    @Autowired
    private UserService userService;

    @GetMapping("/{user_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable("user_id") long userId) {
        UserDTO returnUser = userService.getUser(userId);
        if (returnUser == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(returnUser, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserRegistrationCallbackDTO> createUser(@RequestBody UserWithPasswordDTO user) {
        if(userService.doesEmailAlreadyExist(user.getEmail())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        
        UserRegistrationCallbackDTO createdUser = userService.createUser(user);
        if (createdUser != null)
        {
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/{user_id}")
    @PreAuthorize("#userId == principal.userId or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> editUser(@PathVariable("user_id") long userId, @RequestBody UserEditDTO userEditDTO) {
        if(userEditDTO.getOldPassword() != null && userEditDTO.getNewPassword() != null
        && !userService.isOldPasswordCorrect(userEditDTO.getOldPassword(), userId)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        UserDTO returnUser = userService.editUser(userId, userEditDTO);
        if (returnUser == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(returnUser, HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}")
    @PreAuthorize("#userId == principal.userId or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("user_id") long userId) {
        if (userService.deleteUser(userId))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{user_id}/activities")
    @PreAuthorize("#userId == principal.userId or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Set<Activity>> getUserActivities(@PathVariable("user_id") long userId) {
        Set<Activity> activities = userService.getUserActivities(userId);
        if (activities == null) //Om vi ikke får et faktisk objekt tilbake fra Service, antar feil har skjedd
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}/activities/{activity_id}")
    @PreAuthorize("#userId == principal.userId or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> removeUserFromActivity(@PathVariable("user_id") long userId, @PathVariable("activity_id") long activityId) {
        if (userService.removeUserFromActivity(userId, activityId))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{user_id}/my-activities")
    @PreAuthorize("#userId == principal.userId or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Activity>> getFutureActivities(@PathVariable("user_id") Long userId){
        return new ResponseEntity<> (userService.findFutureActivities(userId), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/organized-activities")
    @PreAuthorize("#userId == principal.userId or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Activity>> getOrganizedActivities(@PathVariable("user_id") Long userId){
        return new ResponseEntity<> (userService.findOrganizedActivities(userId), HttpStatus.OK);
    }
}
