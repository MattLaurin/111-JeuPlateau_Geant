import java.util.*;

public class Tester {
    public static void main(String[] args) {

        

        Scanner scanner = new Scanner(System.in);
        Plateau plateau= new Plateau();
        Player joueur = new Player('1');
        MiniMax miniMax = new MiniMax(joueur);


        plateau.printBoard();

        ArrayList<String> moveAvailable  = Algo.generateMove("E8", plateau, plateau.returnGlobalCase("E8"));
        
        System.out.println("Forced board index : " + plateau.returnGlobalCase("E8"));
        System.out.println("Evaluating possible moves:" + moveAvailable);

        plateau.play("E8", joueur.getCurrent());
        plateau.printBoard();
        
        plateau.play(miniMax.getNextMove("E8", plateau), joueur.getOpponent());
        plateau.printBoard();

        plateau.play("H8", joueur.getCurrent());
        plateau.printBoard();

        plateau.play(miniMax.getNextMove("H8", plateau), joueur.getOpponent());
        plateau.printBoard();

        plateau.play("B8", joueur.getCurrent());
        plateau.printBoard();

        plateau.play(miniMax.getNextMove("B8", plateau), joueur.getOpponent());
        plateau.printBoard();

        System.out.println("Won boards :" + plateau.getWonLocalBoards());




        
        int i = 1;
        
        while (i != 2){
            System.out.println("Avez-vous fini de lire ?");
            String message = scanner.nextLine();
            if (message.toLowerCase().equals("oui") || message.toLowerCase().equals("yes")){
                i = 2;
            }
        }

        /*int nbTurns = 0;
        char playerTurn;

        while (!plateau.checkForGlobalWin()) {
            if (nbTurns % 2 == 0) {
                playerTurn = joueur.getCurrent();
            } else {
                playerTurn = joueur.getOppenent();
            }

            plateau.printBoard();
            
            plateau.playMove(playerTurn, scanner);

            plateau.checkForLocalWin();

            nbTurns++;
        }
        
        scanner.close();*/
    }
}