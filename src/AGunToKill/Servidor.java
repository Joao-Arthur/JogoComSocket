package AGunToKill;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
public class Servidor extends Thread{
    //variáveis do socket
    ServerSocket servidor;
    Socket cliente1;
    Socket cliente2;
    MensagemC1 m1;
    MensagemC2 m2;
    Scanner entrada1;
    Scanner entrada2;
    PrintStream saida1;
    PrintStream saida2;
    boolean conectou;
    //variável usada para verificar se a partida terminou
    boolean fecha;
    @Override
    public void run(){
        criaServidor();
        //threads que vão receber e enviar de cada cliente
        m1 = new MensagemC1();
        m1.start();
        m2 = new MensagemC2();
        m2.start();
    }
    public void criaServidor(){
        try {
            //instancia as variáveis relativas ao socket
            servidor = new ServerSocket(54321);
            cliente1 = servidor.accept();
            cliente2 = servidor.accept();
            entrada1 = new Scanner(cliente1.getInputStream());
            saida1 = new PrintStream(cliente2.getOutputStream());
            entrada2 = new Scanner(cliente2.getInputStream());
            saida2 = new PrintStream(cliente1.getOutputStream());
        }catch(java.net.BindException be){
            //captura de quando o usuário tenta se conectar sem nenhum servidor ativo
            JOptionPane.showMessageDialog(null,"Provavelmente já há algum servidor rodando!", "Aviso!", 0, null);
            //para evitar algum erro de execução o programa fecha
            System.exit(0);
        }catch (IOException ex) {
            //captura das outras exceções
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //método que fecha as conexões do servidor
    public void fecha(){
        try {
            fecha = true;
            cliente1.close();
            cliente2.close();
            saida1.close();
            saida2.close();
            entrada1.close();
            entrada2.close();
            servidor.close();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    //thread que recebe a mensagem do cliente1 e envia para o cliente2
    public class MensagemC1 extends Thread{
        @Override
        public void run(){
            try{
                while(!fecha){
                    entrada1 = new Scanner(cliente1.getInputStream());
                    saida1 = new PrintStream(cliente2.getOutputStream());
                    if((entrada1.nextLine() != null)&&(!entrada1.nextLine().equals(""))){
                        saida1.println(entrada1.nextLine());
                    }
                }
            }catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //thread que recebe a mensagem do cliente2 e envia para o cliente1
    public class MensagemC2 extends Thread{
        @Override
        public void run(){
            try{
                saida1.println("c");
                saida2.println("c");
                while(!fecha){
                    entrada2 = new Scanner(cliente2.getInputStream());
                    saida2 = new PrintStream(cliente1.getOutputStream());
                    if((entrada2.nextLine() != null)&&(!entrada2.nextLine().equals(""))){
                        saida2.println(entrada2.nextLine());
                    }
                }
            }catch (java.util.NoSuchElementException ns){
                //captura de quando o player2 se desconecta
                if(!fecha){
                    JOptionPane.showMessageDialog(null,"Provavelmente o jogador abandonou a partida!", "Aviso!", 0, null);
                    fecha();
                }
                //para evitar algum erro de execução o programa fecha
                System.exit(0);
            }catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}