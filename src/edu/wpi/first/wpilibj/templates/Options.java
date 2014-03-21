package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Options {

    public final SendableChooser autoMode;
    public final SendableChooser trigger, shooter, grip, arms;
    public final SendableChooser driver;

    public static class AutoMode {

        //<editor-fold defaultstate="collapsed" desc="Auto Mode">
        private final int value;

        private static final int val_ONE_BALL = 0;
        private static final int val_TWO_BALL = 1;

        private AutoMode(int value) {
            this.value = value;
        }

        public static final AutoMode ONE_BALL = new AutoMode(val_ONE_BALL);
        public static final AutoMode TWO_BALL = new AutoMode(val_TWO_BALL);
        //</editor-fold>
    }

    public static class Piston {

        //<editor-fold defaultstate="collapsed" desc="Piston">
        private final int value;

        private static final int val_EXTENDED = 0;
        private static final int val_RETRACTED = 1;

        private Piston(int value) {
            this.value = value;
        }

        public static final Piston EXTENDED = new Piston(val_EXTENDED);
        public static final Piston RETRACTED = new Piston(val_RETRACTED);
        //</editor-fold>
    }

    public static class Driver {

        //<editor-fold defaultstate="collapsed" desc="Driver">
        private final int value;

        private static final int val_TEST_DRIVER = 0;
        private static final int val_DUAL_DRIVER = 1;

        private static final String[] val_STRINGS = {"Test Driver", "Dual Driver"};

        private Driver(int value) {
            this.value = value;
        }

        public String toString() {
            return val_STRINGS[this.value];
        }

        public static final Driver TEST_DRIVER = new Driver(val_TEST_DRIVER);
        public static final Driver DUAL_DRIVER = new Driver(val_DUAL_DRIVER);
        //</editor-fold>
    }

    public Options() {
        // <Pistons>

        autoMode = new SendableChooser();
        autoMode.addDefault("One Ball", AutoMode.ONE_BALL);
        autoMode.addObject("Two Ball", AutoMode.TWO_BALL);

        trigger = new SendableChooser();
        trigger.addObject("Extended", Piston.EXTENDED);
        trigger.addDefault("Retracted", Piston.RETRACTED);

        shooter = new SendableChooser();
        shooter.addObject("Extended", Piston.EXTENDED);
        shooter.addDefault("Retracted", Piston.RETRACTED);

        grip = new SendableChooser();
        grip.addObject("Extended", Piston.EXTENDED);
        grip.addDefault("Retracted", Piston.RETRACTED);

        arms = new SendableChooser();
        arms.addObject("Extended", Piston.EXTENDED);
        arms.addDefault("Retracted", Piston.RETRACTED);

        // </Pistons>
        driver = new SendableChooser();
        driver.addDefault("Dual Driver", Driver.DUAL_DRIVER);
        driver.addObject("Test Driver", Driver.TEST_DRIVER);
    }

    public boolean getAutoMode(AutoMode autoModeOption) {
        return this.autoMode.getSelected() == autoModeOption;
    }

    public Object getSelectedautoMode() {
        return this.autoMode.getSelected();
    }

    public boolean getPiston(SendableChooser pistonOption, Piston piston) {
        return pistonOption.getSelected() == piston;
    }

    public Object getSelectedPiston(SendableChooser pistionOption) {
        return pistionOption.getSelected();
    }

    public boolean getDriver(Driver driverOption) {
        return this.driver.getSelected() == driverOption;
    }

    public Object getSelectedDriver() {
        return this.driver.getSelected();
    }
}
