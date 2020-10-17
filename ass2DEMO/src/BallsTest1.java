import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.Sleeper;

public class BallsTest1 {
    static private void drawAnimation(Point start, double dx, double dy) {
        GUI gui = new GUI("title", 200, 200);
        Sleeper sleeper = new Sleeper();
        Ball ball = new Ball((int) start.getX(), (int) start.getY(), 30, java.awt.Color.BLACK);
        //ball.setVelocity(dx, dy);
        Velocity v = Velocity.fromAngleAndSpeed(60, 10);
        ball.setVelocity(v);
        while (true) {
            ball.moveOneStep();
            DrawSurface d = gui.getDrawSurface();
            ball.drawOn(d);
            gui.show(d);
            sleeper.sleepFor(50);  // wait for 50 milliseconds.
        }
    }

    public static void main(String[] args) {
        Point newPoint = new Point(100, 50);
        drawAnimation(newPoint, 10, 10);
    }
}
