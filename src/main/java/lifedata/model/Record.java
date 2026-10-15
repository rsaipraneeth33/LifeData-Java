package lifedata.model;

import java.util.ArrayList;
import java.util.List;

public class Record {
    private final int id;
    private final String title;
    private final String category;
    private final String text;
    private final List<String> tags;

    public Record(int id, String title, String category, String text, List<String> tags) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.text = text;
        this.tags = new ArrayList<>(tags);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getText() { return text; }
    public List<String> getTags() { return new ArrayList<>(tags); }

    public String searchableText() {
        return (title + " " + category + " " + text + " " + String.join(" ", tags)).toLowerCase();
    }

    public String toFileLine() {
        return id + "\t" + encode(title) + "\t" + encode(category) + "\t" +
               encode(text) + "\t" + encode(String.join(",", tags));
    }

    private static String encode(String s) {
        return s.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n");
    }

    private static String decode(String s) {
        StringBuilder out = new StringBuilder();
        boolean escaped = false;
        for (char c : s.toCharArray()) {
            if (escaped) {
                if (c == 't') out.append('\t');
                else if (c == 'n') out.append('\n');
                else out.append(c);
                escaped = false;
            } else if (c == '\\') escaped = true;
            else out.append(c);
        }
        if (escaped) out.append('\\');
        return out.toString();
    }

    public static Record fromFileLine(String line) {
        String[] p = line.split("\t", -1);
        if (p.length < 5) return null;
        String tagText = decode(p[4]);
        List<String> tags = new ArrayList<>();
        if (!tagText.isBlank()) {
            for (String tag : tagText.split(",")) if (!tag.isBlank()) tags.add(tag.trim());
        }
        return new Record(Integer.parseInt(p[0]), decode(p[1]), decode(p[2]), decode(p[3]), tags);
    }

    @Override
    public String toString() {
        return "\nID: " + id + "\nTitle: " + title + "\nCategory: " + category +
               "\nDetails: " + text + "\nTags: " + String.join(", ", tags) + "\n";
    }
}
