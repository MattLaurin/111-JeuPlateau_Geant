import java.util.ArrayList;

public class MiniMax {

    private Player player;

    public MiniMax(Player player) {
        this.player = player;
    }

    public String getNextMove(String move, Plateau plateau) {

        ArrayList<String> moveDispo;
        int forcedBoardIndex=0;

        // Pour garder en tete le nb de move : 
        plateau.recalculateFilledCells();

        if(move.equals("")){
            moveDispo = Algo.generateMove(move, plateau, -1);
        }else{
            forcedBoardIndex = plateau.returnGlobalCase(move);
            moveDispo = Algo.generateMove(move, plateau, forcedBoardIndex);
        }
        

        int bestVal = Integer.MIN_VALUE;
        String bestMove = "";

        for (String m : moveDispo) {
            plateau.play(m, player.getCurrent());

            int oppForcedBoard = plateau.returnGlobalCase(m);
            int baseVal = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, plateau, m, oppForcedBoard);

            // Force bonus pour max la strat
            int boardControlScore = 0;
            if (oppForcedBoard >= 0 && oppForcedBoard < 9 && !plateau.getWonLocalBoards().contains(oppForcedBoard)) {
                boardControlScore = Algo.evaluateLocal(plateau.getLocalBoard(oppForcedBoard), player);
            } else if (plateau.getWonLocalBoards().contains(oppForcedBoard)) {
                boardControlScore = 120; // Boost pcq c'est le best presque tjrs
            }

            // Bonus: Move opening (C ca le best move)
            int openingBonus = 0;
            if (plateau.getWonLocalBoards().isEmpty()) {
                if (m.equals("E5")) openingBonus = 1000; // center
                else if ("A1 A9 I1 I9".contains(m)) openingBonus = 800; // corners
                else if ("A5 E1 E9 I5".contains(m)) openingBonus = 500; // edges
            }

            int totalVal = baseVal + boardControlScore + openingBonus;

            if (totalVal > bestVal) {
                bestVal = totalVal;
                bestMove = m;
            }

            plateau.undo(m);
        }

        if (bestMove.equals("") && !moveDispo.isEmpty()) {
            bestMove = moveDispo.get(0);
        }

        return bestMove;
    }


    // Minimax Algorithm with et elagage Alpha-Beta 
    private int minimax(int depth, boolean isMax, int alpha, int beta, Plateau plateau, String lastMove, int forcedBoardIndex) {
        int score = Algo.evaluateGlobal(plateau, player);

        if (score == 0) {
            int heuristic = 0;
            for (int i = 0; i < 9; i++) {
                heuristic += Algo.evaluateLocal(plateau.getLocalBoard(i), player);
            }
            score = heuristic;
        }

        int filledCells = plateau.getFilledCellNb();
        
        int maxDepth = 6;
        if (filledCells == 0){
            System.out.println("First move");
            maxDepth = 3;
        }else if (filledCells < 5){
            maxDepth = 5;
        }


        if (Math.abs(score) >= 100000 || plateau.isGameOver() || depth == maxDepth) {
            return score - depth;
        }

        ArrayList<String> listeMovesAvailables = Algo.generateMove(lastMove, plateau, forcedBoardIndex);

        listeMovesAvailables.sort((move1, move2) -> {
            plateau.play(move1, player.getCurrent());
            int score1 = Algo.evaluateGlobal(plateau, player);
            plateau.undo(move1);

            plateau.play(move2, player.getCurrent());
            int score2 = Algo.evaluateGlobal(plateau, player);
            plateau.undo(move2);

            return Integer.compare(score2, score1);
        });

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (String me : listeMovesAvailables) {
                plateau.play(me, player.getCurrent());
                int newForced = plateau.returnGlobalCase(me);
                best = Math.max(best, minimax(depth + 1, false, alpha, beta, plateau, me, newForced));
                plateau.undo(me);
                alpha = Math.max(alpha, best);
                if (beta <= alpha) break;
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (String ma : listeMovesAvailables) {
                plateau.play(ma, player.getOpponent());
                int newForced = plateau.returnGlobalCase(ma);
                best = Math.min(best, minimax(depth + 1, true, alpha, beta, plateau, ma, newForced));
                plateau.undo(ma);
                beta = Math.min(beta, best);
                if (beta <= alpha) break;
            }
            return best;
        }
    }
}
