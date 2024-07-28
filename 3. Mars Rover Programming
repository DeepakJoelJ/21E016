import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Enum for Directions
enum Direction {
    NORTH, EAST, SOUTH, WEST
}

// Command Interface
interface Command {
    void execute(Rover rover);
}

// Concrete Commands
class MoveCommand implements Command {
    @Override
    public void execute(Rover rover) {
        rover.moveForward();
    }
}

class TurnLeftCommand implements Command {
    @Override
    public void execute(Rover rover) {
        rover.turnLeft();
    }
}

class TurnRightCommand implements Command {
    @Override
    public void execute(Rover rover) {
        rover.turnRight();
    }
}

// Rover Class
class Rover {
    private int x, y;
    private Direction direction;
    private Grid grid;

    public Rover(int x, int y, Direction direction, Grid grid) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.grid = grid;
    }

    public void moveForward() {
        int[] delta = getDirectionDelta();
        int newX = x + delta[0];
        int newY = y + delta[1];
        if (grid.isWithinBounds(newX, newY) && !grid.hasObstacle(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public void turnLeft() {
        Direction[] directions = Direction.values();
        int currentIndex = direction.ordinal();
        direction = directions[(currentIndex + 3) % 4]; // Turning left
    }

    public void turnRight() {
        Direction[] directions = Direction.values();
        int currentIndex = direction.ordinal();
        direction = directions[(currentIndex + 1) % 4]; // Turning right
    }

    private int[] getDirectionDelta() {
        switch (direction) {
            case NORTH:
                return new int[]{0, 1};
            case EAST:
                return new int[]{1, 0};
            case SOUTH:
                return new int[]{0, -1};
            case WEST:
                return new int[]{-1, 0};
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    public String statusReport() {
        return String.format("Rover is at (%d, %d) facing %s. No Obstacles detected.", x, y, direction);
    }
}

// Grid Class
class Grid {
    private int width, height;
    private Set<Point> obstacles;

    public Grid(int width, int height, List<Point> obstacles) {
        this.width = width;
        this.height = height;
        this.obstacles = new HashSet<>(obstacles);
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean hasObstacle(int x, int y) {
        return obstacles.contains(new Point(x, y));
    }
}

// Point Class for Obstacles
class Point {
    private final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}

// Simulation Function
public class MarsRoverSimulation {
    public static void simulateRover(Grid grid, Rover rover, List<Command> commands) {
        for (Command command : commands) {
            command.execute(rover);
        }
        System.out.println(rover.statusReport());
    }

    public static void main(String[] args) {
        Grid grid = new Grid(10, 10, List.of(new Point(2, 2), new Point(3, 5)));
        Rover rover = new Rover(0, 0, Direction.NORTH, grid);

        List<Command> commands = List.of(
            new MoveCommand(),     // Move to (0, 1)
            new MoveCommand(),     // Move to (0, 2)
            new TurnRightCommand(), // Turn to EAST
            new MoveCommand(),     // Move to (1, 2)
            new TurnLeftCommand(),  // Turn back to NORTH
            new MoveCommand()      // Move to (1, 3)
        );

        simulateRover(grid, rover, commands);
    }
}
