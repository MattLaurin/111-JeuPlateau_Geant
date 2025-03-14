import java.util.*;;

public class Plateau {
    private String[][] boardGlobal; // Le gros tableau (voir figure #4 par exemple ou ya le gros x)
    private String[][][] boardLocal; // Ca c'est les petits tableau si vous regarder dans figure #1 #2 #3
    private ArrayList<String> availableMoves = new ArrayList<>(); // Les moves sont stockes en String "abc"
                                                                  // a = numero du board, b = numero de la row et c =
                                                                  // numero de la col
    private ArrayList<Integer> availableLocalBoards = new ArrayList<>(); // Les valeurs vont de 0 a 8
    private ArrayList<String> reserveMoveForUndo = new ArrayList<String>();
    /**
     *  des qu'un plateau local est gagné,on met celui qui a gagné(X or O) a l'indice correspondant a l'indice du tableau local
     *  exple si X gagne le plateau 3 on met X dans l'indice 3 du tableau indiceLocalBoardComplete
     */
    private String [] indiceLocalBoardComplete= new String[9];
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
                    availableMoves.add(intConvertMove(i,row,col)); // Ajoute chaque case dans les moves available
                }
            }
        }

        int i=0;
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

    public String getNextMove(String move){

        ArrayList<String> moveDispo=(ArrayList<String>) generateMove(move);
        int bestVal = Integer.MIN_VALUE;
        String bestMove="";
        for (String m:moveDispo){
            play(m, player.getCurrent());
            int moveVal=minimax(0,false,Integer.MIN_VALUE, Integer.MAX_VALUE);
            undo(m);
            if (moveVal > bestVal) {
                bestVal = moveVal;
                bestMove=m;
            }else{
                bestMove=moveDispo.get(0);
            }
        }

        if(bestMove.equals("")){
            int o=0;
        }

        //return moveDispo.get(0);
        return bestMove;

    }
    // Algorithme Minimax avec Élagage Alpha-Beta
    private int minimax(int depth, boolean isMax, int alpha, int beta) {
        int score = evaluate();

        if (score == 10 || score == -10 || isGameOver()||depth==3) return score - depth;
        ArrayList<String> listeMovesAvailables = new ArrayList<>();

        if (isMax) { // Tour de l'IA (Maximisation)
            int best = Integer.MIN_VALUE;
            for (String me : listeMovesAvailables) {
                play(me, player.getCurrent());
                printBoard();
                best = Math.max(best, minimax(depth + 1, false, alpha, beta));
                undo(me);
                System.out.print("Board after undo");
                printBoard();
                alpha = Math.max(alpha, best);
                if (beta <= alpha) return best; // Élagage
            }


            return best;
        } else { // Tour du joueur (Minimisation)
            int best = Integer.MAX_VALUE;
            for (String ma:listeMovesAvailables) {
                play(ma, player.getOppenent());
                printBoard();
                best = Math.min(best, minimax(depth + 1, true, alpha, beta));
                undo(ma);
                System.out.print("Board after undo");
                printBoard();
                beta = Math.min(beta, best);
                if (beta <= alpha) return best; // Élagage

            }
            return best;
        }
    }

    public boolean isGameOver() {
        if (evaluate() != 0) return true;
        for (int i=0;i<indiceLocalBoardComplete.length;i++){
            if (indiceLocalBoardComplete[i]==null) return false;
        }

        return true;
    }
    public int evaluate() {

        if((indiceLocalBoardComplete[0]!=null)&&(indiceLocalBoardComplete[1]!=null)&&(indiceLocalBoardComplete[2]!=null))
            if (indiceLocalBoardComplete[0].equals(indiceLocalBoardComplete[1]) && indiceLocalBoardComplete[2].equals(indiceLocalBoardComplete[0])) {
                    if (indiceLocalBoardComplete[0].equals(player.getCurrent())) return 10;
                    if (indiceLocalBoardComplete[0].equals(player.getOppenent())) return -10;
            }
        if((indiceLocalBoardComplete[3]!=null)&&(indiceLocalBoardComplete[4]!=null)&&(indiceLocalBoardComplete[5]!=null))
            if (indiceLocalBoardComplete[3].equals(indiceLocalBoardComplete[4]) && indiceLocalBoardComplete[5].equals(indiceLocalBoardComplete[3])) {

                    if (indiceLocalBoardComplete[3].equals(player.getCurrent())) return 10;
                    if (indiceLocalBoardComplete[3].equals(player.getOppenent())) return -10;
            }
        if((indiceLocalBoardComplete[6]!=null)&&(indiceLocalBoardComplete[7]!=null)&&(indiceLocalBoardComplete[8]!=null))
            if (indiceLocalBoardComplete[6].equals(indiceLocalBoardComplete[7]) && indiceLocalBoardComplete[8].equals(indiceLocalBoardComplete[7])) {
                    if (indiceLocalBoardComplete[6].equals(player.getCurrent())) return 10;
                    if (indiceLocalBoardComplete[6].equals(player.getOppenent())) return -10;
            }

        //col
        if((indiceLocalBoardComplete[0]!=null)&&(indiceLocalBoardComplete[3]!=null)&&(indiceLocalBoardComplete[6]!=null))
            if (indiceLocalBoardComplete[0].equals(indiceLocalBoardComplete[3]) && indiceLocalBoardComplete[3].equals(indiceLocalBoardComplete[6])) {
                if (indiceLocalBoardComplete[0].equals(player.getCurrent())) return 10;
                if (indiceLocalBoardComplete[0].equals(player.getOppenent())) return -10;
            }

        if((indiceLocalBoardComplete[1]!=null)&&(indiceLocalBoardComplete[4]!=null)&&(indiceLocalBoardComplete[7]!=null))
            if (indiceLocalBoardComplete[1].equals(indiceLocalBoardComplete[4]) && indiceLocalBoardComplete[4].equals(indiceLocalBoardComplete[7])) {
                if (indiceLocalBoardComplete[1].equals(player.getCurrent())) return 10;
                if (indiceLocalBoardComplete[1].equals(player.getOppenent())) return -10;
            }

        if((indiceLocalBoardComplete[2]!=null)&&(indiceLocalBoardComplete[5]!=null)&&(indiceLocalBoardComplete[8]!=null))
            if (indiceLocalBoardComplete[2].equals(indiceLocalBoardComplete[5]) && indiceLocalBoardComplete[8].equals(indiceLocalBoardComplete[5])) {
                if (indiceLocalBoardComplete[2].equals(player.getCurrent())) return 10;
                if (indiceLocalBoardComplete[2].equals(player.getOppenent())) return -10;
            }

        //diag
        if((indiceLocalBoardComplete[0]!=null)&&(indiceLocalBoardComplete[4]!=null)&&(indiceLocalBoardComplete[8]!=null))
            if (indiceLocalBoardComplete[0].equals(indiceLocalBoardComplete[4]) && indiceLocalBoardComplete[8].equals(indiceLocalBoardComplete[4])) {
                if (indiceLocalBoardComplete[0].equals(player.getCurrent())) return 10;
                if (indiceLocalBoardComplete[0].equals(player.getOppenent())) return -10;
            }
        if((indiceLocalBoardComplete[2]!=null)&&(indiceLocalBoardComplete[4]!=null)&&(indiceLocalBoardComplete[6]!=null))
            if (indiceLocalBoardComplete[2].equals(indiceLocalBoardComplete[4]) && indiceLocalBoardComplete[6].equals(indiceLocalBoardComplete[2])) {
                if (indiceLocalBoardComplete[2].equals(player.getCurrent())) return 10;
                if (indiceLocalBoardComplete[2].equals(player.getOppenent())) return -10;
            }


        return 0;
    }

    private String [] evaluateLocalBoard(int indiceBoardGlobal){

        String [] tab=new String[2];

        /**
         * verification verticale
         */
        if(this.boardLocal[indiceBoardGlobal][0][0].equals(this.boardLocal[indiceBoardGlobal][1][0])){
            if(this.boardLocal[indiceBoardGlobal][0][0].equals(this.boardLocal[indiceBoardGlobal][2][0])){

                if(!this.boardLocal[indiceBoardGlobal][2][0].equals("-")){
                    tab[0]=String.valueOf(indiceBoardGlobal);
                    tab[1]=this.boardLocal[indiceBoardGlobal][2][0];
                }
            }

        }else if(this.boardLocal[indiceBoardGlobal][0][1].equals(this.boardLocal[indiceBoardGlobal][1][1])){
            if(this.boardLocal[indiceBoardGlobal][0][1].equals(this.boardLocal[indiceBoardGlobal][2][1])){

                if(!this.boardLocal[indiceBoardGlobal][2][1].equals("-")){
                    tab[0]=String.valueOf(indiceBoardGlobal);
                    tab[1]=this.boardLocal[indiceBoardGlobal][2][1];
                }
            }

        }else if(this.boardLocal[indiceBoardGlobal][0][2].equals(this.boardLocal[indiceBoardGlobal][1][2])){
            if(this.boardLocal[indiceBoardGlobal][0][2].equals(this.boardLocal[indiceBoardGlobal][2][2])){

                if(!this.boardLocal[indiceBoardGlobal][2][2].equals("-")){
                    tab[0]=String.valueOf(indiceBoardGlobal);
                    tab[1]=this.boardLocal[indiceBoardGlobal][2][2];
                }
            }

        }

        // verification horizontale
        if(this.boardLocal[indiceBoardGlobal][0][0].equals(this.boardLocal[indiceBoardGlobal][0][1])){
            if(this.boardLocal[indiceBoardGlobal][0][2].equals(this.boardLocal[indiceBoardGlobal][0][1])){

                if(!this.boardLocal[indiceBoardGlobal][0][2].equals("-")){
                    tab[0]=String.valueOf(indiceBoardGlobal);
                    tab[1]=this.boardLocal[indiceBoardGlobal][0][2];
                }
            }

        }else if(this.boardLocal[indiceBoardGlobal][1][0].equals(this.boardLocal[indiceBoardGlobal][1][1])){
            if(this.boardLocal[indiceBoardGlobal][1][2].equals(this.boardLocal[indiceBoardGlobal][1][1])){
                if(!this.boardLocal[indiceBoardGlobal][1][2].equals("-")){
                    tab[0]=String.valueOf(indiceBoardGlobal);
                    tab[1]=this.boardLocal[indiceBoardGlobal][1][2];
                }
            }

        }else if(this.boardLocal[indiceBoardGlobal][2][0].equals(this.boardLocal[indiceBoardGlobal][2][1])){
            if(this.boardLocal[indiceBoardGlobal][2][2].equals(this.boardLocal[indiceBoardGlobal][2][1])){
                if(!this.boardLocal[indiceBoardGlobal][2][2].equals("-")){
                    tab[0]=String.valueOf(indiceBoardGlobal);
                    tab[1]=this.boardLocal[indiceBoardGlobal][2][2];
                }
            }

        }

        //Diagonale
        if(this.boardLocal[indiceBoardGlobal][0][0].equals(this.boardLocal[indiceBoardGlobal][1][1])){
            if(this.boardLocal[indiceBoardGlobal][0][0].equals(this.boardLocal[indiceBoardGlobal][2][2])){

                if(!this.boardLocal[indiceBoardGlobal][2][2].equals("-")){
                    tab[0]=String.valueOf(indiceBoardGlobal);
                    tab[1]=this.boardLocal[indiceBoardGlobal][2][2];
                }
            }

        }else if(this.boardLocal[indiceBoardGlobal][0][2].equals(this.boardLocal[indiceBoardGlobal][1][1])){
            if(this.boardLocal[indiceBoardGlobal][0][2].equals(this.boardLocal[indiceBoardGlobal][2][0])){

                if(!this.boardLocal[indiceBoardGlobal][2][0].equals("-")){
                    tab[0]=String.valueOf(indiceBoardGlobal);
                    tab[1]=this.boardLocal[indiceBoardGlobal][2][0];
                }
            }

        }


       /* if(tab[1].equals("-")){
            int y=0;
        }*/
        return tab;
    }
    /*public void undo(String mm){
        String tab= moveConvertInt(mm);
        char[] tab1= tab.toCharArray();
        this.boardLocal[Character.getNumericValue( tab1[0])][Character.getNumericValue( tab1[1])][Character.getNumericValue( tab1[2])]="-";
        //Evaluer le board Local apres avoir joué pour savoir s'il est gagné ou non
        String [] Tab2=this.evaluateLocalBoard(Character.getNumericValue( tab1[0]));

        if(Tab2[0]!=null||Tab2[1]!=null){
            this.indiceLocalBoardComplete[Integer.parseInt(Tab2[0])]=null;
            for(String mmo:reserveMoveForUndo){
                this.availableMoves.add(mmo);
            }
            reserveMoveForUndo.clear();
            this.availableMoves.add(mm);

        }
    }*/

    public void undo(String move) {
        String tab = "";
        try {
            tab = moveConvertInt(move);
        } catch (Exception ex) {
            return; // Si l'erreur est critique, on annule l'undo
        }

        char[] tab1 = tab.toCharArray();
        int localBoardIndex = Character.getNumericValue(tab1[0]);
        int row = Character.getNumericValue(tab1[1]);
        int col = Character.getNumericValue(tab1[2]);

        // ✅ 1. Restaurer la case jouée sur le plateau à son état initial
        this.boardLocal[localBoardIndex][row][col] = "-"; // Remettre la case vide

        // ✅ 2. Remettre le move dans `availableMoves`
        this.availableMoves.add(move);

        // ✅ 3. Restaurer les cases réservées si la partie locale a été complétée
        if (this.indiceLocalBoardComplete[localBoardIndex] != null) {
            for (String reservedMove : this.reserveMoveForUndo) {
                if (reservedMove.startsWith(Integer.toString(localBoardIndex))) {
                    this.availableMoves.add(reservedMove);
                }
            }
            this.reserveMoveForUndo.removeIf(m -> m.startsWith(Integer.toString(localBoardIndex)));

            // ✅ Annuler la victoire locale du joueur
            this.indiceLocalBoardComplete[localBoardIndex] = null;
        }
    }

    public void play(String move,String player){
        String tab="";
        try{
             tab= moveConvertInt(move);
        }catch(Exception ex){
            int a=1;
        }



       char[] tab1= tab.toCharArray();
        try{
            this.boardLocal[Character.getNumericValue( tab1[0])][Character.getNumericValue( tab1[1])][Character.getNumericValue( tab1[2])]=player;
        }catch(Exception ex){
           int b=1;
        }

        //Evaluer le board Local apres avoir joué pour savoir s'il est gagné ou non
        String [] Tab2=this.evaluateLocalBoard(Character.getNumericValue( tab1[0]));

        try{
            if(Tab2[0]!=null||Tab2[1]!=null){
                this.indiceLocalBoardComplete[Integer.parseInt(Tab2[0])]=Tab2[1];

                for(int i=0; i<3;i++){
                    for(int j=0;j<3;j++){
                        if(this.boardLocal[Integer.parseInt(Tab2[0])][i][j]=="-"){
                            this.reserveMoveForUndo.add(intConvertMove(Integer.parseInt(Tab2[0]),i,j));
                            this.availableMoves.remove(intConvertMove(Integer.parseInt(Tab2[0]),i,j));
                        }
                    }

                }
            }
        }catch(Exception ex){
            String j="";
        }


        //retirer le mov de la liste des moves available
        this.availableMoves.remove(move);
    }
    public Object generateMove(String Move){
        if(Move.equals("I9")){
            int y=0;
        }
        if(Move==""){
            return this.availableMoves.clone();
        }else{
            int tableLocal=returnGlobalCase(Move);

            if(indiceLocalBoardComplete[tableLocal]!=null){

                return this.availableMoves.clone();
            }else{
                ArrayList<String> tabMoveAvailable = new ArrayList<String>();
                for(int i=0;i<3;i++){
                    for(int j=0;j<3;j++){
                        if(boardLocal[tableLocal][i][j].equals("-")){
                            tabMoveAvailable.add( intConvertMove(tableLocal,i,j));
                        }
                    }
                }

                return tabMoveAvailable;
            }
        }


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
            return Character.getNumericValue(tab1[2]);
        }else if(Character.getNumericValue(tab1[1])==1){
            return Character.getNumericValue(tab1[2])+3;
        }else if(Character.getNumericValue(tab1[1])==2){
            return Character.getNumericValue(tab1[2])+6;
        }

        return -1;
    }
    public void setPlayers(char c){
        this.player = new Player(c);
    }
    public Player getPlayers(){
        return this.player;
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