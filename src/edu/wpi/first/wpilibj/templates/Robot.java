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
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SimpleRobot {

    private XboxController xbox1, xbox2;

    private Victor leftFront, leftBack, rightFront, rightBack;
    private RobotDrive drive;
    private Encoder encoder;
    private Gyro gyro;

    private Compressor compressor;

    private Piston trigger, shooter, grip, arms;
    private DigitalInput shooterSwitch;

    private boolean isReloading;

    private SendableChooser OPTION_AUTO_MODE;

    private int DISTANCE_TO_DRIVE_FROM_START;

    public void robotInit() {
        this.xbox1 = new XboxController(1);
        this.xbox2 = new XboxController(2);

        this.leftFront = new Victor(2);
        this.leftBack = new Victor(4);
        this.rightFront = new Victor(1);
        this.rightBack = new Victor(3);
        this.drive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
        this.encoder = new Encoder(2, 3, false, CounterBase.EncodingType.k1X);
        this.encoder.setDistancePerPulse(0.051);
        this.gyro = new Gyro(1);
        this.gyro.setSensitivity(0.007);

        this.compressor = new Compressor(1, 1);

        this.trigger = new Piston(8, 7, false);
        this.shooter = new Piston(6, 5, false);
        this.grip = new Piston(1, 2, false);
        this.arms = new Piston(3, 4, true);
        this.shooterSwitch = new DigitalInput(4);

        this.isReloading = false;

        this.OPTION_AUTO_MODE = new SendableChooser();
        this.OPTION_AUTO_MODE.addDefault("One Ball", AutoMode.ONE_BALL);
        this.OPTION_AUTO_MODE.addObject("Two Ball", AutoMode.TWO_BALL);
        this.OPTION_AUTO_MODE.addObject("Drive Only", AutoMode.DRIVE_ONLY);
        this.OPTION_AUTO_MODE.addObject("Nothing", AutoMode.NOTHING);

        SmartDashboard.putData("Auto Option", this.OPTION_AUTO_MODE);

        this.DISTANCE_TO_DRIVE_FROM_START = 12 * 12 + 6; // 12.5 feet
    }

    public void autonomous() {
        this.start();
        this.grip.setExtended(false);
        this.arms.setExtended(false);
        this.trigger.setExtended(false);
        this.shooter.setExtended(false);
        // Can not use Objects In Switch
        // SendableChooser#getSelected() returns an Object
        if (this.OPTION_AUTO_MODE.getSelected() == AutoMode.TWO_BALL) {
            System.out.println("Two Ball");
            // Two Ball Auto

            // Grab the Ball
            this.grip.extend();
            this.arms.extend();
            Timer.delay(2.0);

            this.grip.retract();

            // Drive
            this.driveDistance(-this.DISTANCE_TO_DRIVE_FROM_START);
            Timer.delay(1.0);

            // Shoot
            this.shoot(true);

            // Reload the Ball
            this.arms.retract();
            Timer.delay(2.0);
            this.grip.extend();

            // Shoot
            Timer.delay(1.0);
            this.shoot(true);

        } else if (this.OPTION_AUTO_MODE.getSelected() == AutoMode.DRIVE_ONLY) {
            System.out.println("Drive Only");
            // Drive Forward Only

            this.driveDistance(-this.DISTANCE_TO_DRIVE_FROM_START);

        } else if (this.OPTION_AUTO_MODE.getSelected() == AutoMode.NOTHING) {
            // Don't Do Anything
            System.out.println("Nothing");
            // WHOO
        } else { // if (this.OPTION_AUTO_MODE.getSelected() == AutoMode.ONE_BALL) {
            // The Defualt one
            // One Ball Auto
            System.out.println("One Ball");
            this.driveDistance(-this.DISTANCE_TO_DRIVE_FROM_START);
            this.grip.extend();
            Timer.delay(0.25);
            this.shoot(true);

        }
        this.stop();
    }

    public void operatorControl() {

        this.start();

        while (isOperatorControl() && isEnabled()) {
            // Driver One
            double rightX = this.xbox1.getAxis(XboxController.AxisType.kRightX);
            double leftY = this.xbox1.getAxis(XboxController.AxisType.kLeftY);

            // Just to update the RobotDrive
            // Then if needed change the values
            this.drive.arcadeDrive(-leftY, -rightX);
            // End Driver One

            // Driver Two
            // ------------------- Arm Control ---------------------
            if (this.xbox2.getY(GenericHID.Hand.kLeft) >= 0.5) { // Joystick forward
                // Lower Arms
                this.arms.extend();
            } else if (this.xbox2.getY(GenericHID.Hand.kLeft) <= -0.5) { // Joystick backward
                // Raise Arms
                if (this.grip.isExtended()) { // Can't Raise Arms With Grip Open
                    this.grip.retract();
                    Timer.delay(0.25);
                }
                this.arms.retract();
            }

            // ------------------- Grip Control --------------------
            if (this.xbox2.getXButton()) { // X Button Grip
                this.grip.retract();
            } else if (this.xbox2.getBButton()) { // B Button Release
                this.grip.extend();
            }

            // ------------------- Shooting ------------------------
            if (this.xbox2.getTrigger(GenericHID.Hand.kRight)) {
                // Right Trigger Shoot With Reload
                if (this.grip.isRetracted()) {
                    this.grip.extend();
                    Timer.delay(0.25);
                }
                this.shoot(true);
            }
            if (this.xbox2.getTrigger(GenericHID.Hand.kLeft)) {
                // Left Trigger Reload
                this.reload();
            }

            // ------------------- Misc Control for Shooter --------
            if (this.xbox2.getBumper(GenericHID.Hand.kRight)) {
                // Right Bumper Disable Shooter
                this.disableShoter();
            }
            if (this.xbox2.getBumper(GenericHID.Hand.kLeft)) {
                // Left Bumper Shoot Without Reload
                if (this.grip.isRetracted()) {
                    this.grip.extend();
                    Timer.delay(0.25);
                }
                this.shoot(false);
            }

            // End Driver Two
            // Counts of Time to Turn the Solenoids off so that Manual Control Works
            this.turnOffSolenoidsWhenReady();

            if (this.isReloading) {
                this.reload();
            }

            SmartDashboard.putBoolean("Ready To Fire", !this.isReloading);

            Timer.delay(0.01);
        }

        this.stop();

    }

    public void test() {
        this.start();

        while (isTest() && isEnabled()) {
            this.turnOffSolenoidsWhenReady();
        }

        this.stop();
    }

    private void shoot(boolean doReload) {
        this.trigger.extend();
        Timer.delay(0.5);
        this.trigger.retract();

        if (doReload) {
            this.reload();
        }
    }

    private void disableShoter() {
        this.shooter.extend();
        Timer.delay(0.5);
        this.trigger.extend();
        Timer.delay(0.1);
        this.shooter.retract();
        this.trigger.retract();
    }

    private void reload() {
        if (!this.isReloading) {
            this.isReloading = true;
            this.shooter.extend();
        } else if (this.shooterSwitch.get()) {
            this.isReloading = false;
            this.shooter.retract();
        }
    }

    private void start() {        
        this.gyro.reset();
        this.compressor.start();
        this.encoder.start();
    }

    private void stop() {
        this.gyro.reset();
        this.compressor.stop();
        this.encoder.stop();
    }

    private void driveDistance(int inches) { // copied from previous code because it worked
        double Kp = 0.01;
        this.encoder.reset();
        gyro.reset();
        if (inches > 0) {
            while (this.encoder.getDistance() <= inches) {
                this.drive.drive(0.75, -this.gyro.getAngle() * Kp);
            }
        } else {
            while (this.encoder.getDistance() >= inches) {
                this.drive.drive(-0.75, this.gyro.getAngle() * Kp);
            }
        }
        this.drive.drive(0.0, 0.0);
    }

    private void turnOffSolenoidsWhenReady() {
        this.shooter.countTime();
        this.trigger.countTime();
        this.arms.countTime();
        this.grip.countTime();
    }

    private void setMotors(double leftAmt, double rightAmt) {
        this.leftBack.set(leftAmt);
        this.leftFront.set(leftAmt);
        this.rightBack.set(rightAmt);
        this.rightFront.set(rightAmt);
    }

    private static class AutoMode {

        private static final int val_ONE_BALL = 0;
        private static final int val_TWO_BALL = 1;
        private static final int val_DRIVE_ONLY = 2;
        private static final int val_NOTHING = 3;

        private final int value;

        private AutoMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static final AutoMode ONE_BALL = new AutoMode(val_ONE_BALL);
        public static final AutoMode TWO_BALL = new AutoMode(val_TWO_BALL);
        public static final AutoMode DRIVE_ONLY = new AutoMode(val_DRIVE_ONLY);
        public static final AutoMode NOTHING = new AutoMode(val_NOTHING);
    }
}
