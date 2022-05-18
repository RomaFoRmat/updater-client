import java.awt.*;
import java.io.File;

public class Starter {


    public static void main(String[] args) {
        final File app = new File("D:\\Bykov_projects\\Spools Scan\\spools-scan-app.exe");
        String sourceHost     = "172.16.172.122";
        Integer sourcePort    = 22;
        String sourceUser     = "root";
        String sourcePassword = "stpc-2plus";
        String sourceFile     = "/root/Projects/Release/upak-server/target/label-app.jar";

        String localFile = "D:\\label-app.jar";

        try {
            Sftp.Downloader.download(sourceHost, sourcePort, sourceUser, sourcePassword, localFile, sourceFile);
            Desktop.getDesktop().open(app);
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }
}
