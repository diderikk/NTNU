package idatt2106.group3.backend.Component;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import idatt2106.group3.backend.Enum.Difficulty;
import idatt2106.group3.backend.Model.Activity;
import idatt2106.group3.backend.Model.Chat;
import idatt2106.group3.backend.Model.DTO.Activity.ActivityRegistrationDTO;
import idatt2106.group3.backend.Repository.ActivityRepository;
import idatt2106.group3.backend.Repository.ChatRepository;

/**
 * Component class responsible for creating and scheduling of activities 
 */
@Component
public class ScheduledActivityCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledActivityCreator.class);

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ChatRepository chatRepository;

    /**
     * A scheduled event, that happens at 06:00 every day
     * If the amount of activities are less than 3 at that moment, 3 activities will be created
     */
    @Scheduled(cron = "0 0 6 * * *")
    public void createActivity() {
        List<ActivityRegistrationDTO> prePlannedActivities = List.of(
                new ActivityRegistrationDTO("Fotball kamp", "Fotball", "11-er kamp i dødens dal", "Fotball, fotballsko",
                        Difficulty.MEDIUM.value, "Trondheim", "Dødens Dal", 10.406649112701416, 63.41899520699223,
                        LocalDateTime.now().plusHours(10), 120, false, 25, null),
                new ActivityRegistrationDTO("Gå tur", "Tur", "Gå tur i Bymarka", "Tur tøy", Difficulty.EASY.value,
                        "Trondheim", "Bymarka", 10.239944458007812, 63.4179582255575, LocalDateTime.now().plusHours(6),
                        120, false, 12, null),
                new ActivityRegistrationDTO("Slappe av i parken", "Slappe av", "Slappe av og snakke i parken", "Ingen",
                        Difficulty.EASY.value, "Trondheim", "Kristiansten festning", 10.412358032037403,
                        63.42765535922034, LocalDateTime.now().plusHours(12), 180, false, 10, null));


        if (activityRepository.findAllFromNow().size() < 3) {
            LOGGER.info("Creating activities because amount was less than 3");
            for (ActivityRegistrationDTO regActivity : prePlannedActivities) {
                registerActivity(regActivity);
            }
        }
    }

    // Stores an activity and chat in database
    private void registerActivity(ActivityRegistrationDTO regActivity) {
        Activity activity = new Activity(regActivity, null);
        Chat chat = new Chat();
        chat = chatRepository.save(chat);
        activity.setChat(chat);
        activityRepository.save(activity);
    }
}
