package AGunToKill;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
//essa é a classe a que pertencem os objetos clientes
public class Cliente extends Thread{   
    //id do jogador
    public int id;
    //variáveis relacionadas a thread
    InputStream servidor;
    Socket cliente;
    PrintStream saida;
    public String entrada = "y210";
    @Override
    //método que roda no start do thread
    public void run(){
        Recebe r = new Recebe();
        r.start();
    }
    public Cliente(int id){
        try {
            //tenta se conectar ao servidor e criar um canal para enviar informações
            cliente = new Socket("localhost", 54321);
            saida = new PrintStream(cliente.getOutputStream());
        } catch (java.net.ConnectException cx){
            //captura de quando o usuário tenta se conectar sem nenhum servidor ativo
            JOptionPane.showMessageDialog(null,"Provavelmente não há nenhum servidor rodando!", "Aviso!", 0, null);
            //para evitar algum erro de execução o programa fecha
            System.exit(0);
        } catch (IOException ex) {
            //captura das outras exceções
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.id = id;
    }
    //método que envia as mensagens para o servidor
    public void envia(String mensagem){
        saida.println(mensagem);
    }
    //classe thread que vai ficar "escutando" o que vier do servidor
    public class Recebe extends Thread{
        @Override
        public void run(){
            try {
                //inicializa as variáveis
                servidor = cliente.getInputStream();
                Scanner s = new Scanner(servidor);
                //enquanto ouver conexão vai ficar jogando a entrada para a variável
                while (s.hasNextLine()) {
                    entrada = s.nextLine();
                }
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}