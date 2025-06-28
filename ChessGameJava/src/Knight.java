import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Knight extends ChessPiece {

    private static final int[][] directions = {
            {-2, -1}, {-2,  1},
            {-1, -2}, {-1,  2},
            { 1, -2}, { 1,  2},
            { 2, -1}, { 2,  1}
    };

    public Knight(Cell startingCell, boolean isWhite) {
        super(startingCell, isWhite, isWhite ? "/ChessSprites/wN.png" : "/ChessSprites/bN.png");
    }

    @Override
    public List<Cell> getLegalMoves(Cell[][] board) {

        List<Cell> legalMoves = new ArrayList<>();

        ChessBoardPanel chessBoard = (ChessBoardPanel) SwingUtilities.getAncestorOfClass( ChessBoardPanel.class, this);
        if (chessBoard == null) return legalMoves;

        for (int[] dir : directions) {

            int newRow = currentCell.getRow() + dir[0];
            int newCol = currentCell.getCol() + dir[1];

            if (isWithinBounds(newRow, newCol)) {
                Cell validCell = board[newRow][newCol];
                ChessPiece occupant = validCell.getOccupyingPiece();

                // Can't move on friendly piece
                if (occupant != null && occupant.isWhite() == this.isWhite())
                    continue;

                if (chessBoard.simulatedMoveAvoidsCheck(this, validCell)) {
                    legalMoves.add(validCell);
                }
            }
        }

        return legalMoves;
    }

    @Override
    public List<Cell> getAttackedCells(Cell[][] board) {
        List<Cell> attacked = new ArrayList<>();

        for (int[] dir : directions) {
            int newRow = currentCell.getRow() + dir[0];
            int newCol = currentCell.getCol() + dir[1];

            if (isWithinBounds(newRow, newCol)) {

                Cell validCell = board[newRow][newCol];
                attacked.add(validCell);
            }
        }

        return attacked;
    }
}
