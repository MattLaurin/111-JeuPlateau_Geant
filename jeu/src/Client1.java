import java.io.*;
import java.net.Socket;

public class Client1 {

    public static void main(String[] args) {

        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        final Plateau plateau = new Plateau();

        try {
            System.out.println("Veillez entré l'adresse du serveur: ");

            MyClient = new Socket("localhost", 8888);

            input = new BufferedInputStream(MyClient.getInputStream());
            output = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                char cmd = 0;
                cmd = (char)input.read();
                System.out.println(cmd);

                // Debut de la partie en joueur blanc
                if(cmd == '1'){
                    System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
                    byte[] aBuffer = new byte[1024];
                    int size = input.available();
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer).trim();
                    plateau.setPlayers(cmd);
                    String move = plateau.getNextMove("");;
                    System.out.println("-------------je dois jouer et envoyer au serveur: "+move);
                    plateau.play(move, plateau.getPlayers().getCurrent());
                    plateau.printBoard();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }

                // Debut de la partie en joueur Noir
               if(cmd == '2'){
                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
                    byte[] aBuffer = new byte[1024];
                    int size = input.available();
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer).trim();
                    plateau.setPlayers(cmd);
                    plateau.printBoard();
                }

                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joue.
                if(cmd == '3'){
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer).trim();
                    System.out.println("Dernier coup :"+ s);
                    plateau.play(s.replaceAll("\\s", ""),plateau.getPlayers().getOppenent());
                    plateau.printBoard();
                    System.out.println("........................................... ");
                    String move = plateau.getNextMove(s);;
                    if(move.equals("A9")){
                        int i=0;
                    }
                    System.out.println("-------------Le best Move est: "+move);
                    plateau.play(move, plateau.getPlayers().getCurrent());
                    plateau.printBoard();

                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }

                // Le dernier coup est invalide
                if(cmd == '4'){
                    System.out.println("Coup invalide, entrez un nouveau coup : ");
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
                    System.out.println("Partie Terminé. Le dernier coup joué est: "+s);
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
            }
        }catch (IOException e) {
            System.out.println(e);
        }

    }
}
