package lifedata;

import java.util.*;
import lifedata.business.Algorithms;
import lifedata.model.Record;
import lifedata.repository.RecordRepository;
import lifedata.service.LifeDataService;

public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final LifeDataService service = new LifeDataService(new RecordRepository());

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" LifeData - Large-Scale Personal Data Analytics");
        System.out.println(" Java DSA Project | Modules 1-6");
        System.out.println("==================================================");
        while (true) {
            System.out.println("\nMAIN MENU\n");
            System.out.println("1. Add personal data record");
            System.out.println("2. View all records");
            System.out.println("3. Search records");
            System.out.println("4. Delete a record");
            System.out.println("5. Edit distance algorithms");
            System.out.println("6. Suffix Array and Kasai LCP");
            System.out.println("7. Matrix-chain multiplication");
            System.out.println("8. Bitmask subset selection");
            System.out.println("9. Network flow");
            System.out.println("10. Bipartite matching");
            System.out.println("11. 3-SAT to CLIQUE and Vertex Cover");
            System.out.println("12. Randomized and parallel algorithms");
            System.out.println("0. Exit\n");
            System.out.print("Enter choice: ");
            String choice=SC.nextLine().trim();
            try {
                switch(choice) {
                    case "1" -> addRecord();
                    case "2" -> viewRecords();
                    case "3" -> searchRecords();
                    case "4" -> deleteRecord();
                    case "5" -> editDistanceDemo();
                    case "6" -> suffixDemo();
                    case "7" -> matrixChainDemo();
                    case "8" -> bitmaskDemo();
                    case "9" -> flowDemo();
                    case "10" -> matchingDemo();
                    case "11" -> npDemo();
                    case "12" -> randomParallelDemo();
                    case "0" -> {
                        System.out.println("Thank you for using LifeData.");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Select a menu number.");
                }
            } catch (Exception e) {
                System.out.println("Could not complete operation: " + e.getMessage());
            }
        }
    }

    private static String ask(String prompt){System.out.print(prompt);return SC.nextLine();}
    private static int askInt(String prompt){while(true){try{return Integer.parseInt(ask(prompt).trim());}catch(NumberFormatException e){System.out.println("Enter a valid integer.");}}}
    private static int[] parseInts(String s){return Arrays.stream(s.split(",")).map(String::trim).filter(x->!x.isEmpty()).mapToInt(Integer::parseInt).toArray();}

    private static void addRecord() {
        String title=ask("Title: "),category=ask("Category (Activity/Event/Notes/Sensor/Preference): ");
        String text=ask("Details: "),tags=ask("Tags separated by commas: ");
        Record r=service.add(title,category,text,tags);System.out.println("Saved successfully. New record ID: "+r.getId());
    }
    private static void viewRecords(){List<Record> records=service.all();if(records.isEmpty())System.out.println("No records found.");else{System.out.println("Total records: "+records.size());records.forEach(System.out::println);}}
    private static void searchRecords(){
        String q=ask("Enter keyword: ");System.out.println("1.KMP  2.Z  3.Rabin-Karp  4.Aho-Corasick");
        String a=ask("Choose algorithm: ");String method=switch(a){case "2"->"z";case "3"->"rk";case "4"->"aho";default->"kmp";};
        List<Record> results=service.search(q,method);System.out.println("Matching records: "+results.size());results.forEach(System.out::println);
    }
    private static void deleteRecord(){int id=askInt("Record ID to delete: ");System.out.println(service.delete(id)?"Record deleted.":"No record with that ID.");}
    private static void editDistanceDemo(){
        String a=ask("Text A (default kitten): ");if(a.isEmpty())a="kitten";String b=ask("Text B (default sitting): ");if(b.isEmpty())b="sitting";
        System.out.println("Levenshtein distance: "+Algorithms.levenshtein(a,b));
        System.out.println("Damerau-Levenshtein distance: "+Algorithms.damerauLevenshtein(a,b));
        System.out.println("Weighted edit distance (substitution cost 2): "+Algorithms.weightedEditDistance(a,b));
    }
    private static void suffixDemo(){
        String s=ask("Enter text (default banana): ");if(s.isEmpty())s="banana";
        int[] sa=Algorithms.suffixArray(s);System.out.println("Suffix array: "+Arrays.toString(sa));
        System.out.println("Sorted suffixes:");for(int i:sa)System.out.println("  "+i+" -> "+s.substring(i));
        System.out.println("Kasai LCP array: "+Arrays.toString(Algorithms.kasaiLcp(s,sa)));
    }
    private static void matrixChainDemo(){
        String input=ask("Dimensions comma-separated (default 10,20,30,40): ");
        int[] d=parseInts(input.isBlank()?"10,20,30,40":input);
        System.out.println(Algorithms.matrixChain(d));
    }
    private static void bitmaskDemo(){
        String input=ask("Activity costs, comma-separated (default 3,5,2,8): ");
        int[] costs=parseInts(input.isBlank()?"3,5,2,8":input);
        int budget=askInt("Budget (default 10; enter 10): ");System.out.println(Algorithms.bitmaskSelection(costs,budget));
    }
    private static void flowDemo(){
        Map<Integer,Map<Integer,Integer>> graph=new LinkedHashMap<>();
        graph.put(0,new LinkedHashMap<>(Map.of(1,3,2,2)));
        graph.put(1,new LinkedHashMap<>(Map.of(2,1,3,2)));
        graph.put(2,new LinkedHashMap<>(Map.of(3,4)));
        graph.put(3,new LinkedHashMap<>());
        System.out.println("Example network: 0 -> {1:3, 2:2}; 1 -> {2:1, 3:2}; 2 -> {3:4}");
        System.out.println("Ford-Fulkerson max flow: "+Algorithms.maxFlow(graph,0,3,false));
        System.out.println("Edmonds-Karp max flow: "+Algorithms.maxFlow(graph,0,3,true));
        System.out.println("Dinic is covered conceptually in README; this example compares augmenting-path variants.");
        System.out.println("Min-cut theorem: at maximum flow, the residual graph separates source from sink.");
    }
    private static void matchingDemo(){
        List<String> left=List.of("Task A","Task B","Task C");
        Map<String,List<String>> edges=new LinkedHashMap<>();
        edges.put("Task A",List.of("CPU 1","CPU 2"));edges.put("Task B",List.of("CPU 2"));edges.put("Task C",List.of("CPU 2","CPU 3"));
        System.out.println("Eligible assignments: "+edges);System.out.println("Maximum matching: "+Algorithms.bipartiteMatching(left,edges));
    }
    private static void npDemo(){
        int[][] clauses={{1,-2,3},{-1,2,4},{2,-3,-4}};
        System.out.println("3-SAT to CLIQUE graph: "+Algorithms.threeSatToClique(clauses));
        Map<Integer,Set<Integer>> graph=new LinkedHashMap<>();graph.put(0,new LinkedHashSet<>(List.of(1,2)));graph.put(1,new LinkedHashSet<>(List.of(0,2)));graph.put(2,new LinkedHashSet<>(List.of(0,1,3)));graph.put(3,new LinkedHashSet<>(List.of(2)));
        System.out.println("Vertex-cover 2-approximation: "+Algorithms.vertexCoverApprox(graph));
    }
    private static void randomParallelDemo(){
        String input=ask("Numbers (default 8,3,9,1,5,2): ");
        int[] nums=parseInts(input.isBlank()?"8,3,9,1,5,2":input);
        System.out.println("Randomized QuickSort: "+Arrays.toString(Algorithms.randomizedQuickSort(nums)));
        List<Integer> stream=List.of(10,20,30,40,50,60,70);
        System.out.println("Reservoir sample k=3: "+Algorithms.reservoirSample(stream,3));
        long n=104729;System.out.println("Miller-Rabin probable prime test for "+n+": "+Algorithms.millerRabin(n,8));
        System.out.println("Prefix sum: "+Arrays.toString(Algorithms.prefixSum(nums)));
        System.out.println("Parallel reduction (sum): "+Algorithms.parallelReduceSum(nums));
        System.out.println("Brent speedup bound for T1=1000, Tinf=20, P=100: "+Algorithms.brentBound(1000,20,100));
    }
}
