package com.lanxinbase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class JavaUI {
    private static Timer timer = null;
    private static int count = 0;

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("SocketIO TEST");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        jFrame.add(panel);

        JLabel label = new JLabel("Thread Num:");
        label.setBounds(10, 20, 80, 32);

        JLabel label2 = new JLabel("Other:");
        label2.setBounds(10, 72, 80, 32);

        JTextField username = new JTextField(20);
        username.setSize(380, 132);
        username.setBounds(90, 20, 150, 32);
        username.setColumns(10);
        username.setText("1");


        JTextField password = new JTextField(20);
        password.setBounds(90, 72, 150, 32);

        JButton button = new JButton("start");
        JButton button1 = new JButton("get Thread");
        button.setBounds(90, 120, 80, 32);
        button1.setBounds(90, 170, 120, 32);

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10000,30000,60, TimeUnit.SECONDS,new LinkedBlockingQueue<>(100));
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setEnabled(false);
                username.setEnabled(false);

                int i = 0;
                for (;;){
                    i++;
//                    poolExecutor.execute(doThread(i));
                    poolExecutor.execute(new WorkRun("1000"+i,2000L));
                    if(i == Integer.parseInt(username.getText())){
                        break;
                    }
                }
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuffer sb = new StringBuffer();
                sb.append("corePoolSize:"+poolExecutor.getCorePoolSize()+"\t")
                        .append("activeCount:"+poolExecutor.getActiveCount()+"\t")
                        .append("taskCount:"+poolExecutor.getTaskCount()+"\t");

                System.out.println(sb);
            }
        });




        panel.add(label);
        panel.add(label2);
        panel.add(username);
        panel.add(password);
        panel.add(button);
        panel.add(button1);

        // 显示窗口
        jFrame.setSize(600, 480);
        jFrame.setBounds(100, 100, 600, 480);

        jFrame.setVisible(true);


    }

    public static Thread doThread(int i){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                doCount();
                System.out.println(count+"->线程名称："+Thread.currentThread().getName()+" doTime:"+System.currentTimeMillis());
            }
        });
        //thread.setName("thread-name-"+i);
//        if (!isSync){
//            thread.start();
//        }else {
//            thread.run();
//        }
        return thread;
    }

    public static void doCount(){
        count++;

    }

    private static String formatDateTime(int e){
        String _str = "00:00:00";
        int _h1,_m1,_s1;
        float _has1;
        String _h = null,_m = null,_s = null;

        _h1 = (e / 3600);
        _has1 = e % 3600;
        _h = _h1 < 10 ? "0" + _h1 : String.valueOf(_h1);

        _m1 = (int)(_has1 / 60);
        _has1 = _has1 % 60;
        _m = _m1 < 10 ? "0" + _m1 : String.valueOf(_m1);

        _s1 = (int) _has1;
        _s = _s1 < 10 ? "0" + _s1 : String.valueOf(_s1);

        return _h + ":" + _m + ":" + _s;
    }
}

