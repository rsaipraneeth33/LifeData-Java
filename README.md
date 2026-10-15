# LifeData — Java DSA Project

LifeData is a console-based Java implementation of the Large-Scale Personal Data Analytics Platform described in the supplied project brief. It stores personal notes, events, preferences, tags and observations, supports string-algorithm search, and includes a menu-driven Algorithm Lab for DSA Modules 1–6.

## Requirements
- JDK 17 or newer
- Visual Studio Code
- VS Code Extension Pack for Java (Microsoft)

## Project structure
```text
LifeData_Java/
├── src/main/java/lifedata/
│   ├── Main.java
│   ├── model/Record.java
│   ├── repository/RecordRepository.java
│   ├── service/LifeDataService.java
│   └── business/Algorithms.java
├── data/records.tsv
└── README.md
```

## Run in VS Code on Windows
1. Extract `LifeData_Java.zip`.
2. Open VS Code → **File → Open Folder** → select the extracted `LifeData_Java` folder.
3. Install **Extension Pack for Java** if VS Code recommends it.
4. Install JDK 17+ if needed. In VS Code Terminal, check `java -version` and `javac -version`.
5. Select **Terminal → New Terminal**.
6. Compile all Java files:
   ```powershell
   javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src\main\java\*.java | ForEach-Object { $_.FullName })
   ```
7. Run the app:
   ```powershell
   java -cp out lifedata.Main
   ```
8. Choose menu option 1 to add a record, option 2 to view records, option 3 to search, or options 5–12 to run algorithm demos.
9. Exit with option 0. Data is saved to `data/records.tsv`.

### If you use Command Prompt instead of PowerShell
Compile:
```bat
for /r src\main\java %f in (*.java) do @echo %f
```
For simpler compiling, use the VS Code Java extension's **Run Java** action on `Main.java`. Alternatively, open PowerShell terminal and use the compile command above.

## Run in VS Code on Ubuntu/Linux/macOS
From the project root:
```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out lifedata.Main
```

## Main menu
1. Add personal data record
2. View all records
3. Search records using KMP, Z, Rabin–Karp or Aho–Corasick
4. Delete a record
5. Edit distance algorithms
6. Suffix Array and Kasai LCP
7. Matrix-chain multiplication
8. Bitmask subset selection
9. Network flow demonstration
10. Bipartite matching
11. 3-SAT to CLIQUE and vertex-cover approximation
12. Randomized and parallel-algorithm demonstrations
0. Exit

## Syllabus coverage
- **M1:** KMP, Z-function, Rabin–Karp, Aho–Corasick, Levenshtein distance.
- **M2:** Suffix array, Kasai LCP.
- **M3:** Levenshtein, Damerau–Levenshtein, weighted edit distance, matrix-chain multiplication, bitmask subset selection.
- **M4:** Ford–Fulkerson and Edmonds–Karp maximum flow, bipartite matching; min-cut concept.
- **M5:** 3-SAT-to-CLIQUE construction, vertex-cover 2-approximation.
- **M6:** Randomized quicksort, reservoir sampling, Miller–Rabin, prefix sum, tree-style reduction, Brent speedup bound.

## Architecture
- `Main.java`: console menu / user interface.
- `service/LifeDataService.java`: validation and coordination between UI and algorithms/storage.
- `business/Algorithms.java`: DSA implementations.
- `repository/RecordRepository.java`: file-based persistence.
- `model/Record.java`: data model.
- `data/records.tsv`: persistent records.

## Academic notes
This is a teaching project, not a production-ready platform. Some advanced concepts are compact demonstrations rather than optimized production implementations. Parallel prefix sum/reduction show the algorithms' results but do not launch concurrent workers. The max-flow menu compares augmenting-path implementations and explains the min-cut relationship; a full Dinic implementation can be added if required by the grading rubric. Bitmask subset search is exponential and limits the input to 24 items. Do not store sensitive personal data.
