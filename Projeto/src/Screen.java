import javax.swing.*;
import java.awt.*;

public class Screen extends JFrame {

    public Screen() {
        configurarTela();
        criarFrames();
    }

    private void configurarTela() {
        setTitle("World Of Tranks");
        setBackground(new Color(3, 26, 41));
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(3, 26, 41));
        setResizable(false);
        setLayout(null);

        JButton jButtonIniciar = new JButton("Start");
        jButtonIniciar.setBounds(500, 700, 80, 50);
        jButtonIniciar.setFont(new Font("Arial", Font.BOLD, 18));
        jButtonIniciar.setForeground(new Color(255, 255, 255));
        jButtonIniciar.setBackground(new Color(40, 100, 60));

        jButtonIniciar.setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 2));
        jButtonIniciar.setFocusPainted(false);

        add(jButtonIniciar);

        JButton jButtonCadastrar = new JButton("Cadastrar");
        jButtonCadastrar.setBounds(280, 275, 80, 50);
        jButtonCadastrar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonCadastrar.setForeground(new Color(255, 255, 255));
        jButtonCadastrar.setBackground(new Color(40, 100, 60));

        jButtonCadastrar.setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 2));
        jButtonCadastrar.setFocusPainted(false);

        add(jButtonCadastrar);

            
        JButton jButtonAgendar = new JButton("Agendar");
        jButtonAgendar.setBounds(718, 275, 80, 50);
        jButtonAgendar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonAgendar.setForeground(new Color(255, 255, 255));
        jButtonAgendar.setBackground(new Color(40, 100, 60));

        jButtonAgendar.setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 2));
        jButtonAgendar.setFocusPainted(false);

        add(jButtonAgendar);


    }
    private void criarFrames() {
        JPanel jPanel1 = new JPanel();
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        jPanel1.setBackground(new Color(0xDCDCDC));

        int x1 = (int)(1200 * 0.1);      // 2% da largura
        int y1 = (int)(400 * 0.1);      // 2% da altura
        int width1 = (int)(420 * 0.96);  // 96% da largura
        int height1 = (int)(300 * 0.96); // 96% da altura
        
        jPanel1.setBounds(x1, y1, width1, height1);
                
        add(jPanel1);

        JPanel jPanel2 = new JPanel();
        jPanel2.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        jPanel2.setBackground(new Color(0xDCDCDC));

        int x2 = (int)(930 * 0.6);      // 2% da largura
        int y2 = (int)(400 * 0.1);      // 2% da altura
        int width2 = (int)(420 * 0.96);  // 96% da largura
        int height2 = (int)(300 * 0.96); // 96% da altura
        
        jPanel2.setBounds(x2, y2, width2, height2);
                
        add(jPanel2);

        JPanel jPanel3 = new JPanel();
        jPanel3.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        jPanel3.setBackground(new Color(0xDCDCDC));

        int x3 = (int)(1200 * 0.1);      // 2% da largura
        int y3 = (int)(630 * 0.6);      // 2% da altura
        int width3 = (int)(420 * 0.96);  // 96% da largura
        int height3 = (int)(300 * 0.96); // 96% da altura
        
        jPanel3.setBounds(x3, y3, width3, height3);
                
        add(jPanel3);

        JPanel jPanel4 = new JPanel();
        jPanel4.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        jPanel4.setBackground(new Color(0xDCDCDC));

        int x4 = (int)(930 * 0.6);      // 2% da largura
        int y4 = (int)(630 * 0.6);      // 2% da altura
        int width4 = (int)(420 * 0.96);  // 96% da largura
        int height4 = (int)(300 * 0.96); // 96% da altura
        
        jPanel4.setBounds(x4, y4, width4, height4);
                
        add(jPanel4);

        setVisible(true);
    }


}
