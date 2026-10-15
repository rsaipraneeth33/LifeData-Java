package lifedata.business;

import java.util.*;

public final class Algorithms {
    private Algorithms() {}

    public static List<Integer> kmp(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        if (pattern.isEmpty()) { for (int i=0;i<=text.length();i++) result.add(i); return result; }
        int[] lps = new int[pattern.length()];
        for (int i=1, len=0; i<pattern.length();) {
            if (pattern.charAt(i)==pattern.charAt(len)) lps[i++]=++len;
            else if (len!=0) len=lps[len-1];
            else lps[i++]=0;
        }
        for (int i=0,j=0;i<text.length();) {
            if (text.charAt(i)==pattern.charAt(j)) { i++; j++; }
            if (j==pattern.length()) { result.add(i-j); j=lps[j-1]; }
            else if (i<text.length() && text.charAt(i)!=pattern.charAt(j)) {
                if (j!=0) j=lps[j-1]; else i++;
            }
        }
        return result;
    }

    public static int[] zArray(String s) {
        int[] z = new int[s.length()];
        int left=0,right=0;
        for (int i=1;i<s.length();i++) {
            if (i<=right) z[i]=Math.min(right-i+1,z[i-left]);
            while (i+z[i]<s.length() && s.charAt(z[i])==s.charAt(i+z[i])) z[i]++;
            if (i+z[i]-1>right) { left=i; right=i+z[i]-1; }
        }
        if (!s.isEmpty()) z[0]=s.length();
        return z;
    }

    public static List<Integer> zSearch(String text, String pattern) {
        List<Integer> out = new ArrayList<>();
        if (pattern.isEmpty()) { for(int i=0;i<=text.length();i++)out.add(i); return out; }
        String combined=pattern+"\u0001"+text;
        int[] z=zArray(combined);
        for(int i=pattern.length()+1;i<combined.length();i++)
            if(z[i]>=pattern.length()) out.add(i-pattern.length()-1);
        return out;
    }

    public static List<Integer> rabinKarp(String text, String pattern) {
        List<Integer> out=new ArrayList<>();
        if(pattern.isEmpty()){for(int i=0;i<=text.length();i++)out.add(i);return out;}
        int n=text.length(),m=pattern.length();
        if(m>n)return out;
        long mod=1_000_000_007L, base=256, ph=0, th=0, power=1;
        for(int i=0;i<m-1;i++)power=power*base%mod;
        for(int i=0;i<m;i++){ph=(ph*base+pattern.charAt(i))%mod;th=(th*base+text.charAt(i))%mod;}
        for(int i=0;i<=n-m;i++){
            if(ph==th && text.regionMatches(i,pattern,0,m))out.add(i);
            if(i<n-m){th=(th-text.charAt(i)*power%mod+mod)%mod;th=(th*base+text.charAt(i+m))%mod;}
        }
        return out;
    }

    public static List<String> ahoCorasickMatches(String text, List<String> patterns) {
        class Node { Map<Character,Node> next=new HashMap<>(); Node fail; List<String> out=new ArrayList<>(); }
        Node root=new Node();
        for(String p:patterns){if(p.isEmpty())continue;Node cur=root;for(char c:p.toCharArray())cur=cur.next.computeIfAbsent(c,k->new Node());cur.out.add(p);}
        Queue<Node> q=new ArrayDeque<>();
        for(Node n:root.next.values()){n.fail=root;q.add(n);}
        while(!q.isEmpty()){
            Node u=q.remove();
            for(var e:u.next.entrySet()){
                char c=e.getKey();Node v=e.getValue(),f=u.fail;
                while(f!=null && !f.next.containsKey(c))f=f.fail;
                v.fail=(f==null)?root:f.next.get(c);
                v.out.addAll(v.fail.out);q.add(v);
            }
        }
        List<String> found=new ArrayList<>();Node cur=root;
        for(char c:text.toCharArray()){
            while(cur!=root && !cur.next.containsKey(c))cur=cur.fail;
            cur=cur.next.getOrDefault(c,root);
            for(String p:cur.out)found.add(p);
        }
        return found;
    }

    public static int levenshtein(String a,String b) {
        int[] prev=new int[b.length()+1];for(int j=0;j<=b.length();j++)prev[j]=j;
        for(int i=1;i<=a.length();i++){int[] cur=new int[b.length()+1];cur[0]=i;
            for(int j=1;j<=b.length();j++)cur[j]=Math.min(Math.min(cur[j-1]+1,prev[j]+1),prev[j-1]+(a.charAt(i-1)==b.charAt(j-1)?0:1));
            prev=cur;}return prev[b.length()];
    }

    public static int damerauLevenshtein(String a,String b) {
        int[][] d=new int[a.length()+1][b.length()+1];
        for(int i=0;i<=a.length();i++)d[i][0]=i;
        for(int j=0;j<=b.length();j++)d[0][j]=j;
        for(int i=1;i<=a.length();i++)for(int j=1;j<=b.length();j++){
            int cost=a.charAt(i-1)==b.charAt(j-1)?0:1;
            d[i][j]=Math.min(Math.min(d[i-1][j]+1,d[i][j-1]+1),d[i-1][j-1]+cost);
            if(i>1&&j>1&&a.charAt(i-1)==b.charAt(j-2)&&a.charAt(i-2)==b.charAt(j-1))
                d[i][j]=Math.min(d[i][j],d[i-2][j-2]+1);
        }return d[a.length()][b.length()];
    }

    public static int weightedEditDistance(String a,String b) {
        int[][] d=new int[a.length()+1][b.length()+1];
        for(int i=1;i<=a.length();i++)d[i][0]=i;
        for(int j=1;j<=b.length();j++)d[0][j]=j;
        for(int i=1;i<=a.length();i++)for(int j=1;j<=b.length();j++)
            d[i][j]=Math.min(Math.min(d[i-1][j]+1,d[i][j-1]+1),d[i-1][j-1]+(a.charAt(i-1)==b.charAt(j-1)?0:2));
        return d[a.length()][b.length()];
    }

    public static int[] suffixArray(String s) {
        Integer[] sa=new Integer[s.length()];for(int i=0;i<s.length();i++)sa[i]=i;
        Arrays.sort(sa,Comparator.comparing(s::substring));
        return Arrays.stream(sa).mapToInt(Integer::intValue).toArray();
    }

    public static int[] kasaiLcp(String s,int[] sa) {
        int n=s.length();int[] rank=new int[n],lcp=new int[n];
        for(int i=0;i<n;i++)rank[sa[i]]=i;
        int k=0;
        for(int i=0;i<n;i++){if(rank[i]==n-1){k=0;continue;}int j=sa[rank[i]+1];
            while(i+k<n&&j+k<n&&s.charAt(i+k)==s.charAt(j+k))k++;
            lcp[rank[i]]=k;if(k>0)k--;}
        return lcp;
    }

    public static Map<String,Object> matrixChain(int[] dims) {
        Map<String,Object> out=new LinkedHashMap<>();
        if(dims.length<2){out.put("minimum multiplications",0);out.put("parenthesization","");return out;}
        int n=dims.length-1;long[][] dp=new long[n][n];int[][] split=new int[n][n];
        for(int len=2;len<=n;len++)for(int i=0;i<=n-len;i++){int j=i+len-1;dp[i][j]=Long.MAX_VALUE;
            for(int k=i;k<j;k++){long cost=dp[i][k]+dp[k+1][j]+(long)dims[i]*dims[k+1]*dims[j+1];
                if(cost<dp[i][j]){dp[i][j]=cost;split[i][j]=k;}}}
        out.put("minimum multiplications",dp[0][n-1]);out.put("parenthesization",buildParen(split,0,n-1));return out;
    }
    private static String buildParen(int[][] split,int i,int j){if(i==j)return "A"+(i+1);int k=split[i][j];return "("+buildParen(split,i,k)+" × "+buildParen(split,k+1,j)+")";}

    public static Map<String,Object> bitmaskSelection(int[] costs,int budget) {
        int bestMask=0,bestCount=-1,bestCost=0;
        if(costs.length>24)throw new IllegalArgumentException("Use at most 24 items for exhaustive bitmask search.");
        for(int mask=0;mask<(1<<costs.length);mask++){int sum=0,count=0;for(int i=0;i<costs.length;i++)if((mask&(1<<i))!=0){sum+=costs[i];count++;}
            if(sum<=budget&&count>bestCount){bestMask=mask;bestCount=count;bestCost=sum;}}
        List<Integer> indexes=new ArrayList<>(),chosen=new ArrayList<>();
        for(int i=0;i<costs.length;i++)if((bestMask&(1<<i))!=0){indexes.add(i);chosen.add(costs[i]);}
        Map<String,Object> out=new LinkedHashMap<>();out.put("selected zero-based indexes",indexes);out.put("selected costs",chosen);out.put("count",bestCount);out.put("total cost",bestCost);out.put("budget",budget);return out;
    }

    private static Map<Integer,Map<Integer,Integer>> residual(Map<Integer,Map<Integer,Integer>> graph){
        Map<Integer,Map<Integer,Integer>> r=new LinkedHashMap<>();
        for(int u:graph.keySet()){r.computeIfAbsent(u,k->new LinkedHashMap<>());for(var e:graph.get(u).entrySet()){r.get(u).put(e.getKey(),e.getValue());r.computeIfAbsent(e.getKey(),k->new LinkedHashMap<>()).putIfAbsent(u,0);}}
        return r;
    }
    public static int maxFlow(Map<Integer,Map<Integer,Integer>> graph,int source,int sink,boolean bfs){
        Map<Integer,Map<Integer,Integer>> r=residual(graph);int flow=0;
        while(true){Map<Integer,Integer> parent=new HashMap<>();parent.put(source,-1);Deque<Integer> q=new ArrayDeque<>();q.add(source);
            while(!q.isEmpty()&&!parent.containsKey(sink)){int u=q.remove();for(var e:r.get(u).entrySet())if(e.getValue()>0&&!parent.containsKey(e.getKey())){parent.put(e.getKey(),u);q.add(e.getKey());if(!bfs)break;} }
            if(!parent.containsKey(sink))break;int add=Integer.MAX_VALUE;
            for(int v=sink;v!=source;v=parent.get(v))add=Math.min(add,r.get(parent.get(v)).get(v));
            for(int v=sink;v!=source;v=parent.get(v)){int u=parent.get(v);r.get(u).put(v,r.get(u).get(v)-add);r.get(v).put(u,r.get(v).getOrDefault(u,0)+add);}flow+=add;
        }return flow;
    }

    public static Map<String,Object> bipartiteMatching(List<String> left,Map<String,List<String>> edges){
        Map<String,String> rightToLeft=new LinkedHashMap<>();
        for(String u:left)augment(u,edges,rightToLeft,new HashSet<>());
        Map<String,Object> out=new LinkedHashMap<>();out.put("size",rightToLeft.size());out.put("right-to-left pairs",rightToLeft);return out;
    }
    private static boolean augment(String u,Map<String,List<String>> edges,Map<String,String> match,Set<String> seen){
        for(String v:edges.getOrDefault(u,List.of())){if(!seen.add(v))continue;if(!match.containsKey(v)||augment(match.get(v),edges,match,seen)){match.put(v,u);return true;}}return false;
    }

    public static int[] randomizedQuickSort(int[] values){int[] a=values.clone();quick(a,0,a.length-1);return a;}
    private static void quick(int[] a,int lo,int hi){if(lo>=hi)return;int pivot=a[lo+new Random().nextInt(hi-lo+1)],i=lo,j=hi;
        while(i<=j){while(a[i]<pivot)i++;while(a[j]>pivot)j--;if(i<=j){int t=a[i];a[i++]=a[j];a[j--]=t;}}
        if(lo<j)quick(a,lo,j);if(i<hi)quick(a,i,hi);}

    public static List<Integer> reservoirSample(List<Integer> stream,int k){
        if(k<0)throw new IllegalArgumentException("k cannot be negative.");List<Integer> out=new ArrayList<>();Random random=new Random();
        for(int i=0;i<stream.size();i++){if(i<k)out.add(stream.get(i));else{int j=random.nextInt(i+1);if(j<k)out.set(j,stream.get(i));}}return out;
    }

    public static boolean millerRabin(long n,int rounds){
        if(n<2)return false;for(int p:new int[]{2,3,5,7,11,13,17,19,23,29,31,37}){if(n==p)return true;if(n%p==0)return false;}
        long d=n-1;int s=0;while((d&1)==0){d>>=1;s++;}
        Random rnd=new Random();
        for(int i=0;i<rounds;i++){long a=2+Math.floorMod(rnd.nextLong(),n-3);long x=modPow(a,d,n);
            if(x==1||x==n-1)continue;boolean passed=false;for(int r=1;r<s;r++){x=mulMod(x,x,n);if(x==n-1){passed=true;break;}}if(!passed)return false;}
        return true;
    }
    private static long modPow(long a,long e,long m){long r=1;while(e>0){if((e&1)==1)r=mulMod(r,a,m);a=mulMod(a,a,m);e>>=1;}return r;}
    private static long mulMod(long a,long b,long m){return java.math.BigInteger.valueOf(a).multiply(java.math.BigInteger.valueOf(b)).mod(java.math.BigInteger.valueOf(m)).longValue();}

    public static int[] prefixSum(int[] a){int[] out=new int[a.length];int sum=0;for(int i=0;i<a.length;i++){sum+=a[i];out[i]=sum;}return out;}
    public static int parallelReduceSum(int[] values){
        if(values.length==0)return 0;
        int[] layer=values.clone();
        while(layer.length>1){
            int[] next=new int[(layer.length+1)/2];
            for(int i=0;i<layer.length;i+=2)
                next[i/2]=i+1<layer.length?layer[i]+layer[i+1]:layer[i];
            layer=next;
        }
        return layer[0];
    }
    public static double brentBound(double work,double criticalPath,int processors){double time=work/processors+criticalPath;return work/time;}

    public static Map<String,Object> threeSatToClique(int[][] clauses){
        List<String> vertices=new ArrayList<>();List<String> edges=new ArrayList<>();
        for(int i=0;i<clauses.length;i++)for(int j=0;j<clauses[i].length;j++)vertices.add("C"+(i+1)+"L"+(j+1)+"("+clauses[i][j]+")");
        for(int i=0;i<clauses.length;i++)for(int j=i+1;j<clauses.length;j++)for(int a=0;a<clauses[i].length;a++)for(int b=0;b<clauses[j].length;b++)
            if(clauses[i][a]!=-clauses[j][b])edges.add("C"+(i+1)+"L"+(a+1)+" -- C"+(j+1)+"L"+(b+1));
        Map<String,Object> out=new LinkedHashMap<>();out.put("vertices",vertices);out.put("compatible edges",edges);return out;
    }

    public static Set<Integer> vertexCoverApprox(Map<Integer,Set<Integer>> graph){
        Set<String> edges=new LinkedHashSet<>();for(var e:graph.entrySet())for(int v:e.getValue())if(e.getKey()!=v)edges.add(Math.min(e.getKey(),v)+":"+Math.max(e.getKey(),v));
        Set<Integer> cover=new TreeSet<>();while(!edges.isEmpty()){String edge=edges.iterator().next();String[] p=edge.split(":");int u=Integer.parseInt(p[0]),v=Integer.parseInt(p[1]);cover.add(u);cover.add(v);edges.removeIf(x->{String[] q=x.split(":");return Integer.parseInt(q[0])==u||Integer.parseInt(q[1])==u||Integer.parseInt(q[0])==v||Integer.parseInt(q[1])==v;});}return cover;
    }
}
