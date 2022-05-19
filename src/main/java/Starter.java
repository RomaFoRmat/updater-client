import java.awt.*;
import java.io.File;

public class Starter {


    public static void main(String[] args) {
        final File app = new File("D:\\Bykov_projects\\Spools Scan\\spools-scan-app.exe");
        String sourceHost     = "172.16.172.122";
        Integer sourcePort    = 22;
        String sourceUser     = "root";
        String sourcePassword = "stpc-2plus";
//        String sourceFile     = "/root/Projects/Release/upak-server/target/label-app.jar";
        String sourceDir     = "/root/Projects/Release/bsw_sgp_api";

        String maxVersion = Sftp.Downloader.getMaxVersionFile(sourceDir);
        String localFile = "D:\\"+maxVersion;

        try {
            Sftp.Downloader.download(sourceHost, sourcePort, sourceUser, sourcePassword, localFile, sourceDir);
            Desktop.getDesktop().open(app);
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }
}
