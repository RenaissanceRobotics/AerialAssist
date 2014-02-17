package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Options {

    public final SendableChooser position, driveDirection, turnAmount;
    public final SendableChooser trigger, shooter, grip, arms;
    public final SendableChooser driver;

    
    public static class Position {
        //<editor-fold defaultstate="collapsed" desc="Position">
        private final int value;
        
        private static final int val_LEFT = 0;
        private static final int val_MIDDLE = 1;
        private static final int val_RIGHT = 2;
        private static final String[] val_STRINGS = {"Left Side", "Middle", "Right Side"};
        
        private Position (int value) {
            this.value = value;
        }
        
        public String toString () {
            return val_STRINGS[this.value];
        }
        
        public static final Position LEFT = new Position (val_LEFT);
        public static final Position MIDDLE = new Position (val_MIDDLE);
        public static final Position RIGHT = new Position (val_RIGHT);
        //</editor-fold>
    }
    
    public static class DriveDirection {
        //<editor-fold defaultstate="collapsed" desc="Drive Direction">
        private final int value;
        
        private static final int val_RIGHT = 0;
        private static final int val_LEFT = 1;
        private static final String[] val_STRINGS = {"Go Right", "Go Left"};
        
        private DriveDirection (int value) {
            this.value = value;
        }
        
        public String toString () {
            return val_STRINGS[this.value];
        }
        
        public static final DriveDirection RIGHT = new DriveDirection (val_RIGHT);
        public static final DriveDirection LEFT = new DriveDirection (val_LEFT);
        //</editor-fold>
    }
    
    public static class TurnAmount {
        //<editor-fold defaultstate="collapsed" desc="Turn Amount">
        private final int value;
        
        private static final int val_90 = 0;
        private static final int val_45 = 1;
        private static final String[] val_STRINGS = {"Turn 90", "Turn 45"};
        
        private TurnAmount (int value) {
            this.value = value;
        }
        
        public String toString () {
            return val_STRINGS[this.value];
        }
        
        public static final TurnAmount DEGREES_90 = new TurnAmount (val_90);
        public static final TurnAmount DEGREES_45 = new TurnAmount (val_45);
        //</editor-fold>
    }
    
    public static class Piston {
        //<editor-fold defaultstate="collapsed" desc="Pistion">
        private final int value;
        
        private static final int val_EXTENDED = 0;
        private static final int val_RETRACTED = 1;
        
        private Piston (int value) {
            this.value = value;
        }
        
        public static final Piston EXTENDED = new Piston (val_EXTENDED);
        public static final Piston RETRACTED = new Piston (val_RETRACTED);
        //</editor-fold>
    }
    
    public static class Driver {
        //<editor-fold defaultstate="collapsed" desc="Driver">
        private final int value;
        
        private static final int val_TEST_DRIVER = 0;
        private static final int val_DUAL_DRIVER = 1;
        
        private static final String[] val_STRINGS = {"Test Driver", "Dual Driver"};
        
        private Driver (int value) {
            this.value = value;
        }
        
        public String toString () {
            return val_STRINGS[this.value];
        }
        
        public static final Driver TEST_DRIVER = new Driver (val_TEST_DRIVER);
        public static final Driver DUAL_DRIVER = new Driver (val_DUAL_DRIVER);
        //</editor-fold>
    }

    public Options() {
        position = new SendableChooser();
        position.addDefault("Left", Position.LEFT);
        position.addObject("Middle", Position.MIDDLE);
        position.addObject("Right", Position.RIGHT);

        driveDirection = new SendableChooser();
        driveDirection.addDefault("Go Left", DriveDirection.LEFT);
        driveDirection.addObject("Go Right", DriveDirection.RIGHT);

        turnAmount = new SendableChooser();
        turnAmount.addDefault("90 Degrees", TurnAmount.DEGREES_90);
        turnAmount.addObject("45 Degrees", TurnAmount.DEGREES_45);
        
        // <Pistons>
        
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
    
    public boolean getPosition (Position position) {
        return this.position.getSelected() == position;
    }
    
    public Object getSelectedPosition () {
        return this.position.getSelected();
    }
    
    public boolean getDriveDirection (DriveDirection driveDirection) {
        return this.driveDirection.getSelected() == driveDirection;
    }
    
    public Object getSelectedDriveDirection () {
        return this.driveDirection.getSelected();
    }
    
    public boolean getTurnAmount (TurnAmount turnAmount) {
        return this.turnAmount.getSelected() == turnAmount;
    }
    
    public Object getSelectedTrunAmount () {
        return this.turnAmount.getSelected();
    }
    
    public boolean getPiston (SendableChooser pistonOption, Piston piston) {
        return pistonOption.getSelected() == piston;
    }
    
    public Object getSelectedPiston (SendableChooser pistionOption) {
        return pistionOption.getSelected();
    }
    
    public boolean getDriver (Driver driverOption) {
        return this.driver.getSelected() == driverOption;
    }
    
    public Object getSelectedDriver () {
        return this.driver.getSelected();
    }
}
