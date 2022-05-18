import com.jcraft.jsch.*;

public class Sftp {
    private static long time0;
    /**
     * Класс для скачивания файла.
     */
    public static class Downloader {

        public static void download(String host, Integer port, String user, String password, String localFile, String sourceFile) {
            try {
                JSch jsch = new JSch();

                Session session = jsch.getSession(user, host, port);
                session.setUserInfo(new MyUserInfo(password));
                session.connect();

                System.out.println("session connected");

                Channel channel = session.openChannel("sftp");
                channel.connect();

                System.out.println("channel connected");

                ChannelSftp channelSftp = (ChannelSftp) channel;

                try {
                    System.out.println("start");
                    time0 = System.currentTimeMillis();
                    channelSftp.get(sourceFile, localFile, new MyProgressMonitor(sourceFile), ChannelSftp.OVERWRITE);
                } catch (SftpException cause) {
                    cause.printStackTrace();
                }

                channelSftp.exit();

                session.disconnect();
            } catch (Exception cause) {
                cause.printStackTrace();
            }
        }
        /**
         * Класс для авторизации на SSH-сервере.
         */
        private static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
            private String password;

            public MyUserInfo(String password) {
                this.password = password;
            }

            public String getPassword() {
                return password;
            }

            public boolean promptYesNo(String str) {
                return true;
            }

            public String getPassphrase() {
                return null;
            }

            public boolean promptPassphrase(String message) {
                return true;
            }

            public boolean promptPassword(String message) {
                return true;
            }

            public void showMessage(String message) {
            }

            public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
                return new String[] { password };
            }
        }

        /**
         * Класс для отслеживания прогресса скачивания/закачивания файла.
         */
        private static class MyProgressMonitor implements SftpProgressMonitor {
            private String sourceFile;

            private long count;
            private long max;
            private long percent;

            public MyProgressMonitor(String sourceFile) {
                this.sourceFile = sourceFile;
            }

            @Override
            public void init(int op, String src, String dest, long max) {
                this.max = max;
                this.count = 0;
                this.percent = -1;
            }

            @Override
            public boolean count(long count) {
                this.count += count;

                if (percent >= this.count * 100 / max) {
                    return true;
                }

                percent = this.count * 100 / max;

                System.out.println("progress: " + percent + " %");

                return true;
            }

            @Override
            public void end() {
                System.out.println("progress: finished in " + (System.currentTimeMillis() - time0) + "ms");
                System.out.println("Программа успешно обновлена!\nЖдите,идет запуск!");
            }
        }
    }

}
