import java.io.*;

class Main {
    public static void main(String[] args) {
        System.out.println("== Последовательное копирование файлов == ");

        File sourceFile1 = new File("C:\\Users\\moyko\\IdeaProjects\\files\\out\\production\\files\\sourceFile1.txt");
        File targetFile1 = new File("C:\\Users\\moyko\\IdeaProjects\\files\\out\\production\\files\\targetFile1.txt");
        performFileCopy(sourceFile1, targetFile1);

        File sourceFile2 = new File("C:\\Users\\moyko\\IdeaProjects\\files\\out\\production\\files\\sourceFile2.txt");
        File targetFile2 = new File("C:\\Users\\moyko\\IdeaProjects\\files\\out\\production\\files\\targetFile2.txt");
        performFileCopy(sourceFile2, targetFile2);

        System.out.println("----------------------------------------------------------------------------------");

        System.out.println("== Параллельное копирование файлов == ");
        Thread copyThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                performFileCopy(new File("C:\\Users\\moyko\\IdeaProjects\\files\\out\\production\\files\\sourceFile1.txt"), new File("C:\\Users\\moyko\\IdeaProjects\\files\\out\\production\\files\\targetFile1.txt"));
            }
        });
        Thread copyThread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                performFileCopy(new File("C:\\Users\\moyko\\IdeaProjects\\files\\out\\production\\files\\sourceFileThread.txt"), new File("C:\\Users\\moyko\\IdeaProjects\\files\\out\\production\\files\\targetFileThread.txt"));
            }
        });

        copyThread1.start();
        copyThread2.start();

        try {
            copyThread1.join();
            copyThread2.join();
        } catch (InterruptedException e) {
            System.err.println("Ошибка при ожидании завершения потоков: " + e.getMessage());
        }

        System.out.println("== Копирование завершено! ==");
    }

    private static void performFileCopy(File source, File destination) {
        long startTime = System.nanoTime();

        try (InputStream inputStream = new FileInputStream(source);
             OutputStream outputStream = new FileOutputStream(destination)) {

            byte[] buffer = new byte[8192];
            long totalBytesCopied = 0;
            int bytesRead;
            long fileSize = source.length();

            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesCopied += bytesRead;

                // Печати прогресс копирования
                double progress = (double) totalBytesCopied / fileSize * 100;
                System.out.printf("Копирование %s в %s: %.2f%% завершено.%n",
                        source.getName(), destination.getName(), progress);
            }

        } catch (IOException e) {
            System.err.printf("Ошибка при копировании из %s в %s: %s%n", source.getName(), destination.getName(), e.getMessage());
            return;
        }

        long duration = System.nanoTime() - startTime;
        System.out.println("Копирование из " + source + " в " + destination + " завершено за " + duration + " наносекунд");
    }
}
