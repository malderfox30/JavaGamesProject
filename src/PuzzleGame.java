import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PuzzleGame extends JFrame {
    private static final int TABLE_WIDTH = 4;
    private static final int TABLE_HEIGHT = 4;
    private static final int PIECE_SIZE = 200;
    private static final int PADDING = 50;
    private static final int NUM_OF_PIECES = TABLE_WIDTH*TABLE_HEIGHT;

    static int w = TABLE_WIDTH*PIECE_SIZE;
    static int h = TABLE_HEIGHT*PIECE_SIZE;

    static Piece[][] numbers = new Piece[TABLE_HEIGHT][TABLE_WIDTH];
    int WHITE_X;
    int WHITE_Y;

    BufferedImage bi;
    Graphics g;

    public PuzzleGame(){
        this.setTitle("Puzzle");
        this.setSize(w + 2*PADDING, h + 2*PADDING);
        addKeyListener(new TAdapter());
        setBackground(Color.WHITE);
        setFocusable(true);
        this.setVisible(true);
        bi = new BufferedImage(w+PADDING*2, h+PADDING*2, BufferedImage.TYPE_3BYTE_BGR);
        g = bi.getGraphics();
        initGame();
    }

    @Override
    public void paint(Graphics g1) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(PADDING, PADDING, w, h);


            for (int i = 0; i < TABLE_HEIGHT; i++) {
                for(int j = 0; j < TABLE_WIDTH; j++){
                    if(i*TABLE_WIDTH + j != NUM_OF_PIECES - 1){
                        g.setColor(Color.GRAY);
                        g.fillRect(numbers[i][j].x, numbers[i][j].y, PIECE_SIZE, PIECE_SIZE);

                        String num = String.valueOf(numbers[i][j].num);
                        Font font = new Font("Helvetica", Font.BOLD, 36);
                        g.setFont(font);
                        g.setColor(Color.BLACK);
                        g.drawString(num, numbers[i][j].x + PIECE_SIZE/3, numbers[i][j].y + 2*PIECE_SIZE/3);
                    }
                }
            }
            for (int z = 0; z <= TABLE_WIDTH; z++) {
                g.drawLine(PADDING, PADDING + z*PIECE_SIZE, PADDING + TABLE_WIDTH*PIECE_SIZE, PADDING + z*PIECE_SIZE);
                g.drawLine(PADDING + z*PIECE_SIZE, PADDING, PADDING + z*PIECE_SIZE, PADDING + TABLE_WIDTH*PIECE_SIZE);
            }
            Toolkit.getDefaultToolkit().sync();

            if(checkComplete()) gameOver(g);

        g1.drawImage(bi, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public void initGame(){
        int[] shuffleNumbers = shufflePuzzle();

        for(int i = 0; i < TABLE_HEIGHT; i++){
            for(int j = 0; j < TABLE_WIDTH; j++){
                if(i*TABLE_WIDTH + j != NUM_OF_PIECES - 1){
                    numbers[i][j] = new Piece(j*PIECE_SIZE + PADDING, i*PIECE_SIZE + PADDING, shuffleNumbers[TABLE_WIDTH*i + j]);
                }
            }
        }

        WHITE_X = TABLE_WIDTH - 1;
        WHITE_Y = TABLE_HEIGHT - 1;
        repaint();
    }

    public int[] shufflePuzzle() {
        int[] arr = new int[NUM_OF_PIECES - 1];
        for(int i = 0; i < NUM_OF_PIECES - 1; i++){
            arr[i] = i + 1;
        }
        Random rand = new Random();
        for (int i = arr.length - 1; i > 0; i--)
        {
            int index = rand.nextInt(i + 1);
            // Simple swap
            int a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }
        return arr;
    }

    public boolean checkComplete(){
        int s = 0;
        for(int i = 0; i < TABLE_HEIGHT; i++){
            for(int j = 0; j < TABLE_WIDTH; j++){
                if(i == WHITE_X){
                    if(j!= WHITE_Y){
                        System.out.print(findPiece(i, j).num);
                        if(findPiece(i, j).num == j*TABLE_WIDTH + i + 1) s++;
                    }
                }
                else{
                    System.out.print(findPiece(i, j).num);
                    if(findPiece(i, j).num == j*TABLE_WIDTH + i + 1) s++;
                }
            }
        }
        System.out.println(s);
        return (s == NUM_OF_PIECES -1);
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect(0,0, this.getWidth(), this.getHeight());

        String msg = "Congratulations!";
        Font small = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.BLACK);
        g.setFont(small);
        g.drawString(msg, (w - metr.stringWidth(msg)) / 2, h / 2);

        JOptionPane.showMessageDialog(this, "Congratulations! You won!");
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT) {
                if(WHITE_X > 0){
                    findPiece(WHITE_X - 1, WHITE_Y).x += PIECE_SIZE;
                    WHITE_X--;
                }
            }

            if (key == KeyEvent.VK_LEFT) {
                if(WHITE_X < TABLE_WIDTH - 1){
                    findPiece(WHITE_X + 1, WHITE_Y).x -= PIECE_SIZE;
                    WHITE_X++;
                }
            }

            if (key == KeyEvent.VK_UP) {
                if(WHITE_Y < TABLE_HEIGHT - 1){
                    findPiece(WHITE_X, WHITE_Y + 1).y -= PIECE_SIZE;
                    WHITE_Y++;
                }
            }

            if (key == KeyEvent.VK_DOWN) {
                if(WHITE_Y > 0){
                    findPiece(WHITE_X, WHITE_Y - 1).y += PIECE_SIZE;
                    WHITE_Y--;
                }
            }
            System.out.println(WHITE_X+ "     " + WHITE_Y);
            repaint();
        }
    }

    public Piece findPiece(int x, int y){
        Piece thatPiece = null;
        for(int i = 0; i < TABLE_WIDTH; i++){
            for(int j = 0; j < TABLE_HEIGHT; j++){
                if(i*TABLE_WIDTH + j != NUM_OF_PIECES - 1){
                    if(numbers[i][j].x == x*PIECE_SIZE + PADDING && numbers[i][j].y == y*PIECE_SIZE + PADDING)
                        thatPiece =  numbers[i][j];
                }
            }
        }
        return  thatPiece;
    }

    public class Piece{
        int x, y, num;

        public Piece(int x, int y, int num){
            this.x = x;
            this.y = y;
            this.num = num;
        }
    }
}
