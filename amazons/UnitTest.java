package amazons;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

import static amazons.Piece.*;

/** The suite of all JUnit tests for the enigma package.
 *  @author
 */
public class UnitTest {

    /**
     * Run the JUnit tests in this package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /**
     * Tests basic correctness of put and get on the initialized board.
     */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    /**
     * Tests proper identification of legal/illegal queen moves.
     */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
        assertTrue(Square.sq("d5").isQueenMove(Square.sq("f3")));
    }

    /**
     * Tests toString for initial board state and a smiling board state. :)
     */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
                    "   - - - B - - B - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B - - - - - - - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - - - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - W - - W - - -\n";

    static final String SMILE =
                    "   - - - - - - - - - -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - S - S - - S - S -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - W - - - - W - -\n"
                    + "   - - - W W W W - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n";


    @Test
    public void testSquaresConstructor() {

        assertEquals("d3", Square.sq(23).toString());
        assertEquals("j10", Square.sq(99).toString());
        assertEquals("a1", Square.sq(0).toString());

    }

    @Test
    public void testSquareFromString() {

        Square testSquare = Square.sq("d3");
        assertEquals(Square.sq(23), testSquare);

        testSquare = Square.sq("j10");
        assertEquals(Square.sq(99), testSquare);

        testSquare = Square.sq("a1");
        assertEquals(Square.sq(0), testSquare);


    }

    @Test
    public void testSquareFromStringColRow() {

        Square testSquare = Square.sq("d", "3");
        assertEquals(Square.sq(23), testSquare);

        testSquare = Square.sq("j", "10");
        assertEquals(Square.sq(99), testSquare);

        testSquare = Square.sq("a", "1");
        assertEquals(Square.sq(0), testSquare);

    }

    @Test
    public void testSquareFromIntColRow() {

        Square testSquare = Square.sq(3, 2);
        assertEquals(Square.sq(23), testSquare);

        testSquare = Square.sq(9, 9);
        assertEquals(Square.sq(99), testSquare);

    }

    @Test
    public void testQueenMove() {

        Square from = Square.sq(1, 1);
        Square to = Square.sq(1, 2);
        assertTrue(from.queenMove(0, 1) == to);

        from = Square.sq("c4");
        to = Square.sq("i10");
        assertTrue(from.queenMove(1, 6) == to);
        to = null;
        assertTrue(from.queenMove(1, 7) == to);

        from = Square.sq("i3");
        to = Square.sq("g1");
        assertTrue(from.queenMove(5, 2) == to);
        to = null;
        assertTrue(from.queenMove(5, 3) == to);


    }

    @Test
    public void testDirection() {
        Square from = Square.sq(1, 1);
        Square to = Square.sq(1, 2);
        assertEquals(from.direction(to), 0);

        from = Square.sq(1, 1);
        to = Square.sq(6, 6);
        assertEquals(from.direction(to), 1);

        from = Square.sq("b4");
        to = Square.sq("h4");
        assertEquals(from.direction(to), 2);

        from = Square.sq("d5");
        to = Square.sq("f3");
        assertEquals(from.direction(to), 3);

        from = Square.sq("d9");
        to = Square.sq("a6");
        assertEquals(from.direction(to), 5);


        from = Square.sq("i7");
        to = Square.sq("c7");
        assertEquals(from.direction(to), 6);

    }

    @Test
    public void testIsUnblockedMove() {

        Board b = new Board();
        b.put(WHITE, Square.sq("d4"));
        b.put(EMPTY, Square.sq("d1"));
        System.out.println(b.toString());
        Square from = Square.sq("a4");
        Square to = Square.sq("g4");
        assertFalse(b.isUnblockedMove(from, to, from));
        assertTrue(b.isUnblockedMove(from, to, Square.sq("d4")));

        from = Square.sq("a10");
        to = Square.sq("a6");
        assertFalse(b.isUnblockedMove(from, to, from));
        assertTrue(b.isUnblockedMove(from, to, Square.sq("a7")));

        b.put(BLACK, Square.sq("h7"));
        b.put(EMPTY, Square.sq("j7"));
        System.out.println(b.toString());

        from = Square.sq("j7");
        to = Square.sq("c7");
        assertFalse(b.isUnblockedMove(from, to, from));
        assertTrue(b.isUnblockedMove(from, to, Square.sq("h7")));

        from = Square.sq("a5");
        to = Square.sq("a7");
        assertFalse(b.isUnblockedMove(from, to, from));

        from = Square.sq("b4");
        to = Square.sq("d4");
        assertFalse(b.isUnblockedMove(from, to, from));

        from = Square.sq("b4");
        to = Square.sq("b5");
        assertTrue(b.isUnblockedMove(from, to, from));


    }

    @Test
    public void testIsLegal() {

        Board b = new Board();
        b.put(WHITE, Square.sq("d4"));
        b.put(EMPTY, Square.sq("d1"));
        b.put(BLACK, Square.sq("h7"));
        b.put(EMPTY, Square.sq("j7"));
        System.out.println(b.toString());
        assertTrue(b.isLegal(Square.sq("d4")));
        assertFalse(b.isLegal(Square.sq("a7")));
        Square from = Square.sq("j4");
        Square to = Square.sq("c4");
        Square spear = Square.sq("a4");
        assertFalse(b.isLegal(from, to));
        assertFalse(b.isLegal(from, to, Square.sq("a4")));

        assertFalse(b.isLegal(Move.mv(from, to, spear)));
        assertFalse(b.isLegal(Move.mv(Square.sq("h4"), to, spear)));
        assertTrue(b.isLegal(Move.mv(Square.sq("j4"),
                Square.sq("h4"), Square.sq("e4"))));

    }

    @Test
    public void testMakeMove() {

        Board b = new Board();
        System.out.println(b.toString());
        Square from = Square.sq("d1");
        Square to = Square.sq("d2");
        Square spear = Square.sq("d4");
        b.makeMove(from, to, spear);
        System.out.println(b.toString());

    }

    @Test
    public void testUndo() {

        Board b = new Board();
        System.out.println(b.toString());
        Square from = Square.sq("d1");
        Square to = Square.sq("d2");
        Square spear = Square.sq("d4");
        b.makeMove(from, to, spear);
        System.out.println(b.toString());
        b.undo();
        System.out.println(b.toString());

    }

    @Test
    public void testBoardCopy() {

        Board b = new Board();
        Square from = Square.sq("d1");
        Square to = Square.sq("d2");
        Square spear = Square.sq("d4");
        b.makeMove(from, to, spear);
        Board c = new Board();
        c.copy(b);
        assertEquals(b.turn(), c.turn());
        assertEquals(b.numMoves(), c.numMoves());
        assertEquals(b.getAllMoves(), c.getAllMoves());
        assertEquals(b.getBoardmap(), c.getBoardmap());
        assertEquals(b.getBoardmap(), c.getBoardmap());
        assertTrue(b.toString().equals(c.toString()));

    }


}


