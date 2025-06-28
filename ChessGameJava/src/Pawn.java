import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessPiece {
    public Pawn(Cell startingCell, boolean isWhite) {
        super(startingCell, isWhite, isWhite ? "/ChessSprites/wP.png" : "/ChessSprites/bP.png");
    }

    @Override
    public java.util.List<Cell> getLegalMoves(Cell[][] board) {
        List<Cell> legalMoves = new ArrayList<>();
        int row = currentCell.getRow();
        int col = currentCell.getCol();
        int direction = isWhite ? -1 : 1;

        // Moving forward one step
        int nextRow = row + direction;
        if (isWithinBounds(nextRow, col)) {
            Cell validCell = board[nextRow][col];

            if(validCell.getOccupyingPiece() == null) {
                legalMoves.add(validCell);

                // Moving forward two steps if we are on the start position of a Pawn
                if ((isWhite && row == 6) || (!isWhite && row == 1)) {
                    int doubleMoveRow = row + 2 * direction;
                    if (isWithinBounds(doubleMoveRow, col)) {
                        validCell = board[doubleMoveRow][col];
                        if(validCell.getOccupyingPiece() == null) {
                            legalMoves.add(validCell);
                            }
                    }
                }
            }
        }

        // Moving on diagonal and capturing a piece
        // -1 and +1 for moving diagonally to the left and to the right
        for (int dCol = -1; dCol <= 1; dCol += 2) {
            int captureCol = col + dCol;
            if (isWithinBounds(nextRow, captureCol)) {
                Cell enemyCell = board[nextRow][captureCol];
                ChessPiece occupant = enemyCell.getOccupyingPiece();
                if (occupant != null && occupant.isWhite() != this.isWhite()) {
                    legalMoves.add(enemyCell);
                }
            }
        }

        return legalMoves;
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}
