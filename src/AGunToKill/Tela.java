package AGunToKill;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
public class Tela extends JPanel implements ActionListener {
    //variável que diz se os dois usuários estão conectados
    boolean conectado;
    //variável para limitar a um só "fim"
    boolean finaliza;
    //imagem da tela "desabilitada"
    ImageIcon IfundoD = new ImageIcon("desabilitado.png");
    Image fundoD = IfundoD.getImage();
    //imagem de fundo do jogo
    ImageIcon Ifundo = new ImageIcon("fundo.png");
    Image fundo = Ifundo.getImage();
    //variáveis usadas no JOptionPane do final da partida
    Object[] opcoes = {"Voltar Para o Menu","Fechar"};
    int valor = 5;
    //timer, que trava a execução por um tempo determinado
    private final Timer timer = new Timer(10, this);
    //id da tela, que se refere ao personagem
    public int id;
    //cria duas instâncias do personagem
    public final Personagem player1 = new Personagem(0, 210, 1);
    public final Personagem player2 = new Personagem(636, 210, 2);
    //por praticidade em fazer a comunicação do servidor e cliente, o mesmo é criado nessa classe, e não da classe principal
    Cliente cliente;
    Tiro tiro;
    //construtor da tela
    public Tela(int id) {
        initTela();
        this.id = id;
    }
    private void initTela() {
        setSize(700,500);
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.LIGHT_GRAY);
        setDoubleBuffered(true);
        timer.start();
        //lida com a parte da comunicação cliente-servidor
        cliente = new Cliente(id);
        cliente.start();
        Verificador verifica = new Verificador();
        verifica.start();
    }
    //estes dois método abaixo são os responsáveis por mostrar os objetos na tela
    //Eu peguei ambos da internet, então não sou capaz de opinar
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Desenha(g);
        Toolkit.getDefaultToolkit().sync();
    }
    private void Desenha(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(!conectado){
            //renderiza o fundo desabilitado
            g2d.drawImage(fundoD,0,0,this);
        }else{
            //renderiza o fundo normal
            g2d.drawImage(fundo,0,0,this);
            //renderiza o player1
            g2d.drawImage(player1.imagem, player1.x,player1.y, this);
            for (Object bull1ini : player1.tiros) {
                Tiro b1 = (Tiro) bull1ini;
                g2d.drawImage(b1.imagem, b1.x,b1.y, this);
            }
            //renderiza o player2
            g2d.drawImage(player2.imagem, player2.x,player2.y, this);
            for (Object bull2ini : player2.tiros) {
                Tiro b2 = (Tiro) bull2ini;
                g2d.drawImage(b2.imagem, b2.x,b2.y, this);
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        atualizaTiros();
        updateChar();
        repaint();
    }
    private void atualizaTiros() {
        //lê objeto por objeto
        for (int i = 0; i < player1.tiros.size(); i++) {
            //pega uma lista dos tiros
            tiro = (Tiro) player1.tiros.get(i);
            if((tiro.x > 700)||(tiro.x + tiro.largura < 0)) {
                //se está fora da tela é removido
                player1.tiros.remove(i);
            } else {
                //se não então pode seguir seu trajeto
                tiro.move(player1.id);
            }
            //verifica se o tiro está nos limites do personagem
            if((tiro.x + tiro.largura >= player2.x)&&(tiro.y > player2.y)&&(tiro.y + tiro.altura < player2.y + player2.altura)){
                //se tiver vida diminui, caso contrário perde
                if(player2.lp >= 1){
                    player2.lp -= 1;
                }else{
                    //só acaba uma vez
                    if(!finaliza){
                        finalizaP1();
                        finaliza = true;
                    }
                }
                //remove o tiro por fim
                player1.tiros.remove(i);
            }
        }
        //o mesmo se repete aqui, porém com o outro personagem
        for (int i = 0; i < player2.tiros.size(); i++) {
            tiro = (Tiro) player2.tiros.get(i);
            if ((tiro.x > 700)||(tiro.x + tiro.largura < 0)) {
                player2.tiros.remove(i);
            } else {
                tiro.move(player2.id);
            }
            if((tiro.x <= player1.x + player1.largura)&&(tiro.y > player1.y)&&(tiro.y + tiro.altura < player1.y + player1.altura)){
                if(player1.lp >= 1){
                    player1.lp -= 1;
                }else{
                    if(!finaliza){
                        finalizaP2();
                        finaliza = true;
                    }
                }
                player2.tiros.remove(i);
            }
        }
    }
    //atualiza a posição dos dois personagens e envia para o servidor
    //só manda para o servidor o que tiver sido mudado
    private void updateChar() {
        switch(id){
            case 1:
                player1.move();
                if(player1.velx != 0){
                    cliente.envia("x"+player1.x);
                }
                if(player1.vely != 0){
                    cliente.envia("y"+player1.y);
                }
                break;
            case 2:
                player2.move();
                if(player2.velx != 0){
                    cliente.envia("x"+player2.x);
                }
                if(player2.vely != 0){
                    cliente.envia("y"+player2.y);
                }   
                break;
        }
    }
    //finaliza a partida para o player1
    public void finalizaP1(){
        switch(id){
            case 1:
                valor = JOptionPane.showOptionDialog(this, "Você ganhou!", "Fim de Jogo!",JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE,null, opcoes,opcoes[0]);
                break;
            case 2:
                valor = JOptionPane.showOptionDialog(this, "Você perdeu!", "Fim de Jogo!",JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE,null, opcoes,opcoes[0]);
                break;
        }
        valor();
    }
    //finaliza a partida para o player2
    public void finalizaP2(){
        switch(id){
            case 1:
                valor = JOptionPane.showOptionDialog(this, "Você perdeu!", "Fim de Jogo!",JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE,null, opcoes,opcoes[0]);
                break;
            case 2:
                valor = JOptionPane.showOptionDialog(this, "Você ganhou!", "Fim de Jogo!",JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE,null, opcoes,opcoes[0]);
                break;
        }
        valor();
    }
    //implementa o que fazer baseado na escolha do jogador
    public void valor(){
        switch(valor){
            case 1:
                System.exit(0);
                break;
        }
    }
    public class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            switch(id){
                case 1:
                    player1.keyReleased(e);
                    break;
                case 2:
                    player2.keyReleased(e);
                    break;
            }
        }
        //provavelmente o que estava ocasionando a perda de informação é o fato disso ser override
        //mas eu não tenho certeza
        @Override
        public void keyPressed(KeyEvent e) {
            if(conectado){
                switch(id){
                    case 1:
                        //pega a tecla pressionada
                        player1.keyPressed(e);
                        //se for espaço então atira
                        if(e.getKeyCode() == KeyEvent.VK_SPACE){
                            player1.atira();
                            //spammar o comando pro servidor sanou o problema de perda de informação (de algum jeito)
                            for(int i = 0; i < 4; i++){
                                cliente.envia("t");
                            }
                        }
                        break;
                    case 2:
                        player2.keyPressed(e);
                        if(e.getKeyCode() == KeyEvent.VK_SPACE){
                            player2.atira();
                            for(int i = 0; i < 4; i++){
                                cliente.envia("t");
                            }
                        }
                        break;
                }
            }
        }
    }
    //classe interna que lida com o que chegar com o servidor
    public class Verificador extends Thread{
        @Override
        public void run(){
            while(true){
                //o primeiro caractere da string se refere ao tipo de informação que chegou
                try{
                    switch(cliente.entrada.charAt(0)){
                        case 'x':
                            //remove o primeiro caracter pois o resto é integer
                            cliente.entrada = cliente.entrada.substring(1);
                            //verifica qual é o jogador que deve ser mudado
                            switch(id){
                                case 1:
                                    player2.x = Integer.parseInt(cliente.entrada);
                                    break;
                                case 2:
                                    player1.x = Integer.parseInt(cliente.entrada);
                                    break;
                            }
                            break;
                        case 'y':
                            cliente.entrada = cliente.entrada.substring(1);
                            switch(id){
                                case 1:
                                    player2.y = Integer.parseInt(cliente.entrada);
                                    break;
                                case 2:
                                    player1.y = Integer.parseInt(cliente.entrada);
                                    break;
                            }
                            break;
                        //a ideia aqui é enviar para o outro cliente o comando para atirar, para diminuir o trafego pelo servidor
                        case 't':
                            //remove o primeiro caracter para não ficar num loop
                            cliente.entrada = cliente.entrada.substring(1);
                            switch(id){
                                case 1:
                                    player2.atira();
                                    break;
                                case 2:
                                    player1.atira();
                                    break;
                            }
                            break;
                        case 'c':
                            cliente.entrada = cliente.entrada.substring(1);
                            conectado = true;
                    }
                //exceção de quando a string fica vazia
                }catch(java.lang.StringIndexOutOfBoundsException io){
                    //valor arbitrário qualquer que não caia no switch
                    cliente.entrada = "NDA";
                }
            }
        }
    }
}