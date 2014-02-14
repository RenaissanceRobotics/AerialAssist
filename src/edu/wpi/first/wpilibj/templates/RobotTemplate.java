package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class RobotTemplate extends SimpleRobot {

    XboxController xbox;
    Gyro gyro;

    RobotDrive drive;

    Victor left1, left2;
    Victor right1, right2;

    Compressor compressor;

    Encoder encoder;

    boolean xButton, bButton;

    Piston trigger, shooter;
    Piston arms, gripper;

    DigitalInput triggerSwitch;

    public void robotInit() {

        this.left1 = new Victor(2);
        this.left2 = new Victor(4);
        this.right1 = new Victor(1);
        this.right2 = new Victor(3);

        this.xbox = new XboxController(1);

        this.gyro = new Gyro(1);
        this.gyro.setSensitivity(0.007);

        this.compressor = new Compressor(1, 1);

        this.encoder = new Encoder(2, 3, false, CounterBase.EncodingType.k1X);
        this.encoder.setDistancePerPulse(0.01);

        this.trigger = new Piston(8, 7);
        this.shooter = new Piston(6, 5);
        this.gripper = new Piston(1, 2);
        this.arms = new Piston(3, 4);

        this.triggerSwitch = new DigitalInput(4);
    }

    public void autonomous() {

    }

    public void operatorControl() {

        gyro.reset();
        compressor.start();
        encoder.start();

        //this.reload();

        while (isOperatorControl() && isEnabled()) {

            if (xbox.getStart()) {
                gyro.reset();
                encoder.reset();
                encoder.stop();
                encoder.start();
            }

            if (this.xbox.getBack()) {
                this.drive((this.xbox.getX(GenericHID.Hand.kLeft) + this.xbox.getY(GenericHID.Hand.kLeft)), (this.xbox.getX(GenericHID.Hand.kLeft) - this.xbox.getY(GenericHID.Hand.kLeft)));
            } else {
                this.drive(0.0);
            }

            if (this.xbox.getAButton()) {
                this.shoot();
            } else if (this.xbox.getBButton()) {
                this.grapBall();
            } else if (this.xbox.getYButton()) {                
                if (this.encoder.getDistance() <=10) {
                    this.drive(0.25);
                }
            } 

            System.out.println("ENCODER: " + this.encoder.getDistance());

            /*if (this.trigger.countTime()) {
                this.trigger.off();
            }
            if (this.trigger.countTime()) {
                this.trigger.off();
            }
            if (this.gripper.countTime()) {
                this.gripper.off();
            }
            if (this.arms.countTime()) {
                this.arms.off();
            }*/
        }
        compressor.stop();
    }

    public void test() {
        // <editor-fold defaultstate="collapsed" desc="Nothing">
        // </editor-fold>
    }

    public void drive(double amount) {
        this.left1.set(amount);
        this.left2.set(amount);
        this.right1.set(-amount);
        this.right2.set(-amount);
    }

    public void drive(double amountRight, double amountLeft) {
        this.left1.set(amountLeft);
        this.left2.set(amountLeft);
        this.right1.set(amountRight);//either the left or right side needs to be negative for the same direction, easier to use
        this.right2.set(amountRight);
    }

    public void spinAround(int degrees) {
        gyro.reset();
        //Set arms to up position
        if (degrees < 0) { //Left
            while (gyro.getAngle() > degrees && isEnabled()) {
                this.drive(-0.5, -0.5);
            }
        } else if (degrees > 0) { //Right
            while (gyro.getAngle() < degrees && isEnabled()) {
                drive(0.5, 0.5);
            }
        }
    }

    public void reload() {
        this.shooter.extend();
        while (!triggerSwitch.get()) {
            Timer.delay(0.05);
        }
        this.shooter.retract(0.1);
        this.shooter.off();
    }

    public void shoot() {
        if (this.arms.isExtended()) {
            this.grapBall();
        }
        this.gripper.extend(1.0);
        this.gripper.off();
        this.trigger.extend(0.5);
        this.trigger.retract(0.1);
        this.trigger.off();

        this.reload();
    }

    public void grapBall() {
        if (this.arms.isRetracted()){
            this.arms.extend(2.0);
        }
        this.gripper.retract(0.3);
        this.arms.retract(1.0);
        this.gripper.off();
        this.arms.off();
    }
}
