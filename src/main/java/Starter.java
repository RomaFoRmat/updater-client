
public class Starter {

    public static void main(String[] args) {
        String sourceHost     = "172.16.172.122";
        Integer sourcePort    = 22; //default port for SSH
        String sourceUser     = "root";
        String sourcePassword = "stpc-2plus";
        String sourceDir     = "/root/Projects/Release/bsw_spools_scan";
        String localFile = "C:\\bsw_spools_scan\\";

        try {
            Sftp.Downloader.download(sourceHost, sourcePort, sourceUser, sourcePassword,sourceDir,localFile);
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }
}
