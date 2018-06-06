package com.example.tetris.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class BoxesModel {
    //方块
    public Point[] boxes = new Point[]{};
    //方块的类型
    private int boxType;
    //方块大小
    private int boxSize;
    //方块画笔
    private Paint boxPaint;

    public BoxesModel(int boxSize) {
        this.boxSize = boxSize;
        //初始化方块画笔
        boxPaint = new Paint();
        boxPaint.setColor(0xff000000);
        boxPaint.setAntiAlias(true);//抗锯齿
    }

    /**
     * 新的方块
     */
    public void newBoxs() {
        //随机生成一个方块
        Random random = new Random();
        boxType = random.nextInt(7);
        switch (boxType) {
            //田
            case 0:
                boxes = new Point[]{new Point(4, 1), new Point(4, 0), new Point(5, 1), new Point(5, 0)};
                break;
            //L
            case 1:
                boxes = new Point[]{new Point(4, 1), new Point(5, 0), new Point(3, 1), new Point(5, 1)};
                break;
            //反L
            case 2:
                boxes = new Point[]{new Point(4, 1), new Point(3, 0), new Point(3, 1), new Point(5, 1)};
                break;
            //
            case 3:
                boxes = new Point[]{new Point(4, 1), new Point(4, 0), new Point(3, 1), new Point(5, 1)};
                break;
            //一
            case 4:
                boxes = new Point[]{new Point(4, 0), new Point(2, 0), new Point(3, 0), new Point(5, 0)};
                break;
            //
            case 5:
                boxes = new Point[]{new Point(4, 1), new Point(4, 0), new Point(5, 1), new Point(5, 2)};
                break;
            //
            case 6:
                boxes = new Point[]{new Point(4, 1), new Point(4, 2), new Point(5, 1), new Point(5, 0)};
                break;
        }
    }

    /**
     * 方块绘制
     */
    public void drawBoxes(Canvas canvas) {
        for (Point boxe : boxes) {
            canvas.drawRect(boxe.x * boxSize, boxe.y * boxSize, boxe.x * boxSize + boxSize, boxe.y * boxSize + boxSize, boxPaint);
        }
    }

    /**
     * 移动
     */
    public boolean move(int x, int y, MapsModel mapsModel) {
        //遍历方块数组 每一个都加上偏移量
        for (Point boxe : boxes) {
            //方块预移动后的点 传入边界判定
            if (checkBoundary(boxe.x + x, boxe.y + y, mapsModel)) {
                return false;
            }
        }
        //遍历方块数组 每一个都加上偏移量
        for (Point boxe : boxes) {
            boxe.x += x;
            boxe.y += y;
        }
        return true;
    }

    /**
     * 旋转
     */
    public boolean rotate(MapsModel mapsModel) {
        if (boxType == 0) {
            return false;
        }
        //遍历方块数组 每一个都绕中心点顺时针旋转90°
        for (Point boxe1 : boxes) {
            //旋转算法 （笛卡尔公式）顺时针旋转90°
            int checkX = -boxe1.y + boxes[0].y + boxes[0].x;
            int checkY = boxe1.x - boxes[0].x + boxes[0].y;
            //将预旋转后的点传入边界判定
            if (checkBoundary(checkX, checkY, mapsModel)) {
                return false;
            }
        }
        //遍历方块数组 每一个都绕中心点顺时针旋转90°
        for (Point boxe : boxes) {
            //旋转算法 （笛卡尔公式）顺时针旋转90°
            int checkX = -boxe.y + boxes[0].y + boxes[0].x;
            int checkY = boxe.x - boxes[0].x + boxes[0].y;
            boxe.x = checkX;
            boxe.y = checkY;
        }
        return true;
    }

    /**
     * 边界判断
     * 传入x y 判断是否出界
     *
     * @param x 方块x坐标
     * @param y 方块y坐标
     * @return true 出界 false 未出界
     */
    private boolean checkBoundary(int x, int y, MapsModel mapsModel) {
        return (x < 0 || y < 0 || x >= mapsModel.maps.length || y >= mapsModel.maps[0].length || mapsModel.maps[x][y]);
    }
}
