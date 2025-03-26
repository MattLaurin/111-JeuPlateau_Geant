import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Plateau plateau= new Plateau();
        Player joueur = new Player('2');

        plateau.play("H8", joueur.getOppenent());
        plateau.printBigBoard();
        plateau.play("D6", joueur.getCurrent());
        plateau.printBigBoard();
        plateau.play("B8", joueur.getOppenent());
        plateau.printBigBoard();
        plateau.play("E6", joueur.getOppenent());
        plateau.printBigBoard();
        plateau.play("E8", joueur.getOppenent());
        plateau.printBigBoard();
        plateau.play("F6", joueur.getCurrent());// 1 apres ceci
        plateau.printBigBoard();
        plateau.play("I7", joueur.getOppenent());
        plateau.printBigBoard();
        plateau.play("G3", joueur.getCurrent());
        plateau.printBigBoard();
        plateau.play("A7", joueur.getOppenent());
        plateau.printBigBoard();
        plateau.play("A3", joueur.getCurrent());
        plateau.printBigBoard();
        plateau.play("C8", joueur.getOppenent());
        plateau.printBigBoard();
        plateau.play("G6", joueur.getCurrent());
        plateau.printBigBoard();
        plateau.play("A8", joueur.getOppenent());//2 ici
        plateau.printBigBoard();
        plateau.play("A6", joueur.getCurrent());
        plateau.printBigBoard();
        plateau.play("D1", joueur.getOppenent());
        plateau.printBigBoard();
        plateau.play("B3", joueur.getCurrent());
        plateau.printBigBoard();
        plateau.play("D7", joueur.getOppenent());
        plateau.printBigBoard();
        plateau.play("C3", joueur.getCurrent());// 3 ici
        plateau.printBigBoard();
        plateau.play("G9", joueur.getOppenent()); // apres avoir joué ça 4 ici


        /*System.out.println("Move A1 en int :" + plateau.moveConvertInt("A1"));
        System.out.println("Move 0,0,0 en String :" + plateau.intConvertMove(0,0,0));
        System.out.println("Move 6,2,0 en String :" + plateau.intConvertMove(6,2,0));

        System.out.println("Move I1 en int :" + plateau.moveConvertInt("I1"));
        System.out.println("Move 8,2,2 en String :" + plateau.intConvertMove(8,2,2));*/

        /*plateau.play("D3","X");
        plateau.play("E3","X");
        plateau.play("F3","X");*/
        plateau.generateMove(" B8 ");
    

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