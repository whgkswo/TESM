package net.whgkswo.tesm.pathfinding.maze;

import java.util.*;

public class MazeGenerator {
    public boolean[][] generate(int width, int height) {
        // 미로의 폭과 높이는 홀수여야 함
        if(width % 2 == 0) width++;
        if(height % 2 == 0) height++;

        // 주어진 폭과 높이로 배열을 초기화 (벽은 false, 길은 true)
        boolean[][] maze = new boolean[height][width];  // height와 width 순서 변경

        // 시작점을 뚫기
        dfs(maze, 1, 1);

        // 생성된 미로 출력
        /*for(int i = 0; i < maze.length; i++) {
            for(int j = 0; j < maze[0].length; j++) {
                System.out.print(maze[i][j] ? "□ " : "■ ");
            }
            System.out.println();
        }*/

        return maze;
    }

    private void dfs(boolean[][] maze, int x, int y) {
        maze[y][x] = true;  // 현재 위치를 길로 만듦
        // 4방향을 리스트로 만들고 섞기
        List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
        Collections.shuffle(directions);
        // 각 방향에 대해 시도
        for(Direction dir : directions) {
            // 다음 위치는 현재 위치에서 2칸 이동
            int nextX = x + (dir.getDx() * 2);
            int nextY = y + (dir.getDy() * 2);
            // 맵 밖으로 나가면 건너뛰기
            if(nextX < 0 || nextX >= maze[0].length || nextY < 0 || nextY >= maze.length) {
                continue;
            }
            // 이미 방문한 곳이면 건너뛰기
            if(maze[nextY][nextX]) {
                continue;
            }
            // 중간 지점을 길로 만들기
            maze[y + dir.getDy()][x + dir.getDx()] = true;
            // 다음 위치로 이동
            dfs(maze, nextX, nextY);
        }
    }

    private enum Direction {
        UP(0, -1),
        RIGHT(1, 0),
        DOWN(0, 1),
        LEFT(-1, 0);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }
    }
}
