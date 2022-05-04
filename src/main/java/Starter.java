public class Starter {

    public static void main(String[] args) {
        String sourceHost     = "172.16.21.220";
        Integer sourcePort    = 22;
        String sourceUser     = "Roman-plus";
        String sourcePassword = "317771624";
        String sourceFile     = "C:\\spools-scan-app-jfx.jar";

        String localFile = "D:\\Users\\spools-scan-app-jfx.jar";

        try {
            Sftp.Downloader.download(sourceHost, sourcePort, sourceUser, sourcePassword, localFile, sourceFile);
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }
}
