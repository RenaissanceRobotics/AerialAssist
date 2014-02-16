package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Robot extends SimpleRobot {

    static XboxController xbox, xbox2;
    static Gyro gyro;

    static RobotDrive drive;

    static Victor left1, left2;
    static Victor right1, right2;

    static Compressor compressor;

    static Encoder encoder;

    static boolean xButton, bButton;

    static Piston trigger, shooter;
    static Piston arms, grip;

    static double delay;
    static final double TRIGGER_DELAY = 0.1, SHOOTER_DELAY = 0, ARMS_DELAY = 0, GRIP_DELAY = 0;

    static boolean isReload;

    static DigitalInput triggerSwitch;

    protected Driver driver = new DualDriver();

    static final int DISTANCE_FROM_WALL_TO_SHOOT = 11 * 12; // 11 feet

    public void robotInit() {

        Robot.left1 = new Victor(2);
        Robot.left2 = new Victor(4);
        Robot.right1 = new Victor(1);
        Robot.right2 = new Victor(3);

        Robot.xbox = new XboxController(1);
        Robot.xbox2 = new XboxController(2);

        Robot.drive = new RobotDrive(left1, left2, right1, right2);

        Robot.gyro = new Gyro(1);
        Robot.gyro.setSensitivity(0.007);

        Robot.compressor = new Compressor(1, 1);

        Robot.encoder = new Encoder(2, 3, false, CounterBase.EncodingType.k1X);
        Robot.encoder.setDistancePerPulse(0.051);

        Robot.trigger = new Piston(8, 7, false);
        Robot.shooter = new Piston(6, 5, false);
        Robot.grip = new Piston(1, 2, true);
        Robot.arms = new Piston(3, 4, true);

        Robot.triggerSwitch = new DigitalInput(4);

        Robot.isReload = false;
    }

    public void autonomous() {
        Robot.encoder.start();
        Robot.reload();

        Robot.driveDistance(120);
        Robot.reload();

        Robot.spinAround(180);
        Robot.reload();

        Robot.driveDistance(120);
        Robot.reload();

        Robot.spinAround(180);
        Robot.reload();

        Robot.shoot();
        Robot.encoder.stop();
    }

    public void operatorControl() {

        gyro.reset();
        compressor.start();
        encoder.start();

        //Robot.reload();
        while (isOperatorControl() && isEnabled()) {
            this.driver.drive();

            if (Robot.isReload) {
                Robot.reload();
            }
            Robot.arms.countTime();
            Robot.grip.countTime();
            Robot.shooter.countTime();
            Robot.trigger.countTime();
            
        }
        compressor.stop();
    }

    public void test() {
        //<editor-fold defaultstate="collapsed" desc="Test">
        Robot.encoder.start();

        double a = 0.0;
        while (isTest() && isEnabled()) {
            System.out.print("Encoder:" + Robot.encoder.getRaw());
            a += Robot.encoder.getRaw();
            System.out.print(" Count:" + a);
            System.out.print(" distance: " + Robot.encoder.getDistance());
            System.out.println();

            if (Robot.xbox.getAButton()) {
                Robot.encoder.reset();
                Robot.encoder.stop();
                Robot.encoder.start();
                a = 0;
            } else if (Robot.xbox.getBButton()) {
                Robot.driveDistance(120);
            } else {
                Robot.drive(0.0);
            }
        }

        Robot.encoder.stop();
        //</editor-fold>
    }

    public static void drive(double amt) {
        //<editor-fold defaultstate="collapsed" desc="Drive">
        Robot.left1.set(amt);
        Robot.left2.set(amt);
        Robot.right1.set(-amt);
        Robot.right2.set(-amt);
        //</editor-fold>
    }

    public static void drive(double amtL, double amtR) {
        // <editor-fold defaultstate="collapsed" desc="Drive">
        Robot.left1.set(amtL);
        Robot.left2.set(amtL);
        Robot.right1.set(amtR);
        Robot.right2.set(amtR);
        // </editor-fold>
    }

    public static void spinAround(int deg) {
        //<editor-fold defaultstate="collapsed" desc="Spin">
        gyro.reset();
        if (deg < 0) { //Left
            while (gyro.getAngle() > deg) {
                Robot.drive(-0.4, -0.4);
            }
        } else if (deg > 0) { //Right
            while (gyro.getAngle() < deg) {
                drive(0.4, 0.4);
            }
        }
        gyro.reset();
        //</editor-fold>
    }

    public static void reload() {
        if (!Robot.isReload) {
            Robot.shooter.extend();
            Robot.isReload = true;
        } else {
            if (Robot.triggerSwitch.get()) {
                Robot.shooter.off();
                Robot.shooter.retract();
                Robot.isReload = false;
            }
        }
    }

    public static void shoot() {
        if (Robot.grip.isRetracted()) {
            Robot.grip.extend();
            Timer.delay(0.25);
            Robot.grip.off();
        }
        Robot.trigger.extend();
        Timer.delay(0.5);
        Robot.trigger.retract();

        Robot.reload();
        //Robot.trigger.off();
    }

    public static void grapBall() {
        //<editor-fold defaultstate="collapsed" desc="Grab Ball">
        if (Robot.arms.isRetracted() & Robot.grip.isExtended()) {
            Robot.arms.extend();
            Robot.grip.retract();
            Robot.delay = ARMS_DELAY + GRIP_DELAY;
        } else if (Robot.arms.isRetracted()) {
            Robot.arms.extend();
            Robot.delay = ARMS_DELAY;
        } else if (Robot.grip.isExtended()) {
            Robot.grip.retract();
            Robot.delay = GRIP_DELAY;
            Timer.delay(Robot.delay);
        }
        Robot.grip.extend();
        Robot.grip.retract();
        Timer.delay(0.3);
        Robot.arms.retract();
        Timer.delay(1.0);
        Robot.grip.off();
        Robot.arms.off();
        //</editor-fold>
    }

    public static void driveDistance(int inches) {
        //<editor-fold defaultstate="collapsed" desc="Drive Distance">
        Robot.encoder.reset();
        if (inches > 0) {
            while (Robot.encoder.getDistance() <= inches) {
                Robot.drive(0.33, -0.25);
            }
        } else {
            while (Robot.encoder.getDistance() >= inches) {
                Robot.drive(-0.33, 0.25);
            }
        }
        Robot.drive(0.0);
        //</editor-fold>
    }
}
