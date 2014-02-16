package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;

public class DualDriver implements Driver {
    
    private int speed;
    
    public DualDriver () {
        speed = 2;
    }

    public void drive() {
        
        // fix stright driving -> 76%
        // button -> safe fire
        // button -> fire no reload
        // display
        
        // Driver One:
        Robot.drive.drive(-Robot.xbox.getAxis(XboxController.AxisType.kLeftY) / this.speed, Robot.xbox.getAxis(XboxController.AxisType.kRightX) / 1.5);

        if (Robot.xbox.getAxis(XboxController.AxisType.kDLeftRight) > 0.5) {
            Robot.spinAround(90);
        } else if (Robot.xbox.getAxis(XboxController.AxisType.kDLeftRight) < -0.5) {
            Robot.spinAround(-90);
        } /*else if (Robot.xbox.getAxis(XboxController.AxisType.kDUpDown) > 0.5) {
            Robot.spinAround(180);
        } else if (Robot.xbox.getAxis(XboxController.AxisType.kDUpDown) > -0.5) {
            Robot.spinAround(180);
        } */
        
        if (Robot.xbox.getStart()) {
            this.speed = 1;
        } else if (Robot.xbox.getBack()) {
            this.speed = 2;
        }
        
        if (Robot.xbox.getAButton()) {
            Robot.driveDistance(Robot.DISTANCE_FROM_WALL_TO_SHOOT); // 11 feet
        }
        
        // Driver Two:
        if (Robot.xbox2.getAxis(XboxController.AxisType.kLeftY) > 0.5) {
            if (Robot.grip.isExtended()) {
                Robot.grip.retract();
                Timer.delay(0.5);
            }
            if (Robot.arms.isRetracted()) { 
                Robot.arms.extend();
            }
        } else if (Robot.xbox2.getAxis(XboxController.AxisType.kLeftY) < -0.5) {
            if (Robot.arms.isExtended()) {
                Robot.arms.retract();
            }
        }
        
        if (Robot.xbox2.getXButton()) {
            if (Robot.grip.isExtended()) {
                Robot.grip.retract();
            }
        } else if (Robot.xbox2.getBButton()) {
            if (Robot.grip.isRetracted())
                Robot.grip.extend();
        }
        
        if (Robot.xbox2.getTrigger(GenericHID.Hand.kRight)) {
            System.out.print("Shooting...");
            Robot.shoot();
        } else if (Robot.xbox2.getTrigger(GenericHID.Hand.kLeft)) {
            System.out.print("Reloading...");
            Robot.reload();
        }
    }

}
