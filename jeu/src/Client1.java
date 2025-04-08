import java.io.*;
import java.net.Socket;

public class Client1 {

    public static void main(String[] args) {

        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        final Plateau plateau = new Plateau();

        try {
            BufferedReader connection = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Entrez l'adresse IP du serveur (ex: localhost ou 127.0.0.1) : ");
            String ip = connection.readLine().trim();

            MyClient = new Socket(ip, 8888);

            input = new BufferedInputStream(MyClient.getInputStream());
            output = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                char cmd = 0;
                cmd = (char)input.read();
                //System.out.println(cmd);

                // Debut de la partie en joueur Rouge
                if(cmd == '1'){
                    //System.out.println("Nouvelle partie! Vous jouer Rouge, entrez votre premier coup : ");
                    byte[] aBuffer = new byte[1024];
                    int size = input.available();
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer).trim();
                    plateau.setPlayers(cmd);

                    String move = plateau.getNextMove(""); // ICI PROBLEME 
                    //System.out.println("Le move jouer est : " + move);
                    
                    plateau.play(move, plateau.getPlayers().getCurrent());
                    //plateau.printboard();

                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }

                // Debut de la partie en joueur Noir
               if(cmd == '2'){
                    //System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des rouges");
                    byte[] aBuffer = new byte[1024];
                    int size = input.available();
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer).trim();
                    plateau.setPlayers(cmd);
                    //plateau.printboard();
                }

                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joue.
                if(cmd == '3'){
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer).trim();
                    //System.out.println("Dernier coup reçu du serveur : " + s + " (length: " + s.length() + ")");
                    plateau.play(s.replaceAll("\\s", ""),plateau.getPlayers().getOpponent());
                    //plateau.printboard();
                    
                    String move = plateau.getNextMove(s);;
                    //System.out.println("-------------Le best Move est: "+move);
                    plateau.play(move, plateau.getPlayers().getCurrent());
                    //plateau.printboard();

                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                    
                }

                // Le dernier coup est invalide
                if(cmd == '4'){
                    //System.out.println("Coup invalide, entrez un nouveau coup : ");
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
                // La partie est terminée
                if(cmd == '5'){
                    byte[] aBuffer = new byte[16];
                    int size = input.available();
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer);
                    System.out.println("👋 Fermeture du programme...");
                    try {
                        Thread.sleep(2500); // att 2.5sec
                    } catch (InterruptedException e) {
                     e.printStackTrace();
                    }

                    input.close();
                    output.close();
                    MyClient.close();
                    System.exit(0);
                }

            }
        }catch (IOException e) {
            System.out.println(e);
        }
    }
}
