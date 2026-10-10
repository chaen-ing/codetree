

import java.io.*;
import java.util.*;

public class Main {
    static int K, M;
    static int[][] map;
    static int[][] tmpMap;
    static boolean[][] visited;
    static ArrayDeque<Integer> treasureList;
    static int[][] move = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    static int maxVal, turn, findR, findC, sumVal;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[6][6];
        for (int i = 1; i <= 5; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= 5; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        treasureList = new ArrayDeque<>();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            treasureList.add(Integer.parseInt(st.nextToken()));
        }

        while (K-- > 0) {
            findR = 0;
            findC = 0;
            // 1. 회전할 격자, 각도 선택
            int[][] newMap = findNewMap();
            //System.out.println("answer" + maxVal + " " + findR + " " + findC);

            // 여기서 찾은거 없으면 종료
            if (findR == 0 && findC == 0)
                break;

            // newMap 반영
            for (int i = 1; i <= 5; i++) {
                for (int j = 1; j <= 5; j++) {
                    map[i][j] = newMap[i][j];
                }
            }

            // 2. 유물 제거 > 채우기 > 반복
            sumVal = 0;
            changeTreasure();
            if(sumVal == 0)    break;
            System.out.print(sumVal + " ");
        }

    }

    public static void changeTreasure() {
        while(true) {
            int tmpSumVal = 0;
            // 1. 유물 제거
            //printMap();
            visited = new boolean[6][6];
            
            for (int a = 1; a <= 5; a++) {
                for (int b = 1; b <= 5; b++) {
                    if (visited[a][b])
                        continue;
                    if(map[a][b] == 0)    continue;
                    
                    visited[a][b] = true;
                    int tmp = mapDfs(a, b);
                    
                    if (tmp >= 3) {
                        //System.out.println("tmp : " + tmp + " " + a+" "+b);                        
                        visited = new boolean[6][6];
                        tmpSumVal += tmp;
                        remove(a, b, map[a][b]);
                        map[a][b] = 0;
                    }
                    
                }
            }
            if(tmpSumVal == 0)    break;
            //System.out.println("currnet tmpSumVal : " + tmpSumVal);
            sumVal += tmpSumVal;
            //printMap();
            
            // 2. 채우기
            for(int i = 1; i <= 5; i++) {
                for(int j = 5; j >= 1; j--) {
                    if(map[j][i] != 0) continue;
                    int n = treasureList.poll();
                    map[j][i] = n;
                    treasureList.offer(n);    
                }
            }
        }

    }

    public static int[][] findNewMap() {
        tmpMap = new int[6][6];
        maxVal = Integer.MIN_VALUE;
        turn = 0;
        findR = 0;
        findC = 0;

        for (int i = 2; i <= 4; i++) {
            for (int j = 2; j <= 4; j++) {
                for (int k = 0; k < 3; k++) {
                    // 1. tmpMap 리셋
                    for (int a = 1; a <= 5; a++) {
                        for (int b = 1; b <= 5; b++) {
                            tmpMap[a][b] = map[a][b];
                        }
                    }

                    // 2. tmpMap에 회전
                    if (k == 0) { // 90
                        tmpMap[i - 1][j - 1] = map[i + 1][j - 1];
                        tmpMap[i - 1][j] = map[i][j - 1];
                        tmpMap[i - 1][j + 1] = map[i - 1][j - 1];

                        tmpMap[i][j - 1] = map[i + 1][j];
                        tmpMap[i][j + 1] = map[i - 1][j];

                        tmpMap[i + 1][j - 1] = map[i + 1][j + 1];
                        tmpMap[i + 1][j] = map[i][j + 1];
                        tmpMap[i + 1][j + 1] = map[i - 1][j + 1];
                    } else if (k == 1) { // 180
                        tmpMap[i - 1][j - 1] = map[i + 1][j + 1];
                        tmpMap[i - 1][j] = map[i + 1][j];
                        tmpMap[i - 1][j + 1] = map[i + 1][j - 1];

                        tmpMap[i][j - 1] = map[i][j + 1];
                        tmpMap[i][j + 1] = map[i][j - 1];

                        tmpMap[i + 1][j - 1] = map[i - 1][j + 1];
                        tmpMap[i + 1][j] = map[i - 1][j];
                        tmpMap[i + 1][j + 1] = map[i - 1][j - 1];
                    } else { // 270
                        tmpMap[i - 1][j - 1] = map[i - 1][j + 1];
                        tmpMap[i - 1][j] = map[i][j + 1];
                        tmpMap[i - 1][j + 1] = map[i + 1][j + 1];

                        tmpMap[i][j - 1] = map[i - 1][j];
                        tmpMap[i][j + 1] = map[i + 1][j];

                        tmpMap[i + 1][j - 1] = map[i - 1][j - 1];
                        tmpMap[i + 1][j] = map[i][j - 1];
                        tmpMap[i + 1][j + 1] = map[i + 1][j - 1];
                    }

                    visited = new boolean[6][6];
                    int tmpVal = 0;
                    for (int a = 1; a <= 5; a++) {
                        for (int b = 1; b <= 5; b++) {
                            if (visited[a][b])
                                continue;
                            visited[a][b] = true;
                            int tmp = dfs(a, b);
                            if (tmp >= 3)
                                tmpVal += tmp;
                        }
                    }
                    // printMap();
                    // System.out.println("tmpVal : " + tmpVal);

                    if (tmpVal > maxVal) { // 획득가치 최대
                        maxVal = tmpVal;
                        turn = k;
                        findR = i;
                        findC = j;
                    } else if (tmpVal == maxVal) {
                        if (k < turn) { // 각도가 가장 작은것
                            maxVal = tmpVal;
                            turn = k;
                            findR = i;
                            findC = j;
                        } else if (k == turn) {
                            if (j < findC) { // 열이 작은
                                maxVal = tmpVal;
                                turn = k;
                                findR = i;
                                findC = j;
                            } else if (j == findC) {
                                if (i < findR) { // 행이 작은
                                    maxVal = tmpVal;
                                    turn = k;
                                    findR = i;
                                    findC = j;
                                }
                            }
                        }
                    }
                }
            }
        }

        // 결정 이후 tmpMap 만들기
        // tmpMap 리셋
        for (int a = 1; a <= 5; a++) {
            for (int b = 1; b <= 5; b++) {
                tmpMap[a][b] = map[a][b];
            }
        }

        int i = findR;
        int j = findC;
        if (turn == 0) { // 90
            tmpMap[i - 1][j - 1] = map[i + 1][j - 1];
            tmpMap[i - 1][j] = map[i][j - 1];
            tmpMap[i - 1][j + 1] = map[i - 1][j - 1];

            tmpMap[i][j - 1] = map[i + 1][j];
            tmpMap[i][j + 1] = map[i - 1][j];

            tmpMap[i + 1][j - 1] = map[i + 1][j + 1];
            tmpMap[i + 1][j] = map[i][j + 1];
            tmpMap[i + 1][j + 1] = map[i - 1][j + 1];
        } else if (turn == 1) { // 180
            tmpMap[i - 1][j - 1] = map[i + 1][j + 1];
            tmpMap[i - 1][j] = map[i + 1][j];
            tmpMap[i - 1][j + 1] = map[i + 1][j - 1];

            tmpMap[i][j - 1] = map[i][j + 1];
            tmpMap[i][j + 1] = map[i][j - 1];

            tmpMap[i + 1][j - 1] = map[i - 1][j + 1];
            tmpMap[i + 1][j] = map[i - 1][j];
            tmpMap[i + 1][j + 1] = map[i - 1][j - 1];
        } else { // 270
            tmpMap[i - 1][j - 1] = map[i - 1][j + 1];
            tmpMap[i - 1][j] = map[i][j + 1];
            tmpMap[i - 1][j + 1] = map[i + 1][j + 1];

            tmpMap[i][j - 1] = map[i - 1][j];
            tmpMap[i][j + 1] = map[i + 1][j];

            tmpMap[i + 1][j - 1] = map[i - 1][j - 1];
            tmpMap[i + 1][j] = map[i][j - 1];
            tmpMap[i + 1][j + 1] = map[i + 1][j - 1];
        }

        return tmpMap;
    }

    public static int dfs(int r, int c) {
        int val = 1;

        for (int i = 0; i < 4; i++) {
            int nr = r + move[i][0];
            int nc = c + move[i][1];

            if (nr < 1 || nc < 1 || nr > 5 || nc > 5)
                continue;
            if (visited[nr][nc])
                continue;
            if (tmpMap[nr][nc] == tmpMap[r][c]) {

                visited[nr][nc] = true;
                val += dfs(nr, nc);
            }
        }

        return val;
    }
    
    public static int mapDfs(int r, int c) {
        int val = 1;

        for (int i = 0; i < 4; i++) {
            int nr = r + move[i][0];
            int nc = c + move[i][1];

            if (nr < 1 || nc < 1 || nr > 5 || nc > 5)
                continue;
            if (visited[nr][nc])
                continue;
            if (map[nr][nc] == map[r][c]) {

                visited[nr][nc] = true;
                val += mapDfs(nr, nc);
            }
        }

        return val;
    }

    public static void remove(int r, int c, int n) {
        for (int i = 0; i < 4; i++) {
            int nr = r + move[i][0];
            int nc = c + move[i][1];

            if (nr < 1 || nc < 1 || nr > 5 || nc > 5)
                continue;
            if (visited[nr][nc])
                continue;
            if (map[nr][nc] != n)
                continue;

            map[nr][nc] = 0;
            visited[nr][nc] = true;
            remove(nr, nc, n);
        }
        return;
    }

    public static void printMap() {
        System.out.println("print map");
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

}
