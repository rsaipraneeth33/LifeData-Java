package lifedata.service;

import lifedata.model.Record;
import lifedata.repository.RecordRepository;
import lifedata.business.Algorithms;
import java.util.*;
import java.util.stream.Collectors;

public class LifeDataService {
    private final RecordRepository repository;
    public LifeDataService(RecordRepository repository) { this.repository = repository; }

    public List<Record> all() { return repository.findAll(); }

    public Record add(String title,String category,String text,String tagText) {
        if(title==null||title.isBlank()||text==null||text.isBlank())
            throw new IllegalArgumentException("Title and details are required.");
        List<Record> records=repository.findAll();
        int next=records.stream().mapToInt(Record::getId).max().orElse(0)+1;
        List<String> tags=Arrays.stream((tagText==null?"":tagText).split(",")).map(String::trim).filter(s->!s.isEmpty()).collect(Collectors.toList());
        Record record=new Record(next,title.trim(),category==null||category.isBlank()?"General":category.trim(),text.trim(),tags);
        records.add(record);repository.saveAll(records);return record;
    }

    public boolean delete(int id) {
        List<Record> records=repository.findAll();boolean changed=records.removeIf(r->r.getId()==id);
        if(changed)repository.saveAll(records);return changed;
    }

    public List<Record> search(String query,String algorithm) {
        if(query==null||query.isBlank())return List.of();
        String q=query.toLowerCase();List<Record> found=new ArrayList<>();
        for(Record r:repository.findAll()){
            String text=r.searchableText();boolean match;
            switch(algorithm.toLowerCase()){
                case "z": match=!Algorithms.zSearch(text,q).isEmpty();break;
                case "rk": match=!Algorithms.rabinKarp(text,q).isEmpty();break;
                case "aho": match=!Algorithms.ahoCorasickMatches(text,List.of(q)).isEmpty();break;
                default: match=!Algorithms.kmp(text,q).isEmpty();
            }
            if(match)found.add(r);
        }return found;
    }
}
