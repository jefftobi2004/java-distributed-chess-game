import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Rook extends ChessPiece {

    private static final int[][] directions = {
            {-1, 0}, {0,  1},
            {0, -1}, {1,  0}
    };

    public Rook(Cell startingCell, boolean isWhite) {

        super(startingCell, isWhite, isWhite ? "/ChessSprites/wR.png" : "/ChessSprites/bR.png");
    }

    @Override
    public List<Cell> getLegalMoves(Cell[][] board) {

        List<Cell> legalMoves = new ArrayList<>();
        ChessBoardPanel chessBoard = (ChessBoardPanel) SwingUtilities.getAncestorOfClass(ChessBoardPanel.class, this);
        if (chessBoard == null) return legalMoves;

        int row = currentCell.getRow();
        int col = currentCell.getCol();

        for (int[] dir : directions) {

            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // We proceed diagonally until we meet another piece, or we go out of bounds
            while (isWithinBounds(newRow, newCol)) {

                Cell validCell = board[newRow][newCol];
                ChessPiece occupant = validCell.getOccupyingPiece();

                if (occupant == null) {
                    // If cell is empty we verify it is a valid movement
                    if (chessBoard.simulatedMoveAvoidsCheck(this, validCell)) {
                        legalMoves.add(validCell);
                    }
                }
                else {
                    // If cell is occupied by a friendly or foe piece we verify it for a valid movement
                    if (occupant.isWhite() != this.isWhite()) {
                        if (chessBoard.simulatedMoveAvoidsCheck(this, validCell)) {
                            legalMoves.add(validCell);
                        }
                    }
                    // After this, it means we hit an obstacle, so we cease movement
                    break;
                }

                // After one iteration, we step further along the diagonal
                newRow += dir[0];
                newCol += dir[1];
            }
        }

        return legalMoves;
    }

    @Override
    public List<Cell> getAttackedCells(Cell[][] board) {
        List<Cell> attacked = new ArrayList<>();

        int row = currentCell.getRow();
        int col = currentCell.getCol();

        for (int[] dir : directions) {

            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // add every square up to and including the first blocker
            while (isWithinBounds(newRow, newCol)) {

                Cell validCell = board[newRow][newCol];
                attacked.add(validCell);
                if (validCell.getOccupyingPiece() != null && !(validCell.getOccupyingPiece() instanceof King)) {
                    break;
                }
                newRow += dir[0];
                newCol += dir[1];
            }
        }
        return attacked;
    }
}
