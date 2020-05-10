import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.UnaryOperator;

public class Main {

    public static void main(String[] args) throws IOException {
        dots();
    }

    private static void dots() {
        FormDots f = new FormDots();
        new Thread(f).start();
    }
}