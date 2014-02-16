package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;

public class DualDriver implements Driver {

    private int speed;
    private final double Kp = 0.01,  MAX_CENTRE_VALUE = 0.2;
    private boolean isDrivingStraight = false;
    

    public DualDriver() {
        speed = 2;
    }

    public void drive() {

        // Driver One:
        double rightX = Robot.xbox.getAxis(XboxController.AxisType.kRightX);
        double leftY = Robot.xbox.getAxis(XboxController.AxisType.kLeftY);

        if (leftY <= MAX_CENTRE_VALUE & leftY >= -MAX_CENTRE_VALUE) {
            Robot.drive(rightX, rightX);

            this.isDrivingStraight = false;
        } else if (rightX <= MAX_CENTRE_VALUE & rightX >= -MAX_CENTRE_VALUE) { // stright
            if (!this.isDrivingStraight) {
                this.isDrivingStraight = true;
                Robot.gyro.reset();
            }
            if (leftY <= -MAX_CENTRE_VALUE) {
                Robot.drive.drive(-leftY / speed, -Robot.gyro.getAngle() * Kp);
            } else if (leftY >= MAX_CENTRE_VALUE) {
                Robot.drive.drive(-leftY / speed, Robot.gyro.getAngle() * Kp);
            }
        } else {
            Robot.drive.drive(-leftY / speed, rightX / 1.5);

            this.isDrivingStraight = false;
        }

        if (Robot.xbox.getAxis(XboxController.AxisType.kDLeftRight)
                > 0.5) {
            Robot.spinAround(90);
        } else if (Robot.xbox.getAxis(XboxController.AxisType.kDLeftRight)
                < -0.5) {
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
        if (Robot.xbox2.getAxis(XboxController.AxisType.kLeftY)
                > 0.5) {
            if (Robot.grip.isExtended()) {
                Robot.grip.retract();
                Timer.delay(0.5);
            }
            if (Robot.arms.isRetracted()) {
                Robot.arms.extend();
            }
        } else if (Robot.xbox2.getAxis(XboxController.AxisType.kLeftY)
                < -0.5) {
            if (Robot.arms.isExtended()) {
                Robot.arms.retract();
            }
        }

        if (Robot.xbox2.getXButton()) {
            if (Robot.grip.isExtended()) {
                Robot.grip.retract();
            }
        } else if (Robot.xbox2.getBButton()) {
            if (Robot.grip.isRetracted()) {
                Robot.grip.extend();
            }
        }

        if (Robot.xbox2.getTrigger(GenericHID.Hand.kRight)) {
            Robot.shoot(true);
        } else if (Robot.xbox2.getTrigger(GenericHID.Hand.kLeft)) {
            Robot.reload();
        }

        if (Robot.xbox2.getBumper(GenericHID.Hand.kLeft)) {
            Robot.disableShooter();
        } else if (Robot.xbox2.getBumper(GenericHID.Hand.kRight)) {
            Robot.shoot(false);
        }
    }

}
