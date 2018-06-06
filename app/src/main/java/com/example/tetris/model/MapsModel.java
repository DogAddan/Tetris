package com.example.tetris.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.tetris.Config;

public class MapsModel {
    //地图
    public boolean[][] maps;
    //地图宽高
    private int mapWidth, mapHeight;
    //方块大小
    private int boxSize;

    //地图画笔
    private Paint mapPaint;
    //辅助线画笔
    private Paint linePaint;
    //状态画笔
    private Paint statePaint;

    public MapsModel(int mapWidth, int mapHeight, int boxSize) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.boxSize = boxSize;
        //初始化地图
        maps = new boolean[Config.MAPX][Config.MAPY];
        //初始化地图画笔
        mapPaint = new Paint();
        mapPaint.setColor(0x50000000);
        mapPaint.setAntiAlias(true);//抗锯齿
        //初始化辅助线画笔
        linePaint = new Paint();
        linePaint.setColor(0xff666666);
        linePaint.setAntiAlias(true);//抗锯齿
        //初始化状态画笔
        statePaint = new Paint();
        statePaint.setColor(0xffff0000);
        statePaint.setAntiAlias(true);//抗锯齿
        statePaint.setTextSize(100);
    }

    /**
     * 绘制地图
     *
     * @param canvas
     */
    public void drawMaps(Canvas canvas) {
        for (int x = 0; x < maps.length; x++) {
            for (int y = 0; y < maps[x].length; y++) {
                if (maps[x][y]) {
                    canvas.drawRect(x * boxSize, y * boxSize, x * boxSize + boxSize, y * boxSize + boxSize, mapPaint);
                }
            }
        }
    }

    /**
     * 绘制辅助线
     *
     * @param canvas
     */
    public void drawLine(Canvas canvas) {
        //绘制竖线
        for (int x = 0; x < maps[0].length; x++) {
            canvas.drawLine(x * boxSize, 0, x * boxSize, mapHeight, linePaint);
        }
        //绘制横线
        for (int y = 0; y < maps[0].length; y++) {
            canvas.drawLine(0, y * boxSize, mapWidth, y * boxSize, linePaint);
        }
    }

    /**
     * 绘制状态
     *
     * @param canvas
     */
    public void drawState(Canvas canvas, boolean isPause, boolean isOver) {
        //绘制暂停状态
        if (isPause && !isOver) {
            canvas.drawText("暂停", mapWidth / 2 - statePaint.measureText("暂停") / 2, mapHeight / 2, statePaint);
        }
        //绘制结束状态
        if (isOver) {
            canvas.drawText("游戏结束", mapWidth / 2 - statePaint.measureText("游戏结束") / 2, mapHeight / 2, statePaint);
        }
    }

    /**
     * 清空地图
     */
    public void cleanMaps() {
        for (int x = 0; x < maps.length; x++) {
            for (int y = 0; y < maps[x].length; y++) {
                maps[x][y] = false;
            }
        }
    }

    /**
     * 消行处理
     */
    public void cleanLine() {
        for (int y = maps[0].length - 1; y > 0; y--) {
            //消行判断
            if (checkLine(y)) {
                //执行消行
                deleteLine(y);
                y++;
            }
        }
    }

    /**
     * 消行判断
     */
    private boolean checkLine(int y) {
        for (boolean[] map : maps) {
            //如果有一个不为true 这行不能消除
            if (!map[y]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 执行消行
     */
    private void deleteLine(int dy) {
        for (int y = dy; y > 0; y--) {
            for (int x = 0; x < maps.length; x++) {
                maps[x][y] = maps[x][y - 1];
            }
        }
    }
}
