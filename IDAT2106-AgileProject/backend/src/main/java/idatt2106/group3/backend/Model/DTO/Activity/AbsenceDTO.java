package idatt2106.group3.backend.Model.DTO.Activity;

import java.util.Set;

/**
 * DTO used for transfers of data between backend and frontend
 * about users absent from activities
 */
public class AbsenceDTO {
    private Set<Long> absentUsersId;

    public AbsenceDTO(Set<Long> absentUsersId) {
        this.absentUsersId = absentUsersId;
    }

    public AbsenceDTO(){}

    public Set<Long> getAbsentUsersId() {
        return absentUsersId;
    }

    public void setAbsentUsersId(Set<Long> absentUsersId) {
        this.absentUsersId = absentUsersId;
    }

    @Override
    public String toString() {
        return "AbsentUsersId{" + '\'' + 
        absentUsersId.toString() + '\'' + 
        "}";
    }
}
