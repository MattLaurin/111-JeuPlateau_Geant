import java.util.*;;

public class Plateau {
    private String[][] boardGlobal; // Le gros tableau (voir figure #4 par exemple ou ya le gros x)
    private String[][][] boardLocal; // Ca c'est les petits tableau si vous regarder dans figure #1 #2 #3
    private ArrayList<String> availableMoves = new ArrayList<>(); // Les moves sont stockes en String "abc"
                                                                  // a = numero du board, b = numero de la row et c =
                                                                  // numero de la col
    private ArrayList<Integer> availableLocalBoards = new ArrayList<>(); // Les valeurs vont de 0 a 8

    public Plateau() {
        boardGlobal = new String[3][3];
        boardLocal = new String[9][3][3]; // 9 tableau de 3x3

        for (int i = 0; i < 9; i++) {
            availableLocalBoards.add(i); // Ajoute chaque boards locaux dans la liste
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    boardLocal[i][row][col] = "-"; // 0 = case vide
                    availableMoves.add(Integer.toString(i) + Integer.toString(row) + Integer.toString(col)); // Ajoute
                                                                                                             // chaque
                                                                                                             // cases
                                                                                                             // dans les
                                                                                                             // moves
                                                                                                             // available
                }
            }
        }
    }

    // Ca sert a convertir A8 par 107 (Quel board jouer, la rangée et la colonne)
    private String moveConvertInt(String move) {
        char lettre = move.charAt(0);
        char chiffre = move.charAt(1);

        int chiffreInt = Character.getNumericValue(chiffre);
        int col = lettre - 'A';
        int row = chiffreInt - 1;

        int boardIndex = (col / 3) * 3 + (row / 3);
        int localRow = row % 3;
        int localCol = col % 3;

        return Integer.toString(boardIndex) + Integer.toString(localRow) + Integer.toString(localCol);
    }

    private String intConvertMove(int boardIndex, int row, int col) {
        int globalCol = (boardIndex % 3) * 3 + col;
        int globalRow = (boardIndex / 3) * 3 + row;

        char colLetter = (char) ('A' + globalCol);
        int rowNumber = globalRow + 1;
        return Integer.toString(colLetter) + Integer.toString(rowNumber); // Va transfer 1,0,7 en A8
    }

    // Demande au joueur de faire son move et le retourne
    private String askForMove(char player, Scanner scanner) {
        System.out.println();
        System.out.print("Joueur (" + player + ") faites votre move : ");
        return scanner.nextLine();
    }

    // Retourne true si le move est legal
    private boolean isLegalMove(String move) {
        if (move.length() != 2) {
            return false;
        }

        char lettre = move.charAt(0);
        char chiffre = move.charAt(1);

        // Vérifie si la lettre est entre A et I et le chiffre entre 1 et 9
        if (lettre >= 'A' && lettre <= 'I' && Character.isDigit(chiffre)) {
            int chiffreInt = Character.getNumericValue(chiffre);
            if (chiffreInt >= 1 && chiffreInt <= 9) {
                // Vérifie si le mouvement est dans la liste des mouvements disponibles
                return availableMoves.contains(moveConvertInt(move));
            }
        }

        return false;
    }

    // Fonction pour jouer un move
    public void playMove(char c, Scanner scanner) {
        String move = askForMove(c, scanner);

        while (!isLegalMove(move)) {
            System.out.println("MOVE ILLEGAL : " + move);
            move = askForMove(c, scanner);
        }
        availableMoves.remove(moveConvertInt(move));

        String convertedMove = moveConvertInt(move);
        System.out.println(
                "MOVE : " + convertedMove.charAt(0) + " " + convertedMove.charAt(1) + " " + convertedMove.charAt(2));
        boardLocal[convertedMove.charAt(0) - '0'][convertedMove.charAt(1) - '0'][convertedMove.charAt(2)
                - '0'] = Character.toString(c);
        System.out.println();
    }

    // Dessine un X ou O dans une partie locale lorsqu'elle est gagnee
    public void updateLocalBoardOnWin(int boardIndex, char player) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                boardLocal[boardIndex][row][col] = Character.toString(player);
            }
        }

    }

    // Retourne vrai si un joueur a gagne une partie locale
    public boolean checkForLocalWin() {
        for (int i = 0; i < 9; i++) {
            if (availableLocalBoards.contains(i)) {
            }
        }
        return false;
    }

    // Retourne vrai si un joeur a gagne la partie globale
    public boolean checkForGlobalWin() {
        return false;
    }

    public void printBoard() {
        System.out.println("  -----------------------");

        for (int globalRow = 8; globalRow >= 0; globalRow--) {
            if (globalRow % 3 == 2 && globalRow != 8) {
                System.out.println("  -----------------------");
            }
            System.out.print((globalRow + 1) + " ");
            for (int globalCol = 0; globalCol < 9; globalCol++) {
                if (globalCol % 3 == 0)
                    System.out.print("| ");
                int boardIndex = (globalCol / 3) * 3 + (globalRow / 3);
                int row = globalRow % 3;
                int col = globalCol % 3;
                System.out.print(boardLocal[boardIndex][row][col] + " ");
            }
            System.out.println("|");
        }
        System.out.println("  -----------------------");
        System.out.println("    A B C   D E F   G H I");
    }

}