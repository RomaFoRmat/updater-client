import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    /**
     * Класс для cоздания *.bat файла
     */
    public static void createFile(File createFile, String fileData) throws IOException {
        if (createFile.createNewFile())
            System.out.println("File created: " + createFile.getName());
        else
            System.out.println("File already exists");
        FileOutputStream fos = new FileOutputStream(createFile);
        fos.write(fileData.getBytes());
        fos.flush();
        fos.close();
    }

    /**
     * Класс для cоздания директории, где будет распологатся прога
     */
    public static void folderTempFiles(String localDir) throws IOException {
        if (!Files.exists(Paths.get(localDir))) {               // does this directory exist?
            Files.createDirectories(Paths.get(localDir));       // create  folder for temporary files
        }
    }

    /**
     * Класс для очистки локальной директории с заданным расширением файла.
     */
    public static void deleteFiles(String localFile, String ext) {

        GenericExtFilter filter = new GenericExtFilter(ext);
        File dir = new File(localFile);

        // выводим все имена файлов с расширением .jar
        String[] list = dir.list(filter);

        if (list != null && list.length == 0) return;

        File fileDelete;

        for (String file : list) {
            String temp = localFile + File.separator + file;
            fileDelete = new File(temp);
            boolean isDeleted = fileDelete.delete();
            System.out.println("file : " + temp + " is deleted : " + isDeleted);
        }
    }

    // внутренний класс, универсальный фильтр расширения
    public static class GenericExtFilter implements FilenameFilter {

        private final String ext;

        public GenericExtFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(ext));
        }
    }
}
