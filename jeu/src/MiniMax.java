import java.util.ArrayList;

public class MiniMax {

    private Player player;
    private long StartTime; 
    private long timeLimit = 3000; // 3 secondes

    public MiniMax(Player player) {
        this.player = player;
    }

    public String getNextMove(String move, Plateau plateau) {

        ArrayList<String> moveDispo;
        int forcedBoardIndex=0;
        plateau.recalculateFilledCells();

        

        if(move.equals("")){
            moveDispo = Algo.generateMove(move, plateau, -1);
        }else{
            forcedBoardIndex = plateau.returnGlobalCase(move);
            moveDispo = Algo.generateMove(move, plateau, forcedBoardIndex);
        }
        
        // Time tracked a partir d'ici <-- 
        StartTime = System.currentTimeMillis();
        //

        int bestVal = Integer.MIN_VALUE;
        String bestMove = "";


        for (String m : moveDispo) {
            plateau.play(m, player.getCurrent());

            int oppForcedBoard = plateau.returnGlobalCase(m);
            int baseVal = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, plateau, m, oppForcedBoard);

            // Force bonus pour max la strat
            int boardControlScore = evaluateBoardControl(plateau, oppForcedBoard);
            int openingScore = evaluateOpening(m, plateau);
            

            int totalVal = baseVal * 2 + boardControlScore * 3 + openingScore; // Adjusted weights
            if (totalVal > bestVal) {
                bestVal = totalVal;
                bestMove = m;
            }

            plateau.undo(m);
        }

        // If time is up, return the best move found so far
        if (System.currentTimeMillis() - StartTime > timeLimit) {
        return bestMove;
        }

        return bestMove;
    }


    //--------Code Pour getNextMove--------//

    private int evaluateBoardControl(Plateau plateau, int forcedBoardIndex) {
        int boardControlScore = 0;
        if (forcedBoardIndex >= 0 && forcedBoardIndex < 9 && !plateau.getWonLocalBoards().contains(forcedBoardIndex)) {
            boardControlScore = Algo.evaluateLocal(plateau.getLocalBoard(forcedBoardIndex), player);
        } else if (plateau.getWonLocalBoards().contains(forcedBoardIndex)) {
            boardControlScore = 120; // Boost because it's the best
        }
        return boardControlScore;
    }

    private int evaluateOpening(String move, Plateau plateau) {
        int openingBonus = 0;
        if (plateau.getWonLocalBoards().isEmpty()) {
            if (move.equals("E5")) openingBonus = 1000; // Center
            else if ("A1 A9 I1 I9".contains(move)) openingBonus = 800; // Corners
            else if ("A5 E1 E9 I5".contains(move)) openingBonus = 500; // Edges
        }
        return openingBonus;
    }



    //--------MiniMax--------//


    // Minimax Algorithm with et elagage Alpha-Beta 
    private int minimax(int depth, boolean isMax, int alpha, int beta, Plateau plateau, String lastMove, int forcedBoardIndex) {

        if (System.currentTimeMillis() - StartTime > timeLimit) {
            return Algo.evaluateGlobal(plateau, player);
        }

        int maxDepth = 6;
        if (System.currentTimeMillis() - StartTime > timeLimit - 500) {
            maxDepth = 4; // Lower depth if the remaining time is below a threshold
        }

        int score = Algo.evaluateGlobal(plateau, player);

        if (score == 0) {
            int heuristic = 0;
            for (int i = 0; i < 9; i++) {
                heuristic += Algo.evaluateLocal(plateau.getLocalBoard(i), player);
            }
            score = heuristic;
        }

        if (Math.abs(score) >= 100000 || plateau.isGameOver() || depth == maxDepth) {
            return score - depth;
        }

        ArrayList<String> listeMovesAvailables = Algo.generateMove(lastMove, plateau, forcedBoardIndex);

        listeMovesAvailables.sort((move1, move2) -> {
            plateau.play(move1, player.getCurrent());
            int score1 = Algo.evaluateGlobal(plateau, player) + Algo.evaluateLocal(plateau.getLocalBoard(forcedBoardIndex), player);
            plateau.undo(move1);
            plateau.play(move2, player.getCurrent());
            int score2 = Algo.evaluateGlobal(plateau, player) + Algo.evaluateLocal(plateau.getLocalBoard(forcedBoardIndex), player);
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
