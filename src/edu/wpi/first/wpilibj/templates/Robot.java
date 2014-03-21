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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SimpleRobot {

    private Options options;

    static Victor left1, left2;
    static Victor right1, right2;
    static RobotDrive drive;

    static XboxController xbox, xbox2;
    static Gyro gyro;

    static Compressor compressor;

    static Encoder encoder;

    static Piston trigger, shooter;
    static Piston arms, grip;

    static DigitalInput triggerSwitch;

    protected Driver driver;

    static final int DISTANCE = 6 * 12; // 6 feet
    static boolean isReload;

    public void robotInit() {

        this.options = new Options();

        SmartDashboard.putData("Trigger Piston", this.options.trigger);
        SmartDashboard.putData("Shooter Piston", this.options.shooter);
        SmartDashboard.putData("Arm Piston", this.options.arms);
        SmartDashboard.putData("Grip Piston", this.options.grip);
        SmartDashboard.putData("Driver", this.options.driver);

        Robot.left1 = new Victor(2);
        Robot.left2 = new Victor(4);
        Robot.right1 = new Victor(1);
        Robot.right2 = new Victor(3);
        Robot.drive = new RobotDrive(left1, left2, right1, right2);

        Robot.xbox = new XboxController(1);
        Robot.xbox2 = new XboxController(2);

        Robot.gyro = new Gyro(1);
        Robot.gyro.setSensitivity(0.007);

        Robot.compressor = new Compressor(1, 1);

        Robot.encoder = new Encoder(2, 3, false, CounterBase.EncodingType.k1X);
        Robot.encoder.setDistancePerPulse(0.051);

        Robot.trigger = new Piston(8, 7, false);
        Robot.shooter = new Piston(6, 5, false);
        Robot.grip = new Piston(1, 2, false);
        Robot.arms = new Piston(3, 4, true);

        Robot.triggerSwitch = new DigitalInput(4);

        Robot.isReload = false;
    }

    public void autonomous() {
        Robot.start();

        Robot.grip.extend();
        Robot.driveDistance(-Robot.DISTANCE);
        Robot.shoot(true);

        Robot.stop();
    }

    public void operatorControl() {
        if (options.getDriver(Options.Driver.DUAL_DRIVER)) {
            this.driver = new DualDriver();
        } else if (options.getDriver(Options.Driver.TEST_DRIVER)) {
            this.driver = new TestDriver();
        }

        trigger.setExtended(options.getPiston(options.trigger, Options.Piston.EXTENDED));
        shooter.setExtended(options.getPiston(options.shooter, Options.Piston.EXTENDED));
        arms.setExtended(options.getPiston(options.arms, Options.Piston.EXTENDED));
        grip.setExtended(options.getPiston(options.grip, Options.Piston.EXTENDED));

        Robot.start();

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
        Robot.stop();
    }

    public void test() {
        // Test only run compressor

        //<editor-fold defaultstate="collapsed" desc="Test">
        Robot.encoder.start();

        Robot.compressor.start();

        while (isTest() && isEnabled()) {

        }
        Robot.compressor.stop();

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
        //<editor-fold defaultstate="collapsed" desc="Reload">
        if (!Robot.isReload) {
            Robot.shooter.extend();
            Robot.isReload = true;
        } else {
            if (Robot.triggerSwitch.get()) {
                Robot.shooter.off();
                Robot.shooter.retract();
                Robot.isReload = false;

                // TODO: play sound?
            }
        }
        //</editor-fold>
    }

    public static void shoot(boolean doReload) {
        //<editor-fold defaultstate="collapsed" desc="Shoot">
        if (Robot.grip.isRetracted()) {
            Robot.grip.extend();
            Timer.delay(0.10);
            Robot.grip.off();
        }
        Robot.trigger.extend();
        Timer.delay(0.5);
        Robot.trigger.retract();

        if (doReload) {
            Robot.reload();
        }
        //</editor-fold>
    }

    public static void driveDistance(int inches) {
        //<editor-fold defaultstate="collapsed" desc="Drive Distance">
        double Kp = 0.01;
        Robot.encoder.reset();
        gyro.reset();
        if (inches > 0) {
            while (Robot.encoder.getDistance() <= inches) {
                Robot.drive.drive(0.75, -Robot.gyro.getAngle() * Kp);
            }
        } else {
            while (Robot.encoder.getDistance() >= inches) {
                Robot.drive.drive(-0.75, Robot.gyro.getAngle() * Kp);
            }
        }
        Robot.drive(0.0);
        //</editor-fold>
    }

    public static void disableShooter() {
        //<editor-fold defaultstate="collapsed" desc="Disable Shooter">
        Robot.shooter.extend();
        Timer.delay(0.5);
        Robot.trigger.extend();
        Timer.delay(0.1);
        Robot.shooter.retract();
        Robot.trigger.retract();
        //</editor-fold>
    }

    public static void start() {
        Robot.gyro.reset();
        Robot.compressor.start();
        Robot.encoder.start();
    }

    public static void stop() {
        Robot.gyro.reset();
        Robot.compressor.stop();
        Robot.encoder.stop();
    }
}
