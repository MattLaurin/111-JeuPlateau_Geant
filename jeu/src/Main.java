import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Plateau plateau= new Plateau();
        Player joueur = new Player('1');

        plateau.play("A1");
        /*System.out.println("Move A1 en int :" + plateau.moveConvertInt("A1"));
        System.out.println("Move 0,0,0 en String :" + plateau.intConvertMove(0,0,0));
        System.out.println("Move 6,2,0 en String :" + plateau.intConvertMove(6,2,0));

        System.out.println("Move I1 en int :" + plateau.moveConvertInt("I1"));
        System.out.println("Move 8,2,2 en String :" + plateau.intConvertMove(8,2,2));*/

        plateau.play("B1");
        plateau.play("I1");
        plateau.generateMove("B1");
    

        plateau.printBoard();
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