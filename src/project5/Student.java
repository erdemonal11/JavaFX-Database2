package project5;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Student {
    
    @Id
    private String studentNumber;
    
    @Temporal(TemporalType.DATE)
    private Date leavingDate;
    
    private boolean minorDegree;

    public Student() {
        
    }

    public Student(String studentNumber, Date leavingDate) {
        this.studentNumber = studentNumber;
        this.leavingDate = leavingDate;
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

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Date getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(Date leavingDate) {
        this.leavingDate = leavingDate;
    }

    public boolean getMinorDegree() {
        return minorDegree;
    }

    public void setMinorDegree(boolean minorDegree) {
        this.minorDegree = minorDegree;
    }
    
}
