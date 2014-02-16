package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;

public class Piston {

    private final Solenoid extend, retract;
    private boolean isExtended;
    private int count;

    public Piston(int extendPos, int retractPos,  boolean isExtended) {
        extend = new Solenoid(extendPos);
        retract = new Solenoid(retractPos);
        
        this.isExtended = isExtended;
        
        count = 0;
    }

    public void extend() {
        this.retract.set(false);
        this.extend.set(true);
        this.isExtended = true;
        
        this.count = 0;
    }

    public void retract() {
        this.extend.set(false);
        this.retract.set(true);
        this.isExtended = false;
        
        this.count = 0;
    }

    public void off() {
        this.extend.set(false);
        this.retract.set(false);
    }
    
    public boolean isExtended () {
        return this.isExtended;
    }
    
    public boolean isRetracted () {
        return !this.isExtended;
    }
    
    public void countTime () {
        if (this.count >= 20) {
            this.off();
            this.count = 0;
        } else {
            this.count++;
        }
    }
}
