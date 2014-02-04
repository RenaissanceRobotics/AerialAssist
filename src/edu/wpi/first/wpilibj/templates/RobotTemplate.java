package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Solenoid;


public class RobotTemplate extends SimpleRobot {

    XboxController xbox;
    DigitalInput compressor;
    Solenoid sol1A, sol1B;
    

    public void robotInit() {
        xbox = new XboxController(1);

        compressor  = new DigitalInput(2);
        sol1A = new Solenoid(1);
        sol1B = new Solenoid(2);
    }

    public void autonomous() {

    }

    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
            System.out.println(compressor.get());
            if (xbox.getAButton()){
                sol1A.set(true); 
               
            } else if (xbox.getBButton()) {
                sol1B.set(true);
                
             } else {
                sol1A.set(false);
                sol1B.set(false); 
            }
            
        }

    }
    
    public void test() {

    }

    public double deadband(double JoystickValue, double DeadbandCutOff) {
        double deadbandreturn;
        if (Math.abs(JoystickValue) < DeadbandCutOff) {
            deadbandreturn = 0.0;
        } else {
            deadbandreturn = (JoystickValue - (Math.abs(JoystickValue) / JoystickValue * DeadbandCutOff)) / (1 - DeadbandCutOff);
            //deadbandreturn = xbox.getY(Hand.kLeft);
        }

        return deadbandreturn;
    }
}
