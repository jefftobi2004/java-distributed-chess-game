import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class King extends ChessPiece {

    int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };

    public King(Cell startingCell, boolean isWhite) {
        super(startingCell, isWhite, isWhite ? "/ChessSprites/wK.png" : "/ChessSprites/bK.png");
    }

    @Override
    public List<Cell> getLegalMoves(Cell[][] board) {
        List<Cell> legalMoves = new ArrayList<>();

        int currentRow = currentCell.getRow();
        int currentCol = currentCell.getCol();

        ChessBoardPanel chessBoard = (ChessBoardPanel) SwingUtilities.getAncestorOfClass(ChessBoardPanel.class, this);
        if (chessBoard == null) return legalMoves;

        for (int[] dir : directions) {

            int newRow = currentRow + dir[0];
            int newCol = currentCol + dir[1];

            if(isWithinBounds(newRow, newCol)) {
                Cell validCell = board[newRow][newCol];
                ChessPiece occupant = validCell.getOccupyingPiece();

                if(occupant != null && this.isWhite() == occupant.isWhite()) {
                    continue;
                }

                boolean underAttack = chessBoard.isCellUnderAttack(validCell, !this.isWhite());
                if(!underAttack) {
                        legalMoves.add(validCell);
                }
            }
        }
        return legalMoves;
    }

    // We will need to overrride this to prevent an infinite loop from taking place in the logic of getLegalMoves()
    @Override
    public List<Cell> getAttackedCells(Cell[][] board) {
        List<Cell> attacked = new ArrayList<>();

        int currentRow = currentCell.getRow();
        int currentCol = currentCell.getCol();

        for (int[] dir : directions) {

            int newRow = currentRow + dir[0];
            int newCol = currentCol + dir[1];

            if(isWithinBounds(newRow, newCol)) {

                Cell validCell = board[newRow][newCol];
                attacked.add(validCell);
            }
        }
        return attacked;
    }

}
