import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class FormDots extends JFrame implements Runnable, MouseListener, MouseMotionListener, KeyListener {

    private final int w = 1280;
    private final int h = 720;

    Point mousePoint = new Point(0, 0);

    int numColors = 3;
    Color[] colors = {new Color(0, 255, 0), new Color(0, 0, 255), new Color(255, 0, 0)};

    int curColor = 0;
    double learningRateDiff = 0;
    boolean exitNow = false;
    boolean displayInfo = false;
    boolean resetNN = false;

    private BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    private BufferedImage pimg = new BufferedImage(w / 8, h / 8, BufferedImage.TYPE_INT_RGB);
    private int frame = 0;

    private NeuralNetwork nn;

    public List<ColoredPoint> points = new ArrayList<>();

    public FormDots() {
        UnaryOperator<Double> sigmoid = x -> 1 / (1 + Math.exp(-x));
        UnaryOperator<Double> dsigmoid = y -> y * (1 - y);
        nn = new NeuralNetwork(0.01, sigmoid, dsigmoid, 2, 10, 6, 3, numColors);

        this.setSize(w + 16, h + 38);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(50, 50);
        this.add(new JLabel(new ImageIcon(img)));
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }

    @Override
    public void run() {
        while (!exitNow)
            this.repaint();

        System.exit(0);
    }

    @Override
    public void paint(Graphics g) {
        if(points.size() > 0) {
            for (int k = 0; k < 10000; k++) {
                ColoredPoint p = points.get((int) (Math.random() * points.size()));
                double nx = (double) p.x / w - 0.5;
                double ny = (double) p.y / h - 0.5;
                nn.feedForward(new double[]{nx, ny});
                double[] targets = new double[numColors];
                targets[p.type] = 1;
                nn.backpropagation(targets);
            }
        }
        for (int i = 0; i < w / 8; i++) {
            for (int j = 0; j < h / 8; j++) {
                double nx = (double) i / w * 8 - 0.5;
                double ny = (double) j / h * 8 - 0.5;

                double[] outputs = nn.feedForward(new double[]{nx, ny});
                double red = 0, green = 0, blue = 0;
                double sumK = 0;

                for (int k = 0; k < outputs.length; k++) {
                    sumK  += outputs[k];

                    red   += outputs[k] * (double) colors[k].getRed();
                    green += outputs[k] * (double) colors[k].getGreen();
                    blue  += outputs[k] * (double) colors[k].getBlue();
                }

                if (sumK > 0.001) {
                    red   /= sumK * 255;
                    green /= sumK * 255;
                    blue  /= sumK * 255;
                }

                int color = ((int)(red * 155 + 100) << 16) | ((int)(green * 155 + 100) << 8) | (int)(blue * 155 + 100);
                pimg.setRGB(i, j, color);
            }
        }
        Graphics ig = img.getGraphics();
        ig.drawImage(pimg, 0, 0, w, h, this);
        for (ColoredPoint p : points) {
            ig.setColor(Color.WHITE);
            ig.fillOval(p.x - 3, p.y - 3, 26, 26);
            ig.setColor(colors[p.type]);
            ig.fillOval(p.x, p.y, 20, 20);
        }

        ig.setColor(Color.WHITE);
        ig.fillOval(mousePoint.x - 16 - 3, mousePoint.y - 38 - 3, 26, 26);
        ig.setColor(colors[curColor]);
        ig.fillOval(mousePoint.x - 16, mousePoint.y - 38, 20, 20);

        if(resetNN) {
            resetNN = false;
            nn.reset();
        }

        ig.setColor(Color.WHITE);
        ig.setFont(ig.getFont().deriveFont(45.0f));
        if(displayInfo) {
            ig.drawString("For info press F1", 38, 59);
            ig.drawString("To exit press ESC", 38, 109);
            ig.drawString("To switch color press N", 38, 159);
            ig.drawString("To reset neural net press R", 38, 209);
            ig.drawString("Learning rate: " + nn.learningRate, 38, 259);
            ig.drawString("Use UP and DOWN to modify", 38, 309);
        }

        if((nn.learningRate < 0.05 && learningRateDiff > 0) || (nn.learningRate > 0.0001 && learningRateDiff < 0))
            nn.learningRate += learningRateDiff;

        g.drawImage(img, 8, 30, w, h, this);
        frame++;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1)
            points.add(new ColoredPoint(e.getX() - 16, e.getY() - 38, curColor));
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            exitNow = true;
        if(e.getKeyCode() == KeyEvent.VK_F1)
            displayInfo = true;
        if(e.getKeyCode() == KeyEvent.VK_R)
            resetNN = true;
        if(e.getKeyCode() == KeyEvent.VK_P)
            points.removeAll(points);
        if(e.getKeyCode() == KeyEvent.VK_UP)
            learningRateDiff = 0.00005;
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
            learningRateDiff = -0.00005;
        if(e.getKeyCode() == KeyEvent.VK_N) {
            ++curColor;
            curColor %= numColors;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_F1)
            displayInfo = false;
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
            learningRateDiff = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePoint = e.getPoint();
    }
}