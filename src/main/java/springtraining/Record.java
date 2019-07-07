package springtraining;

import java.time.LocalDate;
import java.util.Random;

public class Record {

    private static final String[] namePresets = {
            "Samsung",
            "Intel",
            "Asus",
            "Dell",
            "Gigabyte",
            "MSI",
            "Palit",
            "BenQ",
            "SVEN",
            "Cooler Master"
    };

    private static final LocalDate[] datePreset = {
            LocalDate.of(2019, 1, 3),
            LocalDate.of(2019, 1, 15),
            LocalDate.of(2019, 2, 4),
            LocalDate.of(2019, 2, 20),
            LocalDate.of(2019, 3, 3),
            LocalDate.of(2019, 3, 17),
            LocalDate.of(2019, 4, 8),
            LocalDate.of(2019, 4, 24),
            LocalDate.of(2019, 5, 9),
            LocalDate.of(2019, 5, 18)
    };

    private static final int COUNT_LIMIT = 100;

    private static int nextId = 0;

    private int id;
    private LocalDate date;
    private String name;
    private int count;

    public Record() {
        id = ++nextId;
        Random rnd = new Random();
        date = datePreset[rnd.nextInt(datePreset.length)];
        name = namePresets[rnd.nextInt(datePreset.length)];
        count = rnd.nextInt(COUNT_LIMIT);
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        String idStr = String.format("%-8s", "id=" + id);
        String dateStr = String.format("%-20s", "date=" + date.toString());
        String nameStr = String.format("%-20s", "name=" + name);
        String countStr = String.format("%-10s", "count=" + count);
        return "[" + idStr + dateStr + nameStr + countStr + "]";
    }

}
