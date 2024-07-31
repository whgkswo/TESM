package net.whgkswo.tesm.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TextReader {
    public static final String BASE_PATH = System.getProperty("user.dir") + "/src/client/resources";

    public static String read(String filePath){
        try (InputStream inputStream = TextReader.class.getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return String.format("오류 발생 (%s)", e.getClass().getSimpleName());
        }
        /*List<String> lines = new ArrayList<>();
        try{
            lines = Files.readAllLines(Path.of(BASE_PATH + filePath));
        } catch(IOException e){
            lines.add(String.format("오류 발생 (%s)", e.getClass().getSimpleName()));
        }
        return String.join("\n", lines);*/
    }
}
