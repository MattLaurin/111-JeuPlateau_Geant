public class LocalBoard {
    private String[][] board = new String[3][3];
    private boolean won = false;
    private String winner = "";

    public LocalBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "-";
            }
        }
    }

    public void play(int row, int col, String player) {
        if (board[row][col].equals("-")) {
            board[row][col] = player;
            if (checkWin(row, col, player)) {
                won = true;
                winner = player;
            }
        }
    }

    public void undo(int row, int col){
        board[row][col] = "-";
    }

    public boolean isWon() {
        return won;
    }

    public String getWinner() {
        return winner;
    }

    public String getCell(int row, int col){
        return board[row][col];
    }

    public String[][] getBoard() {
        return board;
    }

    public void resetWinStatus() {
        this.won = false;
        this.winner = "";
    }

    private boolean checkWin(int row, int col, String player) {
        // Verifie si le local board en row, columns ou diagonal a ete gagne
        Boolean win= (board[row][0].equals(player) && board[row][1].equals(player) && board[row][2].equals(player)) ||
               (board[0][col].equals(player) && board[1][col].equals(player) && board[2][col].equals(player)) ||
               (board[0][0].equals(player) && board[1][1].equals(player) && board[2][2].equals(player)) ||
               (board[0][2].equals(player) && board[1][1].equals(player) && board[2][0].equals(player));
        
        
        return win;
    }


}