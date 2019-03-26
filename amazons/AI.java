package amazons;


import java.util.Iterator;

import static amazons.Piece.*;

/** A Player that automatically generates moves.
 *  @author A.R. LOEFFLER
 */
class AI extends Player {

    /** The best number to divide N over for max depth. */
    private static final int IDEALDEPTHDIVIDER = 15;
    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.winner() == WHITE || board.winner() == BLACK) {
            return staticScore(board);
        }

        if (sense == 1) {
            int bestSoFar = Integer.MIN_VALUE;
            Iterator<Move> beforeMovesIter = board.legalMoves(WHITE);
            while (beforeMovesIter.hasNext()) {
                Move nextMove = beforeMovesIter.next();
                Board freeze = new Board();
                freeze.copy(board);
                board.makeMove(nextMove);
                int val = findMove(board, depth - 1, false, -1, alpha, beta);
                board.copy(freeze);
                if (saveMove && val > bestSoFar) {
                    _lastFoundMove = nextMove;
                    bestSoFar = val;
                }
                alpha = Math.max(alpha, val);
                if (beta <= alpha) {
                    break;
                }


            }
            return bestSoFar;
        } else {

            int bestSoFar = Integer.MAX_VALUE;
            Iterator<Move> beforeMovesIter = board.legalMoves(BLACK);
            while (beforeMovesIter.hasNext()) {
                Move nextMove = beforeMovesIter.next();
                Board freeze = new Board();
                freeze.copy(board);
                board.makeMove(nextMove);
                int val = findMove(board, depth - 1, false, 1, alpha, beta);
                board.copy(freeze);
                if (saveMove && val < bestSoFar) {
                    _lastFoundMove = nextMove;
                    bestSoFar = val;
                }
                beta = Math.min(beta, val);
                if (beta <= alpha) {
                    break;
                }
            }
            return bestSoFar;
        }
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        int N = board.numMoves();
        return N / IDEALDEPTHDIVIDER + 1;
    }


    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        }


        Iterator<Move> whiteIter = board.legalMoves(WHITE);
        Iterator<Move> blackIter = board.legalMoves(BLACK);

        int whitePossMoves = 0;
        int blackPossMoves = 0;
        while (whiteIter.hasNext()) {
            whitePossMoves += 1;
            whiteIter.next();
        }
        while (blackIter.hasNext()) {
            blackPossMoves += 1;
            blackIter.next();
        }

        return whitePossMoves - blackPossMoves;


    }



}
