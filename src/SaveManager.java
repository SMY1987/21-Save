import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SaveManager {
    public static void main(String[] args) {

        String basePath = "C:\\Users\\1\\Desktop\\Games\\savegames";
        createDirectory(basePath);
        List<GameProgress> saves = List.of(
                new GameProgress(100, 5, 3, 1500.5),
                new GameProgress(80, 3, 2, 950.0),
                new GameProgress(120, 7, 5, 2500.75)
        );
        List<String> savePaths = List.of(
                basePath + "/save1.dat",
                basePath + "/save2.dat",
                basePath + "/save3.dat"
        );
        for (int i = 0; i < saves.size(); i++) {
            saveGame(savePaths.get(i), saves.get(i));
        }

        String zipPath = basePath + "/saves.zip";

        zipFiles(zipPath, savePaths);

        for (String filePath : savePaths) {
            deleteFile(filePath);
        }
    }

    private static void createDirectory(String fullPath) {
        File directory = new File(fullPath);
        if (!directory.exists() && directory.mkdirs()) {
            System.out.println("Создана директория: " + fullPath);
        } else if (directory.exists()) {
            System.out.println("Директория уже существует: " + fullPath);
        } else {
            System.err.println("Не удалось создать директорию: " + fullPath);
        }
    }

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println("Сохранение успешно записано в " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении игры: " + e.getMessage());
        }
    }

    public static void zipFiles(String zipPath, List<String> filePaths) {
        try (FileOutputStream fos = new FileOutputStream(zipPath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String filePath : filePaths) {
                File fileToZip = new File(filePath);
                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                } catch (IOException e) {
                    System.err.println("Ошибка при добавлении файла " + filePath + " в архив: " + e.getMessage());
                }
            }
            System.out.println("Архив успешно создан: " + zipPath);
        } catch (IOException e) {
            System.err.println("Ошибка при создании архива: " + e.getMessage());
        }
    }

    private static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.delete()) {
            System.out.println("Файл " + filePath + " удалён");
        } else {
            System.out.println("Не удалось удалить файл " + filePath);
        }
    }
}