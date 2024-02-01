package idatt2106.group3.backend.Model.DTO.Activity;

import java.nio.charset.StandardCharsets;

import idatt2106.group3.backend.Model.Activity;

/**
 * DTO used for transfers of data about activities
 */
public class ActivityDTO extends ActivitySuperclassDTO {
    private long activityId;
    private long organizerId;
    private String organizerForename;
    private String organizerSurname;
    private long chatId;

    public ActivityDTO(Activity activity){
        super(activity.getTitle(), activity.getType(), activity.getDescription(), activity.getEquipment(), activity.getDifficulty(), activity.getCity(), activity.getPlace(), activity.getLongitude(), activity.getLatitude(), activity.getStartTime(), activity.getDurationMinutes(), activity.isPrivateActivity(), activity.getMaxParticipants(), convertActivityPicture(activity.getActivityPicture()));
        this.activityId = activity.getActivityId();
        if(activity.getOrganizer() != null) {
            this.organizerId = activity.getOrganizer().getUserId();
            this.organizerForename = activity.getOrganizer().getForename();
            this.organizerSurname = activity.getOrganizer().getSurname();
        } 
        if(activity.getChat() != null){
            this.chatId = activity.getChat().getChatId();
        }
    }

    public ActivityDTO(){}

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(long organizerId) {
        this.organizerId = organizerId;
    }

    public String getOrganizerForename() {
        return organizerForename;
    }

    public void setOrganizerForename(String organizerForename) {
        this.organizerForename = organizerForename;
    }

    public String getOrganizerSurname() {
        return organizerSurname;
    }

    public void setOrganizerSurname(String organizerSurname) {
        this.organizerSurname = organizerSurname;
    }

    private static String convertActivityPicture(byte[] activityPicture){
        String activityPictureString = null;
        if(activityPicture != null)activityPictureString = new String(activityPicture, StandardCharsets.UTF_8);
        return activityPictureString;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString()
    {
        return super.toString() + '\'' +
                "activityId=" + activityId + '\'' +
                ", organizerId=" + organizerId + '\'' +
                ", organizerForename=" + organizerForename + '\'' +
                ", organizerSurname=" + organizerSurname + '\'' +
                '}';
    }
}
