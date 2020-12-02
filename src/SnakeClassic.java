import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class SnakeClassic extends JFrame implements ActionListener {
    private final int DELAY = 200;
    private final int SNAKE_PADDING = 1;
    static int w = 400, h = 400;
    int size = 40;

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    static int[] x = new int[100];
    static int[] y = new int[100];
    static int length;
    static int userScore;

    int food_x;
    int food_y;

    Random rand = new Random();
    Timer timer;

    BufferedImage bi;
    Graphics g;
    DatabaseUtils databaseUtils = new DatabaseUtils();

    String username;
    int highScore;

    public SnakeClassic(String username){
        this.username = username;
        highScore = databaseUtils.getUserHighScore(username);
        this.setTitle("Snake Classic");
        this.setSize(w+size*2, h+size*2);
        addKeyListener(new TAdapter());
        setBackground(Color.WHITE);
        setFocusable(true);
        this.setVisible(true);
        bi = new BufferedImage(w+size*2, h+size*2, BufferedImage.TYPE_3BYTE_BGR);
        g = bi.getGraphics();
        userScore = 0;

        initGame();

    }


    public void initGame(){
        length = 1;
        for (int z = 0; z < length; z++) {
            x[z] = 120;
            y[z] = 120;
        }

        locateFood();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g1) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(size, size, w, h);

        userScore = 10*(length-1);
        String score = username + ", Score: "+userScore;
        Font font = new Font("Arial", Font.PLAIN, 14);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString(score, 50, 460);

        if (inGame) {
            g.setColor(Color.BLACK);
            g.fillRect(food_x + SNAKE_PADDING, food_y + SNAKE_PADDING, size - 2*SNAKE_PADDING, size - 2*SNAKE_PADDING);

            for (int z = 0; z < length; z++) {
                g.setColor(Color.RED);
                g.fillRect(x[z] + SNAKE_PADDING , y[z]+  SNAKE_PADDING, size - 2*SNAKE_PADDING, size - 2*SNAKE_PADDING);
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }

        g1.drawImage(bi, 0, 0, this.getWidth(), this.getHeight(), null);
    }


    private void gameOver(Graphics g) {
        JOptionPane.showMessageDialog(this, "Game over! Your score: "+ (length-1)*10);

        databaseUtils.insertScore(username, userScore);
        if(userScore > highScore){
            databaseUtils.updateHighScore(username, userScore);
        }
        ShowHistory showHistory = new ShowHistory(username);
        this.setVisible(false);
        this.dispose();
    }

    private void checkFood() {

        if ((x[0] == food_x) && (y[0] == food_y)) {
            length++;
            locateFood();
        }
    }

    private void move() {

        for (int z = length; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] = (x[0] - size) < size ? w : x[0] - size;
        }

        if (rightDirection) {
            x[0] = x[0] + size >= w + size ? size : x[0] + size;
        }

        if (upDirection) {
            y[0] = y[0] -  size < size ? h : y[0] - size;
        }

        if (downDirection) {
            y[0] = y[0] + size >= h + size ? size : y[0] + size;
        }
    }

    private void checkCollision() {

        for (int z = length; z > 0; z--) {
            if ((length > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void locateFood() {
        boolean rightPlace;

        food_x = (int)((rand.nextDouble()*400+size)/size)*size;
        food_y = (int)((rand.nextDouble()*400+size)/size)*size;

        do{
            rightPlace = true;
            for(int i = 0 ; i < x.length; i++){
                if(food_x == x[i] && food_y == y[i]){
                    food_x = (int)((rand.nextDouble()*400+size)/size)*size;
                    food_y = (int)((rand.nextDouble()*400+size)/size)*size;
                    rightPlace = false;
                    break;
                }
            }
            if(rightPlace){
                break;
            }
        }while (!rightPlace);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkFood();
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}