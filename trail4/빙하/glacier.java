

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class Main {
    static int N, M;
    static int glcCnt = 0;
    static int[][] map;
    static int[][] dir = {{-1, 0,}, {1, 0}, {0, -1}, {0, 1}};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                int n = Integer.parseInt(st.nextToken());
                map[i][j] = n;
                // 빙하면 카운트
                if (n == 1)
                    glcCnt++;
            }
        }

        for (int i = 1; i <= N * M; i++) {
            int tmpGlcCount = glcCnt;
            bfs();
            if (glcCnt == 0) {
                System.out.println(i + " " + tmpGlcCount);
                return;
            }
        }
    }

    static void bfs() {
        ArrayDeque<Node> water = new ArrayDeque<>();
        boolean[][] visited = new boolean[N][M];

        water.offer(new Node(0, 0));
        visited[0][0] = true;

        while (!water.isEmpty()) {
            Node curWater = water.poll();

            for (int j = 0; j < 4; j++) {
                int dr = curWater.r + dir[j][0];
                int dc = curWater.c + dir[j][1];

                if (dr < 0 || dc < 0 || dr >= N || dc >= M)
                    continue;
                if (visited[dr][dc])
                    continue;

                visited[dr][dc] = true;

                if (map[dr][dc] == 0) {
                    water.offer(new Node(dr, dc));
                } else {
                    map[dr][dc] = 0;
                    glcCnt--;
                }
            }
        }
    }

}

class Node {
    int r, c;

    public Node(int r, int c) {
        this.r = r;
        this.c = c;
    }
}
