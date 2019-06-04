package me.trilis.au.java.test5;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

public class Controller {
    private int[][] grid;
    private int lastR = -1;
    private int lastC = -1;
    private boolean areButtonsOpened = false;
    private int left;

    public Controller(int n) {
        grid = generateGrid(n);
        left = n * n;
    }

    public int getNumber(int r, int c) {
        return grid[r][c];
    }

    public Pair<Integer, Integer> getLastCoordinatesId() {
        return new Pair<>(lastR, lastC);
    }

    public enum Result {
        NOTHING, MATCH, NO_MATCH, WIN
    }

    public Result pushButton(int r, int c) {
        if (!areButtonsOpened || (lastR == r && lastC == c)) {
            lastR = r;
            lastC = c;
            areButtonsOpened = true;
            return Result.NOTHING;
        } else {
            var result = (grid[r][c] == grid[lastR][lastC]) ? Result.MATCH : Result.NO_MATCH;
            if (result == Result.MATCH) {
                left -= 2;
            }
            if (left == 0) {
                return Result.WIN;
            }
            areButtonsOpened = false;
            return result;
        }
    }


    private int[][] generateGrid(int n) {
        var grid = new int[n][n];
        var list = new ArrayList<Integer>();
        for (var i = 0; i < n * n; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        var val = 0;
        for (var i = 0; i < n * n; i++) {
            var r = list.get(i) / n;
            var c = list.get(i) % n;
            grid[r][c] = val;
            if (i % 2 == 1) {
                val++;
            }
        }
        return grid;
    }

}
