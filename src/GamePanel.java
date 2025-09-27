import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

// The class GamePanel will inherit from the Class JPanel and will use abstract methods from the ActionListener class
public class GamePanel extends JPanel implements ActionListener {

    // Definition of the size of the rendered screen
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    // The screen will be divided into a 2D array and each unit will represent the size of the apple and the size of the body parts of the snake
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    // This variable will set the speed of the game
    static final int DELAY = 75;
    // X and Y array will hold all the body part of the snake
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    // Number of body parts the snake will have
    int bodyParts = 6;
    // Number of apples the snake has eaten
    int applesEaten = 0;
    // X and Y coordinates of the current apple
    int appleX;
    int appleY;
    // This variable indicates the direction the snake is currently moving: R - Right, L - Left, U - Up and D - Down
    // The game will start with the snake going right
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    // Class constructor method
    GamePanel() {
        // Create an instance of the random class
        random = new Random();
        // Set a preferred size for the game panel
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        // Set background color for game to black
        this.setBackground(Color.black);
        // Component can receive keyboard focus
        this.setFocusable(true);
        // Can respond to keyboard events with the current component
        this.addKeyListener(new MyKeyAdapter());
        // After finishing construction GamePanel, we will call startGame method
        startGame();
    }

    // Method that starts the game
    public void startGame() {
        // When starting the game, we will call the newApple method to create an apple in the screen
        newApple();
        // The game will start running
        running = true;
        // Will finish creating the instance for the timer using the delay declared above
        // We pass the "this" keyword since we are using the ActionListener listener
        timer = new Timer (DELAY, this);
        // We start the timer
        timer.start();
    }

    public void paintComponent(Graphics g) {
        // Handles the default painting behavior of the component, ensures proper rendering and provides graphics context
        super.paintComponent(g);
        draw(g);
    }

    // This method will create a grid, so that each element in the game is part of a single square in the grid
    public void draw(Graphics g) {

        if (running) {
            // We will create lines inside our game panel so that it's easier to see the elements inside our game
            // We will draw lines in the X and Y axis
            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            // We will start drawing the apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            // Then we will start by drawing the snake's head and body
            // Will use a for loop for drawing all the parts of the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // We show on the top of the screen the score for the current game
            g.setColor(Color.red);
            g.setFont(new Font("Century Gothic",Font.BOLD,40));
            // We use FontMetrics in order to be able to line the text in the center of the screen
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }

    // Generate randomly the coordinates for the apple
    public void newApple() {
        // Set the apple somewhere in the X and Y axis and inside one of the squares of the grid
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        // We want the apple to be place evenly inside one of the square in the grid and not anywhere else
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }

    // This method will be in charge of moving the snake after each delay
    public void move() {
        // We will be shifting the body parts of the snake around
        for (int i = bodyParts; i > 0; i--) {
            // We are shifting all the elements of the array over one spot
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        // We will control the direction of where the snake is headed
        switch (direction) {
            // Case for snake moving up
            case 'U':
                // y[0] is the head of the snake
                y[0] = y[0] - UNIT_SIZE;
                break;
            // Case for snake moving down
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            // Case for snake moving left
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            // Case for snake moving right
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
         }
    }

    // This method check if the snake eats the apple
    // If the snake eats the apple, the snake get longer by one, a new apple spawns and the apples eaten counter increases
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    // This method checks if the snakes collides with itself or with the screen four borders
    public void checkCollisions() {
        // This checks if the head of the snake collides with its body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && y[0] == y[i]) {
                running = false;
            }
        }
        // Check if snake's head touches left border of the screen
        if (x[0] < 0) {
            running = false;
        }
        // Check if snake's head touches right border of the screen
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // Check if snake's head touches top border of the screen
        if (y[0] < 0) {
            running = false;
        }
        // Check if snake's head touches bottom border of the screen
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        // End the timer method of the program
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // We draw a "GAME OVER" text over the screen when the game ends
        g.setColor(Color.red);
        g.setFont(new Font("Century Gothic",Font.BOLD,75));
        // We use FontMetrics in order to be able to line the text in the center of the screen
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        // We show in the game over screen the final score of the game
        g.setColor(Color.red);
        g.setFont(new Font("Century Gothic",Font.BOLD,40));
        // We use FontMetrics in order to be able to line the text in the center of the screen
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Final Score: " + applesEaten,(SCREEN_WIDTH - metrics2.stringWidth("Final Score: " + applesEaten))/2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                // Switch case to make snake go left
                case KeyEvent.VK_LEFT:
                    // This if condition checks current snake direction, in order to not let user move the snake into itself
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                // Switch case to make snake go right
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                // Switch case to make snake go up
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                // Switch case to make snake go down
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;

            }
        }

    }
}
