package frc.robot.commands.vision;

import frc.robot.Robot;
import frc.robot.constants.VisionConstants;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// Leave room for motor IDS in the event that there is a turret
public class TurretTrackLime extends CommandBase {

    private double m_rotP;

    public TurretTrackLime(double kRotP) {
        addRequirements(Robot.drive);
        addRequirements(Robot.limelight);

        m_rotP = kRotP;
    }

    @Override

    public void initialize() {
        SmartDashboard.putString("Current Command", "LiveTurretTrack");
    }

    @Override
    public void execute() {
        double getAngularTargetError = -Robot.limelight.getXOffsetFromTarget();
        double robotAngle = (360 - Robot.drive.getRawYaw()) % 360;
        double power = -m_rotP * getAngularTargetError;
        if (!(Math.abs(getAngularTargetError) < VisionConstants.kDriveRotationDeadband)) {
            Robot.drive.setPowerFeedforward(power + -Robot.oi.getDriveJoyRightY(),
                    -power + -Robot.oi.getDriveJoyRightY());
        }
        /*else if (!(Math.abs(getAngularTargetError) > VisionConstants.kDriveRotationDeadband)) {
            Robot.drive.setPowerFeedforward(-power + Robot.oi.getDriveJoyLeftY(),
                    power + Robot.oi.getDriveJoyLeftY());
        }*/ else {
            Robot.drive.setPowerFeedforward(-Robot.oi.getDriveJoyRightY(), -Robot.oi.getDriveJoyRightY());
        }

        SmartDashboard.putNumber("Left Power", power);
        SmartDashboard.putNumber("Right Power", -power);
        SmartDashboard.putNumber("Robot Angle", robotAngle);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.drive.setPowerZero();
    }

}