package project5;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Slot {
    
    @Id
    private String slotCode;
    private String slotName;
    private int credit;
    private int semester;

    public Slot() {
    
    }
    
    public Slot(String slotCode, String slotName) {
        this.slotCode = slotCode;
        this.slotName = slotName;
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

    public String getSlotCode() {
        return slotCode;
    }

    public void setSlotCode(String slotCode) {
        this.slotCode = slotCode;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
    
}
