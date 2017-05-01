import java.io.*;
import java.util.*;

public class Govern {
    private static HashMap<String, Set<String>> graph = new HashMap<>();

    private static HashMap<String, Boolean> visited = new HashMap<>();
    private static List<String> documents = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        initGraph();

        for (String document : graph.keySet()) {
            if (!visited.getOrDefault(document, false)) {
                visit(document);
            }
        }

        saveResult();
    }


    private static void visit(String document) {
        Stack<String> stack = new Stack<>();
        stack.push(document);
        visited.put(document, true);

        while (!stack.isEmpty()) {
            String vertex = stack.peek();
            String unvisited = getAnyUnvisitedConnectedVertex(vertex);
            if (unvisited == null) {
                documents.add(vertex);
                stack.pop();
            } else {
                visited.put(unvisited, true);
                stack.push(unvisited);
            }
        }
    }


   /*
    With list
    private static void visit(String document) {
        Stack<String> stack = new Stack<>();
        stack.push(document);

        while (!stack.isEmpty()) {
            String vertex = stack.peek();
            Set<String> unvisited = getUnvisitedConnectedVertexes(vertex);
            if (unvisited.isEmpty()) {
                visited.put(vertex, true);
                documents.add(vertex);
                stack.pop();
            } else {
                for (String v : unvisited) {
                    stack.push(v);
                }
            }
        }
    }*/

    private static String getAnyUnvisitedConnectedVertex(String vertex) {
        Set<String> connected = graph.get(vertex);
        for (String v : connected) {
            if (!visited.getOrDefault(v, false)) {
                return v;
            }
        }
        return null;
    }

    private static Set<String> getUnvisitedConnectedVertexes(String vertex) {
        Set<String> connected = graph.get(vertex);
        Set<String> unvisited = new HashSet<>();
        for (String v : connected) {
            if (!visited.getOrDefault(v, false)) {
                unvisited.add(v);
            }
        }
        return unvisited;
    }

    private static void saveResult() throws Exception {
        PrintWriter writer = new PrintWriter("govern.out", "UTF-8");
        for (String document : documents) {
            writer.println(document);
        }
        writer.println();
        writer.close();
    }

    private static void initGraph() throws IOException {
        File data = new File("govern.in");
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] lines = line.split(" ");
                graph.putIfAbsent(lines[0], new HashSet<>());
                graph.putIfAbsent(lines[1], new HashSet<>());

                Set<String> connections = graph.get(lines[0]);
                connections.add(lines[1]);
            }
        }
    }
}
