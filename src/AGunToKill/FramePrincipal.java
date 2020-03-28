package AGunToKill;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
//classe principal, que controla tudo
public class FramePrincipal extends JFrame implements ActionListener{
    Servidor server;
    FramePrincipal frame;
    Tela tela;
    //contrutor da classe
    public FramePrincipal() {
        initComponents();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BotaoServer = new javax.swing.JButton();
        BotaoCliente = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("A Gun To Kill");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        BotaoServer.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        BotaoServer.setText("Criar Jogo");
        BotaoServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotaoServerActionPerformed(evt);
            }
        });

        BotaoCliente.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        BotaoCliente.setText("Conectar-se");
        BotaoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotaoClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(314, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BotaoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BotaoServer, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(315, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(150, Short.MAX_VALUE)
                .addComponent(BotaoServer, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                .addComponent(BotaoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(150, Short.MAX_VALUE))
        );

        BotaoCliente.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //método ativado ao se clicar o botão que cria a partida
    private void BotaoServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoServerActionPerformed
        //comando necessário para criar uma nota tela sobreposta
        BotaoServer.addActionListener((ActionListener) this);
        //novo objeto servidor
        server = new Servidor();
        //instancia o servidor
        server.start();
        //inicia o jogo para o jogador 1
        novaTela(1);
        //desabilita os botões, para que não interfiram na nova tela
        BotaoServer.setEnabled(false);
        BotaoCliente.setEnabled(false);
    }//GEN-LAST:event_BotaoServerActionPerformed
    //método ativado ao clicar o botão que se conecta a partida
    private void BotaoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoClienteActionPerformed
        //inicia o jogo para o jogador 2
        novaTela(2);
        //desabilita os botões, para que não interfiram na nova tela
        BotaoServer.setEnabled(false);
        BotaoCliente.setEnabled(false);
    }//GEN-LAST:event_BotaoClienteActionPerformed
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        novoMenu();
    }
    // esse método vai criar a tela para o usuário com os devidos parâmetros
    public void novaTela(int id){
        //cria a tela
        tela = new Tela(id);
        add(tela);     
        setSize(700, 500);
        setResizable(false);     
        setTitle("A Gun To Kill");
        setLocationRelativeTo(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //cria o verificador do final da partida
        Verificador verifica = new Verificador();
        verifica.start();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotaoCliente;
    private javax.swing.JButton BotaoServer;
    // End of variables declaration//GEN-END:variables
    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Ainda não suportado"); //To change body of generated methods, choose Tools | Templates.
    }
    public static void novoMenu(){
        //expressão lambda (do java 8) que o netbeans criou automaticamente
        java.awt.EventQueue.invokeLater(() -> {
            FramePrincipal frame = new FramePrincipal();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            //seta o ícone (peguei o código no StackOverflow)
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage("icone.png"));
        });
    }
    //classe interna que verifica se o usuário voltou para o menu do jogo
    public class Verificador extends Thread{
        @Override
        public void run(){
            while(true){
                System.out.print("");
                if(tela.valor == 0){
                    tela.valor = 5;
                    switch(tela.id){
                        case 1:
                            server.fecha();
                            break;
                    }
                    remove(tela);
                    novoMenu();
                }
            }
        }
    }
}