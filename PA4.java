import java.io.*;
import java.util.*;


// Data structures for representing a graph

class Edge {
   int succ;
   Edge next;

   Edge(int succ, Edge next) {
      this.succ = succ;
      this.next = next;
   }
}

class Graph {
   Edge[] A; 
   // A[u] points to the head of a liked list;
   // p in the list corresponds to an edge u -> p.succ in the graph

   Graph(int n) {
      // initialize a graph with n vertices and no edges
      A = new Edge[n];
   }

   void addEdge(int u, int v) {
      // add an edge i -> j to the graph

      A[u] = new Edge(v, A[u]);
   }
}


// your "main program" should look something like this:

public class DFSStarter {

   static Graph graph; // global variable representing the graph

   static int[] color; // global variable storing the color
                       // of each node during DFS: 
                       //    0 for white, 1 for gray, 2 for black

   static int[] parent;  // global variable representing the parent 
                         // of each node in the DFS forest
   
   static boolean cycleFound = false; //    We will change to this to true if a cycle is found
   
   static int finalNode; //    We use this to trace the path when a cycle is discovered
   
   static ArrayList<Integer> finalPath = new ArrayList<Integer>(); //    We will output this if there is a cycle
   

   static void recDFS(int u) {
       if (!cycleFound) {
//           Perform a recursive DFS, starting at u
           color[u] = 1;
           Edge pointer = graph.A[u];
           while (pointer != null) {
               int succ = pointer.succ;
//               Cycle found
               if (color[succ] == 1) {
                   cycleFound = true;
                   parent[succ] = u;
                   finalNode = u;
                   generatePath();
                   return;
                   
               }
               else if (color[succ] == 0){
                   parent[succ] = u;
                   recDFS(succ);
               }
               pointer = pointer.next;
           }
           color[u] = 2;
       }
   }
   
   static void generatePath() {
       int pointer = finalNode;
       finalPath.add(pointer);
       pointer = parent[pointer];
       while (pointer != finalNode) {
           finalPath.add(pointer);
           pointer = parent[pointer];
       }
   }

   static void DFS() {
//     Performs a "full" DFS on graph
       for (int i = 1; i < color.length; i++) {
           if (color[i] == 0 && !cycleFound) {
               recDFS(i);
           }
       }
   }

   public static void main(String[] args) throws IOException {
       BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);
       Scanner scanner = new Scanner(System.in);
       
//     We set values n and m based on the first line of the input
       String[] firstLine = scanner.nextLine().split(" ");
       int n = Integer.parseInt(firstLine[0]);
       int m = Integer.parseInt(firstLine[1]);
       
//     We set up the arrays and the Graph object with the
//     appropriate numbers
       graph = new Graph(n + 1);
       color = new int[n + 1]; 
       parent = new int[n + 1];
       
//     Now we populate the graph
       for (int i = 0; i < m; i++) {
           String[] line = scanner.nextLine().split(" ");
           int from = Integer.parseInt(line[0]);
           int to = Integer.parseInt(line[1]);
           graph.addEdge(from, to);
       }
       
       DFS(); //    Here we call the DFS function which does most of the heavy lifting
       
       
       if (cycleFound) {
           buff.write("1\n");
           for (int i = finalPath.size() - 1; i >= 0; i--) {
               buff.write(finalPath.get(i) + " ");
           }
       }
       else {
           buff.write("0");
       }
       
       
       scanner.close();
       buff.close();
   }

}
