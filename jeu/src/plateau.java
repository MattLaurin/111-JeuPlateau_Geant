import java.util.*;

public class plateau {
    private String[][] boardGlobal; // Le gros tableau (voir figure #4 par exemple ou ya le gros x)
    private String[][][] boardLocal; // Ca c'est les petits tableau si vous regarder dans figure #1 #2 #3 
    private boolean[] boardGagné; // Contient les boards qui ont été remporté par un joueur

    private String lastMove; 

    public plateau(){
        boardGlobal = new String[3][3]; 
        boardLocal = new String[9][3][3]; // 9 tableau de 3x3
        boardGagné = new boolean[9]; // Garde l'indice tableau gagné

        for (int i = 0; i<9; i++){
            boardGagné[i] = false; // tous les tableau "Draw" jsp si vrm necessaire
            for (int row=0; row<3; row++){    
                for (int col=0; col<3; col++){
                    boardLocal[i][row][col] = "-"; // 0 = case vide
                }
            }
        }
    }

    // Ca sert a convertir A8 par exemple en (Quel board jouer, la rangée et la colonne) donc ici ex : (1, 0, 7)
    public int[] moveConvertInt(String move){
        char lettreCol = move.charAt(0);
        int nbRow = Character.getNumericValue(move.charAt(1));

        int col = lettreCol - 'A'; // A-I  à 0-8
        int row = 9 - nbRow; // 1 - 9 --> 0 - 8 

        int boardIndex = (row / 3 ) *3 + (col / 3); // 0-8
        int localRow = row % 3; // 0-2
        int localCol = col % 3; // 0-2
        System.out.println(boardIndex);
        return new int[]{boardIndex, localRow, localCol};
    }

    private String intConvertMove(int boardIndex, int row, int col){
        int globalCol = (boardIndex % 3) * 3 + col;
        int globalRow = (boardIndex / 3) * 3 + row;

        char colLetter = (char) ('A' + globalCol);
        int rowNumber = globalRow + 1;
        return "" + colLetter + rowNumber; // Va transfer 1,0,7 en A8
    }

    // Faire ici validMove, faire move, getPossible move, etc.


    public void printBoard(){
        System.out.println("  -----------------------");

        for (int globalRow = 8; globalRow >= 0; globalRow--) {  
            if (globalRow % 3 == 2 && globalRow != 8) {
                System.out.println("  -----------------------");
            }
            System.out.print((globalRow + 1) + " ");
            for (int globalCol = 0; globalCol < 9; globalCol++) {
                if (globalCol % 3 == 0) System.out.print("| ");
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
