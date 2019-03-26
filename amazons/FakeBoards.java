package amazons;

/** Miscellaneous final board situations for testing purposes.
 * @author A.R. LOEFFLER
 * */
public class FakeBoards {

    /** Runs all fake board tests with ARGS not doing anything. */
    public static void main(String[] args) {

    }

    /** An empty piece. */
    static final Piece E = Piece.EMPTY;
    /** A white piece. */
    static final Piece W = Piece.WHITE;
    /** A black piece. */
    static final Piece B = Piece.BLACK;
    /** A spear. */
    static final Piece S = Piece.SPEAR;
    /** A restricted board layout for testing. */
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

    /** Used for building a board B with TARGET. */
    public static void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
    }
}
