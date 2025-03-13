import java.util.*;;

public class Plateau {
    private String[][] boardGlobal; // Le gros tableau (voir figure #4 par exemple ou ya le gros x)
    private String[][][] boardLocal; // Ca c'est les petits tableau si vous regarder dans figure #1 #2 #3
    private ArrayList<String> availableMoves = new ArrayList<>(); // Les moves sont stockes en String "abc"
                                                                  // a = numero du board, b = numero de la row et c =
                                                                  // numero de la col
    private ArrayList<Integer> availableLocalBoards = new ArrayList<>(); // Les valeurs vont de 0 a 8
    private Player player;

    public Plateau() {
        player = new Player('1'); // set le player...
        boardGlobal = new String[3][3];
        boardLocal = new String[9][3][3]; // 9 tableau de 3x3

        for (int i = 0; i < 9; i++) {
            availableLocalBoards.add(i); // Ajoute chaque boards locaux dans la liste
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    boardLocal[i][row][col] = "-"; // 0 = case vide
                    availableMoves.add(Integer.toString(i) + Integer.toString(row) + Integer.toString(col)); // Ajoute chaque case dans les moves available
                }
            }
        }
    }

    // Ca sert a convertir A8 par 107 (Quel board jouer, la rangée et la colonne)
    public String moveConvertInt(String move) {
        char lettreCol = move.charAt(0);
        int nbRow = Character.getNumericValue(move.charAt(1));
    
        int col = lettreCol - 'A'; // A-I  à 0-8
        int row = 9 - nbRow; // 1 - 9 --> 0 - 8 
    
        int boardIndex = (row / 3) * 3 + (col / 3); // Ajustement pour savoir quel board jouer 
        int localRow = row % 3; // 0-2
        int localCol = col % 3; // 0-2
    
        return Integer.toString(boardIndex) + Integer.toString(localRow) + Integer.toString(localCol);
    }

    public String intConvertMove(int boardIndex, int row, int col) {
        int globalCol = (boardIndex % 3) * 3 + col;
        int globalRow = (boardIndex / 3) * 3 + row;

        char colLetter = (char) ('A' + globalCol);
        int rowNumber = 9 - globalRow;
        return Character.toString(colLetter) + Integer.toString(rowNumber); // Va transfer 1,0,7 en A8
    }
    public String intConvertMove2(){

        return null;
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
    public void play(String move){
       String tab= moveConvertInt(move);
       char[] tab1= tab.toCharArray();
        this.boardLocal[Character.getNumericValue( tab1[0])][Character.getNumericValue( tab1[1])][Character.getNumericValue( tab1[2])]=move;
    }
    public ArrayList<String> generateMove(String Move){
        int tableLocal=returnGlobalCase(Move);
        ArrayList<String> tabMoveAvailable = new ArrayList<String>();
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(boardLocal[tableLocal][i][j]=="-"){
                    tabMoveAvailable.add( intConvertMove(tableLocal,i,j));
                }
            }
        }
        return tabMoveAvailable;
    }

    /**
     * returne un nombre de 0 a 8 qui equivaut a la table local ou jouer le coup
     * @param move
     * @return
     */
    public int returnGlobalCase(String move){
        String tab= moveConvertInt(move);
        char[] tab1= tab.toCharArray();

        if(Character.getNumericValue(tab1[1])==0){
            return Character.getNumericValue(tab1[2])+6;
        }else if(Character.getNumericValue(tab1[1])==1){
            return Character.getNumericValue(tab1[2])+3;
        }else if(Character.getNumericValue(tab1[1])==2){
            return Character.getNumericValue(tab1[2])+1;
        }

        return -1;
    }
    public void setPlayers(char c){
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