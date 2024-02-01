package idatt2106.group3.backend.Service;

import idatt2106.group3.backend.Component.EmailComponent;
import idatt2106.group3.backend.Enum.SortingType;
import idatt2106.group3.backend.Model.Activity;
import idatt2106.group3.backend.Model.Chat;
import idatt2106.group3.backend.Model.Message;
import idatt2106.group3.backend.Model.User;
import idatt2106.group3.backend.Model.UserSecurityDetails;
import idatt2106.group3.backend.Model.DTO.SortFilterQueryDTO;
import idatt2106.group3.backend.Model.DTO.Activity.AbsenceDTO;
import idatt2106.group3.backend.Model.DTO.Activity.ActivityDTO;
import idatt2106.group3.backend.Model.DTO.Activity.ActivityRegistrationDTO;
import idatt2106.group3.backend.Model.DTO.User.UserNameDTO;
import idatt2106.group3.backend.Repository.ActivityRepository;
import idatt2106.group3.backend.Repository.ChatRepository;
import idatt2106.group3.backend.Repository.MessageRepository;
import idatt2106.group3.backend.Repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivityService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired(required = false)
    private EmailComponent emailSender;

    /**
     * Returns activity based on activityId stored in the database
     * @param activityId
     * @return ActivityDTO object
     */
    public ActivityDTO getActivity(long activityId)
    {
        LOGGER.info("getActivity(long activityId) called with activityId: {}", activityId); 
        Optional<Activity> activity = activityRepository.findById(activityId);
        if(activity.isPresent()) return new ActivityDTO(activity.get());
        return null;
    }

    /**
     * Returns all activities stored in the database
     * @return List of activities
     */
    public List<ActivityDTO> getActivities()
    {
        LOGGER.info("getActivities() called");
        return activityRepository.findAllFromNow().stream().map(activity -> new ActivityDTO(activity)).collect(Collectors.toList());
    }

    /**
     * Returns a sorted or filtered list based on filters defined by the user
     * @param filter
     * @return List of ActivityDTO objects
     */
    public List<ActivityDTO> getActivitiesWithFilterAndSorting(SortFilterQueryDTO filter){
        LOGGER.info("getActivitiesWithFilterAndSorting(SortFilterQueryDTO) called with amount: {} sortQuery: {}",filter.getAmount(), filter.getSearchQuery());
        String searchQuery = "%" + filter.getSearchQuery() + "%";
        List<Activity> list;
        list = sortAndFilter(filter, searchQuery);
        return list.stream().map(activity -> new ActivityDTO(activity)).collect(Collectors.toList());
    }

    /**
     * Finds the user who created the activity, and sets it as the organizer
     * Saves the Activity object in the database
     * @param activity
     * @return ActivityDTO object
     */
    public ActivityDTO createActivity(ActivityRegistrationDTO activity)
    {
        LOGGER.info("createActivity(Activity activity) called. Activity Title: {}", activity.getTitle());
        // To get currently logged in user
        UserSecurityDetails creatorUser = (UserSecurityDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Finds this user
        Optional<User> optionalUser = userRepository.findById(creatorUser.getUserId());
        Chat chat = new Chat();
        chat = chatRepository.save(chat);
        if(!optionalUser.isPresent()) return null;
        Activity createdActivity = new Activity(activity, optionalUser.get());
        createdActivity.setChat(chat);
        createdActivity = activityRepository.save(createdActivity);
        // Saves User with organizer, and returns a ActivityDTO object
        return new ActivityDTO(createdActivity);
    }

    /**
     * Finds the activity based on id and changes all given fields based on given activityRegistrationDTO
     * @param activityId
     * @param activityRegDTO input DTO object
     * @return ActivityDTO
     */
    public ActivityDTO editActivity(long activityId, ActivityRegistrationDTO activityRegDTO)
    {
        LOGGER.info("editActivity(long activityId, Activity activity) called with activityId: {}", activityId);
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        if(optionalActivity.isPresent()){
            Activity activity = optionalActivity.get();
            activity.setTitle(activityRegDTO.getTitle());
            activity.setType(activityRegDTO.getType());
            activity.setDescription(activityRegDTO.getDescription());
            activity.setEquipment(activityRegDTO.getEquipment());
            activity.setDifficulty(activityRegDTO.getDifficulty());
            activity.setCity(activityRegDTO.getCity());
            activity.setPlace(activityRegDTO.getPlace());
            activity.setLongitude(activityRegDTO.getLongitude());
            activity.setLatitude(activityRegDTO.getLatitude());
            activity.setStartTime(activityRegDTO.getStartTime());
            activity.setDurationMinutes(activityRegDTO.getDurationMinutes());
            activity.setPrivateActivity(activityRegDTO.isPrivateActivity());
            activity.setMaxParticipants(activityRegDTO.getMaxParticipants());
            if(activityRegDTO.getActivityPicture() != null)activity.setActivityPicture(activityRegDTO.getActivityPicture().getBytes());
            activity.getUsers().stream().forEach(user -> emailSender.sendEditMail(user, activity));
            return new ActivityDTO(activityRepository.save(activity));
        }
        return null;
    }

    /**
     * Finds activity based on id, removes it, sends cancellation emails
     * to all participants, and deletes the activity from the database
     * @param activityId
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteActivity(long activityId)
    {
        LOGGER.info("deleteActivity(long activityId) called with activityId: {}", activityId);
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        if(optionalActivity.isPresent()){
            Activity activity = optionalActivity.get();

            // Should not be able to delete activity after it is finished
            if(activity.getStartTime().plusMinutes(activity.getDurationMinutes()).isBefore(LocalDateTime.now())) {
                return false;
            }

            List<User> activityUsers = new ArrayList<>();
            for(User user : activity.getUsers()){
                user.getActivities().remove(activity);
                activityUsers.add(user);
                emailSender.sendCancelationMail(user, activity);
            }
            userRepository.saveAll(activityUsers);
            activityRepository.save(activity);
            activityRepository.deleteById(activityId);
            if(activity.getChat() != null){
                Set<Message> messages = activity.getChat().getMessages();
                messageRepository.deleteAll(messages);
                chatRepository.delete(activity.getChat());
            }
            return !activityRepository.existsById(activityId);
        }
        return false;
    }

    /**
     * Adds a user to an activity, user becomes a participant
     * Checks if both are stored in database
     * @param activityId
     * @param userId
     * @return true if user was added successfully
     */
    public boolean addUserToActivity(long activityId, long userId)
    {
        LOGGER.info("addUserToActivity(long activityId) called with activityId: {}, and userId: {}", activityId, userId);

        Optional<Activity> activityOptional = activityRepository.findById(activityId);
        if(!activityOptional.isPresent()) {
            LOGGER.warn("Did not find activity with activityId: {}. Returning false", activityId);
            return false;
        }

        if(activityOptional.get().getUsers().size() >= activityOptional.get().getMaxParticipants()){
            LOGGER.warn("This acitivity is already full: {}. Returning false", activityId);
            return false;
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()) {
            LOGGER.warn("Did not find user with userId: {}. Returning false", userId);
            return false;
        }
        User user = userOptional.get();

        Set<Activity> activities = user.getActivities();
        if (activities == null) {
            return false;
        }
        Activity activity = activityOptional.get();
        user.getActivities().add(activity);

        userRepository.save(user);
        return true;
    }

    /**
     * Returns activity's Chat object
     * @param activityId
     * @return Chat entity object
     */
    public Chat getChat(long activityId){
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        if(optionalActivity.isPresent()){
            return optionalActivity.get().getChat();
        }
        return null;
    }

    /**
     * Checks if a user is the organizer of the activity
     * @param activityId
     * @param userId
     * @return true if user is the orhanizer, false otherwise
     */
    public boolean checkIfOrganizerOfActivity(long activityId, long userId){
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        if(optionalActivity.isPresent()){
            return optionalActivity.get().getOrganizer().getUserId() == userId;
        }
        return false;
    }

    /**
     * Checks if the user is a participant of an existing activity based on activityId
     * @param activityId
     * @param userId
     * @return true if participant, false otherwise
     */
    public boolean checkIfParticipant(long activityId, long userId){
        Optional<Integer> optionalActivityId = activityRepository.findIfUserIsParticipantOfActivity(userId, activityId); 
        return optionalActivityId.isPresent();
    }

    /**
     * Method that increases "absence" counter for users that were absent in an activity.
     * Also sets activity's markedAbsence boolean to true, so that we know that the activity
     * already has marked user absences.
     * @param activityId
     * @param absenceDTO
     * @return a set of UserIds of users that were absent. If it fails, returns an empty HashSet.
     */
    public Set<Long> markAbsent(long activityId, AbsenceDTO absenceDTO) {
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        if(optionalActivity.isPresent()){
            Set<Long> absentUsersId = absenceDTO.getAbsentUsersId();
            List<User> users = userRepository.findAllById(absentUsersId);
            for(User user : users) {
                user.setAbsence(user.getAbsence() + 1);
            }
            Activity activity = optionalActivity.get();
            activity.setMarkedAbsence(true);

            return absentUsersId;
        }
        return new HashSet<>();
    }

    /**
     * Returns names of all participants of an existing activity based on activityId
     * @param activityId
     * @return Set of UserNameDTO objects
     */
    public Set<UserNameDTO> getUsers(long activityId)
    {
        LOGGER.info("getUsers(long activityId) called with activityId: {}", activityId); 
        Optional<Activity> activityOptional = activityRepository.findById(activityId);
        if(activityOptional.isPresent()) {
            Activity activity = activityOptional.get();
            Set<User> users = activity.getUsers();
            Set<UserNameDTO> userNameDTOs = new HashSet<>();
            for(User user : users) {
                userNameDTOs.add(new UserNameDTO(user));
            }
            return userNameDTOs;
        }
        return new HashSet<>();
    }

    /**
     * Returns a list of activities from repository depending on sort/filter DTO from frontend
     * @param filter SortFilterQueryDTO input
     * @param searchQuery title and description search substring
     * @return List of filtered/sorted activities
     */
    private List<Activity> sortAndFilter(SortFilterQueryDTO filter, String searchQuery){
        
        if(filter.getDifficulty() == null && filter.getSortingType() == SortingType.DATE) 
            return activityRepository.findActivitiesOnDateWithoutFilter(searchQuery, filter.getAmount());
        else if(filter.getDifficulty() != null && filter.getSortingType() == SortingType.DATE) 
            return activityRepository.findActivitiesOnDateWithFilter(searchQuery, filter.getAmount(), filter.getDifficulty());
        else if(filter.getDifficulty() == null && filter.getSortingType() == SortingType.PARTICIPANT_AMOUNT) 
            return activityRepository.findActivitiesOnAmountWithoutFilter(searchQuery, filter.getAmount());
        else if(filter.getDifficulty() != null && filter.getSortingType() == SortingType.PARTICIPANT_AMOUNT) 
            return activityRepository.findActivitiesOnAmountWithFilter(searchQuery, filter.getAmount(), filter.getDifficulty());
        else if(filter.getDifficulty() == null && filter.getSortingType() == SortingType.DISTANCE)
            return activityRepository.findActivitiesOnDistanceWithoutFilter(searchQuery, filter.getAmount(), filter.getUserLongitude(), filter.getUserLatitude());
        else if(filter.getDifficulty() != null && filter.getSortingType() == SortingType.DISTANCE)
            return activityRepository.findActivitiesOnDistanceWithFilter(searchQuery, filter.getAmount(), filter.getUserLongitude(), filter.getUserLatitude(), filter.getDifficulty());
        else if(filter.getDifficulty() == null && filter.getSortingType() == SortingType.NONE)
            return activityRepository.findActivitiesOnNoneWithoutFilter(searchQuery, filter.getAmount());
        else if(filter.getDifficulty() != null && filter.getSortingType() == SortingType.NONE)
            return activityRepository.findActivitiesOnNoneWithFilter(searchQuery,filter.getAmount(),filter.getDifficulty());

        return activityRepository.findAll();
    }
}
