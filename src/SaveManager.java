import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SaveManager {
    public static void main(String[] args) {

        GameProgress save1 = new GameProgress(100, 5, 3, 1500.5);
        GameProgress save2 = new GameProgress(80, 3, 2, 950.0);
        GameProgress save3 = new GameProgress(120, 7, 5, 2500.75);

        String saveDirPath = "C:\\Users\\1\\Desktop\\Games\\savegames";

        File saveDir = new File(saveDirPath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        String savePath1 = saveDirPath + "/save1.dat";
        String savePath2 = saveDirPath + "/save2.dat";
        String savePath3 = saveDirPath + "/save3.dat";
        saveGame(savePath1, save1);
        saveGame(savePath2, save2);
        saveGame(savePath3, save3);

        String zipPath = saveDirPath + "/saves.zip";
        List<String> filePaths = List.of(savePath1, savePath2, savePath3);
        zipFiles(zipPath, filePaths);

        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (file.exists() && file.delete()) {
                System.out.println("Файл " + filePath + " удалён");
            } else {
                System.out.println("Не удалось удалить файл " + filePath);
            }
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
}