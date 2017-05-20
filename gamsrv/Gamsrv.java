import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

import static java.util.Arrays.binarySearch;

public class Gamsrv {

    private static UndirectedGraph graph = new UndirectedGraph();
    private static int[] clients;
    private static double distTo[];
    private static double maxDistTo[];
    private static Comparator<Dist> minHeapComparator =(e1,e2) -> {
        return e1.val == e2.val ? 0 : e1.val < e2.val ? 1 : -1;
    };

    private static IndexMinPQ<Double> queue ;


    public static void main(String[] args) throws Exception {
        int nodesNumber;
        int connectionsNumber;


        File data = new File("gamsrv.in");
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            String[] networkData = br.readLine().split(" ");
            nodesNumber = Integer.valueOf(networkData[0]);
            connectionsNumber = Integer.valueOf(networkData[1]);
            String clientsData[] = br.readLine().split(" ");

            clients = new int[clientsData.length];
            int cnt = 0;
            for (String client : clientsData) {
                clients[cnt++] = Integer.valueOf(client);
            }


            for (String line; (line = br.readLine()) != null; ) {
                String[] topology = line.split(" ");
                int from = Integer.valueOf(topology[0]);
                int to = Integer.valueOf(topology[1]);
                double latency = Double.valueOf(topology[2]);

                graph.addEdge(from, to, latency);
            }
        }

        maxDistTo = new double[nodesNumber + 1];
        for (int client : clients) {
            resetDistances(nodesNumber);
            distTo[client] = 0;
            maxDistTo[client] = Double.POSITIVE_INFINITY;

            queue = new IndexMinPQ<>(nodesNumber + 1);
            queue.insert(client, 0.0);

            while (!queue.isEmpty()) {
                relax(queue.delMin());
            }
        }

        double minDist = findMinDist();

        saveResult(minDist);
    }

    private static void saveResult(double result) throws Exception {
        PrintWriter writer = new PrintWriter("gamsrv.out", "UTF-8");
        writer.printf("%.0f\n",result);
        writer.close();
    }

    private static double findMinDist() {
        double min = maxDistTo[1];
        for (int i = 2; i < maxDistTo.length; i++) {
            if (maxDistTo[i] < min) {
                min = maxDistTo[i];
            }
        }

        return min;
    }

    private static void resetDistances(int nodesNumber) {
        distTo = new double[nodesNumber + 1];
        for (int i = 0; i < distTo.length; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }
    }

    private static void relax(int vertex) {
        Set<Edge> connections = graph.connections(vertex);
        for (Edge connection : connections) {
            int to = connection.to;
            double calculatedDist = distTo[vertex] + connection.weight;
            if (distTo[to] > calculatedDist) {
                distTo[to] = calculatedDist;
                updateMaxDist(to, calculatedDist);

                if (queue.contains(to)) {
                    queue.changeKey(to, distTo[to]);
                } else {
                    queue.insert(to, distTo[to]);
                }
            }
        }
    }

    private static void updateMaxDist(int vertex, double dist) {
        if (isClient(vertex)) {
            maxDistTo[vertex] = Double.POSITIVE_INFINITY;
            return;
        }

        if (dist > maxDistTo[vertex]) {
            maxDistTo[vertex] = dist;
        }
    }

    private static boolean isClient(int vertex) {
        return binarySearch(clients, vertex) > 0;
    }

    private static class Edge {
        int from;
        int to;
        double weight;

        public Edge(int from, int to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }


    private static class Dist {
        int to;
        int val;

        public Dist(int to, int val) {
            this.to = to;
            this.val = val;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Dist dist = (Dist) o;

            return to == dist.to;
        }

        @Override
        public int hashCode() {
            return to;
        }
    }

    private static class UndirectedGraph {
        HashMap<Integer, Set<Edge>> edges = new HashMap<>();

        public void addEdge(int from, int to, double weight) {
            edges.putIfAbsent(from, new HashSet<>());
            edges.putIfAbsent(to, new HashSet<>());

            Set<Edge> fromConnections = edges.get(from);
            fromConnections.add(new Edge(from, to, weight));

            Set<Edge> toConnections = this.edges.get(to);
            toConnections.add(new Edge(to, from, weight));
        }

        public Set<Edge> connections(int vertex) {
            return edges.get(vertex);
        }
    }



    private static class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
        private int maxN;        // maximum number of elements on PQ
        private int n;           // number of elements on PQ
        private int[] pq;        // binary heap using 1-based indexing
        private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
        private Key[] keys;      // keys[i] = priority of i

        /**
         * Initializes an empty indexed priority queue with indices between {@code 0}
         * and {@code maxN - 1}.
         * @param  maxN the keys on this priority queue are index from {@code 0}
         *         {@code maxN - 1}
         * @throws IllegalArgumentException if {@code maxN < 0}
         */
        public IndexMinPQ(int maxN) {
            if (maxN < 0) throw new IllegalArgumentException();
            this.maxN = maxN;
            n = 0;
            keys = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
            pq   = new int[maxN + 1];
            qp   = new int[maxN + 1];                   // make this of length maxN??
            for (int i = 0; i <= maxN; i++)
                qp[i] = -1;
        }

        /**
         * Returns true if this priority queue is empty.
         *
         * @return {@code true} if this priority queue is empty;
         *         {@code false} otherwise
         */
        public boolean isEmpty() {
            return n == 0;
        }

        /**
         * Is {@code i} an index on this priority queue?
         *
         * @param  i an index
         * @return {@code true} if {@code i} is an index on this priority queue;
         *         {@code false} otherwise
         * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
         */
        public boolean contains(int i) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            return qp[i] != -1;
        }

        /**
         * Returns the number of keys on this priority queue.
         *
         * @return the number of keys on this priority queue
         */
        public int size() {
            return n;
        }

        /**
         * Associates key with index {@code i}.
         *
         * @param  i an index
         * @param  key the key to associate with index {@code i}
         * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
         * @throws IllegalArgumentException if there already is an item associated
         *         with index {@code i}
         */
        public void insert(int i, Key key) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
            n++;
            qp[i] = n;
            pq[n] = i;
            keys[i] = key;
            swim(n);
        }

        /**
         * Returns an index associated with a minimum key.
         *
         * @return an index associated with a minimum key
         * @throws NoSuchElementException if this priority queue is empty
         */
        public int minIndex() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            return pq[1];
        }

        /**
         * Returns a minimum key.
         *
         * @return a minimum key
         * @throws NoSuchElementException if this priority queue is empty
         */
        public Key minKey() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            return keys[pq[1]];
        }

        /**
         * Removes a minimum key and returns its associated index.
         * @return an index associated with a minimum key
         * @throws NoSuchElementException if this priority queue is empty
         */
        public int delMin() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            int min = pq[1];
            exch(1, n--);
            sink(1);
            assert min == pq[n+1];
            qp[min] = -1;        // delete
            keys[min] = null;    // to help with garbage collection
            pq[n+1] = -1;        // not needed
            return min;
        }

        /**
         * Returns the key associated with index {@code i}.
         *
         * @param  i the index of the key to return
         * @return the key associated with index {@code i}
         * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
         * @throws NoSuchElementException no key is associated with index {@code i}
         */
        public Key keyOf(int i) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            else return keys[i];
        }

        /**
         * Change the key associated with index {@code i} to the specified value.
         *
         * @param  i the index of the key to change
         * @param  key change the key associated with index {@code i} to this key
         * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
         * @throws NoSuchElementException no key is associated with index {@code i}
         */
        public void changeKey(int i, Key key) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            keys[i] = key;
            swim(qp[i]);
            sink(qp[i]);
        }

        /**
         * Change the key associated with index {@code i} to the specified value.
         *
         * @param  i the index of the key to change
         * @param  key change the key associated with index {@code i} to this key
         * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
         * @deprecated Replaced by {@code changeKey(int, Key)}.
         */
        @Deprecated
        public void change(int i, Key key) {
            changeKey(i, key);
        }

        /**
         * Decrease the key associated with index {@code i} to the specified value.
         *
         * @param  i the index of the key to decrease
         * @param  key decrease the key associated with index {@code i} to this key
         * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
         * @throws IllegalArgumentException if {@code key >= keyOf(i)}
         * @throws NoSuchElementException no key is associated with index {@code i}
         */
        public void decreaseKey(int i, Key key) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            if (keys[i].compareTo(key) <= 0)
                throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
            keys[i] = key;
            swim(qp[i]);
        }

        /**
         * Increase the key associated with index {@code i} to the specified value.
         *
         * @param  i the index of the key to increase
         * @param  key increase the key associated with index {@code i} to this key
         * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
         * @throws IllegalArgumentException if {@code key <= keyOf(i)}
         * @throws NoSuchElementException no key is associated with index {@code i}
         */
        public void increaseKey(int i, Key key) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            if (keys[i].compareTo(key) >= 0)
                throw new IllegalArgumentException("Calling increaseKey() with given argument would not strictly increase the key");
            keys[i] = key;
            sink(qp[i]);
        }

        /**
         * Remove the key associated with index {@code i}.
         *
         * @param  i the index of the key to remove
         * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
         * @throws NoSuchElementException no key is associated with index {@code i}
         */
        public void delete(int i) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            int index = qp[i];
            exch(index, n--);
            swim(index);
            sink(index);
            keys[i] = null;
            qp[i] = -1;
        }


        /***************************************************************************
         * General helper functions.
         ***************************************************************************/
        private boolean greater(int i, int j) {
            return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
        }

        private void exch(int i, int j) {
            int swap = pq[i];
            pq[i] = pq[j];
            pq[j] = swap;
            qp[pq[i]] = i;
            qp[pq[j]] = j;
        }


        /***************************************************************************
         * Heap helper functions.
         ***************************************************************************/
        private void swim(int k) {
            while (k > 1 && greater(k/2, k)) {
                exch(k, k/2);
                k = k/2;
            }
        }

        private void sink(int k) {
            while (2*k <= n) {
                int j = 2*k;
                if (j < n && greater(j, j+1)) j++;
                if (!greater(k, j)) break;
                exch(k, j);
                k = j;
            }
        }


        /***************************************************************************
         * Iterators.
         ***************************************************************************/

        /**
         * Returns an iterator that iterates over the keys on the
         * priority queue in ascending order.
         * The iterator doesn't implement {@code remove()} since it's optional.
         *
         * @return an iterator that iterates over the keys in ascending order
         */
        public Iterator<Integer> iterator() { return new HeapIterator(); }

        private class HeapIterator implements Iterator<Integer> {
            // create a new pq
            private IndexMinPQ<Key> copy;

            // add all elements to copy of heap
            // takes linear time since already in heap order so no keys move
            public HeapIterator() {
                copy = new IndexMinPQ<Key>(pq.length - 1);
                for (int i = 1; i <= n; i++)
                    copy.insert(pq[i], keys[pq[i]]);
            }

            public boolean hasNext()  { return !copy.isEmpty();                     }
            public void remove()      { throw new UnsupportedOperationException();  }

            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                return copy.delMin();
            }
        }



    }

}
