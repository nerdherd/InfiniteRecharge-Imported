/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.nerdherd.lib.drivetrain.experimental.ShiftingDrivetrain;
import com.nerdherd.lib.motor.motorcontrollers.CANMotorController;
import com.nerdherd.lib.motor.motorcontrollers.NerdyFalcon;
import com.nerdherd.lib.pneumatics.Piston;

import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.simulation.SimDeviceDataJNI;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
// import edu.wpi.first.wpilibj.geometry.Pose2d;
// import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.simulation.AnalogGyroSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.VecBuilder;



import frc.robot.Robot;
import frc.robot.RobotMap;
// import frc.robot.commands.auto.Slalom;
import frc.robot.constants.DriveConstants;

// import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends ShiftingDrivetrain {
  /**
   * Creates a new Drive.
   */
  private DifferentialDrivetrainSim m_driveSim;
  // private AnalogGyroSim m_gyroSim;
  private SimDevice m_gyroSim;
  // private EncoderSim m_leftEncoderSim;
  // private EncoderSim m_rightEncoderSim;
  private Field2d m_field;
  
  public Compressor compressor;


  public Drive() {
    super(new NerdyFalcon(RobotMap.kLeftMasterTalonID),
     new NerdyFalcon(RobotMap.kRightMasterTalonID),
    new CANMotorController[] {
      new NerdyFalcon(RobotMap.kLeftFollowerTalon1ID),
    },
    new CANMotorController[] {
      new NerdyFalcon(RobotMap.kRightFollowerTalon1ID),
    },
     true, false, new Piston(RobotMap.  kShifterPort1ID, RobotMap.kShifterPort2ID),
      DriveConstants.kTrackWidth);
      
    // (NerdyFalcon) super.m_leftSlaves[0]
    // super.m_rightSlaves[0].followCANMotorController(super.m_rightMaster);

    // super.m_rightSlaves[]  
    //  super.configAutoChooser(Robot.autoChooser);
     super.configMaxVelocity(DriveConstants.kMaxVelocity);
     super.configSensorPhase(false, true);
     super.configTicksPerFoot(DriveConstants.kLeftTicksPerFoot, DriveConstants.kRightTicksPerFoot);
     super.configLeftPIDF(DriveConstants.kLeftP, DriveConstants.kLeftI, DriveConstants.kLeftD, DriveConstants.kLeftF);
     super.configRightPIDF(DriveConstants.kRightP, DriveConstants.kRightI, DriveConstants.kRightD, DriveConstants.kRightF);
     super.configStaticFeedforward(DriveConstants.kLeftRamseteS, DriveConstants.kRightRamseteS);
    
     super.m_leftMaster.configCurrentLimitContinuous(50);
     super.m_rightMaster.configCurrentLimitContinuous(50);
     super.m_leftMaster.configCurrentLimitPeak(50);
     super.m_rightMaster.configCurrentLimitPeak(50);

    //  super.m_leftMaster.configMotionMagic(2, 1);
    //  super.m_rightMaster.configMotionMagic(2, 1);

     super.m_leftSlaves[0].configCurrentLimitContinuous(50);
     super.m_rightSlaves[0].configCurrentLimitContinuous(50);
     super.m_leftSlaves[0].configCurrentLimitPeak(50);
     super.m_rightSlaves[0].configCurrentLimitPeak(50);
     setCoastMode();

     
     compressor = new Compressor(3, PneumaticsModuleType.CTREPCM);
     
    //  m_leftEncoder.setDistancePerPulse(Constants.DriveConstants.kEncoderDistancePerPulse)
    //  m_leftEncoder.setDistancePerPulse(Constants.DriveConstants.kEncoderDistancePerPulse)

     resetEncoders();
     m_field = new Field2d();
     SmartDashboard.putData("Field", m_field);

     if(RobotBase.isSimulation()){
      m_driveSim = new DifferentialDrivetrainSim(
        LinearSystemId.identifyDrivetrainSystem(
          DriveConstants.kVLinear, DriveConstants.kALinear, DriveConstants.kVAngular, DriveConstants.kAAngular), 
        DCMotor.getFalcon500(2), 
        DriveConstants.gearReduction, 
        DriveConstants.kTrackWidth, 
        DriveConstants.wheelRadiusMeters, 
        VecBuilder.fill(0.001, 0.001, 0.001, 0.1, 0.1, 0.005, 0.005));
      
        m_gyroSim = SimDevice.create("navX-Sensor", 0);
     }
  }

  @Override
  public void simulationPeriodic() {
    // m_driveSim.setInputs(-m_leftMaster.getVoltage() * RobotController.getBatteryVoltage(),
        // m_rightMaster.getVoltage() * RobotController.getBatteryVoltage());
    // m_driveSim.setPose(new Pose2d(0.762, 0.762, new Rotation2d(0))); // Slalom
    // m_driveSim.setPose( new Pose2d(0.762, 2.032, new Rotation2d(0))); // Bounce
    m_driveSim.setInputs(simLeftVolt, simRightVolt);
    m_driveSim.update(0.02);
    int dev = SimDeviceDataJNI.getSimDeviceHandle("navX-Sensor[0]");
    SimDouble angle = new SimDouble(SimDeviceDataJNI.getSimValueHandle(dev, "Yaw"));
    angle.set(-m_driveSim.getHeading().getDegrees());

    // m_leftEncoderSim.setDistance(m_driveSim.getLeftPositionMeters());
    // m_leftEncoderSim.setRate(m_driveSim.getLeftVelocityMetersPerSecond());
    // m_rightEncod erSim.setDistance(m_driveSim.getRightPositionMeters());
    // m_rightEncoderSim.setRate(m_driveSim.getRightVelocityMetersPerSecond());

    SmartDashboard.putNumber("Sim Pose X Meters", m_driveSim.getPose().getX());
    SmartDashboard.putNumber("Sim Pose Y Meters", m_driveSim.getPose().getY());
    SmartDashboard.putNumber("Sim heading", m_driveSim.getPose().getRotation().getDegrees());
    m_field.setRobotPose(m_driveSim.getPose());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    super.updateOdometry();
    super.reportToSmartDashboard();
  }
}
