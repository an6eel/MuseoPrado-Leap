package group7.museoprado;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main extends JFrame {

    private javax.swing.JLabel image;
    private javax.swing.JTextField state;
    private javax.swing.JTextField text;

    private LeapListener lp;
    private Thread lpthr;
    private Controller lpcont;
    private DBHandler db;
    long timegesture;
    long timewoframe=0;
    boolean hidden;

    public Main() throws InterruptedException, ExecutionException, IOException {

        initComponents();
        getContentPane().setBackground(new java.awt.Color(189, 125, 40));
        state.setBackground(new java.awt.Color(189, 125, 40));
        state.setForeground(Color.red);
        state.setText("Leap Motion OFF");
        //image.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("")).getImage().getScaledInstance(1000,700,Image.SCALE_DEFAULT)));
        image.setBounds(WIDTH*2, WIDTH*2, WIDTH, HEIGHT);
        image.setText("Hello");
        pack();

        lp= new LeapListener();
        lpthr=new Thread(lp);
        lpthr.start();
        lpcont= new Controller();
        lpcont.addListener(lp);
        db = new DBHandler();
        timegesture= System.currentTimeMillis();
        hidden = true;
    }

    void initComponents(){
        state = new javax.swing.JTextField();
        image = new javax.swing.JLabel();
        text = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Museo del prado");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(189, 125, 40));
        setLocation(new java.awt.Point(150, 50));
        setMinimumSize(new java.awt.Dimension(1500, 1000));
        setPreferredSize(new java.awt.Dimension(1500, 1000));

        state.setEditable(false);
        state.setText("Leap Motion OFF");
        state.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        state.setOpaque(false);


        //image.setIcon(new javax.swing.ImageIcon(getClass().getResource("group7/museoprado/prado.jpg"))); // NOI18N
        //image.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new javax.swing.ImageIcon(getClass().getResource("group7/museoprado/prado.jpg")))); // NOI18N
        //image.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        text.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(state, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(145, 145, 145)
                                                .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(352, 352, 352)
                                                .addComponent(text, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(355, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(146, Short.MAX_VALUE)
                                .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addComponent(text, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(state, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    public void start(){
        Frame fm = lp.getUltimoFrame();

        lpcont.enableGesture(Gesture.Type.TYPE_CIRCLE);
        lpcont.enableGesture(Gesture.Type.TYPE_KEY_TAP);
        lpcont.enableGesture(Gesture.Type.TYPE_SWIPE);

        if(fm.hands().count()==2){
            timewoframe=System.currentTimeMillis();
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
            if(left.sphereRadius()<40){
                //image.setText("Ya se muestran autores u obras");

                Gesture gesture= lp.getUltimoGesto();

                if(gesture!=null && (System.currentTimeMillis()-timegesture)>1000 ){
                    System.out.println(System.currentTimeMillis()-timegesture);
                    Hand hdgesture = gesture.hands().get(0);
                    if (hdgesture.isLeft())
                        System.out.println("Gesto con la izquierda");
                    else
                        System.out.println("Gesto con la derecha");
                    timegesture=System.currentTimeMillis();
                    switch (gesture.type()) {
                        case TYPE_SWIPE:
                            System.out.println("Swipe");

                            if (hdgesture.palmVelocity().getX() > 0)
                                image.setText("Siguiente Obra");
                            else
                                image.setText("Obra anterior");
                            break;
                        case TYPE_CIRCLE:
                            CircleGesture circle = new CircleGesture(gesture);

                            if(circle.pointable().direction().angleTo(circle.normal())<= Math.PI/4)
                                image.setText("Siguiente autor");
                            else
                                image.setText("Autor anterior");
                            break;
                        case TYPE_KEY_TAP:
                            KeyTapGesture tap = new KeyTapGesture(gesture);



                    }

                }


            }

            image.setText(Float.toString(right.direction().angleTo(right.palmNormal())));
            text.setText("Velocity: "+right.palmVelocity().getX()+ "  "+ right.palmVelocity().getY()+ " " + right.palmVelocity().getZ());
        }



        if( (System.currentTimeMillis()-timewoframe) > 3000 )
            hidden();
        else if(hidden)
            showText();


    }

    void hidden(){
        hidden = true;
        image.setText("Coloca las manos sobre el dispositivo");
        pack();
    }

    void showText(){
        image.setText("Las manos están sobre el dispositivo");
        hidden=false;
        pack();
    }

    public static void main(String args[]) throws InterruptedException, ExecutionException, IOException {

        Main main= new Main();
        main.setVisible(true);
        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    Main main= new Main();
                    main.setVisible(true);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/

        while(main.lp.isConnected()){
            main.state.setText("Leap Motion ON");
            main.pack();
            main.start();
        }



    }
}
