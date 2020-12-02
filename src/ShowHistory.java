import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ShowHistory extends JFrame {
    Container container = getContentPane();
    JButton playBtn = new JButton("PLAY");
    JButton exitBtn = new JButton("EXIT");
    Label historyLbl = new Label();
    Label greetingLbl = new Label();
    JTable table;
    JScrollPane sp;
    String username;
    String[][] data;
    DatabaseUtils databaseUtils = new DatabaseUtils();
    int highScore;

    public ShowHistory(String username){
        this.username = username;

        highScore = databaseUtils.getUserHighScore(username);
        data = databaseUtils.getUserHistory(username);
        String[] column = {"Date", "Score"};

        greetingLbl.setText("Welcome " + username + "! Your high score: " + highScore);
        historyLbl.setText("History");
        table = new JTable(data, column);
        sp = new JScrollPane(table);

        greetingLbl.setBounds(50,20, 300, 30);
        historyLbl.setBounds(50, 70, 300, 30);
        sp.setBounds(0, 100, 400, 300);
        playBtn.setBounds(50, 450, 100, 30);
        exitBtn.setBounds(250, 450, 100, 30);

        container.setLayout(null);
        container.add(greetingLbl);
        container.add(historyLbl);
        container.add(sp);
        container.add(playBtn);
        container.add(exitBtn);

        playBtn.addActionListener(e -> {
            SnakeClassic snakeClassic = new SnakeClassic(username);
            //PuzzleGame puzzleGame = new PuzzleGame();
            this.setVisible(false);
            this.dispose();
        });

        exitBtn.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame();
            this.setVisible(false);
            this.dispose();
        });

        this.setTitle("Game Lobby");
        this.setBounds(10, 10, 400, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        System.out.println(Arrays.deepToString(data));
    }
}
