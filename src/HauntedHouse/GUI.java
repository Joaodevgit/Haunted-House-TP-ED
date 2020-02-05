package HauntedHouse;

import ed.adt.OrderedListADT;
import ed.adt.UnorderedListADT;
import ed.exceptions.ElementNotFoundException;
import ed.exceptions.EmptyCollectionException;
import ed.exceptions.NonAvailablePath;
import ed.exceptions.NonComparableException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * <i>Graphical User Interface</i>
 *
 * @author Francisco Spínola
 */
public class GUI implements ActionListener {

    private boolean blocked;
    private Instance instance;
    private Files files;
    private boolean sound;
    private Map<Room> map;
    private JLabel points;
    private JLabel pos;
    private final JButton easy;
    private final JButton normal;
    private final JButton hard;
    private JButton normalMode;
    private JButton simulationMode;
    private JButton close;
    private JButton highscore;
    private JButton levelChange;
    private JButton ok;
    private JButton okHighscore;
    private JButton soundB;
    private JButton menuB;
    private JButton okGhost;
    private JTextField playerName = new JTextField(3);
    private JFrame level;
    private JFrame game;
    private JFrame player;
    private JFrame highscoreFrame;
    private JFrame ghost;
    private Clip clip;

    //private OrderedListADT<Player> ranking;
    /**
     * <p>
     * <strong>Estado do programa</strong></p>
     * <p>
     * 0: Escolha do nível de dificuldade</p>
     * <p>
     * 1: Escolha do nome do jogador</p>
     * <p>
     * 2: Menu principal</p>
     */
    private short status;

    /**
     * Método construtor para a GUI. Chama o método responsável pela escolha do
     * nível de dificuldade.
     *
     * @param map mapa do jogo
     */
    public GUI(Map map) throws IOException, ElementNotFoundException {
        this.blocked = false;
        this.instance = new Instance();
        this.instance.setPos((Room)map.getEntranceRoom());
        this.instance.setScore(map.getPoints());
        this.files = new Files();
        this.sound = true;
        this.easy = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/easy.png"))));
        this.normal = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/normal2.png"))));
        this.hard = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/hard.png"))));
        this.map = map;
        this.status = 0;
        this.levelChanger();
    }

    /**
     * Método responsável por alterar o nível de dificuldade do jogo.
     */
    private void levelChanger() {
        this.blocked = true;
        
        //Creating the frame and panels
        this.level = new JFrame("Dificuldade");
        JPanel panel = new JPanel();
        JPanel text = new JPanel();
        JPanel main = new JPanel(new BorderLayout());
        this.easy.setBorderPainted(false);
        this.easy.setContentAreaFilled(false);
        this.easy.setMargin(new Insets(0, 0, 0, 0));
        this.easy.setOpaque(false);
        this.normal.setBorderPainted(false);
        this.normal.setContentAreaFilled(false);
        this.normal.setMargin(new Insets(0, 0, 0, 0));
        this.normal.setOpaque(false);
        this.hard.setBorderPainted(false);
        this.hard.setContentAreaFilled(false);
        this.hard.setMargin(new Insets(0, 0, 0, 0));
        this.hard.setOpaque(false);
        JLabel dif = new JLabel("DIFICULDADE");
        dif.setFont(new Font("font", Font.ITALIC, 20));
        text.add(dif);

        //Adding content to the frame
        panel.setLayout(new GridLayout(1, 3, 5, 10));
        panel.add(this.easy);
        panel.add(this.normal);
        panel.add(this.hard);
        panel.setBackground(Color.BLACK);
        text.setBackground(Color.BLACK);
        main.add(text, BorderLayout.NORTH);
        main.add(panel, BorderLayout.CENTER);
        this.level.getContentPane().add(main);

        //Configuring the frame
        this.level.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.level.setResizable(false);
        this.level.setUndecorated(true);
        this.level.setSize(300, 110);
        this.level.setLocationRelativeTo(null);
        this.level.setVisible(true);

        //Adding the listeners
        this.easy.addActionListener(this);
        this.normal.addActionListener(this);
        this.hard.addActionListener(this);
    }

    /**
     * Altera o estado da música (ligada/desligada).
     */
    private void sound() {
        if (this.sound) {
            this.sound = false;
            this.clip.stop();
        } else {
            this.sound = true;
            this.clip.start();
        }
    }

    /**
     * Escolha do nome do jogador.
     */
    private void playerName() throws IOException {
        //Creating the frame, panels and button
        this.player = new JFrame("Jogador");
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel main = new JPanel();
        this.playerName = new JTextField(21);
        this.ok = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/submit.png"))));
        this.ok.setBorderPainted(false);
        this.ok.setContentAreaFilled(false);
        this.ok.setMargin(new Insets(0, 0, 0, 0));
        this.ok.setOpaque(false);
        JLabel text = new JLabel("NOME DO JOGADOR");
        text.setFont(new Font("font", Font.ITALIC, 18));

        //Adding content to the frame
        main.setLayout(new BorderLayout());
        panel.add(this.playerName);
        panel2.add(this.ok, BorderLayout.CENTER);
        panel.setBackground(Color.BLACK);
        panel2.setBackground(Color.BLACK);
        main.add(text, BorderLayout.NORTH);
        main.add(panel, BorderLayout.CENTER);
        main.add(panel2, BorderLayout.SOUTH);
        main.setBackground(Color.BLACK);
        this.player.getContentPane().add(main);

        //Configuring the frame
        this.player.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.player.setResizable(false);
        this.player.setUndecorated(true);
        this.player.setSize(250, 140);
        this.player.setLocationRelativeTo(null);
        this.player.setVisible(true);

        //Adding the listener
        this.ok.addActionListener(this);
    }

    /**
     * Menu principal.
     */
    private void mainMenu() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        //Creating the frame
        this.game = new JFrame("Casa Assombrada");
        this.close = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/close.png"))));
        this.highscore = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/highscore.png"))));
        this.levelChange = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/level.png"))));
        this.normalMode = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/normal.png"))));
        this.simulationMode = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/simulation.png"))));
        this.soundB = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/sound.png"))));

        //Adding content to the frame
        this.game.add(new BackgroundPane());

        //Configuring the frame
        this.game.setUndecorated(true);
        this.game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.game.setResizable(false);
        this.game.setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDisplayMode().getWidth(), GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
                        getDisplayMode().getHeight());
        this.game.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.game.setLocationRelativeTo(null);
        this.game.setVisible(true);

        this.clip = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("lib/sounds/Fable-Lychfield-Cemetary.wav"));
        this.clip.open(inputStream);
        if (this.sound) {
            this.clip.start();
        }

        //Adding the listeners
        this.close.addActionListener(this);
        this.normalMode.addActionListener(this);
        this.simulationMode.addActionListener(this);
        this.levelChange.addActionListener(this);
        this.highscore.addActionListener(this);
        this.soundB.addActionListener(this);
    }

    /**
     * Classe que estrutura o menu principal
     */
    class BackgroundPane extends JPanel {

        /**
         * Método construtor que estrutura o menu principal do jogo
         */
        public BackgroundPane() throws IOException {
            setLayout(new BorderLayout());
            JLabel label = new JLabel(new ImageIcon(
                    ImageIO.read(new File("lib/images/background.jpg")).getScaledInstance(GraphicsEnvironment.
                            getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth(),
                            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
                                    getDisplayMode().getHeight(), Image.SCALE_DEFAULT)));

            add(label);

            JPanel optionsPane = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTH;

            optionsPane.setBackground(Color.BLACK);
            optionsPane.add(this.makeButton(GUI.this.normalMode), gbc);
            optionsPane.add(this.makeButton(GUI.this.simulationMode), gbc);
            optionsPane.add(this.makeButton(GUI.this.highscore), gbc);
            optionsPane.add(this.makeButton(GUI.this.levelChange), gbc);
            optionsPane.add(this.makeButton(GUI.this.soundB), gbc);
            optionsPane.add(this.makeButton(GUI.this.close), gbc);

            add(optionsPane, BorderLayout.LINE_END);
        }

        /**
         * Método que altera o formato visual de um <i>JButton</i>.
         *
         * @param btn botão a ser alterado
         * @return botão alterado
         */
        private JButton makeButton(JButton btn) {
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setOpaque(false);
            return btn;
        }
    }

    public void highscore() throws IOException, NonComparableException {
        this.blocked = true;
        
        this.highscoreFrame = new JFrame("Classificações");
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        panel1.setLayout(new GridLayout(0, 4));
        panel3.setLayout(new BorderLayout(10, 10));
        
        this.highscoreFrame.setSize(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth()/* - 500*/, 
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight()/* - 200*/
        );
        JLabel label = new JLabel(new ImageIcon(ImageIO.read(new File("lib/images/TOP25.jpg")).
                            getScaledInstance(this.highscoreFrame.getWidth(), this.highscoreFrame.getHeight(), Image.SCALE_DEFAULT)));

        //Button config
        this.okHighscore = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/ok.png"))));
        this.okHighscore.setBorderPainted(false);
        this.okHighscore.setContentAreaFilled(false);
        this.okHighscore.setMargin(new Insets(0, 0, 0, 0));
        this.okHighscore.setOpaque(false);
        
        //Add content to the frame
        int i = 0;
        OrderedListADT<InstanceTUI> list = this.files.loadByDifficulty(this.instance.getLevel());
        Iterator<InstanceTUI> iter = list.iterator();
        panel1.setBackground(Color.BLACK);
        panel1.add(new JLabel("<html><strong><font color=\"white\">COLOCAÇÃO</font></strong></html>"));
        panel1.add(new JLabel("<html><strong><font color=\"white\">NOME</font></strong></html>"));
        panel1.add(new JLabel("<html><strong><font color=\"white\">PONTOS</font></strong></html>"));
        panel1.add(new JLabel("<html><strong><font color=\"white\">MAPA</font></strong></html>"));
        while (iter.hasNext() && i < 25) {
            InstanceTUI instanceTUI = iter.next();
            int pos = ++i;
            String line = "\n" + pos + "º   -   Nome: " + instanceTUI.getName() + "   -   Pontos: " + instanceTUI.getScore()+ "   -   Mapa: " + instanceTUI.getMapName() + "\n";
            panel1.add(new JLabel("<html><font color=\"white\">" + pos + "º " + "</font></html>"));
            panel1.add(new JLabel("<html><font color=\"white\">" + instanceTUI.getName() + "</font></html>"));
            panel1.add(new JLabel("<html><font color=\"white\">" + String.valueOf(instanceTUI.getScore() + "</font></html>")));
            panel1.add(new JLabel("<html><font color=\"white\">" + instanceTUI.getMapName() + "</font></html>"));
        }
        panel2.setBackground(Color.BLACK);
        panel2.add(this.okHighscore, BorderLayout.SOUTH);
        String levelS;
        switch (this.instance.getLevel()) {
            case 1:
                levelS = "Básico";
                break;
            case 2:
                levelS = "Normal";
                break;
            case 3:
                levelS = "Difícil";
                break;
            default:
                levelS = "Normal";
        }
        panel3.add(new JLabel("<html><font color=\"red\">DIFICULDADE: " + levelS + "</font></html>"), BorderLayout.EAST);
        panel3.add(panel1, BorderLayout.SOUTH);
        panel3.setBackground(Color.BLACK);
        this.highscoreFrame.add(label);
        this.highscoreFrame.getContentPane().add(panel3, BorderLayout.NORTH);
        this.highscoreFrame.getContentPane().add(panel2, BorderLayout.SOUTH);
        
        //Frame config
        this.highscoreFrame.setLocationRelativeTo(null);
        this.highscoreFrame.setUndecorated(true);
        this.highscoreFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.highscoreFrame.setResizable(false);
        this.highscoreFrame.setVisible(true);
        
        //Action listeners
        this.okHighscore.addActionListener(this);
    }
    
    /**
     * Jogo em Modo Simulação.
     */
    private void simulation() throws ElementNotFoundException, NonAvailablePath, EmptyCollectionException {
        this.blocked = true;
        
        Iterator<Room> iter = this.map.iteratorShortestPath(this.map.getEntranceRoom(), this.map.getBestExitRoom());
        System.out.print("O caminho mais curto é: ");
        while (iter.hasNext()) {
            Room res = iter.next();
            System.out.print(res.getName() + " ");
        }
        int points = (int) this.map.shortestPathWeight(this.map.getEntranceRoom(), this.map.getBestExitRoom()) * this.instance.getLevel();
        System.out.println("O utilizador perderia: " + points + " pontos de vida");

    }

    /**
     * Jogo em Modo Normal.
     */
    private void normal() throws LineUnavailableException, IOException, UnsupportedAudioFileException, ElementNotFoundException {
        this.game = new JFrame("Casa Assombrada");
        this.menuB = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/menu.png"))));
        this.menuB.setBorderPainted(false);
        this.menuB.setContentAreaFilled(false);
        this.menuB.setMargin(new Insets(0, 0, 0, 0));
        this.menuB.setOpaque(false);
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel panel2 = new JPanel(new BorderLayout(0, -30));
        GridBagConstraints gbc = new GridBagConstraints();
        
        this.points = new JLabel("<html><h1><font color=\"white\">PONTOS: " + this.instance.getScore() + "</font></h1></html>");
        this.pos = new JLabel("<html><h3><font color=\"white\">APOSENTO ATUAL: " + this.instance.getPos().getName().toUpperCase() + "</font></h3></html>");
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        
        panel.add(this.points, gbc);
        gbc.gridy = 1;
        panel.add(this.pos, gbc);
        panel.setBackground(Color.BLACK);
        
        panel2.add(this.soundB);
        panel2.add(this.menuB);
        
        panel2.add(panel, BorderLayout.NORTH);
        panel2.setBackground(Color.BLACK);
        this.game.add(panel2, BorderLayout.EAST);
        this.game.add(new GamePane());
        
        //Configuring audio
        this.clip = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("lib/sounds/gamemusic.wav"));
        this.clip.open(inputStream);
        if (this.sound) {
            this.clip.start();
        }
        
        //Configuring the frame
        this.game.setUndecorated(true);
        this.game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.game.setResizable(false);
        this.game.setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDisplayMode().getWidth(), GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
                        getDisplayMode().getHeight());
        this.game.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.game.setLocationRelativeTo(null);
        this.game.setVisible(true);
        
        this.soundB.addActionListener(this);
        this.menuB.addActionListener(this);
    }
    
    class GamePane extends JPanel implements ActionListener {
        JButton[] doors;
        UnorderedListADT<Room> rooms;
                
        public GamePane() throws IOException, ElementNotFoundException {
            setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            JPanel panel2 = new JPanel();
            panel2.setLayout(new BorderLayout());
            panel2.add(new JLabel(new ImageIcon("lib/images/ok.png")));
            
            this.rooms = GUI.this.map.getRoomEdges(GUI.this.instance.getPos().getName());
            Iterator<Room> iter = this.rooms.iterator();
            panel.setLayout(new GridLayout(2, this.rooms.size(), 20, 20));
            while (iter.hasNext()) {
                JLabel jlabel = new JLabel("<html><h1><strong><font color=\"white\">" + iter.next().getName().toUpperCase() + "</font></strong></h1></html>", (int)JLabel.CENTER_ALIGNMENT);
                panel.add(jlabel);
            }
            
            this.doors = new JButton[this.rooms.size()];
            
            int i = 0;
            while (i < this.rooms.size()) {
                this.doors[i] = this.makeButton(new JButton(new ImageIcon(ImageIO.read(
                        new File("lib/images/closedDoor.png")).getScaledInstance(GraphicsEnvironment.
                            getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth() / 5, 
                                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
                                    getDisplayMode().getHeight() / 2, Image.SCALE_DEFAULT))));
                panel.add(this.doors[i]);
                this.doors[i].addActionListener(this);
                i++;
            }
            panel.setBackground(Color.BLACK);
            
            add(panel, BorderLayout.SOUTH);
            add(panel2, BorderLayout.NORTH);
        }
        
        /**
         * Método que altera o formato visual de um <i>JButton</i>.
         *
         * @param btn botão a ser alterado
         * @return botão alterado
         */
        private JButton makeButton(JButton btn) {
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setOpaque(false);
            return btn;
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            try {
                Iterator<Room> iter = this.rooms.iterator();
                Room room = new Room();
                if (ev.getSource().equals(this.doors[0])) {
                    room = iter.next();
                } else if (ev.getSource().equals(this.doors[1])) {
                    iter.next();
                    room = iter.next();
                } else if (ev.getSource().equals(this.doors[2])) {
                    iter.next();
                    iter.next();
                    room = iter.next();
                } else if (ev.getSource().equals(this.doors[3])) {
                    iter.next();
                    iter.next();
                    iter.next();
                    room = iter.next();
                }
                long remove = GUI.this.instance.getScore() - (GUI.this.map.getEdgeWeight(GUI.this.instance.getPos().getName(), room.getName()) * GUI.this.instance.getLevel());
                if (remove < 0)
                    remove = 0;
                if (GUI.this.instance.getScore() != remove)
                    GUI.this.ghost(GUI.this.map.getEdgeWeight(GUI.this.instance.getPos().getName(), room.getName()) * GUI.this.instance.getLevel());
                GUI.this.instance.setScore(remove);
                GUI.this.instance.setPos(room);
                GUI.this.points.setText("<html><h1><font color=\"white\">PONTOS: " + GUI.this.instance.getScore() + "</font></h1></html>");
                GUI.this.pos.setText("<html><h3><font color=\"white\">APOSENTO ATUAL: " + GUI.this.instance.getPos().getName().toUpperCase() + "</font></h3></html>");
            } catch (ElementNotFoundException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Método que apresenta a janela que demonstra os pontos a serem retirados por encontrar um fantasma.
     * 
     * @param points pontos a serem retirados ao encontrar um determinado fantasma
     * @throws IOException erro ao procurar a biblioteca de imagens
     */
    private void ghost(long points) throws IOException {
        //Start the variables
        this.ghost = new JFrame("Fantasma");
        this.okGhost = new JButton(new ImageIcon(ImageIO.read(new File("lib/images/ok.png"))));
        this.okGhost.setBorderPainted(false);
        this.okGhost.setContentAreaFilled(false);
        this.okGhost.setMargin(new Insets(0, 0, 0, 0));
        this.okGhost.setOpaque(false);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JLabel label = new JLabel(new ImageIcon(ImageIO.read(
                        new File("lib/images/ghost.png")).getScaledInstance(GraphicsEnvironment.
                            getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth() / 5, 
                                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
                                    getDisplayMode().getHeight() / 2, Image.SCALE_DEFAULT)));
        
        //Layout config and Add content to panels
        panel1.setLayout(new BorderLayout());
        panel1.add(new JLabel("<html><h1><font color=\"white\">FANTASMA ENCONTRADO!!!</font></h3></html>"), BorderLayout.NORTH);
        panel1.add(label, BorderLayout.SOUTH);
        panel1.setBackground(Color.BLACK);
        
        panel2.add(new JLabel("<html><h3><font color=\"white\">Ups! Encontraste um fantasma... Perdeste " + points + "pontos</font></h3></html>"));
        panel2.setBackground(Color.BLACK);
        
        panel3.add(this.okGhost);
        panel3.setBackground(Color.BLACK);
        
        //Add content to the frame
        this.ghost.getContentPane().add(panel1, BorderLayout.NORTH);
        this.ghost.getContentPane().add(panel2, BorderLayout.CENTER);
        this.ghost.getContentPane().add(panel3, BorderLayout.SOUTH);
        
        //Frame config
        this.ghost.setSize(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth()/* - 500*/, 
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight()/* - 200*/
        );
        this.ghost.setLocationRelativeTo(null);
        this.ghost.setUndecorated(true);
        this.ghost.setResizable(false);
        this.ghost.setVisible(true);
        
        //Action listener
        this.okGhost.addActionListener(this);
    }

    /**
     * Método responsável por definir o que se sucede aquando determinada ação é
     * executada.
     *
     * @param ev evento
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        String[] ok = {"OK"};
        if (ev.getSource().equals(this.easy)) {
            this.level.dispose();
            this.instance.setLevel(this.instance.EASY);
            this.blocked = false;
            if (this.status == 0) {
                this.status = 1;
                try {
                    this.playerName();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (ev.getSource().equals(this.normal)) {
            this.level.dispose();
            this.instance.setLevel(this.instance.NORMAL);
            this.blocked = false;
            if (this.status == 0) {
                this.status = 1;
                try {
                    this.playerName();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (ev.getSource().equals(this.hard)) {
            this.level.dispose();
            this.instance.setLevel(this.instance.HARD);
            this.blocked = false;
            if (this.status == 0) {
                this.status = 1;
                try {
                    this.playerName();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (ev.getSource().equals(this.ok)) {
            if (this.playerName.getText().equals("")) {
                JOptionPane.showOptionDialog(this.player, "Campo vazio!", "Aviso",
                        JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, ok, ok[0]);
            } else if (!this.playerName.getText().matches("^[a-zA-Z0-9 ]+$")) {
                JOptionPane.showOptionDialog(this.player, "Insira apenas carateres alfanuméricos!", "Aviso",
                        JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, ok, ok[0]);
            } else {
                if (this.status == 1) {
                    this.status = 2;
                    this.instance.setName(this.playerName.getText());
                    try {
                        this.mainMenu();
                    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                this.player.dispose();
            }
        } else if (ev.getSource().equals(this.okHighscore)) {
            this.highscoreFrame.dispose();
            this.blocked = false;
        } else if (ev.getSource().equals(this.close) && !this.blocked) {
            System.exit(0);
        } else if (ev.getSource().equals(this.levelChange) && !this.blocked) {
            this.levelChanger();
        } else if (ev.getSource().equals(this.highscore) && !this.blocked) {
            try {
                this.highscore();
            } catch (IOException | NonComparableException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (ev.getSource().equals(this.normalMode) && !this.blocked) {
            this.clip.stop();
            this.game.dispose();
            try {
                this.normal();
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException | ElementNotFoundException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (ev.getSource().equals(this.simulationMode) && !this.blocked) {
            try {
                this.simulation();
            } catch (ElementNotFoundException | NonAvailablePath | EmptyCollectionException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (ev.getSource().equals(this.soundB) && !this.blocked) {
            this.sound();
        } else if (ev.getSource().equals(this.menuB)) {
            this.clip.stop();
            this.game.dispose();
            try {
                this.mainMenu();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (ev.getSource().equals(this.okGhost)) {
            this.ghost.dispose();
        }
    }
}