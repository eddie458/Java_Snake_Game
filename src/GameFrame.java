import javax.swing.JFrame;

public class GameFrame extends JFrame {
    GameFrame() {
        GamePanel panel  = new GamePanel();
        this.add(panel);
        // Set the name of the window
        this.setTitle("Snake Game by Eddie458");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // The user can not change the size of the window
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        // The screen will always appear in the middle of the computer screen
        this.setLocationRelativeTo(null);
    }
}
