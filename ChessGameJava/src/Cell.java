import java.awt.*;

/**
 * We will use this class to identify the position and notation of each cell on the Chess Board.
 * Furthermore, we will initialize the variable bounds with the necessary information for filling a rectangle on the board.
 * We do this by setting the upper left and lower right coordinates of the cell in `bounds`
 */

public class Cell {
    private int row, col;
    private String notation;
    private Rectangle bounds;
    private boolean highlighted = false;
    private ChessPiece occupyingPiece;

    public Cell(int row, int col, int tileSize) {
        this.row = row;
        this.col = col;
        this.notation = (char)('a' + col) + String.valueOf(8 - row);
        this.bounds = new Rectangle(col * tileSize, row * tileSize, tileSize, tileSize);
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void paintCell(Graphics g, boolean isWhite) {
        // We color the cell with the base colors of the board
        g.setColor(isWhite ? new Color(240, 217, 181) : new Color(181, 136, 99));
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        if (highlighted) {
            // If we press on a chess piece, then we highlight the cell with a transparent color
            Color translucentYellow = new Color(255, 255, 0, 80);  // alpha 80
            g.setColor(translucentYellow);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

            // We'll add a transparent grey circle in the middle as well
            Color translucentGray = new Color(128, 128, 128, 120);
            g.setColor(translucentGray);

            int diameter = Math.min(bounds.width, bounds.height) / 4; // circle a quarter of the cell size
            int circleX = bounds.x + (bounds.width - diameter) / 2;
            int circleY = bounds.y + (bounds.height - diameter) / 2;

            g.fillOval(circleX, circleY, diameter, diameter);
        }
    }

    public ChessPiece getOccupyingPiece() {
        return occupyingPiece;
    }

    public void setOccupyingPiece(ChessPiece piece) {
        this.occupyingPiece = piece;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getNotation() {
        return notation;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
