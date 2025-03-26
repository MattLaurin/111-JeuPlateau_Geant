import java.util.*;

public class Plateau{
    //--------Constantes globales--------//
    private Player player; // joueur courant
    private HashMap<Integer, LocalBoard> globalBoard = new HashMap<>();
    private LocalBoard[] localBoards = new LocalBoard[9];
    private MiniMax miniMax;

    //--------Constantes des plateaux--------//
    private int nbPlateau = 9; 
    private int nbCol = 3;
    private int nbRow = 3;

    //--------Variables de parties--------//
    public Boolean GlobalIsWon = false; // Partie terminée 
    public List<Integer> wonLocalBoards = new ArrayList<>(); // LocalBoard gagnés

    //--------Var pour Eval--------//
    private int filledCellCount = 0;

     //--------Code Conditions de jeu--------//
     /**
      *  Plateau contient le main game, quand est gagner le globalboard,
      *  la fonction play, etc. Les algo sont dans algo.java, minimax est dans sa classe seul
      *  Puis tout ce qui se retrouve en rapport au localBoards est dans la classe.
      */
    
    public Plateau(){
        player = new Player('1');
        miniMax = new MiniMax(player);
        for (int i =0; i < nbPlateau; i++){
            localBoards[i] = new LocalBoard();
            globalBoard.put(i, new LocalBoard());
        }        
    }

    public void setPlayers(char c){
        this.player = new Player(c);
        this.miniMax = new MiniMax(player);
    }

    public Player getPlayers(){
        return this.player;
    }
    

    public boolean checkForGlobalWin(){

        // Verification des lignes et colonnes
        for (int i = 0; i < nbRow; i++){
            if (checkLocalBoardWin(globalBoard.get(i*3),globalBoard.get(i*3+1) , globalBoard.get(i*3+2))){ 
                GlobalIsWon = true;
                //Won grâce aux Rangées
            }
            if (checkLocalBoardWin(globalBoard.get(i),globalBoard.get(i + 3) , globalBoard.get(i + 6))){
                GlobalIsWon = true; 
                // Won grâces aux Colonnes
            }
        }
        
        // Verification des diagonales
        if (checkLocalBoardWin(globalBoard.get(0), globalBoard.get(4), globalBoard.get(8))) {
            GlobalIsWon = true;
            //Won grâce à la diagonale top left a bottom right
        }
        if (checkLocalBoardWin(globalBoard.get(2), globalBoard.get(4), globalBoard.get(6))) {
            GlobalIsWon = true;
            //Won grace à la diagonale top right a bottom left
        }
        return GlobalIsWon;

    }

    public boolean checkLocalBoardWin(LocalBoard b1, LocalBoard b2, LocalBoard b3){
        return b1.isWon() && b2.isWon() && b3.isWon(); 
    }

    public boolean isGameOver(){
        if (GlobalIsWon){
            return true;
        }
        for (int i=0; i<nbPlateau; i++){
            if (!globalBoard.get(i).isWon()){
                return false;
            }
        }
        return true;
    }
    
    public List<Integer> getWonLocalBoards() {
        return wonLocalBoards;
    }

    public LocalBoard getLocalBoard(int index) {
        return globalBoard.get(index);
    }

    

     //--------Lien avec Eval--------//

   public int returnGlobalCase(String move) {
    
    String tab = Algo.moveConvertInt(move);
    
    if (tab.length() != 3) {
        System.out.println("Invalid converted move: " + tab);
        return -1;
    }

    int boardIndex = Character.getNumericValue(tab.charAt(0));
    int localRow = Character.getNumericValue(tab.charAt(1));
    int localCol = Character.getNumericValue(tab.charAt(2));

    
    int targetBoard = (localRow * 3 + localCol);
   
    return targetBoard;
}

    public int getFilledCellNb(){ // Retourne le nb de "move" jouer
        return filledCellCount;
    }

    public void recalculateFilledCells(){
        filledCellCount = 0;
        for (int i =0; i<nbPlateau; i++){
            String[][] board = globalBoard.get(i).getBoard();
            for (int j = 0; j < nbRow; j++){
                for (int k = 0; k < nbCol; k++){
                    if (!board[j][k].equals("-")){
                        filledCellCount++;
                    }
                }
            }
        }
    }


    //--------Partie Jeu--------//

    public void play(String move, String player) {
        String convertedMove = Algo.moveConvertInt(move);
        int boardIndex = Character.getNumericValue(convertedMove.charAt(0));
        int row = Character.getNumericValue(convertedMove.charAt(1));
        int col = Character.getNumericValue(convertedMove.charAt(2));
        LocalBoard localBoard = globalBoard.get(boardIndex);
        localBoard.play(row, col, player);
        if (localBoard.isWon() && !wonLocalBoards.contains(boardIndex)) {
            wonLocalBoards.add(boardIndex);
        }
    }

    public void undo(String move) {
        String convertedMove = Algo.moveConvertInt(move);
        char[] tab1 = convertedMove.toCharArray();
        int boardIndex = Character.getNumericValue(tab1[0]);
        int row = Character.getNumericValue(tab1[1]);
        int col = Character.getNumericValue(tab1[2]);
        LocalBoard localBoard = globalBoard.get(boardIndex);
        localBoard.undo(row, col);

        // Reset the isWon status of the LocalBoard
        if (localBoard.isWon()) {
            localBoard.resetWinStatus();
        }

        // Remove the board from wonLocalBoards if necessary
        if (wonLocalBoards.contains(boardIndex)) {
            wonLocalBoards.remove(Integer.valueOf(boardIndex));
        }
    }

    public String getNextMove(String move){
        return miniMax.getNextMove(move,this);
    }


    //--------Testing Purposes (A delete avant remise ou whatever) --------//
    /*
    * Ici je met des methodes pour tester whatever ou des appels a Tester.java (anciennement main.java)
    * Ça sert vraiment a rien par rapport aux calculs, etc. (Aussi mettre les prints ici au pire)
    */ 

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
                System.out.print(globalBoard.get(boardIndex).getCell(localRow, localCol) + " ");
            }
            System.out.println("|");
        }
        System.out.println("  -----------------------");
        System.out.println("    A B C   D E F   G H I");
   }

}