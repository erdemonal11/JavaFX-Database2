package project5;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SlotAndCourse implements Serializable {

    @EmbeddedId
    private CKForSlotAndCourse ck;

    @JoinColumn(name = "slotCode", referencedColumnName = "slotCode", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Slot slot;

    @JoinColumn(name = "courseCode", referencedColumnName = "courseCode", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Course course;
    
    @JoinColumn(name = "studentNumber", referencedColumnName = "studentNumber", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CCR ccr;

    private int yearTaken;
    private String termTaken;
    private String grade;

    public int getYearTaken() {
        return yearTaken;
    }

    public void setYearTaken(int yearTaken) {
        this.yearTaken = yearTaken;
    }

    public String getTermTaken() {
        return termTaken;
    }

    public void setTermTaken(String termTaken) {
        this.termTaken = termTaken;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CKForSlotAndCourse getCk() {
        return ck;
    }

    public void setCk(CKForSlotAndCourse ck) {
        this.ck = ck;
    }

    public CCR getCcr() {
        return ccr;
    }

    public void setCcr(CCR ccr) {
        this.ccr = ccr;
    }

}
