import java.util.ArrayList;
import java.util.List;

public class Algo {
    public static String moveConvertInt(String move) {
        char lettreCol = move.charAt(0);
        int nbRow = Character.getNumericValue(move.charAt(1));
    
        int col = lettreCol - 'A'; // A-I  à 0-8
        int row = 9 - nbRow; // 1 - 9 --> 0 - 8 
    
        int boardIndex = (row / 3) * 3 + (col / 3); // Ajustement pour savoir quel board jouer 
        int localRow = row % 3; // 0-2
        int localCol = col % 3; // 0-2
    
        return Integer.toString(boardIndex) + Integer.toString(localRow) + Integer.toString(localCol);
    }

    public static String intConvertMove(int boardIndex, int row, int col) {
        // Validate row and col values
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            return "Invalid move";
        }

        int globalCol = (boardIndex % 3) * 3 + col;
        int globalRow = (boardIndex / 3) * 3 + row;

        char colLetter = (char) ('A' + globalCol);
        int rowNumber = 9 - globalRow;
        return Character.toString(colLetter) + Integer.toString(rowNumber); // Va transfer 1,0,7 en A8
    }

    //--------Code Eval--------//

    public static int evaluateLocal(LocalBoard localBoard, Player player) {
        String[][] board = localBoard.getBoard();
        String currentPlayer = player.getCurrent();
        String opponent = player.getOpponent();
        int score = 0;
    
        // Un poids pour les cases, initialiser d'avance pour eviter de calculer a chaque fois.
        int[][] weights = {
            {3, 2, 3},
            {2, 4, 2},
            {3, 2, 3}
        };
    
        // Evaluate rows, columns, and diagonals
        int playerDiag1 = 0, opponentDiag1 = 0;
        int playerDiag2 = 0, opponentDiag2 = 0;
    
        for (int i = 0; i < 3; i++) {
            int playerRow = 0, opponentRow = 0;
            int playerCol = 0, opponentCol = 0;
    
            for (int j = 0; j < 3; j++) {
                // Row evaluation
                if (board[i][j].equals(currentPlayer)) playerRow++;
                else if (board[i][j].equals(opponent)) opponentRow++;
    
                // Column evaluation
                if (board[j][i].equals(currentPlayer)) playerCol++;
                else if (board[j][i].equals(opponent)) opponentCol++;
    
                // Points pour 'chaque' case 
                if (board[i][j].equals(currentPlayer)) score += weights[i][j];
                else if (board[i][j].equals(opponent)) score -= weights[i][j];
            }
    
            // Add scores for rows and columns
            if (playerRow > 0 && opponentRow == 0) score += playerRow * 10;
            if (opponentRow > 0 && playerRow == 0) score -= opponentRow * 10;
            if (playerCol > 0 && opponentCol == 0) score += playerCol * 10;
            if (opponentCol > 0 && playerCol == 0) score -= opponentCol * 10;
    
            // Diagonal evaluation
            if (board[i][i].equals(currentPlayer)) playerDiag1++;
            else if (board[i][i].equals(opponent)) opponentDiag1++;
            if (board[i][2 - i].equals(currentPlayer)) playerDiag2++;
            else if (board[i][2 - i].equals(opponent)) opponentDiag2++;
        }
    
        // Add scores for diagonals
        if (playerDiag1 > 0 && opponentDiag1 == 0) score += playerDiag1 * 10;
        if (opponentDiag1 > 0 && playerDiag1 == 0) score -= opponentDiag1 * 10;
        if (playerDiag2 > 0 && opponentDiag2 == 0) score += playerDiag2 * 10;
        if (opponentDiag2 > 0 && playerDiag2 == 0) score -= opponentDiag2 * 10;
    
        return score;
    }



    public static int evaluateGlobal(Plateau plateau, Player player) {
        String currentPlayer = player.getCurrent();
        String opponent = player.getOpponent();
        int score = 0;
    
        int[] globalWeights = {3, 2, 3, 2, 5, 2, 3, 2, 3};
        String[] globalBoard = new String[9];
    
        for (int i = 0; i < 9; i++) {
            LocalBoard local = plateau.getLocalBoard(i);
            if (plateau.getWonLocalBoards().contains(i)) {
                globalBoard[i] = local.getWinner();
            } else {
                globalBoard[i] = "-";
    
                // Call localEval pour balancer le choix
                int localEval = evaluateLocal(local, player);
                score += localEval * globalWeights[i]; // Poids en fonction de la position
            }
        }
    
        // Sert a evaluer les lignes 
        int[][] lines = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };
    
        for (int[] line : lines) {
            int playerCount = 0, opponentCount = 0; // Compte le nb de win etc. (Aka l'euristique)
    
            for (int idx : line) {
                if (globalBoard[idx].equals(currentPlayer)) playerCount++;
                else if (globalBoard[idx].equals(opponent)) opponentCount++;
            }
    
            if (playerCount == 3) return 100000;
            if (opponentCount == 3 ) return -100000;
    
            if (playerCount > 0 && opponentCount == 0) score += playerCount * 100;
            if (opponentCount > 0 && playerCount == 0) score -= opponentCount * 120;
            
        }
    
        return score;
    }


    //--------Appel a l'algo pour le jeu--------//

    public static ArrayList<String> generateMove(String move, Plateau plateau, int forcedBoardIndex) {
    ArrayList<String> availableMoves = new ArrayList<>();
    List<Integer> wonBoards = plateau.getWonLocalBoards();

    
    if (forcedBoardIndex != -1 && !wonBoards.contains(forcedBoardIndex) && !plateau.getLocalBoard(forcedBoardIndex).isFull()) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (plateau.getLocalBoard(forcedBoardIndex).getCell(row, col).equals("-")) {
                    availableMoves.add(intConvertMove(forcedBoardIndex, row, col));
                }
            }
        }
    } else {
        for (int i = 0; i < 9; i++) {
            if (!wonBoards.contains(i) && !plateau.getLocalBoard(i).isFull()) {
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        if (plateau.getLocalBoard(i).getCell(row, col).equals("-")) {
                            availableMoves.add(intConvertMove(i, row, col));
                        }
                    }
                }
            }
        }
    }

    return availableMoves;
}

}