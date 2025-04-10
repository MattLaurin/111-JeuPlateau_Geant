import java.util.*;
import java.util.concurrent.*;

public class MiniMax {

    private Player player;
    private long StartTime; 
    private long timeLimit = 3000; // 3 secondes

    private volatile int maxDepthReached = 0;

    public MiniMax(Player player) {
        this.player = player;
    }

    //--------Code Pour getNextMove--------//

    public String getNextMove(String move, Plateau plateau) {
    ArrayList<String> moveDispo;

    int forcedBoardIndex;
    if (move.equals("")) {
        forcedBoardIndex = -1;
    } else {
        forcedBoardIndex = plateau.returnGlobalCase(move);
    }

    moveDispo = Algo.generateMove(move, plateau, forcedBoardIndex);


    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    StartTime = System.currentTimeMillis();
    maxDepthReached = 0;

    String bestMoveSoFar = moveDispo.get(0); // fallback a un ancien move.
    int bestScoreSoFar = Integer.MIN_VALUE;

    int currentDepth = 6;
    int maxAllowedDepth = 8;

    

    while (currentDepth <= maxAllowedDepth) {
        List<Future<MoveScore>> futures = new ArrayList<>();
        int depthForThisRound = currentDepth;
        long timeRemaining = timeLimit - (System.currentTimeMillis() - StartTime);

        if (timeRemaining < 100) break;

        for (String m : moveDispo) {
            Plateau cloned = plateau.deepClone();
            futures.add(executor.submit(() -> {
                cloned.play(m, player.getCurrent());
                int forcedBoard = cloned.returnGlobalCase(m);
                int baseScore = minimax(1, false, Integer.MIN_VALUE, Integer.MAX_VALUE, cloned, m, forcedBoard, depthForThisRound);

                int boardControlScore = evaluateBoardControl(cloned, forcedBoard);
                int openingScore = evaluateOpening(m, cloned);
                int totalScore = baseScore * 2 + boardControlScore * 3 + openingScore;

                return new MoveScore(m, totalScore);
            }));
        }

        boolean completedInTime = true;
        List<MoveScore> results = new ArrayList<>();

        for (Future<MoveScore> f : futures) {
            try {
                long timeLeft = timeLimit - (System.currentTimeMillis() - StartTime);
                if (timeLeft <= 5) {
                    completedInTime = false;
                    break;
                }
                results.add(f.get(timeLeft, TimeUnit.MILLISECONDS));
            } catch (TimeoutException e) {
                completedInTime = false;
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!completedInTime) break;

        MoveScore bestAtThisDepth = results.stream()
                .max(Comparator.comparingInt(ms -> ms.score))
                .orElse(new MoveScore(bestMoveSoFar, bestScoreSoFar));

        bestMoveSoFar = bestAtThisDepth.move;
        bestScoreSoFar = bestAtThisDepth.score;
        maxDepthReached = currentDepth;

        currentDepth++;
    }

    executor.shutdownNow();
    System.out.println("Final best move: " + bestMoveSoFar + " | depth=" + maxDepthReached);
    return bestMoveSoFar;
}



    //--------Code pour choisir le best move--------//

    private int evaluateBoardControl(Plateau plateau, int forcedBoardIndex) {
        int boardControlScore = 0;
        if (forcedBoardIndex >= 0 && forcedBoardIndex < 9 && !plateau.getWonLocalBoards().contains(forcedBoardIndex)) {
            boardControlScore = Algo.evaluateLocal(plateau.getLocalBoard(forcedBoardIndex), player);
        } else if (plateau.getWonLocalBoards().contains(forcedBoardIndex)) {
            boardControlScore = 120; // Boost  --> c the best
        }
        return boardControlScore;
    }

    private int evaluateOpening(String move, Plateau plateau) {
        int openingBonus = 0;
        if (plateau.getFilledCellNb() == 0 && player.getCurrent().equalsIgnoreCase("X")) {
            if (move.equals("E5")) openingBonus = 1000; // Center
            else if ("A1 A9 I1 I9".contains(move)) openingBonus = 800; // Corners
            else if ("A5 E1 E9 I5".contains(move)) openingBonus = 500; // Edges
        }
        return openingBonus;
    }



    //--------MiniMax--------//


    // Minimax Algorithm with et elagage Alpha-Beta 
    private int minimax(int depth, boolean isMax, int alpha, int beta, Plateau plateau, String lastMove, int forcedBoardIndex, int maxDepth) {
    if (System.currentTimeMillis() - StartTime > timeLimit - 30) {
        return Algo.evaluateGlobal(plateau, player);
    }

    int score = Algo.evaluateGlobal(plateau, player);

    if (!Thread.currentThread().getName().contains("pool")) {
        maxDepthReached = Math.max(maxDepthReached, depth);
    }

    if (Math.abs(score) >= 100000 || plateau.isGameOver() || depth == maxDepth) {
        return score - depth;
    }

    ArrayList<String> moves = Algo.generateMove(lastMove, plateau, forcedBoardIndex);

    // ----- Tri intelligent des coups avec MoveScore -----
    List<MoveScore> orderedMoves = new ArrayList<>();
    for (String m : moves) {
        plateau.play(m, isMax ? player.getCurrent() : player.getOpponent());
        int forcedBoard = plateau.returnGlobalCase(m);

        int evalGlobal = Algo.evaluateGlobal(plateau, player);
        int evalLocal = (forcedBoard >= 0 && forcedBoard < 9)
            ? Algo.evaluateLocal(plateau.getLocalBoard(forcedBoard), player)
            : 0;

        plateau.undo(m);
        orderedMoves.add(new MoveScore(m, evalGlobal + evalLocal));
    }

    orderedMoves.sort((a, b) -> isMax
        ? Integer.compare(b.score, a.score)
        : Integer.compare(a.score, b.score));

    // ----- Minimax principal -----
    if (isMax) {
        int best = Integer.MIN_VALUE;
        for (MoveScore ms : orderedMoves) {
            String m = ms.move;
            plateau.play(m, player.getCurrent());
            int newForced = plateau.returnGlobalCase(m);
            best = Math.max(best, minimax(depth + 1, false, alpha, beta, plateau, m, newForced, maxDepth));
            plateau.undo(m);
            alpha = Math.max(alpha, best);
            if (beta <= alpha) break;
        }
        return best;
    } else {
        int best = Integer.MAX_VALUE;
        for (MoveScore ms : orderedMoves) {
            String m = ms.move;
            plateau.play(m, player.getOpponent());
            int newForced = plateau.returnGlobalCase(m);
            best = Math.min(best, minimax(depth + 1, true, alpha, beta, plateau, m, newForced, maxDepth));
            plateau.undo(m);
            beta = Math.min(beta, best);
            if (beta <= alpha) break;
        }
        return best;
    }
}


}
