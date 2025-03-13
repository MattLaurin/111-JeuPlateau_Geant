import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Plateau plateau= new Plateau();
        Player joueur = new Player('1');
        int nbTurns = 0;
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
        
        scanner.close();
    }
}