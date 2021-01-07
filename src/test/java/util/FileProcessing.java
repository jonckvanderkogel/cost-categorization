package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessing {
    public static Stream<String> streamLinesFromFile(String fileName) {
        String filePath = FileProcessing.class.getClassLoader().getResource(fileName).getPath();
        try {
            Stream<String> stream = Files.lines(Paths.get(filePath));
            return stream;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static List<String> parseLinesFromFile(String fileName) {
        return streamLinesFromFile(fileName)
                .collect(Collectors.toList());
    }
}
