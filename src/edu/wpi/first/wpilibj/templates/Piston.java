package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Piston {

    private final Solenoid extend, retract;
    private boolean isExtended;

    public Piston(int extendPos, int retractPos) {
        extend = new Solenoid(extendPos);
        retract = new Solenoid(retractPos);
    }

    public void extend() {
        this.retract.set(false);
        this.extend.set(true);
        this.isExtended = true;
    }
    
    public void extend(double delay) {
        this.retract.set(false);
        this.extend.set(true);
        this.isExtended = true;
        Timer.delay(delay);
    }


    public void retract() {
        this.extend.set(false);
        this.retract.set(true);
        this.isExtended = false;
    }
    
    public void retract(double delay) {
        this.extend.set(false);
        this.retract.set(true);
        this.isExtended = false;
        Timer.delay(delay);
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
}
