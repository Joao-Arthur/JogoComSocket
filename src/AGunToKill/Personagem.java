package AGunToKill;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
public class Personagem extends Sprite {
    //variáveis da velocidade
    public int velx;
    public int vely;
    //identificação do personagem
    public int id;
    //pontos de vida do personagem
    public int lp = 3;
    //arraylist que lista os tiros disparados pelo personagem
    public ArrayList tiros;
    //variável que recebe a tecla 
    public int key;
    //construtor da classe
    public Personagem(int x, int y, int id) {
        //passa a posição como parâmetro para a super-classe
        super(x, y);
        //passa como parâmetro para essa classe a variável id
        this.id = id;
        //carrega a imagem
        iniciaPersonagem();
    }
    private void iniciaPersonagem() {
        //instancia o arraylist
        tiros = new ArrayList();
        //cada personagem tem a sua própria "skin", ou seja, imagem
        switch(id){
            case 1:
                carregaImg("char_01.png");
                break;
            case 2:
                carregaImg("char_02.png");
                break;
        }
    }
    //método chamado para movimentar o personagem
    public void move() {
        //impede que os players saiam da tela da horizontal
        switch(id){
            case 1:
                while(x + velx < 0){
                    velx++;
                } 
                while(x + velx > 100){
                    velx--;
                }       
                break;
            case 2:
                while(x + largura + velx < 600){
                    velx++;
                }  
                while(x + largura + velx > 694){
                    velx--;
                }
                break;
        }
        //impede que os players saiam da tela na vertical
        while(y + vely < 0){
            vely++;
        }
        while(y + vely > 441){
            vely--;
        }       
        //por fim a posição recebe o novo valor, já tratado
        x += velx;
        y += vely;
    }
    //método que detecta a tecla pressionada
    public void keyPressed(KeyEvent e) {
        key = e.getKeyCode();
        switch(key){
            case KeyEvent.VK_LEFT: 
                velx = -1;
                break;
            case KeyEvent.VK_RIGHT:
                velx = 1;
                break;
            case KeyEvent.VK_UP:
                vely = -1;
                break;
            case KeyEvent.VK_DOWN:
                vely = 1;
                break;
        }
    }
    //método que é chamado quando o usuário solta a tecla
    public void keyReleased(KeyEvent e) {
        key = e.getKeyCode();
        //como há somente duas variáveis de velocidade, só precisa de duas instruções
        switch(key){
            case KeyEvent.VK_LEFT: 
            case KeyEvent.VK_RIGHT:
                velx = 0;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                vely = 0;
                break;
        }
    }
    //método que implementa o tiro do personagem
    public void atira() {
        //cada personagem tem uma posição inicial para o tiro
        switch(id){
            case 1:
                tiros.add(new Tiro(x + largura - 5, (y + altura / 2) - 1));
                break;
            case 2:
                tiros.add(new Tiro(x - 3, (y + altura / 2) - 1));
                break;
        }
    }
}