package amazons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Iterator;
import java.util.Collections;
import static amazons.Piece.EMPTY;
import static amazons.Piece.WHITE;
import static amazons.Piece.BLACK;
import static amazons.Piece.SPEAR;

/** The state of an Amazons Game.
 *  @author A.R. LOEFFLER
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {

        this._turn = model.turn();
        this._winner = model.winner();
        this._numMoves = model.numMoves();
        this._allMoves = model._allMoves;
        this._boardmap = new HashMap<>();
        for (Square s : model._boardmap.keySet()) {
            this.put(model.get(s), s.col(), s.row());
        }
        ArrayList<Square> emptyList = new ArrayList<>();
        for (int n = 0; n < SIZE * SIZE; n += 1) {
            if (_boardmap.get(Square.sq(n)) == null) {
                emptyList.add(Square.sq(n));
            }
        }
        for (Square s : emptyList) {
            _boardmap.put(s, EMPTY);
        }

    }

    /** Clears the board to the initial position. */
    void init() {
        _turn = WHITE;
        _winner = EMPTY;
        _numMoves = 0;
        _allMoves = new Stack<>();
        _boardmap = new HashMap<>();
        put(BLACK, 3, 9);
        put(BLACK, 6, 9);
        put(BLACK, 0, 6);
        put(BLACK, 9, 6);
        put(WHITE, 3, 0);
        put(WHITE, 6, 0);
        put(WHITE, 0, 3);
        put(WHITE, 9, 3);
        ArrayList<Square> emptyList = new ArrayList<>();
        for (int n = 0; n < SIZE * SIZE; n += 1) {
            if (_boardmap.get(Square.sq(n)) == null) {
                emptyList.add(Square.sq(n));
            }
        }
        for (Square s : emptyList) {
            _boardmap.put(s, EMPTY);
        }
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return _numMoves;
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {

        return _winner;
    }

    /** Returns the list of all moves in the game so far. */
    Stack<Move> getAllMoves() {
        return _allMoves;
    }
    /** Returns the board map. */
    HashMap<Square, Piece> getBoardmap() {
        return _boardmap;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return _boardmap.get(s);
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return get(Square.sq(col, row));
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        _boardmap.put(s, p);
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        _winner = EMPTY;
        put(p, Square.sq(col, row));
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (!from.isQueenMove(to)) {
            return false;
        }
        ArrayList<Square> squareList = from.getSquaresFromTo(to);
        for (Square s : squareList) {
            if ((s != from) && (s != asEmpty)) {
                if (get(s) != EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        if ((get(from) == _turn) && (Square.exists(from.col(), from.row()))) {
            return true;
        }

        return false;
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        if ((isLegal(from)) && (isUnblockedMove(from, to, from))) {
            return true;
        }
        return false;
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        return isLegal(from, to) && isUnblockedMove(to, spear, from);
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to(), move.spear());
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {

        Piece og = get(from);
        put(og, to);
        put(EMPTY, from);
        put(SPEAR, spear);
        _numMoves += 1;
        _allMoves.push(Move.mv(from, to, spear));
        _turn = _turn.opponent();

    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        if ((move != null) && (isLegal(move))) {
            makeMove(move.from(), move.to(), move.spear());
        }
        Iterator<Move> possMoves = legalMoves(_turn);
        if (!possMoves.hasNext()) {
            _winner = _turn.opponent();
        }
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {

        Move prevMove = _allMoves.pop();
        put(get(prevMove.to()), prevMove.from());
        put(EMPTY, prevMove.to());
        put(EMPTY, prevMove.spear());
        _numMoves -= 1;
        _turn = _turn.opponent();

    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = -1;
            _steps = 0;
            _asEmpty = asEmpty;
            _firstMoves = new Stack<>();
            for (int n = 0; n < 8; n += 1) {
                Square currSquare = from.queenMove(n, 1);
                boolean reachable;
                if (currSquare != null) {
                    reachable = isUnblockedMove(from, currSquare, asEmpty);
                } else {
                    reachable = false;
                }
                if (reachable) {
                    _firstMoves.push(currSquare);
                }
            }
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            assert !(_dir > 7);
            toReturn = _from.queenMove(_dir, _steps);
            toNext();
            return toReturn;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            if (_dir < 0) {
                if (_firstMoves.empty()) {
                    _dir = 8;
                } else {
                    starter = _firstMoves.pop();
                    _dir = _from.direction(starter);
                    _steps = 1;
                }
            } else {
                Square nextSquare = _from.queenMove(_dir, _steps + 1);
                if ((nextSquare != null)
                        && (isUnblockedMove(_from, nextSquare, _asEmpty))) {
                    _steps += 1;
                } else {
                    _dir = -1;
                    _steps = 0;
                    toNext();
                }
            }
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
        /** Initial moves available. */
        private Stack<Square> _firstMoves;
        /** Final value to return. */
        private Square toReturn;
        /** Initial square. */
        private Square starter;
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            _inSquare = false;
            _checkingMove = false;
            _done = false;
            _breakFlag = false;

            toNext();
        }

        @Override
        public boolean hasNext() {
            return !_done;
        }

        @Override
        public Move next() {
            assert (!_done);
            toReturn = _nextMove;
            toNext();
            return toReturn;
        }

        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {

            _breakFlag = false;
            if (_inSquare) {

                if (_pieceMoves == NO_SQUARES) {
                    _pieceMoves = reachableFrom(_currSquare, _currSquare);
                }
                if (!_checkingMove) {
                    if (!_pieceMoves.hasNext()) {
                        _inSquare = false;
                        _checkingMove = false;
                        toNext();
                        _breakFlag = true;
                    } else {
                        _currMove = _pieceMoves.next();
                        _checkingMove = true;
                    }
                }
                if (!_breakFlag) {
                    if (_spearThrows == NO_SQUARES) {
                        _spearThrows = reachableFrom(_currMove, _currSquare);
                    }
                    if (!_spearThrows.hasNext()) {
                        _checkingMove = false;
                        _spearThrows = NO_SQUARES;
                        toNext();
                    } else {
                        _currThrow = _spearThrows.next();
                        _nextMove = Move.mv(_currSquare, _currMove, _currThrow);
                    }
                }
            } else {
                if (_startingSquares.hasNext()) {
                    Square tmpSquare = _startingSquares.next();
                    if (get(tmpSquare) == _fromPiece) {
                        _currSquare = tmpSquare;
                        _pieceMoves = NO_SQUARES;
                        _inSquare = true;
                        toNext();
                    } else {
                        toNext();
                    }
                } else {
                    _done = true;
                }
            }

        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
        /** Current square. */
        private Square _currSquare;
        /** Current move. */
        private Square _currMove;
        /** Current square from which to throw from. */
        private Square _currThrow;
        /** Next move. */
        private Move _nextMove;
        /** Returns whether we're currently exploring moves in square. */
        private boolean _inSquare;
        /** Returns whether we're currently exploring moves from square. */
        private boolean _checkingMove;
        /** Returns whether we're done. */
        private boolean _done;
        /** Value to return. */
        private Move toReturn;
        /** Special flag to ensure control. */
        private boolean _breakFlag;
    }

    @Override
    public String toString() {

        String result = "";
        int row = SIZE - 1;
        for (; row >= 0; row -= 1) {
            result += "  ";
            int col = 0;
            for (; col < SIZE; col += 1) {
                result += " " + get(Square.sq(col, row)).toString();
            }
            result += "\n";
        }
        return result;
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
    /** HashMap of squares as keys to pieces. */
    private HashMap<Square, Piece> _boardmap;
    /** Total number of moves in game so far for this board. */
    private int _numMoves;
    /** Stack of all moves made in game so far. */
    private Stack<Move> _allMoves;

}
