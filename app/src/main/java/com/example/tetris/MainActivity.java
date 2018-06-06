package com.example.tetris;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.tetris.control.GameControl;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //地图控件
    View view;
    //游戏控制器
    GameControl gameControl;
    //
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String type = (String) msg.obj;
            if (type == null) {
                return;
            }
            switch (type) {
                case "invalidate":
                    //刷新重绘
                    view.invalidate();
                    break;
                case "pause":
                    ((Button) findViewById(R.id.btnPause)).setText("暂停");
                    break;
                case "continue":
                    ((Button) findViewById(R.id.btnPause)).setText("继续");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //实例化游戏控制器
        gameControl = new GameControl(handler,this);
        initView();
        initListener();
    }

    /**
     * 初始化视图
     */
    public void initView() {
        //1.得到父容器
        FrameLayout layoutGame = (FrameLayout) findViewById(R.id.layoutGame);
        //2.实例化游戏区域
        view = new View(this) {
            //重写游戏区域绘制
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                gameControl.draw(canvas);
            }
        };
        //3.设置游戏区域大小
        view.setLayoutParams(new ViewGroup.LayoutParams(Config.MAPWHITH, Config.MAPHEIGHT));
        //4.设置背景颜色
        view.setBackgroundColor(0x10000000);
        //5.添加进父容器
        layoutGame.addView(view);
    }

    /**
     * 初始化监听
     */
    public void initListener() {
        findViewById(R.id.btnUp).setOnClickListener(this);
        findViewById(R.id.btnDown).setOnClickListener(this);
        findViewById(R.id.btnLeft).setOnClickListener(this);
        findViewById(R.id.btnRight).setOnClickListener(this);
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnPause).setOnClickListener(this);
    }

    /**
     * 捕捉点击事件
     */
    @Override
    public void onClick(View v) {
        gameControl.onClick(v.getId());
        //重绘
        view.invalidate();
    }
}
