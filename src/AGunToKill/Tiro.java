package AGunToKill;
public class Tiro extends Sprite {
    //construtor da classe, que instancia o tiro
    public Tiro(int x, int y) {
        //passa para a super-classe a posição como parâmetro
        super(x, y);  
        //abre o método que carrega a imagem
        iniciaTiro();
    }
    private void iniciaTiro() {  
        carregaImg("tiro.png");  
    }
    //esse é o método chamado pelos players para calcular a posição do tiro
    public void move(int id) {
        //dependendo qual jogador a bala tem uma posição a seguir
        switch(id){
            case 1:
                x += 5;  
                break;
            case 2:
                x -= 5;
                break;
        }
    }
}