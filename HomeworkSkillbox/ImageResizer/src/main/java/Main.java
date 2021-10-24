import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        String srcFolder = "D:\\1";
        String dstFolder = "D:\\2";
        int processors = Runtime.getRuntime().availableProcessors();

        File srcDir = new File(srcFolder);

      //  long start = System.currentTimeMillis();

        File[] files = srcDir.listFiles();
        if(files.length / processors <= 1){
            new MyThread(files, dstFolder, 1).start();
        }else{
            int step = files.length / processors;
            for (int i = 0; i < processors; i++){
                int overflow = files.length % processors;
                int sizeArray = (i + 1) * step + overflow < files.length ? step : step + overflow;
                File[] files1 = new File[sizeArray];
                System.arraycopy(files, i *step, files1,0, sizeArray);
                new MyThread(files1,dstFolder, i+1).start();
            }
        }


       // System.out.println("Duration: " + (System.currentTimeMillis() - start));
    }
}
