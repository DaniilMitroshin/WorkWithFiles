import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        //test1();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean flag = false;
            while (!flag) {
                System.out.println("""
                        Введите что вы хотите сделать(1/2):
                         1- распечатать размер директории
                         2-глубокое копирование папки""");
                String x = scanner.nextLine().trim();
                switch (x) {
                    case "1":
                        flag = true;
                        PrintSize();
                        break;
                    case "2":
                        flag = true;
                        folderCopy();
                        break;
                    default:
                        System.out.println("Попробуйте еще раз!");
                        break;

                }


            }
        }
    }



    private static void folderCopy() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                try {
                    System.out.println("Введите откуда копировать: ");
                    Path pathFrom = Paths.get(scanner.nextLine().trim());
                    System.out.println("Введите куда копировать: ");
                    Path pathTo = Paths.get(scanner.nextLine().trim());
                    if (!Files.exists(pathFrom)) {
                        throw new FileNotFoundException("Такого пути для копирования не существует");
                    }
                    if (!Files.exists(pathTo)) {
                        Files.createDirectories(pathTo);
                        System.out.println("Создана директория: " + pathTo);
                    }
                    folderCopyFunction(pathFrom, pathTo);
                    break;
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static void folderCopyFunction(Path pathFrom, Path pathTo) {
        try {
            if (Files.isDirectory(pathFrom)) {
                Path newDir = pathTo.resolve(pathFrom.getFileName());
                Files.createDirectories(newDir);
                try (Stream<Path> stream = Files.list(pathFrom)) {
                    stream.forEach(e -> {
                        folderCopyFunction(e, newDir);
                    });
                }
            } else {

                Files.copy(pathFrom, pathTo.resolve(pathFrom.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void PrintSize(){
        try(Scanner scanner = new Scanner(System.in)){
            for(;;){
                System.out.println("Введите путь: ");
                String path = scanner.nextLine().trim();
                if (path.equalsIgnoreCase("exit")){
                    break;
                }
                try {
                    File file = new File(path);
                    if (!file.exists()){
                        throw new FileNotFoundException("Такого файла не существует, попробуйте еще раз");
                    }
                    long[] res = {fileCheck(file)};
                    System.out.println(res[0]);
                    convertBytes(res[0]);

                } catch (FileNotFoundException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }
    private static long fileCheck(File file){
        long res = 0;
        if (file.isDirectory()){
            if (file.listFiles()!=null){
                File[] files = file.listFiles();
                for (File tempfile : files){
                    res += fileCheck(tempfile);

                }
            }
        }else {
            res += file.length();
        }
        return res;
    }



    private static void test1(){
        File folder = new File("data");
        System.out.println(folder.isDirectory());
        if (folder.listFiles()!=null) {
            List<File> files = new ArrayList<>(List.of(folder.listFiles()));
            files.forEach(e -> System.out.println(e.getName()));
            final long[] totalSize = {0};
            for (File file : files) {
                totalSize[0] += file.length();
            }
            System.out.println(totalSize[0]);
            convertBytes(totalSize[0]);
        }
    }

    public static void convertBytes(long bytes) {
        long gb = bytes / (1024 * 1024 * 1024);
        long remainder = bytes % (1024 * 1024 * 1024);

        long mb = remainder / (1024 * 1024);
        remainder = remainder % (1024 * 1024);

        long kb = remainder / 1024;
        remainder = remainder % 1024;

        long b = remainder;

        System.out.printf("%d GB, %d MB, %d KB, %d B%n", gb, mb, kb, b);
    }


}

