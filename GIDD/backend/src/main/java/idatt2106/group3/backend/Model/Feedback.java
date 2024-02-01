package idatt2106.group3.backend.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity class for storing information about a feedback
 * with feedbackId as primary key
 */
@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long feedbackId;
    private String feedback;

    public Feedback(String feedback) {
        this.feedback = feedback;
    }

    public Feedback(){}

    public long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString()
    {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
