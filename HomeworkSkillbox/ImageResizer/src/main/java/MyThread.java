import lombok.AllArgsConstructor;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@AllArgsConstructor
public class MyThread extends Thread{
    File[] files;
    String dstFolder;
    int number;

    @Override
    public void run() {
        try {
            for (File file : files) {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    continue;
                }

                int newWidth = 300;
                int newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth)
                );
                BufferedImage newImage = Scalr.resize(image,newWidth,newHeight, Scalr.OP_ANTIALIAS);

                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);
            }
            System.out.println("Thread " + number + ", " + files.length + " files" );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
