// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.subsystems.HorizontalElevator;
import frc.robot.subsystems.VerticalElevator;

public class VerticalFirstHorizontalCommand extends CommandBase {
  /** Creates a new HighScore. */

  private VerticalElevator verticalElevator;
  private HorizontalElevator horizontalElevator;
  
  private double verticalSetpoint;
  private double horizontalSetpoint;

  private boolean auto;

  private long time;

  public VerticalFirstHorizontalCommand(VerticalElevator verticalElevator, HorizontalElevator horizontalElevator,
    double verticalSetpoint, double horizontalSetpoint, boolean auto
  ) {
    this.horizontalElevator = horizontalElevator;
    this.verticalElevator = verticalElevator;

    this.verticalSetpoint = verticalSetpoint;
    this.horizontalSetpoint = horizontalSetpoint;

    this.auto = auto;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    this.verticalElevator.setSetpoint(this.verticalSetpoint);

    if (auto) {
      time = System.currentTimeMillis() + 2000;
    }

    // this.horizontalElevator.setSetpoint(this.horizontalSetpoint);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // if (verticalElevator.pid.atSetpoint()) {
    if (verticalElevator.pid.getPositionError() < 0.3) {
      this.horizontalElevator.setSetpoint(this.horizontalSetpoint);
    }


    //FOR AUTOS ONLY! Makes the command run the drive towards PID Commands effectively making it an all in one command.  NOTE: normally it would only be like this but for some reason it needs to run constantly
    if (auto) {
      this.verticalElevator.driveTowardsPid();
      this.horizontalElevator.driveTowardsPid();
    }

    System.out.println("Command working");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (auto) {
      return System.currentTimeMillis() > time;
    } else {
      return this.verticalElevator.pid.atSetpoint() && this.horizontalElevator.pid.atSetpoint();
    }
  }
}
