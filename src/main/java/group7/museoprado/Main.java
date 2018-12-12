package group7.museoprado;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class Main extends JFrame implements Runnable{

    //Frame Main

    private JLabel image;
    private JTextField rcg;
    private JTextField lpstate;
    private JButton salir;
    private JButton Obras;
    private JButton Artistas;
    private JTextField welcome;
    private JLabel bg;

    // Frame Gallery Obras
    private JFrame gallery1;
    private JLabel bggallery;
    private JLabel photo;
    private JButton previous;
    private JButton next;
    private JButton list;
    private JButton info;
    private JButton out;
    private JTextField name;

    // Frame Gallery Autores

    private JFrame gallery2;
    private JLabel bggallery2;
    private JLabel photo2;
    private JButton previous2;
    private JButton next2;
    private JButton list2;
    private JButton info2;
    private JButton out2;
    private JTextField name2;


    // Frame Autor
    private JFrame screen1;
    private JLabel photo3;
    private JButton list3;
    private JButton info3;
    private JButton out3;
    private JTextField name3;

    // Frame Obras Autor
    private JFrame screen2;
    private JLabel photo4;
    private JButton info4;
    private JButton out4;
    private JTextField name4;
    private JButton previous3;
    private JButton next3;

    // TODO Frame Photo
    private JFrame screen3;
    private JLabel photo5;

    // TODO Frame Info
    private JFrame screen4;
    private JTextArea inf;
    private JScrollPane scroll;


    private LeapListener lp;
    private Thread lpthr;
    private Controller lpcont;
    private DBHandler db;
    long timegesture;
    long timewoframe=0;
    boolean hidden;
    String PATH;

    public enum nscreen{
        ON_MUSEUM,
        ON_MAIN,
        ON_GALLERY1,
        ON_GALLERY2,
        ON_AUTHOR,
        ON_PAINTINGS_AUTHOR,
        ON_SHOW,
        ON_INFO
    }

    private nscreen state;
    private Robot robot;

    public Main() throws InterruptedException, ExecutionException, IOException, AWTException {


        lp= new LeapListener();
        lpthr=new Thread(lp);
        lpthr.start();
        lpcont= new Controller();
        lpcont.addListener(lp);
        db = new DBHandler();
        timegesture= System.currentTimeMillis();
        hidden = true;
        robot = new Robot();
        timegesture=0;

        Path currentRelativePath = Paths.get("");
        PATH = currentRelativePath.toAbsolutePath().toString();


        initComponents();
        state=nscreen.ON_MUSEUM;


    }

    private void configure(int screen){

        switch (screen){
            case 0: {
                image = new JLabel();
                rcg = new JTextField();
                lpstate = new JTextField();
                salir = new JButton();
                Obras = new JButton();
                Artistas = new JButton();
                welcome = new JTextField();
                bg = new JLabel();

                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                setMinimumSize(new Dimension(1500, 1000));
                setTitle("Museo del Prado");
                Container contentPane = getContentPane();
                contentPane.setLayout(null);

                image.setPreferredSize(new Dimension(1000, 700));
                image.setOpaque(true);
                image.setBorder(new LineBorder(new Color(91, 138, 227), 5, true));
                image.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        state = nscreen.ON_MAIN;
                        imageMouseClicked(e);
                    }
                });
                contentPane.add(image);
                image.setIcon(new ImageIcon(new javax.swing.ImageIcon(PATH+"/src/main/assets/prado.jpg").getImage().getScaledInstance(1000, 700, Image.SCALE_DEFAULT)));
                image.setBounds((this.getWidth() / 2 - image.getPreferredSize().width / 2), (this.getHeight() / 2 - image.getPreferredSize().height / 2), 1000, 700);

                rcg.setText("Not recognizing..");
                rcg.setForeground(Color.red);
                contentPane.add(rcg);
                rcg.setBounds(new Rectangle(new Point(1375, 950), rcg.getPreferredSize()));

                lpstate.setForeground(Color.red);
                lpstate.setText("Leap Motion OFF");
                contentPane.add(lpstate);
                lpstate.setBounds(new Rectangle(new Point(25, 950), lpstate.getPreferredSize()));

                //---- salir ----
                salir.setBackground(new Color(107, 50, 4));
                salir.setText("Salir");
                salir.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                salir.setHorizontalAlignment(SwingConstants.CENTER);
                salir.setForeground(Color.white);
                salir.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                salir.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ExitMain();
                    }
                });
                contentPane.add(salir);
                salir.setBounds(575, 640, 350, 140);

                Obras.setText("Ver Obras Destacadas");
                Obras.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                Obras.setHorizontalAlignment(SwingConstants.CENTER);
                Obras.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                Obras.setForeground(Color.white);
                Obras.setBackground(new Color(107, 50, 4));
                Obras.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        state=nscreen.ON_GALLERY2;
                        try {
                            openGalleryObras();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                contentPane.add(Obras);
                Obras.setBounds(575, 460, 350, 140);

                Artistas.setBackground(new Color(107, 50, 4));
                Artistas.setText("Ver Autores Destacados");
                Artistas.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                Artistas.setHorizontalAlignment(SwingConstants.CENTER);
                Artistas.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                Artistas.setForeground(Color.white);
                Artistas.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        state=nscreen.ON_GALLERY1;
                        try {
                            openGalleryAutores();
                        } catch (ExecutionException e1) {
                            e1.printStackTrace();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                contentPane.add(Artistas);
                Artistas.setBounds(575, 280, 350, 140);

                welcome.setEditable(false);
                welcome.setFont(new Font("Ubuntu", Font.PLAIN, 80));
                welcome.setHorizontalAlignment(SwingConstants.CENTER);
                welcome.setText("Bienvenidos al Museo!");
                welcome.setForeground(new Color(255, 204, 0));
                welcome.setBorder(new LineBorder(Color.black, 2, true));
                contentPane.add(welcome);
                welcome.setBounds((this.getMinimumSize().width/2-welcome.getPreferredSize().width/2),40,welcome.getPreferredSize().width,welcome.getPreferredSize().height);

                bg.setIcon(new ImageIcon(new javax.swing.ImageIcon(PATH+"/src/main/assets/bg.jpg").getImage().getScaledInstance(this.getWidth(),this.getHeight(), Image.SCALE_DEFAULT)));
                bg.setPreferredSize(new Dimension(1500, 1000));
                bg.setMinimumSize(new Dimension(1500, 1000));
                bg.setMaximumSize(new Dimension(6000, 44422));
                contentPane.add(bg);
                bg.setBounds(0, -30, 1500, 1000);

                contentPane.setPreferredSize(new Dimension(1030, 675));
                pack();
                setLocationRelativeTo(getOwner());
            } break;
            case 1: {
                bggallery = new JLabel();
                photo = new JLabel();
                out = new JButton();
                info = new JButton();
                list = new JButton();
                photo = new JLabel();
                previous = new JButton();
                next = new JButton();
                name = new JTextField();

                gallery1.setMinimumSize(new Dimension(1000, 800));
                gallery1.setResizable(false);
                gallery1.setTitle("Autores Destacados");
                Container frame1ContentPane = gallery1.getContentPane();
                frame1ContentPane.setLayout(null);
                gallery1.pack();
                gallery1.setLocationRelativeTo(gallery1.getOwner());
                gallery1.setVisible(true);
                gallery1.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        state=nscreen.ON_MAIN;
                    }
                });

                name.setEditable(false);
                name.setFont(new Font("Ubuntu", Font.PLAIN, 40));
                name.setHorizontalAlignment(SwingConstants.CENTER);
                name.setText("Bienvenidos al Museo!");
                name.setForeground(new Color(255, 204, 0));
                name.setBorder(new LineBorder(Color.black, 2, true));
                gallery1.getContentPane().add(name);
                name.setBounds((gallery1.getMinimumSize().width/2-name.getPreferredSize().width/2),40,name.getPreferredSize().width,name.getPreferredSize().height);


                out.setBackground(new Color(107, 50, 4));
                out.setText("Salir");
                out.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                out.setHorizontalAlignment(SwingConstants.CENTER);
                out.setForeground(Color.white);
                out.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                out.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        state=nscreen.ON_MAIN;
                        gallery1.dispose();
                    }
                });
                gallery1.getContentPane().add(out);
                out.setBounds(800, 700, 150, 50);
                out.setVisible(true);

                info.setBackground(new Color(107, 50, 4));
                info.setText("Información Autor");
                info.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                info.setHorizontalAlignment(SwingConstants.CENTER);
                info.setForeground(Color.white);
                info.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                info.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        openInfo(0,state);
                    }
                });
                gallery1.getContentPane().add(info);
                info.setBounds(400, 700, 250, 50);
                info.setVisible(true);

                list.setBackground(new Color(107, 50, 4));
                list.setText("Mostrar sus obras");
                list.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                list.setHorizontalAlignment(SwingConstants.CENTER);
                list.setForeground(Color.white);
                list.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                list.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            openScreen2(state);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                gallery1.getContentPane().add(list);
                list.setBounds(50, 700, 250, 50);
                list.setVisible(true);

                previous.setBackground(new Color(107, 50, 4));
                previous.setText("<<");
                previous.setFont(new Font("URW Gothic L", Font.BOLD, 30));
                previous.setHorizontalAlignment(SwingConstants.CENTER);
                previous.setForeground(Color.yellow);
                previous.setOpaque(false);
                previous.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            previousAuthor();
                        } catch (ExecutionException e1) {
                            e1.printStackTrace();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                gallery1.getContentPane().add(previous);
                previous.setBounds(25, 100, 100, 500);
                previous.setVisible(true);

                next.setBackground(new Color(107, 50, 4));
                next.setText(">>");
                next.setFont(new Font("URW Gothic L", Font.BOLD, 30));
                next.setHorizontalAlignment(SwingConstants.CENTER);
                next.setForeground(Color.yellow);
                next.setOpaque(false);
                next.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            nextAuthor();
                        } catch (ExecutionException e1) {
                            e1.printStackTrace();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                gallery1.getContentPane().add(next);
                next.setBounds(875, 100, 100, 500);
                next.setVisible(true);

                photo.setPreferredSize(new Dimension(700, 500));
                photo.setOpaque(true);
                photo.setBorder(new LineBorder(new Color(91, 138, 227), 5, true));
                gallery1.getContentPane().add(photo);
                photo.setBounds(150,100,700,500);
                photo.setVisible(true);

                bggallery.setIcon(new ImageIcon(new javax.swing.ImageIcon(PATH+"/src/main/assets/bg.jpg").getImage().getScaledInstance(gallery1.getWidth(),gallery1.getHeight(), Image.SCALE_DEFAULT)));
                bggallery.setPreferredSize(new Dimension(gallery1.getWidth(), gallery1.getHeight()));
                bggallery.setMinimumSize(new Dimension(gallery1.getWidth(), gallery1.getHeight()));
                bggallery.setMaximumSize(new Dimension(6000, 44422));
                gallery1.getContentPane().add(bggallery);
                bggallery.setBounds(0, 0, gallery1.getWidth(), gallery1.getHeight());
                bggallery.setVisible(true);
            } break;
            case 2: {
                bggallery = new JLabel();
                photo2 = new JLabel();
                out2 = new JButton();
                info2 = new JButton();
                list2 = new JButton();
                photo2 = new JLabel();
                previous2 = new JButton();
                next2 = new JButton();
                name2 = new JTextField();

                gallery2.setMinimumSize(new Dimension(1000, 800));
                gallery2.setResizable(false);
                gallery2.setTitle("Obras Destacadas");
                Container frame1ContentPane = gallery2.getContentPane();
                frame1ContentPane.setLayout(null);
                gallery2.pack();
                gallery2.setLocationRelativeTo(gallery2.getOwner());
                gallery2.setVisible(true);
                gallery2.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        state=nscreen.ON_MAIN;
                    }
                });

                name2.setEditable(false);
                name2.setFont(new Font("Ubuntu", Font.PLAIN, 40));
                name2.setHorizontalAlignment(SwingConstants.CENTER);
                name2.setForeground(new Color(255, 204, 0));
                name2.setText("Hello");
                //name2.setMaximumSize(new Dimension(500,40));
                name2.setBorder(new LineBorder(Color.black, 2, true));
                gallery2.getContentPane().add(name2);
                name2.setBounds((gallery2.getMinimumSize().width/2-500/2),40,500,50);

                out2.setBackground(new Color(107, 50, 4));
                out2.setText("Salir");
                out2.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                out2.setHorizontalAlignment(SwingConstants.CENTER);
                out2.setForeground(Color.white);
                out2.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                out2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        state=nscreen.ON_MAIN;
                        gallery2.dispose();
                    }
                });
                gallery2.getContentPane().add(out2);
                out2.setBounds(800, 700, 150, 50);
                out2.setVisible(true);

                info2.setBackground(new Color(107, 50, 4));
                info2.setText("Información Obra");
                info2.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                info2.setHorizontalAlignment(SwingConstants.CENTER);
                info2.setForeground(Color.white);
                info2.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                info2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        openInfo(1,state);
                    }
                });
                gallery2.getContentPane().add(info2);
                info2.setBounds(400, 700, 250, 50);
                info2.setVisible(true);

                list2.setBackground(new Color(107, 50, 4));
                list2.setText("Mostrar Autor");
                list2.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                list2.setHorizontalAlignment(SwingConstants.CENTER);
                list2.setForeground(Color.white);
                list2.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                list2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        state=nscreen.ON_AUTHOR;
                        try {
                            openScreen1();
                        } catch (ExecutionException e1) {
                            e1.printStackTrace();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                gallery2.getContentPane().add(list2);
                list2.setBounds(50, 700, 250, 50);
                list2.setVisible(true);

                previous2.setBackground(new Color(107, 50, 4));
                previous2.setText("<<");
                previous2.setFont(new Font("URW Gothic L", Font.BOLD, 30));
                previous2.setHorizontalAlignment(SwingConstants.CENTER);
                previous2.setForeground(Color.yellow);
                previous2.setOpaque(false);
                previous2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            previousPaint();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                gallery2.getContentPane().add(previous2);
                previous2.setBounds(25, 100, 100, 500);
                previous2.setVisible(true);

                next2.setBackground(new Color(107, 50, 4));
                next2.setText(">>");
                next2.setFont(new Font("URW Gothic L", Font.BOLD, 30));
                next2.setHorizontalAlignment(SwingConstants.CENTER);
                next2.setForeground(Color.yellow);
                next2.setOpaque(false);
                next2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            nextPaint();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                gallery2.getContentPane().add(next2);
                next2.setBounds(875, 100, 100, 500);
                next2.setVisible(true);

                photo2.setPreferredSize(new Dimension(700, 500));
                photo2.setOpaque(true);
                photo2.setBorder(new LineBorder(new Color(91, 138, 227), 5, true));
                gallery2.getContentPane().add(photo2);
                photo2.setBounds(150,100,700,500);
                photo2.setVisible(true);

                bggallery.setIcon(new ImageIcon(new javax.swing.ImageIcon(PATH+"/src/main/assets/bg.jpg").getImage().getScaledInstance(gallery2.getWidth(),gallery2.getHeight(), Image.SCALE_DEFAULT)));
                bggallery.setPreferredSize(new Dimension(gallery2.getWidth(), gallery2.getHeight()));
                bggallery.setMinimumSize(new Dimension(gallery2.getWidth(), gallery2.getHeight()));
                bggallery.setMaximumSize(new Dimension(6000, 44422));
                gallery2.getContentPane().add(bggallery);
                bggallery.setBounds(0, 0, gallery2.getWidth(), gallery2.getHeight());
                bggallery.setVisible(true);
            } break;
            case 3: {
                photo3 = new JLabel();
                list3 = new JButton();
                info3 = new JButton();
                out3 = new JButton();
                name3 = new JTextField();

                screen1.setMinimumSize(new Dimension(1000, 800));
                screen1.setResizable(false);
                screen1.setTitle("Autor");
                Container frame1ContentPane = screen1.getContentPane();
                frame1ContentPane.setLayout(null);
                screen1.pack();
                screen1.setLocationRelativeTo(screen1.getOwner());
                screen1.setVisible(true);
                screen1.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        state=nscreen.ON_GALLERY2;
                    }
                });

                name3.setEditable(false);
                name3.setFont(new Font("Ubuntu", Font.PLAIN, 40));
                name3.setHorizontalAlignment(SwingConstants.CENTER);
                name3.setText("Bienvenidos al Museo!");
                name3.setForeground(new Color(255, 204, 0));
                name3.setBorder(new LineBorder(Color.black, 2, true));
                screen1.getContentPane().add(name3);
                name3.setBounds((screen1.getMinimumSize().width/2-name3.getPreferredSize().width/2),40,name3.getPreferredSize().width,name3.getPreferredSize().height);

                out3.setBackground(new Color(107, 50, 4));
                out3.setText("Salir");
                out3.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                out3.setHorizontalAlignment(SwingConstants.CENTER);
                out3.setForeground(Color.white);
                out3.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                out3.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        state=nscreen.ON_GALLERY2;
                        screen1.dispose();
                    }
                });
                screen1.getContentPane().add(out3);
                out3.setBounds(800, 700, 150, 50);
                out3.setVisible(true);

                info3.setBackground(new Color(107, 50, 4));
                info3.setText("Información Autor");
                info3.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                info3.setHorizontalAlignment(SwingConstants.CENTER);
                info3.setForeground(Color.white);
                info3.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                info3.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        openInfo(1,state);
                    }
                });
                screen1.getContentPane().add(info3);
                info3.setBounds(400, 700, 250, 50);
                info3.setVisible(true);

                list3.setBackground(new Color(107, 50, 4));
                list3.setText("Mostrar sus obras");
                list3.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                list3.setHorizontalAlignment(SwingConstants.CENTER);
                list3.setForeground(Color.white);
                list3.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                list3.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            openScreen2(state);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                screen1.getContentPane().add(list3);
                list3.setBounds(50, 700, 250, 50);
                list3.setVisible(true);

                photo3.setPreferredSize(new Dimension(700, 500));
                photo3.setOpaque(true);
                photo3.setBorder(new LineBorder(new Color(91, 138, 227), 5, true));
                screen1.getContentPane().add(photo3);
                photo3.setBounds(150,100,700,500);
                photo3.setVisible(true);

                screen1.getContentPane().add(bggallery);
            } break;
            case 4: {
                photo4 = new JLabel();
                info4 = new JButton();
                out4 = new JButton();
                name4 = new JTextField();
                previous3 = new JButton();
                next3 = new JButton();

                screen2.setMinimumSize(new Dimension(1000, 800));
                screen2.setResizable(false);
                screen2.setTitle("Autor");
                Container frame1ContentPane = screen2.getContentPane();
                frame1ContentPane.setLayout(null);
                screen2.pack();
                screen2.setLocationRelativeTo(screen2.getOwner());
                screen2.setVisible(true);

                name4.setEditable(false);
                name4.setFont(new Font("Ubuntu", Font.PLAIN, 40));
                name4.setHorizontalAlignment(SwingConstants.CENTER);
                name4.setForeground(new Color(255, 204, 0));
                name4.setText("Hello");
                //name2.setMaximumSize(new Dimension(500,40));
                name4.setBorder(new LineBorder(Color.black, 2, true));
                screen2.getContentPane().add(name4);
                name4.setBounds((screen2.getMinimumSize().width/2-500/2),40,500,50);

                photo4.setPreferredSize(new Dimension(700, 500));
                photo4.setOpaque(true);
                photo4.setBorder(new LineBorder(new Color(91, 138, 227), 5, true));
                screen2.getContentPane().add(photo4);
                photo4.setBounds(150,100,700,500);
                photo4.setVisible(true);

                info4.setBackground(new Color(107, 50, 4));
                info4.setText("Información Obra");
                info4.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                info4.setHorizontalAlignment(SwingConstants.CENTER);
                info4.setForeground(Color.white);
                info4.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                info4.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        openInfo(1,state);
                    }
                });
                screen2.getContentPane().add(info4);
                info4.setBounds(200, 700, 250, 50);
                info4.setVisible(true);

                out4.setBackground(new Color(107, 50, 4));
                out4.setText("Salir");
                out4.setFont(new Font("URW Gothic L", Font.BOLD, 24));
                out4.setHorizontalAlignment(SwingConstants.CENTER);
                out4.setForeground(Color.white);
                out4.setBorder(UIManager.getBorder("RootPane.frameBorder"));

                screen2.getContentPane().add(out4);
                out4.setBounds(600, 700, 150, 50);
                out4.setVisible(true);

                previous3.setBackground(new Color(107, 50, 4));
                previous3.setText("<<");
                previous3.setFont(new Font("URW Gothic L", Font.BOLD, 30));
                previous3.setHorizontalAlignment(SwingConstants.CENTER);
                previous3.setForeground(Color.yellow);
                previous3.setOpaque(false);
                previous3.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            previousPaint();

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                screen2.getContentPane().add(previous3);
                previous3.setBounds(25, 100, 100, 500);
                previous3.setVisible(true);

                next3.setBackground(new Color(107, 50, 4));
                next3.setText(">>");
                next3.setFont(new Font("URW Gothic L", Font.BOLD, 30));
                next3.setHorizontalAlignment(SwingConstants.CENTER);
                next3.setForeground(Color.yellow);
                next3.setOpaque(false);
                next3.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            nextPaint();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                screen2.getContentPane().add(next3);
                next3.setBounds(875, 100, 100, 500);
                next3.setVisible(true);

                screen2.getContentPane().add(bggallery);

            } break;
            case 5: {

                inf= new JTextArea();
                scroll = new JScrollPane();

                screen4.setPreferredSize(new Dimension(400, 400));
                screen4.setResizable(false);
                screen4.setTitle("Información");
                Container frame1ContentPane = screen4.getContentPane();
                frame1ContentPane.setLayout(null);
                screen4.pack();
                screen4.setLocationRelativeTo(screen4.getOwner());
                screen4.setVisible(true);
                screen4.getContentPane().add(scroll);

                inf.setLineWrap(true);
                inf.setEditable(false);
                inf.setBorder(UIManager.getBorder("RootPane.frameBorder"));
                scroll.setViewportView(inf);
                screen4.getContentPane().add(scroll);

                scroll.setBounds(0,0,400,400);

            } break;
        }
    }

    void initComponents(){
        gallery1 = new JFrame();
        gallery2 = new JFrame();
        screen1 = new JFrame();
        screen2 = new JFrame();
        screen4 = new JFrame();
        configure(0);
    }

    private void ExitMain() {
        this.dispose();
        System.exit(0);
    }

    private void openGalleryAutores() throws ExecutionException, InterruptedException, IOException {
        configure(1);
        QueryDocumentSnapshot art=db.getFirstArtist();
        String urlp = (String) art.get("urlImg");
        name.setText(art.getId());
        URL url = new URL(urlp);
        Image image = ImageIO.read(url);
        Image scaled = image.getScaledInstance(photo.getWidth(),photo.getHeight(),Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(scaled);
        photo.setIcon(icon);

    }

    private void openGalleryObras() throws IOException {
        configure(2);
        QueryDocumentSnapshot paint = db.getFirstPaintingAll();
        String urlp = (String) paint.get("url");
        name2.setText(paint.getId());
        URL url = new URL(urlp);
        Image image = ImageIO.read(url);
        Image scaled = image.getScaledInstance(photo2.getWidth(),photo2.getHeight(),Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(scaled);
        photo2.setIcon(icon);
    }

    private void openScreen1() throws ExecutionException, InterruptedException, IOException {
        configure(3);
        QueryDocumentSnapshot art=db.getArtistOfAll();
        String urlp = (String) art.get("urlImg");
        name3.setText(art.getId());
        URL url = new URL(urlp);
        Image image = ImageIO.read(url);
        Image scaled = image.getScaledInstance(photo3.getWidth(),photo3.getHeight(),Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(scaled);
        photo3.setIcon(icon);

    }

    private void openScreen2(nscreen sts) throws IOException {
        configure(4);
        final nscreen st =sts;
        out4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                state=st;
                screen2.dispose();
            }
        });
        screen2.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                state=st;
            }
        });

        state=nscreen.ON_PAINTINGS_AUTHOR;
        QueryDocumentSnapshot art=db.getActualArtist();
        QueryDocumentSnapshot paint=db.getFirstPaint();
        screen2.setTitle("Obras de "+ art.getId());
        String urlp = (String) paint.get("url");
        name4.setText(paint.getId());
        URL url = new URL(urlp);
        Image image = ImageIO.read(url);
        Image scaled = image.getScaledInstance(photo4.getWidth(),photo4.getHeight(),Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(scaled);
        photo4.setIcon(icon);

    }

    private void nextAuthor() throws ExecutionException, InterruptedException, IOException {
        QueryDocumentSnapshot q=db.nextArtist();
        String urlp = (String) q.get("urlImg");
        name.setText(q.getId());
        URL url = new URL(urlp);
        Image image = ImageIO.read(url);
        Image scaled = image.getScaledInstance(photo.getWidth(),photo.getHeight(),Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(scaled);
        photo.setIcon(icon);
    }

    private void previousAuthor() throws ExecutionException, InterruptedException, IOException {
        QueryDocumentSnapshot q=db.previousArtist();
        String urlp = (String) q.get("urlImg");
        name.setText(q.getId());
        URL url = new URL(urlp);
        Image image = ImageIO.read(url);
        Image scaled = image.getScaledInstance(photo.getWidth(),photo.getHeight(),Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(scaled);
        photo.setIcon(icon);
    }

    private void previousPaint() throws IOException {
        switch (state){
            case ON_GALLERY2:
                QueryDocumentSnapshot q=db.previousPaintingAll();
                name2.setText(q.getId());
                String urlp = (String) q.get("url");
                URL url = new URL(urlp);
                Image image = ImageIO.read(url);
                Image scaled = image.getScaledInstance(photo2.getWidth(),photo2.getHeight(),Image.SCALE_DEFAULT);
                ImageIcon icon = new ImageIcon(scaled);
                photo2.setIcon(icon);
                break;
        }
    }

    private void openInfo(int type,nscreen st) {
        configure(5);
        final nscreen sts=st;
        state=nscreen.ON_INFO;
        screen4.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                state=sts;
                super.windowClosing(e);
            }
        });
        String text;
        if(type==1){
            text = db.getInfoPaint();
        }
        else{
            text=db.getInfoArtist();
        }

        inf.setText(text);

        inf.setSelectionStart(0);
        inf.setSelectionEnd(0);

    }

    private void nextPaint() throws IOException {
        switch (state){
            case ON_GALLERY2:
                QueryDocumentSnapshot q=db.nextPaintingAll();
                name2.setText(q.getId());
                String urlp = (String) q.get("url");
                URL url = new URL(urlp);
                Image image = ImageIO.read(url);
                Image scaled = image.getScaledInstance(photo2.getWidth(),photo2.getHeight(),Image.SCALE_DEFAULT);
                ImageIcon icon = new ImageIcon(scaled);
                photo2.setIcon(icon);
                break;
            case ON_PAINTINGS_AUTHOR:
                QueryDocumentSnapshot z=db.getNextPaint();
                name4.setText(z.getId());
                String urlz = (String) z.get("url");
                URL urlx = new URL(urlz);
                Image imagez = ImageIO.read(urlx);
                Image scaledz = imagez.getScaledInstance(photo4.getWidth(),photo4.getHeight(),Image.SCALE_DEFAULT);
                ImageIcon iconz = new ImageIcon(scaledz);
                photo4.setIcon(iconz);
                break;

        }
    }

    private void imageMouseClicked(MouseEvent e) {
        image.setVisible(false);
        image.setEnabled(false);
    }

    public void listen() throws InterruptedException, ExecutionException, IOException {

        if(lp.isConnected()){
            lpstate.setText("Leap Motion ON");
            lpstate.setForeground(Color.green);

            Frame fm=lp.getUltimoFrame();

            if(fm!=null && fm.hands().count()>0){
                rcg.setText("1 mano reconocida");
                rcg.setForeground(Color.green);

                Hand ref;
                if(fm.hands().count()==2){
                    ref=fm.hands().get(0).isRight() ? fm.hands().get(0) : fm.hands().get(1);
                    rcg.setText("2 manos reconocidad.Referencia: Derecha");
                }
                else
                    ref = fm.hands().get(0);


                // IMPLEMENT LEAP ACTIONS

                double beginX=0,beginY=0,wth=0,ht=0;
                double rangeX,rangeY;

                switch (state){
                    case ON_MAIN:
                    case ON_MUSEUM:
                        beginX= this.getLocation().x;
                        beginY= this.getLocation().y;
                        wth= this.getWidth();
                        ht = this.getHeight();
                        break;
                    case ON_GALLERY1:
                        beginX = gallery1.getLocation().x;
                        beginY = gallery1.getLocation().y;
                        wth= gallery1.getWidth();
                        ht = gallery1.getHeight();
                        break;
                    case ON_GALLERY2:
                        beginX = gallery2.getLocation().x;
                        beginY = gallery2.getLocation().y;
                        wth= gallery2.getWidth();
                        ht = gallery2.getHeight();
                        break;
                    case ON_AUTHOR:
                        beginX = screen1.getLocation().x;
                        beginY = screen1.getLocation().y;
                        wth= screen1.getWidth();
                        ht = screen1.getHeight();
                        break;
                    case ON_PAINTINGS_AUTHOR:
                        beginX = screen2.getLocation().x;
                        beginY = screen2.getLocation().y;
                        wth= screen2.getWidth();
                        ht = screen2.getHeight();
                        break;
                    case ON_INFO:
                        // TODO
                        break;
                    case ON_SHOW:
                        //TODO
                        break;
                }
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                beginX = beginY = 0;

                wth = screenSize.getWidth();
                ht = screenSize.getHeight();

                rangeX =fm.interactionBox().width();
                rangeY = 2*fm.interactionBox().height();

                float POSX= fm.hands().get(0).palmPosition().getX();
                float POSY = fm.hands().get(0).palmPosition().getY();
                POSY = POSY<15 ? 0:POSY;

                int X = (int)beginX + (int)((POSX+rangeX)*(wth/(2*rangeX)));
                int Y = (int)beginY + (int)((rangeY-POSY)*(ht/(rangeY)));

                boolean onlyindex=true;
                boolean closed = true;

                for(Finger f:ref.fingers()){
                    if(f.type()== Finger.Type.TYPE_INDEX){
                        onlyindex = onlyindex && f.isExtended();
                        closed = closed && !f.isExtended();
                    }
                    else{
                        onlyindex = onlyindex && !f.isExtended();
                        closed = closed && !f.isExtended();
                    }
                }

                if(onlyindex)
                    robot.mouseMove(X,Y);

                if(closed && (System.currentTimeMillis()-timegesture)>2000){
                    timegesture=System.currentTimeMillis();
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }

                // TODO PENSAR MAS GESTOS

                //System.out.println(ref.palmNormal().getX());
                if((System.currentTimeMillis()-timegesture)>2000){

                    switch (state){
                        case ON_GALLERY1:
                            if(Math.abs(ref.palmNormal().getX())>0.85){
                                if(ref.palmVelocity().getX()>100){
                                    timegesture=System.currentTimeMillis();
                                    nextAuthor();
                                }
                                else if(ref.palmVelocity().getX()<-100){
                                    timegesture=System.currentTimeMillis();
                                    previousAuthor();
                                }


                            }
                            break;
                        case ON_GALLERY2:
                        case ON_PAINTINGS_AUTHOR:

                            if(fm.hands().count()==2){
                                Hand left,right;

                                if (fm.hands().get(0).isLeft()){
                                    left = fm.hands().get(0);
                                    right = fm.hands().get(1);
                                }
                                else{
                                    left = fm.hands().get(1);
                                    right = fm.hands().get(0);
                                }

                                if (Math.abs(left.palmNormal().getX()) > 0.85 && Math.abs(right.palmNormal().getX()) > 0.85){
                                    if (left.palmVelocity().getX() < -25 && right.palmVelocity().getX() > 25){
                                        System.out.println("ZOOM IN");
                                    }

                                    else if (left.palmVelocity().getX() > 25 && right.palmVelocity().getX() < -25){
                                        System.out.println("ZOOM OUT");
                                    }
                                }
                            }
                            else if(Math.abs(ref.palmNormal().getX())>0.85){
                                if(ref.palmVelocity().getX()>100){
                                    timegesture=System.currentTimeMillis();
                                    nextPaint();
                                }

                                else if(ref.palmVelocity().getX()<-100){
                                    timegesture=System.currentTimeMillis();
                                    previousPaint();
                                }
                            }

                            break;
                        case ON_SHOW:
                            // TODO ZOOM
                            break;
                        case ON_INFO:
                            if(ref.palmVelocity().getX()<-200 && ref.palmVelocity().getY()<-200)
                                screen4.dispose();
                            break;
                    }
                }

                //System.out.println(ref.palmNormal().getX()+" "+ ref.palmNormal().getY()+" "+ref.palmNormal().getZ());
            }
            else {
                rcg.setText("Not recognizing...");
                rcg.setForeground(Color.red);
            }

        }
        else{
            lpstate.setText("Leap Motion OFF");
            lpstate.setForeground(Color.red);
        }


    }

    @Override
    public void run() {
        while(true){
            /*Frame fm=lp.getUltimoFrame();
            if(fm.hands().count()==1){
                Hand hd = fm.hands().get(0);
                System.out.println(hd.palmPosition().getX());
            }*/
            try {
                listen();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String args[]) throws InterruptedException, ExecutionException, IOException, AWTException {

        Main main= new Main();
        main.setVisible(true);
        Thread mainthr=new Thread(main);
        mainthr.start();

    }


}
