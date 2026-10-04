

import java.util.*;
import java.io.*;

/**
 * map : . 안전한 돌 / S 미끄러운 돌 / # 천적이 사는 
 *    - S : 착지 불가
 *    - # : 지나가는 것도 불가능
 *
 */

public class Main {
    
    static int N, Q;
    static char [][] map;
    static int [][][] visited;
    static int [] dr = {-1, 1, 0, 0};
    static int [] dc = {0, 0, -1, 1};
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        
        N = Integer.parseInt(br.readLine());
        map = new char [N+1][N+1];
        for(int i = 1; i <= N; i++) {
            String str = br.readLine();
            for(int j = 1; j <= N; j++) {
                map[i][j] = str.charAt(j-1);
            }
        }
        
        Q = Integer.parseInt(br.readLine());
        while(Q --> 0) {
            st = new StringTokenizer(br.readLine());
            System.out.println(
                    findRoute(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()))
                    );
        }
        
    }
    
    public static int findRoute(int startR, int startC, int endR, int endC) {
        // 시작이나 도착이 돌이 아닌 경우 바로 리턴
        if(map[startR][startC] != '.' || map[endR][endC] != '.')    return -1;
        
        // 해당 위치에 어떤 jump력으로 방문했는지 체크
        visited = new int [N+1][N+1][6];
        for(int i = 1; i <= N; i++){
            for(int j = 1; j <= N; j++){
                Arrays.fill(visited[i][j], Integer.MAX_VALUE);
            }
        }
        
        // 짧은 시간 먼저 빼기
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.offer(new Node(startR, startC, 1, 0));
        visited[startR][startC][1] = 0; 
        
        
        while(!queue.isEmpty()) {
            Node node = queue.poll();
            if(node.time > visited[node.r][node.c][node.jump])    continue;
            
            // System.out.println(node.r + " " + node.c + " "+ node.jump + " " + node.time);
            
            if(node.r == endR && node.c == endC) {
                return node.time;
            }
            
            // 1. jump
            for(int i = 0; i < 4; i++) {
                int nr = node.r + dr[i] * node.jump;
                int nc = node.c + dc[i] * node.jump;
                
                // 도착가능, 방문여부(jump) 체크 
                if(nr <= 0 || nc <= 0 || nr > N || nc > N)    continue;
                if(map[nr][nc] != '.') continue;
                
                // 지나는길 천적 
                boolean flag = false; 
                if(node.r != nr) {
                    int max = Math.max(node.r, nr);
                    int min = Math.min(node.r, nr);
                    
                    for(int j = min+1; j < max; j++) {
                        if(map[j][nc] == '#') {
                            flag = true;
                            break;
                        }
                    }
                }else {
                    int max = Math.max(node.c, nc);
                    int min = Math.min(node.c, nc);
                    
                    for(int j = min+1; j < max; j++) {
                        if(map[nr][j] == '#') {
                            flag = true;
                            break;
                        }
                    }
                }
                if(flag) {
                    continue;
                }
                
                int nTime = node.time + 1;
                if(nTime >= visited[nr][nc][node.jump])    continue;
                visited[nr][nc][node.jump] = nTime;
                    queue.offer(
                        new Node(nr, nc, node.jump, nTime)
                    );
                
            }
            
            // 2. 점프력 증가
            int nJump = node.jump + 1;
            if(nJump <= 5) {
                int nTime = node.time + (nJump * nJump);
                
                if(nTime <visited[node.r][node.c][nJump]) {
                    visited[node.r][node.c][nJump] = nTime;
                    queue.offer(new Node(node.r, node.c, nJump, nTime));
                }
                
                
            }

            
            // 3. 점프력 감소
            for(int i = node.jump - 1; i >= 1; i--) {
                int nTime = node.time+1;
        
                if(nTime >= visited[node.r][node.c][i])    continue;
                
                visited[node.r][node.c][i] = nTime;
                queue.offer(new Node(node.r, node.c, i, nTime));
            }
            
        }
        
        return -1;
        
    }


}

class Node implements Comparable<Node>{
    int r;
    int c;
    int jump;
    int time;
    
    public Node(int r, int c, int jump, int time) {
        this.r = r;
        this.c = c;
        this.jump = jump;
        this.time = time;
    }
    
    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.time, o.time);
    }
}

