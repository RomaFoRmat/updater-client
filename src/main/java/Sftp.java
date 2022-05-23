import com.jcraft.jsch.*;

import java.io.File;
import java.util.Vector;

public class Sftp {
    private static long time0;
    private static String FILE_NAME;
    public static  final String FILE_DATA = "chcp 1251\n" + "set file_name = %1\n" + "start javaw -jar %1\n" + "exit";

    /**
     * Класс для скачивания файла.
     */
    public static class Downloader {

        public static void download(
                String host, Integer port, String user, String password, String sourceDir, String localFile) {
            try {
                JSch jsch = new JSch();

                Session session = jsch.getSession(user, host, port);               //создаём сессию
                session.setUserInfo(new MyUserInfo(password));                     //авто-подстановка пароля
                session.connect();                                                 //соединение
                System.out.println("Session connected");


                Channel channel = session.openChannel("sftp");                //открываем канал для передачи файлов
                channel.connect();                                                 //соединение
                System.out.println("Channel connected");

                ChannelSftp channelSftp = (ChannelSftp) channel;
                String maxVersionFile = getMaxVersionFile(channelSftp, sourceDir); //путь к файлу с max версией клиента

                //скачивание файла c удаленного сервера:
                if (FILE_NAME != null) {
                    FileUtil.folderTempFiles(localFile);
                    FileUtil.deleteFiles(localFile, ".jar");
                    System.out.println("Start update");
                    time0 = System.currentTimeMillis();
                    channelSftp.get(maxVersionFile, localFile,
                            new MyProgressMonitor(maxVersionFile), ChannelSftp.OVERWRITE);
                    FileUtil.createFile(new File("spools-scan-run.bat"),FILE_DATA);
                    Runtime.getRuntime().exec(" cmd /c start  spools-scan-run.bat" + " " + localFile + FILE_NAME);
                } else {
                    System.out.println("No such file");
                }

                channelSftp.exit();
                session.disconnect();
            } catch (Exception cause) {
//                cause.printStackTrace();
                System.out.println("Cannot make connection to SFTP server || Directory not found");
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
                return new String[]{password};
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
                this.count += count;    //счетчик,хранит общее количество считанных байт

                if (percent >= this.count * 100 / max) {
                    return true;
                }
                percent = this.count * 100 / max;

                System.out.println("Progress: " + percent + " %");

                return true;
            }

            @Override
            public void end() {
                System.out.println("Progress: finished in " + (System.currentTimeMillis() - time0) + "ms");
                System.out.println("Программа успешно обновлена!\nЖдите,идет запуск!");
            }
        }

        /**
         * Класс для получения файла с актуальной версией.
         */
        public static String getMaxVersionFile(ChannelSftp channelSftp, String dir) throws SftpException {
            Vector<ChannelSftp.LsEntry> files = channelSftp.ls(dir);
            double maxVersion = 0;
            for (ChannelSftp.LsEntry entry : files) {
                if (!entry.getAttrs().isDir()) {
                    String path = entry.getFilename();
                    String name = entry.getFilename().replace(".jar", "");
                    String[] result = name.split("-");
                    double version = Double.parseDouble(result[result.length - 1]);
                    if (version > maxVersion) {
//                        maxVersion = version;
                        FILE_NAME = path;
                    }
                }
            }
            System.out.println("Последняя версия программы: " + dir + "/" + FILE_NAME);

            return dir + "/" + FILE_NAME;

        }

    }

}

