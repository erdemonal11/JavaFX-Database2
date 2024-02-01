
package project5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class CCR implements Serializable {

    @EmbeddedId
    private CCRId CCRId;

    @JoinColumn(name = "studentNumber", referencedColumnName = "studentNumber", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Student student;

    public CCR() {

    }

    public CCR(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public CCRId getCCRId() {
        return CCRId;
    }

    public void setCCRId(CCRId CCRId) {
        this.CCRId = CCRId;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
