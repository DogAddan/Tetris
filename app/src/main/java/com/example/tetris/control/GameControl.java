package com.example.tetris.control;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.tetris.Config;
import com.example.tetris.R;
import com.example.tetris.model.BoxesModel;
import com.example.tetris.model.MapsModel;

public class GameControl {
    //方块模型
    private BoxesModel boxesModel;
    //地图模型
    private MapsModel mapsModel;
    //自动下落线程
    private Thread downThread;
    //
    private Handler handler;
    //暂停状态
    private boolean isPause;
    //结束状态
    private boolean isOver;

    public GameControl(Handler handler, Context context) {
        this.handler = handler;
        initData(context);
    }

    /**
     * 初始化数据
     */
    private void initData(Context context) {
        //获得屏幕宽度
        int width = getScreenWidth(context);
        //设置地图宽度 = 屏幕宽度 * 2/3
        Config.MAPWHITH = width * 2 / 3;
        //设置地图高度 = 宽度 * 2
        Config.MAPHEIGHT = Config.MAPWHITH * 2;
        //初始化方块大小 = 地图宽度/10
        int boxSize = Config.MAPWHITH / Config.MAPX;
        //实例化地图模型
        mapsModel = new MapsModel(Config.MAPWHITH, Config.MAPHEIGHT, boxSize);
        //实例化方块模型
        boxesModel = new BoxesModel(boxSize);
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {
        //绘制地图
        mapsModel.drawMaps(canvas);
        //方块绘制
        boxesModel.drawBoxes(canvas);
        //绘制辅助线
        mapsModel.drawLine(canvas);
        //绘制状态
        mapsModel.drawState(canvas, isPause, isOver);
    }

    /**
     * 开始游戏
     */
    private void startGame() {
        //结束状态设置为false
        isOver = false;
        //暂停状态设置为false
        isPause = false;
        //清空地图
        mapsModel.cleanMaps();
        //生成新的方块
        boxesModel.newBoxs();
        //检测线程是否启动
        if (downThread == null) {
            downThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            sleep(500);//休眠500ms
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //判断是否处于结束状态
                        //判断是否处于暂停状态
                        if (isPause || isOver) {
                            continue;
                        }
                        //执行一次下落
                        moveDown();
                        //通知主线程刷新view
                        Message msg = new Message();
                        msg.obj = "invalidate";
                        handler.sendMessage(msg);
                    }
                }
            };
            downThread.start();//启动线程
        }

    }

    /**
     * 暂停游戏
     */
    private void pauseGame() {
        if (isPause) {
            isPause = false;
            Message msg = new Message();
            msg.obj = "pause";
            handler.sendMessage(msg);
        } else {
            isPause = true;
            Message msg = new Message();
            msg.obj = "continue";
            handler.sendMessage(msg);
        }
    }

    /**
     * 游戏结束
     */
    private boolean checkOver() {
        for (int i = 0; i < boxesModel.boxes.length; i++) {
            if (mapsModel.maps[boxesModel.boxes[i].x][boxesModel.boxes[i].y]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 下落
     */
    private boolean moveDown() {
        //1.移动成功 不作处理
        if (boxesModel.move(0, 1, mapsModel)) {
            return true;
        }
        //2.移动失败 堆积处理
        for (int i = 0; i < boxesModel.boxes.length; i++) {
            mapsModel.maps[boxesModel.boxes[i].x][boxesModel.boxes[i].y] = true;
        }
        //3.消行处理
        mapsModel.cleanLine();
        //4.生成新方块
        boxesModel.newBoxs();
        //5.游戏结束判断
        isOver = checkOver();

        return false;
    }

    /**
     * 点击事件
     *
     * @param id
     */
    public void onClick(int id) {
        switch (id) {
            //上
            case R.id.btnUp:
                if (isPause) {
                    return;
                }
                boxesModel.rotate(mapsModel);
                break;
            //下
            case R.id.btnDown:
                if (isPause || isOver) {
                    return;
                }
                //快速下落
                while (true) {
                    //如果下落失败 结束循环
                    if (!moveDown()) {
                        break;
                    }
                }
                break;
            //右
            case R.id.btnLeft:
                if (isPause) {
                    return;
                }
                boxesModel.move(-1, 0, mapsModel);
                break;
            //左
            case R.id.btnRight:
                if (isPause) {
                    return;
                }
                boxesModel.move(1, 0, mapsModel);
                break;
            //开始
            case R.id.btnStart:
                startGame();
                break;
            //暂停
            case R.id.btnPause:
                pauseGame();
                break;
        }
    }

    /**
     * 获得屏幕宽度
     */
    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.widthPixels;
    }
}
