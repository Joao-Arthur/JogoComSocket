package AGunToKill;
import java.awt.Image;
import javax.swing.ImageIcon;
//essa é a superclasse do personagem e do tiro
public class Sprite {
    //variáveis referentes ao objeto sprite
    public int x;
    public int y;
    public int largura;
    public int altura;
    public Image imagem;
    //construtor que se auto-referencia
    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
    }
    //método que carrega a imagem para a tela e pega suas dimensões
    public void carregaImg(String local) {
        ImageIcon ii = new ImageIcon(local);
        imagem = ii.getImage();
        largura = imagem.getWidth(null);
        altura = imagem.getHeight(null);
    }
}