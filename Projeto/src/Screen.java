import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Screen extends JFrame{

    JFrame jCadastro;


    public Screen() {
        telaMenu();
        botoesTelaPrincipal();
        framesMenu();
        setVisible(true);
    }
    private void telaMenu() {
        // Configuração da tela principal
        setTitle("World Of Tranks");
        setSize(1100, 800);
        setBackground(new Color(3, 26, 41));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(3, 26, 41));
        setResizable(false);
        setLayout(null);
    }
    private void botoesTelaPrincipal() {
        // Setar cor, fonte do texto e local do botao
        JButton jButtonIniciar = new JButton("Start");
        jButtonIniciar.setBounds(500, 700, 80, 50);
        jButtonIniciar.setFont(new Font("Arial", Font.BOLD, 18));
        jButtonIniciar.setForeground(new Color(255, 255, 255));
        jButtonIniciar.setBackground(new Color(40, 100, 60));

        // Criação da borda de cor preta dos botoes
        jButtonIniciar.setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 2));
        jButtonIniciar.setFocusPainted(false);

        add(jButtonIniciar); //Adicionar botão de iniciar

        // Setar cor, fonte do texto e local do botao
        JButton jButtonCadastrar = new JButton("Cadastrar");
        jButtonCadastrar.setBounds(280, 275, 80, 50);
        jButtonCadastrar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonCadastrar.setForeground(new Color(255, 255, 255));
        jButtonCadastrar.setBackground(new Color(40, 100, 60));

        // Criação da borda de cor preta dos botoes
        jButtonCadastrar.setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 2));
        jButtonCadastrar.setFocusPainted(false);

        add(jButtonCadastrar); //Adicionar botão de Cadastrar
        jButtonCadastrar.addActionListener(this::cadastrar); //Adicionar função do botão

        // Setar cor, fonte do texto e local do botao
        JButton jButtonAgendar = new JButton("Agendar");
        jButtonAgendar.setBounds(718, 275, 80, 50);
        jButtonAgendar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonAgendar.setForeground(new Color(255, 255, 255));
        jButtonAgendar.setBackground(new Color(40, 100, 60));

        // Criação da borda de cor preta dos botoes
        jButtonAgendar.setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 2));
        jButtonAgendar.setFocusPainted(false);

        add(jButtonAgendar); //Adicionar botão de Agendamento
        jButtonAgendar.addActionListener(this::agendar); //Adicionar função do botão

        // Setar cor, fonte do texto e local do botao
        JButton jButtonRelatorio = new JButton("Relatorio");
        jButtonRelatorio.setBounds(280, 613, 80, 50);
        jButtonRelatorio.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonRelatorio.setForeground(new Color(255, 255, 255));
        jButtonRelatorio.setBackground(new Color(40, 100, 60));

        // Criação da borda de cor preta dos botoes
        jButtonRelatorio.setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 2));
        jButtonRelatorio.setFocusPainted(false);

        add(jButtonRelatorio); //Adicionar botão de Relatorio

    }
    private void framesMenu() {
        // Cor, tamanho e local do frame
        JPanel jPanel1 = new JPanel();
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        jPanel1.setBackground(new Color(0xDCDCDC));

        int x1 = (int)(1200 * 0.1);
        int y1 = (int)(400 * 0.1);
        int width1 = (int)(420 * 0.96); 
        int height1 = (int)(300 * 0.96);
        
        // Criação do frame
        jPanel1.setBounds(x1, y1, width1, height1);     
        add(jPanel1);

        // Cor, tamanho e local do frame
        JPanel jPanel2 = new JPanel();
        jPanel2.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        jPanel2.setBackground(new Color(0xDCDCDC));

        int x2 = (int)(930 * 0.6);
        int y2 = (int)(400 * 0.1);
        int width2 = (int)(420 * 0.96);
        int height2 = (int)(300 * 0.96);
        
        // Criação do frame
        jPanel2.setBounds(x2, y2, width2, height2);  
        add(jPanel2);

        // Cor, tamanho e local do frame
        JPanel jPanel3 = new JPanel();
        jPanel3.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        jPanel3.setBackground(new Color(0xDCDCDC));

        int x3 = (int)(1200 * 0.1); 
        int y3 = (int)(630 * 0.6);
        int width3 = (int)(420 * 0.96);
        int height3 = (int)(300 * 0.96);
        
        // Criação do frame
        jPanel3.setBounds(x3, y3, width3, height3); 
        add(jPanel3);

        // Cor, tamanho e local do frame
        JPanel jPanel4 = new JPanel();
        jPanel4.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        jPanel4.setBackground(new Color(0xDCDCDC));

        int x4 = (int)(930 * 0.6);      
        int y4 = (int)(630 * 0.6);  
        int width4 = (int)(420 * 0.96);
        int height4 = (int)(300 * 0.96);
        
        // Criação do frame
        jPanel4.setBounds(x4, y4, width4, height4);               
        add(jPanel4);
    }
    private void telaCadastro() {
        jCadastro = new JFrame();
        jCadastro.setTitle("Cadastro De Tanques");
        jCadastro.setSize(800, 500);
        jCadastro.setBackground(new Color(3, 26, 41));
        jCadastro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jCadastro.setLocationRelativeTo(this);
        jCadastro.getContentPane().setBackground(new Color(3, 26, 41));
        jCadastro.setResizable(false);
        jCadastro.setLayout(null);

        conteudoCadastro();

        jCadastro.setVisible(true);
    }
    private void conteudoCadastro() {
        JLabel label = new JLabel("Nome Do Tanque");
        label.setBounds(350, 100, 300, 50);
        label.setForeground(new Color(255, 255, 255));
        jCadastro.add(label);

        JTextField nomeTank = new JTextField();
        nomeTank.setBounds(350, 135, 100, 20);
        jCadastro.add(nomeTank);

        JRadioButton leve = new JRadioButton("Tanque Leve", false);
        JRadioButton medio = new JRadioButton("Tanque Medio", false);
        JRadioButton pesado = new JRadioButton("Tanque Pesado", false);

        JLabel tiposTank = new JLabel("Tipos De Tanques");
        tiposTank.setBounds(350, 150, 300, 80);
        tiposTank.setForeground(new Color(255, 255, 255));
        jCadastro.add(tiposTank);

        leve.setBounds(250, 200, 100, 40);
        leve.setBackground(new Color(3, 26, 41));
        leve.setForeground(new Color(255, 255, 255));

        medio.setBounds(350, 200, 105, 40);
        medio.setBackground(new Color(3, 26, 41));
        medio.setForeground(new Color(255, 255, 255));

        pesado.setBounds(455, 200, 120, 40);
        pesado.setBackground(new Color(3, 26, 41));
        pesado.setForeground(new Color(255, 255, 255));
        
    
        jCadastro.add(leve);
        jCadastro.add(medio);
        jCadastro.add(pesado);

    }
    private void agendar(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "ERRADOOOO");      
    }
    private void cadastrar(ActionEvent e) {
        telaCadastro();
    }
}
