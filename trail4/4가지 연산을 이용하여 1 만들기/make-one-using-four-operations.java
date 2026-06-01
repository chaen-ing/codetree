

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        boolean[] visited = new boolean[N * 2];
        ArrayDeque<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(N, 0));

        while (!queue.isEmpty()) {
            Node cur = queue.poll();

            if (cur.n == 1) {
                System.out.println(cur.cnt);
                return;
            }

            if (!visited[cur.n - 1]) {
                queue.offer(new Node(cur.n - 1, cur.cnt + 1));
                visited[cur.n - 1] = true;
            }
            if (!visited[cur.n + 1]) {
                queue.offer(new Node(cur.n + 1, cur.cnt + 1));
                visited[cur.n + 1] = true;
            }
            if ((!visited[cur.n / 2]) && (cur.n % 2 == 0)) {
                queue.offer(new Node(cur.n / 2, cur.cnt + 1));
                visited[cur.n / 2] = true;
            }
            if ((!visited[cur.n / 3]) && (cur.n % 3 == 0)) {
                queue.offer(new Node(cur.n / 3, cur.cnt + 1));
                visited[cur.n / 3] = true;
            }
        }

    }

}

class Node {
    int n;
    int cnt;

    public Node(int n, int cnt) {
        this.n = n;
        this.cnt = cnt;
    }
}
