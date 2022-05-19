package frc.robot;

import javax.swing.plaf.nimbus.AbstractRegionPainter;

import com.nerdherd.lib.drivetrain.shifting.ShiftHigh;
import com.nerdherd.lib.drivetrain.shifting.ShiftLow;
import com.nerdherd.lib.motor.commands.SetMotorPower;
import com.nerdherd.lib.oi.AbstractOI;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.intake.IntakeBalls;
import frc.robot.commands.intake.Stow;
import frc.robot.commands.other.SetAngle;
import frc.robot.commands.shooting.ShootBall;

public class PS4OI extends AbstractOI {
    public PS4Controller ps4Controller;
    public PS4Controller ps4Controller2;

    public JoystickButton intake_1;
    public JoystickButton startShooting_2;
    public JoystickButton hoodAngle_5;
    public JoystickButton outtakeBrushes_8;
    public JoystickButton stow_10;
    public JoystickButton outtake_LT;
    public JoystickButton stow_RB;
    public JoystickButton shiftHigh_B;
    public JoystickButton shiftLow_A;


    public PS4OI(){
        ps4Controller = new PS4Controller(0);
        ps4Controller2 = new PS4Controller(1);

        intake_1 = new JoystickButton(ps4Controller, Button.kCircle.value); // Actually square button
        startShooting_2 = new JoystickButton(ps4Controller, Button.kTriangle.value); // Actually triangle button
        
        hoodAngle_5 = new JoystickButton(ps4Controller2, Button.kCross.value); // Actually circle button
        outtakeBrushes_8 = new JoystickButton(ps4Controller2, Button.kTriangle.value); // Actually triangle button
        stow_10 = new JoystickButton(ps4Controller2, Button.kCircle.value); // Actually square button
        outtake_LT = new JoystickButton(ps4Controller2, Button.kSquare.value); // Actually cross button
        shiftHigh_B = new JoystickButton(ps4Controller2, Button.kL2.value);
        shiftLow_A = new JoystickButton(ps4Controller2, Button.kR1.value);

        intake_1.whenPressed(new IntakeBalls());
        startShooting_2.whileHeld(new ShootBall());
        hoodAngle_5.whenPressed(new SetAngle().alongWith(
                new InstantCommand(() -> Robot.hopper.setPowerWithoutTop(-0.4, -0.8)),
                new SetMotorPower(Robot.index, -0.33), new InstantCommand(() -> Robot.hopper.setTopHopperPower(0.41))));
        outtakeBrushes_8.whenHeld(new InstantCommand(() -> Robot.hopper.setTopHopperPower(-0.41)));
        stow_10.whenPressed(new Stow());
        // wallShot_11.whenPressed(new WallShot().alongWith(new InstantCommand(() -> Robot.hood.setAngleMotionMagic(Robot.hood.storedAngle))));
        outtake_LT.whenPressed(new SetMotorPower(
            Robot.intakeRoll, -0.75).alongWith(
                new InstantCommand(() -> Robot.hopper.setPowerWithoutTop(-0.4, -0.8)),
                new SetMotorPower(Robot.index, -0.33), new InstantCommand(() -> Robot.hopper.setTopHopperPower(0.41))));
        shiftHigh_B.whenPressed(new ShiftHigh(Robot.drive));
        shiftLow_A.whenPressed(new ShiftLow(Robot.drive));
        // climbReady_X.whenPressed(new ClimberReady());
        // climbLift_Y.whileHeld(new ClimberLift());
    }


    @Override
    public void initLoggingData() {
        // TODO Auto-generated method stub
        
    }


    @Override
    public double getDriveJoyLeftX() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public double getDriveJoyLeftY() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public double getDriveJoyRightX() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public double getDriveJoyRightY() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public double getOperatorJoyX() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public double getOperatorJoyY() {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
