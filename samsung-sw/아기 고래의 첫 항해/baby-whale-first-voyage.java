import java.util.*;
import java.io.*;

/**
- N * N
- 0 바다, 1 암초
- r,c 출발 
- d 방향 상하좌우
**/

/**
- 목표 : 헤엄칠 수 있는 모든 바다 탐험
- 1,2를 반복한다

- 1. 인접탐험
    - 상하좌우 중 방문하지 않은 곳으로 이동
    - 우선순위가 있음 
        - 현재 보는 방향
        - 좌회전 후 직진
        - 우회전 후 직진
        - 뒤로 이동 (180도 회전)
    - 이동 후 d는 이동한 방향으로 갱신됨
    - 방문가능한 곳이 없을때까지 반복

- 2. 가장 가까운 바다로 이동
    - 인접칸에 미방문 바다가 없다면, 아직 방문하지 않은 칸 중 가장 가까운 칸으로 이동
    - 갔던 곳 지나도 됨 > 그러나 해당 bfs를 위한 visited 는 따로 필요
    - 가장 가까운게 여러개라면 행번호 작은거, 열번호 작은거 순 > 상좌우하
    - 최단거리로 이동해서 옮기기
    - 마지막 이동방향으로 방향 갱신

- 출력 : 방문하는 칸 위치 출력. 시작도 포함

**/
public class Main {
    static int N, r, c, d, cnt;
    static int [][] map;
    static boolean [][] visited;
    static int [] dd = {0, 3, 1, 2};
    static int [] dr = {-1, 0, 1, 0}; // 상 우 하 좌
    static int [] dc = {0, 1, 0, -1};
    static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        d = Integer.parseInt(st.nextToken());
        cnt = 0;

if(d == 1) d = 0;       // 상
else if(d == 2) d = 2;  // 하
else if(d == 3) d = 3;  // 좌
else if(d == 4) d = 1;  // 우

        map = new int [N+1][N+1];
        visited = new boolean [N+1][N+1];

        for(int i = 1; i <= N; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 1; j <= N; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
                if(map[i][j] == 0)  cnt++;
            }
        }

        while(cnt != 0){
            explorer();
            //System.out.println("explorer : " + r + " " + c + " " + d + " " + cnt);

            if(cnt == 0)    break;

            moveToSea(r, c);
            //System.out.println("moveToSea : " + r + " " + c + " " + d);
        }
    }

    public static void explorer(){
        ArrayDeque<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(r, c, d));
        visited[r][c] = true;
        System.out.println(r + " " + c );
        cnt--;
        int nr = r;
        int nc = c;
        int nd = d;

        while(!queue.isEmpty()){
            if(cnt == 0)    return;
            Node cur = queue.poll();

            for(int i = 0; i < 4; i++){
                d = (cur.d + dd[i]) % 4;
                r = cur.r + dr[d];
                c = cur.c + dc[d];
                //System.out.println(r + " " + c + " " + d + "**");

                if(r == 0 || c == 0 || r > N || c > N)  continue;
                if(map[r][c] == 1)    continue;
                if(visited[r][c]) continue;

                // 이동 가능한 경우
                queue.offer(new Node(r, c, d));
                visited[r][c] = true;
                cnt--;
                System.out.println(r + " " + c);
                nr = r;
                nc = c;
                nd = d;
                break;
            }
        }
        r = nr;
        c = nc;
        d = nd;
    }

    public static void moveToSea(int startR, int startC){
        boolean [][] tmpVisited = new boolean [N+1][N+1];
        int [] tmpDr = {-1,0,0,1}; // 상 좌 우 하
        int [] tmpDc = {0,-1,1,0};
        
        ArrayDeque<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(r, c, d));
        tmpVisited[r][c] = true;

        while(!queue.isEmpty()){
            int size = queue.size();
            ArrayList<Node> list = new ArrayList<>();

            for(int s = 0; s < size; s++){
                Node cur = queue.poll();

                for(int i = 0; i < 4; i++){
                    r = cur.r + tmpDr[i];
                    c = cur.c + tmpDc[i];
                    if(i == 0)  d = 0;
                    else if(i == 1)  d = 3;
                    else if(i == 2) d = 1;
                    else d = 2;

                    if(r == 0 || c == 0 || r > N || c > N)  continue;
                    if(map[r][c] == 1)    continue;
                    if(tmpVisited[r][c]) continue;

                    // 이동 가능한 경우
                    if(!visited[r][c]){
                        list.add(new Node(r, c, d));
                    }
                    queue.offer(new Node(r, c, d));
                    tmpVisited[r][c] = true;
                }
            }
            
            if(!list.isEmpty()){
                Collections.sort(list);
                // 문제에서 지정한 최단경로로 이동했을 때의 마지막 방향
                d = findLastDirection(startR, startC, list.get(0).r, list.get(0).c);

                r = list.get(0).r;
                c = list.get(0).c;
                return;
            }
        }
    }

    public static int findLastDirection(int sr, int sc, int tr, int tc){
    int[][] dist = new int[N + 1][N + 1];

    for(int i = 1; i <= N; i++){
        Arrays.fill(dist[i], -1);
    }

    ArrayDeque<int[]> queue = new ArrayDeque<>();
    queue.offer(new int[]{tr, tc});
    dist[tr][tc] = 0;

    // 목적지에서 역으로 거리 계산
    while(!queue.isEmpty()){
        int[] cur = queue.poll();

        for(int i = 0; i < 4; i++){
            int nr = cur[0] + dr[i];
            int nc = cur[1] + dc[i];

            if(nr < 1 || nc < 1 || nr > N || nc > N) continue;
            if(map[nr][nc] == 1) continue;
            if(dist[nr][nc] != -1) continue;

            dist[nr][nc] = dist[cur[0]][cur[1]] + 1;
            queue.offer(new int[]{nr, nc});
        }
    }

    // 문제의 이동 우선순위: 좌, 하, 우, 상
    int[] routeD = {3, 2, 1, 0};

    int cr = sr;
    int cc = sc;
    int lastD = d;

    while(cr != tr || cc != tc){
        for(int nd : routeD){
            int nr = cr + dr[nd];
            int nc = cc + dc[nd];

            if(nr < 1 || nc < 1 || nr > N || nc > N) continue;
            if(map[nr][nc] == 1) continue;

            // 목적지까지 거리가 정확히 1 감소하는 칸
            if(dist[nr][nc] == dist[cr][cc] - 1){
                cr = nr;
                cc = nc;
                lastD = nd;
                break;
            }
        }
    }

    return lastD;
}
}

class Node implements Comparable<Node>{
    int r;
    int c;
    int d;

    public Node(int r, int c, int d){
        this.r = r;
        this.c = c;
        this.d = d;
    }

    @Override
    public int compareTo(Node o){
        if(o.r == this.r){
            return Integer.compare(this.c, o.c);
        }
        return Integer.compare(this.r, o.r);
    }
}