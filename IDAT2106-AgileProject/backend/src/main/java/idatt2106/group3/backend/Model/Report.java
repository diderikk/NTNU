package idatt2106.group3.backend.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Entity class for storing information about a report
 * with reportId as primary key
 */
@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportId;
    private String description;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "userId", insertable = false, updatable = false)
    private User reportWriter;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "userId", insertable = false, updatable = false)
    private User reportedUser;

    public Report(String description, User reportWriter, User reportedUser) {
        this.description = description;
        this.reportWriter = reportWriter;
        this.reportedUser = reportedUser;
    }

    public Report(String description) {
        this.description = description;
    }

    public Report(){}

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getReportWriter() {
        return reportWriter;
    }

    public void setReportWriter(User reportWriter) {
        this.reportWriter = reportWriter;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    @Override
    public String toString()
    {
        return "Report{" +
                "reportId=" + reportId +
                ", description='" + description + '\'' +
                ", reportWriter=" + reportWriter +
                ", reportedUser=" + reportedUser +
                '}';
    }
}
