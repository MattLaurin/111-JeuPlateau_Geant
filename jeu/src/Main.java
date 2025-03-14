public class Main {
    public static void main(String[] args) {
        Plateau plateau= new Plateau();
        Player joueur = new Player('1');

        plateau.play("A9");

        plateau.play("B1");
        plateau.play("I1");
        plateau.generateMove("B1");

        plateau.printBoard();
    }
}