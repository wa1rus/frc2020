
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.unit.subsystems.drivetrain;

import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.junit.Before;
import org.junit.Test;

import frc.robot.common.ConsoleLogger;
import frc.robot.common.IDrivetrainSubsystem;
import frc.robot.common.ILogger;
import frc.robot.common.Location;
import frc.robot.common.Position;
import frc.robot.mocks.subsystems.MockDrivetrain;

/**
 * Add your docs here.
 */

@RunWith(DataProviderRunner.class)
public class Feature_move_robot_using_high_level_commands {
    Location _location;
    ILogger _logger;
    IDrivetrainSubsystem _drivetrain;

    @DataProvider
    public static Object[][] rotationData() {
        // initialHeading, rotation, finalHeading
        return new Object[][] { { 10, 50, 60 }, { 350, 20, 10 }, { 40, -60, 340 }, { 180, -40, 140 } };
    }

    @DataProvider
    public static Object[][] moveForwardData() {
        // heading, initalPos(x,y), distance, finalPos(x,y)
        return new Object[][] { { 0, new Position(0, 0), 1.5, new Position(0, 1.5) },
                { 0, new Position(2.4, 4.9), 2.5, new Position(2.4, 7.4) },
                // with rotation
                { 45, new Position(0, 0), 8.6, new Position(6.08111, 6.08111) },
                { 90, new Position(2, 1), 2, new Position(4, 1) }, { 180, new Position(2, 2), 2, new Position(2, 0) },
                { 270, new Position(2, 2), 2, new Position(0, 2) },
                // invalid values wont move the robot
                { 0, new Position(1, 1), -1, new Position(1, 1) } };
    }

    @DataProvider
    public static Object[][] moveBackwardData() {
        // initalPos(x,y), distance, finalPos(x,y)
        return new Object[][] { { new Position(0, 0), 1.5, new Position(0, 0) },
                { new Position(2.4, 4.9), 2.5, new Position(2.4, 2.4) },
                // invalid values wont move the robot
                { new Position(1, 1), -1, new Position(1, 1) } };
    }

    @Before
    public void beforeEachTest() {
        _location = new Location();
        _logger = new ConsoleLogger();
        _drivetrain = new MockDrivetrain(_logger, _location);
    }

    @Test
    @UseDataProvider("rotationData")
    public void Scenario_rotate_robot_by_x_degrees(double initialHeading, double rotation, double finalHeading) {

        _location.updateHeading(initialHeading);
        _drivetrain.rotate(rotation);
        assertEquals(finalHeading, _location.getHeading(), 0);
    }

    @Test
    @UseDataProvider("moveForwardData")
    public void Scenario_move_forward(double heading, Position initialPosition, double distance,
            Position finalPosition) {

        _location.updatePosition(initialPosition);
        _location.updateHeading(heading);
        _drivetrain.moveForward(distance);
        Position pos = _location.getPosition();

        assertEquals(finalPosition.getX(), pos.getX(), 0.001);
        assertEquals(finalPosition.getY(), pos.getY(), 0.001);
    }

    @Test
    @UseDataProvider("moveBackwardData")
    public void Scenario_move_backward(Position initialPosition, double distance, Position finalPosition) {

        _location.updatePosition(initialPosition);
        _drivetrain.moveBackward(distance);
        Position pos = _location.getPosition();

        assertEquals(finalPosition.getX(), pos.getX(), 0.001);
        assertEquals(finalPosition.getY(), pos.getY(), 0.001);
    }

    // @Test
    // // @UseDataProvider("moveBackwardData")
    // public void Scenario_move_North(Position initialPosition, double distance,
    // Position finalPosition) {

    // _location.updatePosition(initialPosition);
    // _drivetrain.moveNorth(distance);
    // Position pos = _location.getPosition();

    // assertEquals(finalPosition.getX(), pos.getX(), 0.001);
    // assertEquals(finalPosition.getY(), pos.getY(), 0.001);
    // }
}
