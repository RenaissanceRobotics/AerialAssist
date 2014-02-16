package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.GenericHID;

public class TestDriver implements Driver {
    
    private boolean isSprinting;
    
    public TestDriver () {
        isSprinting = false;
    }

    public void drive() {
        ///*
        // Drive
        // x + y and x - y
        // make xbox more accurate
        double rightPow = Robot.xbox.getX(GenericHID.Hand.kLeft) + Robot.xbox.getY(GenericHID.Hand.kLeft);
        double leftPow = Robot.xbox.getX(GenericHID.Hand.kLeft) - Robot.xbox.getY(GenericHID.Hand.kLeft);
        if (!this.isSprinting) {
            rightPow = rightPow / 2;
            leftPow = leftPow / 2;
        }

        Robot.drive(leftPow, rightPow);
            //Arms Higher/Lower // Grip/Ungrip
        // keep arms retracted when raising
        if (Robot.xbox.getY(GenericHID.Hand.kRight) <= -0.5) {
            Robot.grip.retract();
            Robot.arms.extend();
        } else if (Robot.xbox.getY(GenericHID.Hand.kRight) <= 0.5) {
            Robot.grip.retract();
            Robot.arms.retract();
        }
        if (Robot.xbox.getBumper(GenericHID.Hand.kLeft)) {
            Robot.grip.retract();
        } else if (Robot.xbox.getBumper(GenericHID.Hand.kRight)) {
            Robot.grip.extend();
        }
        //Sprint
        if (Robot.xbox.getStart()) {
            this.isSprinting = true;
        } else if (Robot.xbox.getBack()) {
            this.isSprinting = false;
        }
        // Loading // Reloading
        if (Robot.isReload || Robot.xbox.getXButton()) {
            Robot.reload();
        } else if (Robot.xbox.getTrigger(GenericHID.Hand.kRight) || Robot.xbox.getTrigger(GenericHID.Hand.kLeft)) {
            Robot.shoot();
        }
        // Spin 
        if (Robot.xbox.getAxis(XboxController.AxisType.kDLeftRight) > 0.5) {
            Robot.spinAround(180);
        } else if (Robot.xbox.getAxis(XboxController.AxisType.kDLeftRight) < -0.5) {
            Robot.spinAround(-180);
        }

        System.out.println("ENCODER: " + Robot.encoder.getDistance());
    }
}
