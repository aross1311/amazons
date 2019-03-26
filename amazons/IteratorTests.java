package amazons;
import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Arrays;

/** Junit tests for our Board iterators.
 *  @author
 */
public class IteratorTests {

    /** Run the JUnit tests in this package. */
    public static void main(String[] ignored) {
        textui.runClasses(IteratorTests.class);
    }

    /** Tests reachableFromIterator to make sure it returns all reachable
     *  Squares. This method may need to be changed based on
     *   your implementation. */
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMTESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLEFROMTESTSQUARES.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMTESTSQUARES.size(), numSquares);
        assertEquals(REACHABLEFROMTESTSQUARES.size(), squares.size());

        b.init();
        buildBoard(b, MYINITTESTBOARD);
        numSquares = 0;
        squares = new HashSet<>();
        reachableFrom = b.reachableFrom(Square.sq("d1"), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(MYINITTESTSQUARES.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(MYINITTESTSQUARES.size(), numSquares);
        assertEquals(MYINITTESTSQUARES.size(), squares.size());

    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, MYINITTESTBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(b.isLegal(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(2176, numMoves);
        assertEquals(2176, moves.size());

        b = new Board();
        buildBoard(b, MYRESTRICTEDTESTBOARD);
        numMoves = 0;
        moves = new HashSet<>();
        legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(b.isLegal(m));
            numMoves += 1;
            moves.add(m);
        }
        for (Move m : moves) {
            System.out.println(m);
        }
    }


    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
        System.out.println(b);
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] REACHABLEFROMTESTBOARD = {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, W, W },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, E, B, E },
            { E, E, E, S, E, W, E, E, B, E },
            { E, E, E, S, S, S, B, W, B, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
    };

    static final Piece[][] MYINITTESTBOARD = {
            { E, E, E, B, E, E, B, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { B, E, E, E, E, E, E, E, E, B },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { W, E, E, E, E, E, E, E, E, W },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, W, E, E, W, E, E, E },
    };

    static final Piece[][] MYRESTRICTEDTESTBOARD = {
            { S, S, S, S, S, S, S, S, S, S },
            { S, S, S, S, S, S, S, S, S, S },
            { S, S, S, S, S, S, S, S, S, S },
            { S, S, S, S, S, S, S, S, S, S },
            { S, S, S, S, S, S, S, S, S, S },
            { S, S, S, S, S, S, S, S, S, S },
            { S, S, S, S, S, S, S, S, S, S },
            { S, S, S, S, S, S, S, S, S, S },
            { S, S, S, E, E, S, S, S, S, S },
            { B, E, S, W, E, S, S, S, S, S },
    };

    static final Set<Square> REACHABLEFROMTESTSQUARES =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Set<Square> MYINITTESTSQUARES =
            new HashSet<>(Arrays.asList(Square.sq("e2"),
                    Square.sq("f3"),
                    Square.sq("g4"),
                    Square.sq("h5"),
                    Square.sq("i6"),
                    Square.sq("c2"),
                    Square.sq("b3"),
                    Square.sq("d2"),
                    Square.sq("d3"),
                    Square.sq("d4"),
                    Square.sq("d5"),
                    Square.sq("d6"),
                    Square.sq("d7"),
                    Square.sq("d8"),
                    Square.sq("d9"),
                    Square.sq("c1"),
                    Square.sq("b1"),
                    Square.sq("a1"),
                    Square.sq("e1"),
                    Square.sq("f1")));

}

