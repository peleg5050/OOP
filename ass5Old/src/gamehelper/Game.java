// ID: 208387969

package gamehelper;


import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;
import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import interfaces.Collidable;
import interfaces.Sprite;
import sprites.Ball;
import sprites.Block;
import sprites.Paddle;

import java.awt.Color;

/**
 * gamehelper.Game - The class hold the sprites and the collidables, and will be in charge of the animation
 * The class create a list of sprites and a list of environment, initialize a new game and run the game.
 */
public class Game {
    // The amount of the blocks at the beginning
    private static final int AMOUNT_BLOCKS = 57;
    // The amount of the balls at the beginning
    private static final int AMOUNT_BALLS = 3;
    private static final int ZERO = 0;
    // Characteristics
    private GUI gui;
    // gamehelper.Counter of the blocks
    private Counter counterBlocks;
    // gamehelper.Counter of the balls
    private Counter counterBalls;
    // gamehelper.Counter of the score
    private Counter counterScore;
    // Const number of the size of the board
    public static final int SIZE_OF_BOARD_X = 800;
    public static final int SIZE_OF_BOARD_Y = 600;
    // Const number of the thickness (width) of the board
    public static final int THICKNESS_OF_BOARD = 20;
    // Const number of the width of the block
    public static final int WIDTH_OF_BLOCK = 40;
    // Const number of the height of the block
    public static final int HEIGHT_OF_BLOCK = 25;
    // The width of the paddle
    public static final int WIDTH_OF_PADDLE = 100;
    // The height of the paddle
    public static final int HEIGHT_OF_PADDLE = 10;
    // Const number for the start rectangle point (x and y)
    public static final int START_RECT_X = SIZE_OF_BOARD_X - 20;
    public static final int START_RECT_Y = SIZE_OF_BOARD_Y - 350;
    // Const number of the radius of the ball
    public static final int RADIUS = 5;
    // The angle of ball 1
    public static final double ANGLE_BALL1 = 25;
    // The angle of ball 2
    public static final double ANGLE_BALL2 = 25;
    // The angle of ball 3
    public static final double ANGLE_BALL3 = 25;
    // The angle of ball 1
    public static final double SPEED_BALL1 = 5;
    // The angle of ball 2
    public static final double SPEED_BALL2 = 5;
    // The angle of ball 3
    public static final double SPEED_BALL3 = 5;
    private SpriteCollection sprites;
    private GameEnvironment environment;
    // The point of the start of the board (the upper left point)
    private Point pointUpperLeft = new Point(0, 0);

    /**
     * Constructor that create a list of sprites and a list of environment.
     */
    public Game() {
        // Create a list of the environment (things that can be collided with) in the environment
        this.sprites = new SpriteCollection();
        // Create a list of the environment (things that can be collided with) in the environment
        this.environment = new GameEnvironment();
        // Create a gamehelper.Counter of the blocks (initial in the size of the blocks at the beginning of the game)
        this.counterBlocks = new Counter(AMOUNT_BLOCKS);
        // Create a gamehelper.Counter of the balls (initial in the size of the balls at the beginning of the game)
        this.counterBalls = new Counter(AMOUNT_BALLS);
        // Create a gamehelper.Counter of the score (initial in 0)
        this.counterScore = new Counter(ZERO);
    }

    /**
     * addCollidable - Add the given collidable.
     *
     * @param collidable - The collide object that we want to add.
     */
    public void addCollidable(Collidable collidable) {
        // Add the collidable object to the List of the collidables
        this.environment.addCollidable(collidable);
    }

    /**
     * addCollidable - Add the given sprite.
     *
     * @param sprite - The sprite object that we want to add.
     */
    public void addSprite(Sprite sprite) {
        // Add the sprite object to the List of the sprites
        this.sprites.addSprite(sprite);
    }

    /**
     * initialize - Initialize a new game.
     * create the Blocks , sprites.Ball and sprites.Paddle , and add them to the game.
     */
    public void initialize() {
        // Create a PrintingHitListener
        //PrintingHitListener printHitListener = new PrintingHitListener();
        // Create a gamehelper.BlockRemover (that holds a reference to the counter)
        BlockRemover blockRemover = new BlockRemover(this, this.counterBlocks);
        // Create a gamehelper.BallRemover (that holds a reference to the counter)
        BallRemover ballRemover = new BallRemover(this, this.counterBalls);
        // Create a Score Tracking Listener
        ScoreTrackingListener scoreTracking = new ScoreTrackingListener(counterScore);
        // Create a window with the title "gamehelper.Game"
        // which is 800 pixels wide and 600 pixels high
        gui = new GUI("gamehelper.Game", SIZE_OF_BOARD_X, SIZE_OF_BOARD_Y);
        // KeyboardSensor interface from the biuoop class (created from a gui object) - help us to read the key presses
        biuoop.KeyboardSensor keyboard = gui.getKeyboardSensor();
        // Create blocks
        // left rib
        Block block1 = new Block(new Rectangle(new Point(pointUpperLeft.getX(), THICKNESS_OF_BOARD),
                THICKNESS_OF_BOARD, SIZE_OF_BOARD_Y), Color.darkGray);
        // right rib
        Block block2 = new Block(new Rectangle(new Point((SIZE_OF_BOARD_X - THICKNESS_OF_BOARD), THICKNESS_OF_BOARD),
                THICKNESS_OF_BOARD, (SIZE_OF_BOARD_Y - THICKNESS_OF_BOARD)), Color.darkGray);
        // upper rib
        Block block3 = new Block(new Rectangle(pointUpperLeft, SIZE_OF_BOARD_X, THICKNESS_OF_BOARD), Color.darkGray);
        // lower rib
        Block block4 = new Block(new Rectangle(new Point(pointUpperLeft.getX(), SIZE_OF_BOARD_Y),
                SIZE_OF_BOARD_X, THICKNESS_OF_BOARD), Color.GRAY);
        // Add the blocks to the game
        block1.addToGame(this);
        block2.addToGame(this);
        block3.addToGame(this);
        block4.addToGame(this);
        // Register the gamehelper.BallRemover as a listener of the death-region block (the lower block),
        // so that gamehelper.BallRemover will be notified whenever a ball hits the death-region.
        block4.addHitListener(ballRemover);
        // Create the blacks
        //
        // runs on the 6 rows (from the low row to the height row, and for each row add one more block)
        for (int i = 0; i < 6; i++) {
            // The X and the Y value of the upper left point of the current block
            double x, y;
            // The upper left point of the rectangle
            Point upperLeft;
            // The current rectangle
            Rectangle rectangle;
            for (int j = 1; j < 8 + i; j++) {
                // Chose color for the block
                int numColor = i;
                Color color;
                switch (numColor) {
                    // Then colorRandom = ....
                    case 0:
                        color = Color.GREEN;
                        break;
                    case 1:
                        color = Color.PINK;
                        break;
                    case 2:
                        color = Color.ORANGE;
                        break;
                    case 3:
                        color = Color.MAGENTA;
                        break;
                    case 4:
                        color = Color.DARK_GRAY;
                        break;
                    case 5:
                        color = Color.CYAN;
                        break;
                    default:
                        color = Color.white;
                }
                x = START_RECT_X - (j * WIDTH_OF_BLOCK);
                y = START_RECT_Y - (i * HEIGHT_OF_BLOCK);
                // Create the upper left point of the rectangle
                upperLeft = new Point(x, y);
                // Create the rectangle
                rectangle = new Rectangle(upperLeft, WIDTH_OF_BLOCK, HEIGHT_OF_BLOCK);
                Block block = new Block(rectangle, color);
                block.addToGame(this);
                //block.addHitListener(printHitListener);
                // The blockRemover will be the listener of each block
                block.addHitListener(blockRemover);
                // The scoreTracking will be the listener of each block
                block.addHitListener(scoreTracking);
            }
        }
        // Create a new block for the paddle paddle
        //
        Block paddleBlock = new Block(new Rectangle(new Point(400, 570), WIDTH_OF_PADDLE,
                HEIGHT_OF_PADDLE), Color.GREEN);
        // Create a new paddle
        Paddle paddle = new Paddle(keyboard, paddleBlock);
        // Add the paddle to the game
        paddle.addToGame(this);
        // Create 2 balls
        //
        // Create a new ball (ball1) (in black color) with radius , and his center is the starting point that we got
        Ball ball1 = new Ball(70, 200, RADIUS, Color.red);
        // Set the velocity for ball 1
        Velocity velocity1 = Velocity.fromAngleAndSpeed(ANGLE_BALL1, SPEED_BALL1);
        ball1.setVelocity(velocity1);
        ball1.setGame(this.environment);
        // Add ball1 to the game
        ball1.addToGame(this);
        // Create a new ball (ball2) (in gray color) with radius , and his center is the starting point that we got
        Ball ball2 = new Ball(40, 200, RADIUS, Color.DARK_GRAY);
        // Set the velocity for ball 2
        Velocity velocity2 = Velocity.fromAngleAndSpeed(ANGLE_BALL2, SPEED_BALL2);
        ball2.setVelocity(velocity2);
        ball2.setGame(this.environment);
        // Add the ball to the game
        ball2.addToGame(this);
        // Create a new ball (ball3) (in blue color) with radius , and his center is the starting point that we got
        Ball ball3 = new Ball(55, 200, RADIUS, Color.BLUE);
        // Set the velocity for ball 3
        Velocity velocity3 = Velocity.fromAngleAndSpeed(ANGLE_BALL3, SPEED_BALL3);
        ball3.setVelocity(velocity3);
        ball3.setGame(this.environment);
        // Add the ball to the game
        ball3.addToGame(this);
    }

    /**
     * run - The function run the game (start the animation loop).
     */
    public void run() {
        Sleeper sleeper = new Sleeper();
        // How many frames per seconds pass (60)
        int framesPerSecond = 60;
        // How many milliseconds Per Frame pass (1000)
        int millisecondsPerFrame = 1000 / framesPerSecond;
        // Create a Score indicator
        ScoreIndicator scoreIndicator = new ScoreIndicator(counterScore);
        while (true) {
            // timing
            long startTime = System.currentTimeMillis();
            // The draw
            DrawSurface draw = gui.getDrawSurface();
            draw.setColor(Color.LIGHT_GRAY);
            draw.fillRectangle((int) pointUpperLeft.getX(), (int) pointUpperLeft.getY(),
                    SIZE_OF_BOARD_X, SIZE_OF_BOARD_Y);
            // Add the sprites to the draw (draw the sprites)
            this.sprites.drawAllOn(draw);
            scoreIndicator.drawOn(draw);
            // Show the draw
            gui.show(draw);
            this.sprites.notifyAllTimePassed();
            // timing
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
            // Close the program when the amount of the blocks is 0
            if (counterBlocks.getValue() == ZERO) {
                // Clearning an entire level (destroying all blocks) is worth another 100 points.
                counterScore.increase(100);
                gui.close();
            }
            if (counterBalls.getValue() == ZERO) {
                gui.close();
            }
        }
    }

    /**
     * removeCollidable - Remove the given collidable (and by this we remove blocks from the game).
     *
     * @param collide - The things that can be collided with (as Blocks and the sprites.Paddle)
     * (that we wont to remove).
     */
    public void removeCollidable(Collidable collide) {
        // Remove the collidable object from the List of the collidables
        this.environment.removeCollide(collide);

    }

    /**
     * removeSprite - Remove the given sprite (and by this we remove blocks from the game).
     *
     * @param sprite - The object that can be drawn to the screen (that we wont to remove).
     */
    public void removeSprite(Sprite sprite) {
        // Remove the sprite object from the List of the sprites
        this.sprites.removeGivenSprite(sprite);
    }
}
