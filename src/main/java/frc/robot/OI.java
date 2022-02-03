package frc.robot;

import com.nerdherd.lib.pneumatics.commands.ExtendPiston;
import com.nerdherd.lib.pneumatics.commands.RetractPiston;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OI {
    public OI() {
        SmartDashboard.putData("Extend Piston", new ExtendPiston(Robot.piston));
        SmartDashboard.putData("Retract Piston", new RetractPiston(Robot.piston));
    }
}
