import java.util.*;;

public class Plateau {
    private String[][] boardGlobal; // Le gros tableau (voir figure #4 par exemple ou ya le gros x)
    private String[][][] boardLocal; // Ca c'est les petits tableau si vous regarder dans figure #1 #2 #3
    private ArrayList<String> availableMoves = new ArrayList<>(); // Les moves sont stockes en String "000" et non "A1"
    /**
     * des qu'un plateau local est gagné,on met celui qui a gagné(X or O) a l'indice
     * correspondant a l'indice du tableau local
     * exple si X gagne le plateau 3 on met X dans l'indice 3 du tableau
     * indiceLocalBoardComplete
     */
    private String[] indiceLocalBoardComplete = new String[9];
    private Player player;

    public Plateau() {
        player = new Player('1'); // set le player...
        boardGlobal = new String[3][3];
        boardLocal = new String[9][3][3]; // 9 tableau de 3x3

        // Initialize boardGlobal with empty strings
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardGlobal[i][j] = "-";
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    boardLocal[i][row][col] = "-"; // 0 = case vide
                    availableMoves.add(Integer.toString(i) + Integer.toString(row) + Integer.toString(col));
                }
            }
        }
    }

    // Sert a convertir A9 en 000
    public String moveConvertInt(String move) {
        if (move.length() < 2) {
            System.out.println("ERREUR: moveConvertInt() a reçu un move invalide: " + move);
            return "-1";
        }
        move = move.trim().toUpperCase(); // S'assurer qu'il n'y a pas d'espace et que c'est en majuscule
        char lettreCol = move.charAt(0);
        int nbRow = Character.getNumericValue(move.charAt(1));

        if (nbRow < 1 || nbRow > 9) {
            System.out.println("ERREUR: nbRow invalide = " + nbRow);
            return "-1";
        }

        int col = lettreCol - 'A';
        int row = 9 - nbRow;

        if (col < 0 || col > 8 || row < 0 || row > 8) {
            System.out.println("ERREUR: Colonne ou Ligne invalide");
            return "-1";
        }

        int boardIndex = (row / 3) * 3 + (col / 3);
        int localRow = row % 3;
        int localCol = col % 3;

        return Integer.toString(boardIndex) + Integer.toString(localRow) + Integer.toString(localCol);
    }

    // Sert a convertir 000 en A9
    public String intConvertMove(int boardIndex, int row, int col) {
        int globalCol = (boardIndex % 3) * 3 + col;
        int globalRow = (boardIndex / 3) * 3 + row;

        char colLetter = (char) ('A' + globalCol);
        int rowNumber = 9 - globalRow;
        return Character.toString(colLetter) + Integer.toString(rowNumber);
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
                String convertedMove = moveConvertInt(move);
                int boardIndex = Character.getNumericValue(convertedMove.charAt(0));
                if (indiceLocalBoardComplete[boardIndex] == null && availableMoves.contains(convertedMove)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getNextMove(String lastMove) {
        int bestValue = Integer.MIN_VALUE;
        String bestMove = null;
        char currentPlayer = player.getCurrent();
        char opponent = player.getOppenent();
        ArrayList<String> moveDispo = generateMove(lastMove);

        for (String move : moveDispo) {
            play(move); // Simuler le move
            int moveValue = MiniMaxAb(3, false, currentPlayer, opponent, Integer.MIN_VALUE, Integer.MAX_VALUE);
            undoMove(move); // Undo move apres (essai erreur)

            if (moveValue > bestValue) {
                bestMove = move;
                bestValue = moveValue;
            }
        }
        return bestMove;
    }

    private int MiniMaxAb(int depth, boolean isMaximising, char player, char opponent, int alpha, int beta) {
        int boardValue = evaluateForMinMax();
        if (Math.abs(boardValue) == 100 || availableMoves.isEmpty() || depth == 0) {
            return boardValue;
        }
        if (isMaximising) { // Tour du AI
            int maxEval = Integer.MIN_VALUE;
            for (String move : availableMoves) {
                if (!isLegalMove(move))
                    continue; // Skip les moves invalid
                play(move);
                int eval = MiniMaxAb(depth - 1, false, player, opponent, alpha, beta);
                undoMove(move);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                    break; // L'elagage
            }
            return maxEval;
        } else { // Tour de l'enemi
            int minEval = Integer.MAX_VALUE;
            for (String move : availableMoves) {
                if (!isLegalMove(move))
                    continue; // Skip les moves invalid
                play(move);
                int eval = MiniMaxAb(depth - 1, true, player, opponent, alpha, beta);
                undoMove(move);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha)
                    break; // L'elagage
            }
            return minEval;
        }
    }

    public void undoMove(String move) {
        String tab = moveConvertInt(move);
        if (tab.equals("-1")) {
            System.out.println("ERREUR: undoMove() a reçu un move invalide: " + move);
            return;
        }

        char[] tab1 = tab.toCharArray();
        int boardIndex = Character.getNumericValue(tab1[0]);
        int row = Character.getNumericValue(tab1[1]);
        int col = Character.getNumericValue(tab1[2]);

        boardLocal[boardIndex][row][col] = "-";
        availableMoves.add(tab);
    }

    private int evaluateForMinMax() {
        // Check rows for victory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardGlobal[i][j].equals("X")) {
                    return 100; // AI wins
                } else if (boardGlobal[i][j].equals("O")) {
                    return -100; // Opponent wins
                }
            }
        }

        // Check columns for victory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardGlobal[j][i].equals("X")) {
                    return 100; // AI wins
                } else if (boardGlobal[j][i].equals("O")) {
                    return -100; // Opponent wins
                }
            }
        }

        // Check diagonals for victory
        if (boardGlobal[0][0].equals("X") && boardGlobal[1][1].equals("X") && boardGlobal[2][2].equals("X")) {
            return 100; // AI wins
        } else if (boardGlobal[0][0].equals("O") && boardGlobal[1][1].equals("O") && boardGlobal[2][2].equals("O")) {
            return -100; // Opponent wins
        }

        if (boardGlobal[0][2].equals("X") && boardGlobal[1][1].equals("X") && boardGlobal[2][0].equals("X")) {
            return 100; // AI wins
        } else if (boardGlobal[0][2].equals("O") && boardGlobal[1][1].equals("O") && boardGlobal[2][0].equals("O")) {
            return -100; // Opponent wins
        }

        // If no one has won, return 0
        return 0;
    }

    public void play(String move) {
        String tab = moveConvertInt(move);
        if (tab.equals("-1")) {
            System.out.println("ERREUR: play() a reçu un move invalide: " + move);
            return;
        }
        char[] tab1 = tab.toCharArray();
        int boardIndex = Character.getNumericValue(tab1[0]);
        int row = Character.getNumericValue(tab1[1]);
        int col = Character.getNumericValue(tab1[2]);

        boardLocal[boardIndex][row][col] = String.valueOf(player.getCurrent());
        availableMoves.remove(tab);
    }

    public ArrayList<String> generateMove(String move) {
        if (move.isEmpty()) {
            return this.availableMoves;
        } else {
            int tableLocal = returnGlobalCase(move);

            if (indiceLocalBoardComplete[tableLocal] != null) {
                // Senser verifier si le local board est complketer
                // CA ICI CA SEMBLE PAS MARCHER
                ArrayList<String> validMoves = new ArrayList<>();
                for (String availableMove : availableMoves) {
                    int boardIndex = Character.getNumericValue(availableMove.charAt(0));
                    if (indiceLocalBoardComplete[boardIndex] == null) {
                        validMoves.add(availableMove);
                    }
                }
                return validMoves;
            } else {
                ArrayList<String> tabMoveAvailable = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (boardLocal[tableLocal][i][j].equals("-")) {
                            tabMoveAvailable.add(intConvertMove(tableLocal, i, j));
                        }
                    }
                }
                return tabMoveAvailable;
            }
        }
    }

    /**
     * returne un nombre de 0 a 8 qui equivaut a la table local ou jouer le coup
     * 
     * @param move
     * @return
     */
    public int returnGlobalCase(String move) {
        String tab = moveConvertInt(move);

        char[] tab1 = tab.toCharArray();
        int rowIndex = Character.getNumericValue(tab1[1]);
        int colIndex = Character.getNumericValue(tab1[2]);

        int boardIndex = (rowIndex * 3) + colIndex; // Fix calcul

        return boardIndex;
    }

    public void setPlayers(char c) {
        this.player = new Player(c);
    }

    public void printBoard() {
        for (int row = 0; row < 9; row++) {
            if (row % 3 == 0) {
                System.out.println("  -----------------------");
            }
            System.out.print((9 - row) + " "); // Ca print le 1 - 2- ... 9 a gauche ca
            for (int col = 0; col < 9; col++) {
                if (col % 3 == 0) {
                    System.out.print("| ");
                }
                int boardIndex = (row / 3) * 3 + (col / 3);
                int localRow = row % 3;
                int localCol = col % 3;
                System.out.print(boardLocal[boardIndex][localRow][localCol] + " ");
            }
            System.out.println("|");
        }
        System.out.println("  -----------------------");
        System.out.println("    A B C   D E F   G H I");
    }
}