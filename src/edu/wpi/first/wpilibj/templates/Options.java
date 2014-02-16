package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Options {

    public final SendableChooser position, driveDirection, turnAmount;
    public final SendableChooser trigger, shooter, grip, arms;

    
    public static class Position {
        //<editor-fold defaultstate="collapsed" desc="Position">
        private final int value;
        
        private static final int val_LEFT = 0;
        private static final int val_MIDDLE = 1;
        private static final int val_RIGHT = 2;
        
        private Position (int value) {
            this.value = value;
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
        
        private DriveDirection (int value) {
            this.value = value;
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
        
        private TurnAmount (int value) {
            this.value = value;
        }
        
        public static final TurnAmount DEGREES_90 = new TurnAmount (val_90);
        public static final TurnAmount DEGREES_45 = new TurnAmount (val_45);
        //</editor-fold>
    }
    
    public static class Piston {
        private final int value;
        
        private static final int val_EXTENDED = 0;
        private static final int val_RETRACTED = 1;
        
        private Piston (int value) {
            this.value = value;
        }
        
        public static final Piston EXTENDED = new Piston (val_EXTENDED);
        public static final Piston RETRACTED = new Piston (val_RETRACTED);
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
        trigger.addObject("Retracted", Piston.RETRACTED);
        
        shooter = new SendableChooser();
        shooter.addObject("Extended", Piston.EXTENDED);
        shooter.addObject("Retracted", Piston.RETRACTED);
        
        grip = new SendableChooser();
        grip.addObject("Extended", Piston.EXTENDED);
        grip.addObject("Retracted", Piston.RETRACTED);
        
        arms = new SendableChooser();
        arms.addObject("Extended", Piston.EXTENDED);
        arms.addObject("Retracted", Piston.RETRACTED);
        
        // </Pistons>
    }
    
    public boolean getPosition (Position position) {
        return this.position.getSelected() == position;
    }
    
    public boolean getDriveDirection (DriveDirection driveDirection) {
        return this.driveDirection.getSelected() == driveDirection;
    }
    
    public boolean getTurnAmount (TurnAmount turnAmount) {
        return this.turnAmount.getSelected() == turnAmount;
    }
    
    public boolean getPiston (SendableChooser pistonOption, Piston piston) {
        return pistonOption.getSelected() == piston;
    }
}
