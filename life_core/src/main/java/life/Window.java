package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.OptionalInt;



public class Window extends JFrame {

    Life life;

    JPanel jPanel;

    Timer timer;


    public Window(Life life) {


        this.life = life;

        jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
//                if (life.cellArrayCopy != null) {
                for (int x = 0; x < Config.X_S; x++)
                    for (int y = 0; y < Config.Y_S; y++) {
                        g.setColor(life.cellArray[x][y].cellStatus.getColor());
//                        g.setColor(getColor(life.cellArray[x][y]));
                        g.fillRect(x * Config.SIZE, y * Config.SIZE, Config.SIZE, Config.SIZE);
                    }
                g.setColor(Color.DARK_GRAY);
                for (int x = 0; x <= Config.X_S; x++) {
                    g.drawLine(x * Config.SIZE, 0, x * Config.SIZE, Config.Y_S * Config.SIZE);
                }
                for (int y = 0; y <= Config.Y_S; y++) {
                    g.drawLine(0, y * Config.SIZE, Config.X_S * Config.SIZE, y * Config.SIZE);
                }

            }
//            }

        };

        timer = new Timer(Config.DELAY, new ActionListener() {
            boolean b = true;

            @Override
            public void actionPerformed(ActionEvent e) {
//                   if(b){
//                       life.step_1();
//                       b=false;
//                   }
//                   else {
//                       life.step_2();
//                       b=true;
//                   }
                life.lifeCycle();
                setInfo();
                repaint();
                if(Life.Info.isRepeated.isPresent()){
                    timer.stop();

                    System.out.println("Possible repeated cycle " + Life.Info.life_step + " Match: " + Life.Info.isRepeated.getAsInt() + " Restart: " + Life.Info.restart_count);
                    Life.Info.isRepeated= OptionalInt.empty();
//                    life.restart();
//                    timer.start();
                }
            }
        });

        jPanel.setBackground(Color.BLACK);
        jPanel.setPreferredSize(new Dimension(Config.X_S * Config.SIZE, Config.Y_S * Config.SIZE));
        add(jPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();

        setLocationRelativeTo(null);
//        setResizable(false);
        setTitle("Life");
        setVisible(true);
//
        addMouseListener(new MouseAdapter() {
            boolean isRunning=false;
            @Override
            public void mousePressed(MouseEvent e) {

                if(MouseEvent.BUTTON3==e.getButton() && !timer.isRunning()){
                    life.lifeCycle();
                    setInfo();
                    repaint();
                    return;
                }

//                super.mousePressed(e);
//                System.out.println("Mouse pressed");
                if(isRunning) {
                    timer.stop();
                    isRunning = false;
                }
                else {
                    timer.start();
                    isRunning=true;
                }
            }
        });

        setInfo();
//        timer.start();
    }

     void setInfo(){
        String formatString=String.format("Step:%d Cells:%d Max.gen:%d Max.gen.count:%d Delta:%d",
                Life.Info.life_step,
                Life.Info.live_cells_count,
                Life.Info.max_generation,
                Life.Info.count_of_max_generations,
                Life.Info.life_step - Life.Info.max_generation
        );
        setTitle(formatString);
    }

    public Color getColor(Cell cell){

//        if(cell.statistics.generationCount==Life.Info.max_generation)
//            return new Color(0xFFFFFFFF, true);
//        if(cell.statistics.generationCount==(Life.Info.max_generation-1))
//            return Color.YELLOW;
//
//        if(cell.cellStatus==CellStatus.LIVE){
//            int max_color=0x00_FF__FF_FF;
//            Color c =new Color(255, 255, 255, 255);
//            float argument=cell.statistics.generationCount*1.f/ Life.Info.max_generation;
////            int color=(int)(argument*max_color);
//            Color color1= new Color(argument, argument, argument);
//            Color color2=color1.darker();
//            return  color1;
//        }

//        if(cell.cellStatus==CellStatus.LIVE){
//            double coefficeent=cell.statistics.generationCount*1.0/ Life.Info.max_generation;
//            if(coefficeent<0.5)
//                return Color.GRAY;
//            return Color.WHITE;
//        }


        return switch (cell.cellStatus){
            case NONE -> Color.BLACK;
            case LIVE -> Color.WHITE;
            case BORN -> Color.GREEN;
            case DEAD -> Color.GRAY;
        };
    }

}
