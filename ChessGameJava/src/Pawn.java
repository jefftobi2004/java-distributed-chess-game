import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

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

        ChessBoardPanel chessBoard = (ChessBoardPanel) SwingUtilities.getAncestorOfClass(ChessBoardPanel.class, this);
        if (chessBoard == null) return legalMoves;

        // Moving forward one step
        int nextRow = row + direction;
        if (isWithinBounds(nextRow, col)) {
            Cell validCell = board[nextRow][col];

            if(validCell.getOccupyingPiece() == null && chessBoard.simulatedMoveAvoidsCheck(this, validCell)) {
                legalMoves.add(validCell);

                // Moving forward two steps if we are on the start position of a Pawn
                if ((isWhite && row == 6) || (!isWhite && row == 1)) {
                    int doubleMoveRow = row + 2 * direction;
                    if (isWithinBounds(doubleMoveRow, col)) {
                        validCell = board[doubleMoveRow][col];
                        if(validCell.getOccupyingPiece() == null && chessBoard.simulatedMoveAvoidsCheck(this, validCell)) {
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
                if (occupant != null && occupant.isWhite() != this.isWhite() && chessBoard.simulatedMoveAvoidsCheck(this, enemyCell)) {
                    legalMoves.add(enemyCell);
                }
            }
        }

        return legalMoves;
    }

    @Override
    public List<Cell> getAttackedCells(Cell[][] board) {
        List<Cell> attacked = new ArrayList<>();

        int dir = isWhite() ? -1 : 1;
        int row = currentCell.getRow();
        int col = currentCell.getCol();

        for (int dc = -1; dc <= 1; dc += 2) {
            int newRow = row + dir;
            int newCol = col + dc;
            if (isWithinBounds(newRow, newCol)) {
                attacked.add(board[newRow][newCol]);
            }
        }

        return attacked;
    }

}
