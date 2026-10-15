package lifedata.repository;

import lifedata.model.Record;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecordRepository {
    private final Path file;

    public RecordRepository() {
        this.file = Paths.get("data", "records.tsv");
        try {
            Files.createDirectories(file.getParent());
            if (Files.notExists(file)) {
                Files.writeString(file,
                    "1\tMorning walk\tActivity\tWalked for 30 minutes and tracked daily steps using a phone sensor.\thealth,walking,sensor\n" +
                    "2\tProject idea\tNotes\tBuild a personal data dashboard with keyword search and event history.\tproject,ideas,study\n" +
                    "3\tStudy session\tEvent\tReviewed algorithms, graph networks, dynamic programming and parallel processing.\tstudy,algorithms\n",
                    StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize data storage: " + e.getMessage(), e);
        }
    }

    public synchronized List<Record> findAll() {
        List<Record> records = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                try {
                    Record r = Record.fromFileLine(line);
                    if (r != null) records.add(r);
                } catch (RuntimeException ignored) { }
            }
        } catch (IOException e) {
            System.out.println("Storage read error: " + e.getMessage());
        }
        records.sort(Comparator.comparingInt(Record::getId));
        return records;
    }

    public synchronized void saveAll(List<Record> records) {
        List<String> lines = new ArrayList<>();
        for (Record r : records) lines.add(r.toFileLine());
        try {
            Files.write(file, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not save records: " + e.getMessage(), e);
        }
    }

    public Path getFile() { return file; }
}
