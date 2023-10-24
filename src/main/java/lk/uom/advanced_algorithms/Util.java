package lk.uom.advanced_algorithms;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class Util {

    @SneakyThrows
    public static List<Long> loadData(Path path) {
        List<String> raw = Files.readAllLines(path, StandardCharsets.UTF_8);
        return raw.stream()
                .flatMap(line -> Arrays.stream(line.split(",")).map(Long::parseLong))
                .toList();
    }

    public static long duration(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.MICROS.between(start, end);
    }

    public static void main(String[] args) {
        Path path = Path.of("data", "insert", "set1", "data_1.txt");
        var result = loadData(path);
        System.out.println(result);
    }
}
