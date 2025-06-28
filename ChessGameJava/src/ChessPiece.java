import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ChessPiece extends JButton {
    protected Cell currentCell;
    protected boolean isWhite;
    protected int pieceSize = 100;

    public ChessPiece(Cell startingCell, boolean isWhite, String iconPath) {
        setCurrentCell(startingCell);
        this.isWhite = isWhite;

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        Image scaled = icon.getImage().getScaledInstance(pieceSize, pieceSize, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaled));

        centerOnCell();

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addActionListener(e -> onPieceClicked());
    }

    private void onPieceClicked() {
        ChessBoardPanel board = (ChessBoardPanel) SwingUtilities.getAncestorOfClass(ChessBoardPanel.class, this);
        if (board == null) return;

        // We check if there is currently another piece selected when we click
        ChessPiece selected = board.getSelectedPiece();

        if (selected == null) {
            // If there's no piece selected we'll set this one to be,
            // but only if its color matches the color of the current player's pieces
            if (this.isWhite() == board.isWhiteTurn()) {
                selectAndHighlightLegalMoves(board);
            }
            return;
        }

        // If another piece is already selected, then we check if this piece is an enemy that can captured
        if (this != selected && this.isWhite() != selected.isWhite()) {
            Cell myCell = this.getCurrentCell(); // We find the cell where the enemy piece is place on
            if (myCell.isHighlighted()) { // If it is highlighted, then we can capture it
                selected.moveTo(myCell); // Capture the cell
                board.setSelectedPiece(null);
                board.clearHighlights();
                board.repaint();
                return;
            }
        }

        // Selecting a different piece of the same color
        if (this != selected && this.isWhite() == board.isWhiteTurn()) {
            selectAndHighlightLegalMoves(board);
            return;
        }

        // Deselecting current piece
        if (selected == this) {
            board.clearHighlights();
            board.setSelectedPiece(null);
            board.repaint();
        }

    }

    private void selectAndHighlightLegalMoves(ChessBoardPanel board) {
        board.clearHighlights();
        board.setSelectedPiece(this);
        currentCell.setHighlighted(true);

        for (Cell moveCell : getLegalMoves(board.getCells())) {
            moveCell.setHighlighted(true);
            System.out.print(moveCell.getNotation());
        }
        board.repaint();
        System.out.println();
    }

    public void moveTo(Cell targetCell) {
        // If the cell where we can move has an enemy piece on it, we need to eliminate it before moving
        ChessPiece enemy = targetCell.getOccupyingPiece();
        if (enemy != null && enemy.isWhite() != this.isWhite()) {
            Container parent = enemy.getParent();
            if(parent != null) {
                parent.remove(enemy);
                parent.revalidate();
            }
            enemy.setCurrentCell(null);
        }

        // We move the piece to the new Cell
        setCurrentCell(targetCell);

        // Changing the turn for the other color player and repainting the Panel
        ChessBoardPanel board = (ChessBoardPanel) SwingUtilities.getAncestorOfClass(ChessBoardPanel.class, this);
        if (board != null) {
            board.switchTurn();
            board.repaint();
        }
    }

    public void setCurrentCell(Cell newCell) {
        if (this.currentCell != null) {
            this.currentCell.setOccupyingPiece(null);
        }
        this.currentCell = newCell;
        if (newCell != null) {
            newCell.setOccupyingPiece(this);
            centerOnCell();
        }
    }

    public void centerOnCell() {
        Rectangle cellBounds = currentCell.getBounds();
        int x = cellBounds.x + (cellBounds.width - pieceSize) / 2;
        int y = cellBounds.y + (cellBounds.height - pieceSize) / 2;
        setBounds(x, y, pieceSize, pieceSize);
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public boolean isWhite() {
        return isWhite;
    }

    protected static boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    /**
     * We use this method to find the legal moves of a certain Chess Piece
     * @param board The matrix of cells on the Chess Board
     * @return A list of cells where the piece calling this method can move
     */
    public abstract java.util.List<Cell> getLegalMoves(Cell[][] board);


    /**
     * We use this method to find the capturing cells of a certain Chess Piece.<br><br>
     * In usual cases, the movement cells will correspond with the capturing ones.
     * But there are special scenarios (ex. Pawns) where a piece's movement differs from its capturing movement.<br>
     * In these scenarios, the method should be overridden.<br><br>
     * This will prove useful later on, when we will try to prevent the King from moving into attacked cells.
     * @param board The matrix of cells on the Chess Board
     * @return A list of cells where the piece calling this method could capture another piece
     */
    public java.util.List<Cell> getAttackedCells(Cell[][] board) {
        return getLegalMoves(board);
    }

}
