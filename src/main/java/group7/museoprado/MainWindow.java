package group7.museoprado;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainWindow {

    private LeapListener lp;
    private Thread lpthr;
    private Controller lpcont;
    private JPanel panel;
    private JTextArea text;
    private JTextArea text2;
    private JTextArea textArea1;
    private DBHandler db;
    long timegesture;

    public MainWindow() throws InterruptedException, ExecutionException, IOException {
        lp= new LeapListener();
        lpthr=new Thread(lp);
        lpthr.start();
        lpcont= new Controller();
        lpcont.addListener(lp);
        db = new DBHandler();
        timegesture= System.currentTimeMillis();
        panel.setLayout(null);
        panel.add(text);
        Dimension size = text.getPreferredSize();
        text.setBounds(200,100,size.width,size.height);
        text.setLocation(panel.getWidth()-100,panel.getHeight()-100);
    }

    public void start(){
        Frame fm = lp.getUltimoFrame();

        //lpcont.enableGesture(Gesture.Type.TYPE_CIRCLE);
        //lpcont.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        lpcont.enableGesture(Gesture.Type.TYPE_SWIPE);

        if(fm.hands().count()==2){
            Hand left,right;
            if(fm.hands().get(0).isLeft()){
                left = fm.hands().get(0);
                right = fm.hands().get(1);
            }
            else{
                left = fm.hands().get(1);
                right = fm.hands().get(0);
            }


            /*
            Mano cerrada = 40
             */

            Gesture gesture= lp.getUltimoGesto();

            if(gesture!=null && (System.currentTimeMillis()-timegesture)>1000 ){
                System.out.println(System.currentTimeMillis()-timegesture);
                Hand hdgesture = gesture.hands().get(0);
                if (hdgesture.isLeft())
                    System.out.println("Gesto con la izquierda");
                else
                    System.out.println("Gesto con la derecha");

                switch (gesture.type()) {
                    case TYPE_SWIPE:
                        timegesture=System.currentTimeMillis();
                        if (hdgesture.palmVelocity().getX() > 0)
                            System.out.println("Gesto swipe hacia derecha");
                        else
                            System.out.println("Gesto swipe hacia izquierda");
                        break;
                }

            }

            text.setText("Normal: "+left.palmNormal().getX()+ "  "+ left.palmNormal().getY()+ " " + left.palmNormal().getZ());

        }
        else{
            text.setText("");
            text2.setText("");
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {

        MainWindow frame= new MainWindow();

        JFrame main = new JFrame("Museo Del Prado");
        main.setContentPane(frame.panel);
        main.pack();
        main.setVisible(true);

        while(frame.lp.isConnected()){
            frame.start();
        }

    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
