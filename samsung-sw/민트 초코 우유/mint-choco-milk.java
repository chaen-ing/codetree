

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

/**
 * N * N이고 N<=50 음식 F : T 민트 / C 초코 / M 우유 조합이 가
 */

public class Main {
    static int N, T;
    static int[][] food; // 비트마스킹
    static int[][] belief;
    static int[] sumB; // 각 그룹별신앙심 (총 7개)
    static PriorityQueue<Rep> repQueue;
    static int[][] move = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    // static boolean [][] visited;

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());

        food = new int[N + 1][N + 1];
        for (int i = 1; i <= N; i++) {
            String str = br.readLine();
            for (int j = 1; j <= N; j++) {
                char c = str.charAt(j - 1);

                if (c == 'T')
                    food[i][j] = 1;
                else if (c == 'C')
                    food[i][j] = 2;
                else
                    food[i][j] = 4;
            }
        }

        belief = new int[N + 1][N + 1];
        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= N; j++) {
                belief[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        repQueue = new PriorityQueue<>();

        while (T-- > 0) {
            // 1. 집단 구하기 (BFS) : 아침 + 점심
            // 대표 구하기
            // 구하면서 구성원 수 + 하면
            // 대표들은 우선순위 큐에 넣어놓기
            findGroup();

            // 2. 신앙심 변화 배열을 추가로 유지하면서 대표에게 추가되는 값을 기록

            // 3. 전파
            // 전파하면서 변화하는 신앙심을 변화 배열에 기록
            // 변화된 집합 정보를 map에 기록
            // defense 배열 필요
            spread();

            // 4. 배열 돌면서 그룹별 더하기
            sumBelief();
            
            System.out.print(sumB[7] + " ");
            System.out.print(sumB[3] + " ");
            System.out.print(sumB[5] + " ");
            System.out.print(sumB[6] + " ");
            System.out.print(sumB[4] + " ");
            System.out.print(sumB[2] + " ");
            System.out.println(sumB[1]);
        }

    }

    public static void findGroup() {
        boolean[][] visited = new boolean[N + 1][N + 1];
        ArrayDeque<Rep> queue;

        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                if (visited[i][j])
                    continue;

                // 그룹 탐색 시작
                queue = new ArrayDeque<>();

                queue.offer(new Rep(i, j));
                visited[i][j] = true;

                int repR = i;
                int repC = j;
                int group = food[i][j];
                int maxBelief = belief[i][j];
                int cnt = 1;

                while (!queue.isEmpty()) {
                    Rep cur = queue.poll();

                    for (int k = 0; k < 4; k++) {
                        int nr = cur.r + move[k][0];
                        int nc = cur.c + move[k][1];

                        if (nr <= 0 || nc <= 0 || nr > N || nc > N)
                            continue;
                        if (food[nr][nc] != group)
                            continue;
                        if (visited[nr][nc])
                            continue;

                        queue.offer(new Rep(nr, nc));
                        visited[nr][nc] = true;
                        cnt++;
                        if (belief[nr][nc] > maxBelief || (belief[nr][nc] == maxBelief && nr < repR)
                                || (belief[nr][nc] == maxBelief && nr == repR && nc < repC)) {

                            repR = nr;
                            repC = nc;
                            maxBelief = belief[nr][nc];
                        }
                    }
                }

                // 대표자 선정
                repQueue.offer(new Rep(repR, repC, maxBelief + cnt, group));
                belief[repR][repC] = (maxBelief + cnt);
            }
        }

    }

    public static void spread() {
        boolean[][] visited = new boolean[N + 1][N + 1];

        while (!repQueue.isEmpty()) {
            Rep rep = repQueue.poll();

            int dir = rep.belief % 4;

            int nr = rep.r;
            int nc = rep.c;
            //System.out.println(nr + " " + nc + " " + rep.belief + " " + rep.group);
            if (visited[nr][nc])
                continue;

            int nBelief = rep.belief - 1;
            belief[nr][nc] = 1;

            while (true) {
                //printmap();
                nr += move[dir][0];
                nc += move[dir][1];

                if (nr <= 0 || nc <= 0 || nr > N || nc > N)
                    break;
                if (nBelief <= 0)
                    break;
                if (food[nr][nc] == rep.group)
                    continue;

                // 강한 전파
                int y = belief[nr][nc];

                if (nBelief > y) {
                    nBelief = nBelief - y - 1;
                    food[nr][nc] = rep.group;
                    belief[nr][nc]++;
                } else { // 약한 전파
                    food[nr][nc] |= rep.group;
                    belief[nr][nc] += nBelief;
                    nBelief = 0;
                }

                visited[nr][nc] = true;
            //    printmap();
            }
        }
    }

    public static void sumBelief() {
        sumB = new int[8];
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                sumB[food[i][j]] += belief[i][j];
            }
        }
    }

    public static void printmap() {
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                System.out.print(food[i][j] + " " + belief[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

}

class Rep implements Comparable<Rep> {
    int r;
    int c;
    int belief;
    int group;

    public Rep(int r, int c, int belief, int group) {
        this.r = r;
        this.c = c;
        this.belief = belief;
        this.group = group;
    }

    public Rep(int r, int c) {
        this.r = r;
        this.c = c;
        this.belief = 0;
        this.group = 0;
    }

    @Override
    public int compareTo(Rep o) {
        int thisCount = Integer.bitCount(this.group);
        int oCount = Integer.bitCount(o.group);

        if (thisCount != oCount) {
            return Integer.compare(thisCount, oCount);
        }

        if (this.belief != o.belief) {
            return Integer.compare(o.belief, this.belief);
        }

        if (this.r != o.r) {
            return Integer.compare(this.r, o.r);
        }

        return Integer.compare(this.c, o.c);

    }

}
