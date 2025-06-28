import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessBoardPanel extends JPanel {
    private static final int TILE_SIZE = 120;
    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;

    private boolean isWhiteTurn = true;

    private Cell[][] cells;

    private ChessPiece selectedPiece = null;

    public ChessBoardPanel() {
        setPreferredSize(new Dimension(TILE_SIZE * BOARD_WIDTH, TILE_SIZE * BOARD_HEIGHT));
        setLayout(null);
        initCells();
        initMouseListener();
        addChessPieces();
    }

    private void initCells() {
        cells = new Cell[BOARD_HEIGHT][BOARD_WIDTH];
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                cells[row][col] = new Cell(row, col, TILE_SIZE);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        boolean isWhite = true;
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                Cell cell = cells[row][col];
                cell.paintCell(g, isWhite);

                Rectangle bounds = cell.getBounds();
                if(row == 7) {
                    g.setColor(isWhite ? Color.BLACK : Color.WHITE);
                    g.setFont(new Font("SansSerif", Font.PLAIN, 15));
                    g.drawString(String.valueOf(cell.getNotation().charAt(0)),
                            bounds.x + TILE_SIZE - 15, bounds.y + TILE_SIZE - 5);
                }

                if(col == 0) {
                    g.setColor(isWhite ? Color.BLACK : Color.WHITE);
                    g.setFont(new Font("SansSerif", Font.PLAIN, 15));
                    g.drawString(String.valueOf(cell.getNotation().charAt(1)),
                            bounds.x + 5, bounds.y + 15);
                }

                isWhite = !isWhite;
            }
            isWhite = !isWhite;
        }
    }

    private void initMouseListener() {
//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                Point clickPoint = e.getPoint();
//
//                for (int row = 0; row < BOARD_HEIGHT; row++) {
//                    for (int col = 0; col < BOARD_WIDTH; col++) {
//                        Cell cell = cells[row][col];
//                        if (cell.getBounds().contains(clickPoint)) {
//                            System.out.println("Clicked on: " + cell.getNotation());
//                        }
//                    }
//                }
//            }
//        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                System.out.println("Clicked at: " + clickPoint);
                for (int row = 0; row < BOARD_HEIGHT; row++) {
                    for (int col = 0; col < BOARD_WIDTH; col++) {
                        Cell clickedCell = cells[row][col];
                        if (clickedCell.getBounds().contains(clickPoint)) {
                            if (clickedCell.isHighlighted() && selectedPiece != null) {
                                selectedPiece.moveTo(clickedCell);
                                setSelectedPiece(null);
                                clearHighlights();
                                repaint();
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Checks whether a given cell is under attack by any pieces of the specified color.
     *
     * @param cell The target cell to check.
     * @param byWhite True to check for attacks by white pieces, false for black.
     * @return True if the cell is attacked, false otherwise.
     */
    public boolean isCellUnderAttack(Cell cell, boolean byWhite) {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                ChessPiece piece = cells[row][col].getOccupyingPiece();
                if (piece != null && piece.isWhite() == byWhite) {
                    java.util.List<Cell> moves = piece.getAttackedCells(cells);
                    if (moves.contains(cell)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Finds the cell where the King of the specified color is located.
     *
     * @param forWhite true -> white King, false -> black King
     * @return the Cell containing the King, or null if not found
     */
    public Cell findKingCell(boolean forWhite) {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                ChessPiece piece = cells[row][col].getOccupyingPiece();
                if (piece instanceof King && piece.isWhite() == forWhite) {
                    return cells[row][col];
                }
            }
        }
        return null;
    }

    /**
     * Checks if the King of the specified color is currently under attack.
     *
     * @param forWhite true to check if the white King is in check, false for the black King
     * @return true if the King is under attack (in check), false otherwise
     */
    public boolean isInCheck(boolean forWhite) {
        Cell kingCell = findKingCell(forWhite);
        if (kingCell == null) {
            System.out.println("No king cell found !!!");
            return false;
        } else {
            System.out.println("Is king under attack: " + isCellUnderAttack(kingCell, !forWhite));
            return isCellUnderAttack(kingCell, !forWhite);
        }

    }

    /**
     * Simulates moving a piece to a destination cell and checks whether the player's King
     * remains safe (not in check) after this hypothetical move.
     *
     * This method temporarily updates the board state, checks for the condition, then restores the board.
     * It will not result in any visual clutter since we are not repainting
     *
     * @param piece the ChessPiece to move
     * @param destination the Cell to move the piece to
     * @return true if the move would solve the check,
     *         false if the King would still be in check after the move
     */
    public boolean simulatedMoveAvoidsCheck(ChessPiece piece, Cell destination) {

        Cell original = piece.getCurrentCell();
        ChessPiece captured = destination.getOccupyingPiece();

        original.setOccupyingPiece(null);
        piece.setCurrentCell(destination);
        destination.setOccupyingPiece(piece);

        boolean stillInCheck = isInCheck(piece.isWhite());

        piece.setCurrentCell(original);
        destination.setOccupyingPiece(captured);

        return !stillInCheck;
    }


    private void addChessPieces() {
        addPawns();     addKings();
        addKnights();   addBishops();
        addRooks();     addQueens();
    }

    private void addPawns() {
        for (char col = 'a'; col <= 'h'; col++) {
            String notationWhite = col + "2";
            Cell startingCell = getCellByNotation(notationWhite);
            if (startingCell != null) {
                Pawn whitePawn = new Pawn(startingCell, true);
                add(whitePawn);
            }

            String notationBlack = col + "7";
            startingCell = getCellByNotation(notationBlack);
            if (startingCell != null) {
                Pawn blackPawn = new Pawn(startingCell, false);
                add(blackPawn);
            }
        }
        repaint();
    }

    private void addKings() {
        Cell whiteKingCell = getCellByNotation("e1");
        Cell blackKingCell = getCellByNotation("e8");

        if (whiteKingCell != null) {
            King whiteKing = new King(whiteKingCell, true);
            add(whiteKing);
        }

        if (blackKingCell != null) {
            King blackKing = new King(blackKingCell, false);
            add(blackKing);
        }
        repaint();
    }

    private void addKnights() {
        // White knights at b1, g1
        String[] whiteNotations = {"b1", "g1"};
        for (String notation : whiteNotations) {
            Cell cell = getCellByNotation(notation);
            if (cell != null) {
                Knight knight = new Knight(cell, true);
                add(knight);
            }
        }

        // Black knights at b8, g8
        String[] blackNotations = {"b8", "g8"};
        for (String notation : blackNotations) {
            Cell cell = getCellByNotation(notation);
            if (cell != null) {
                Knight knight = new Knight(cell, false);
                add(knight);
            }
        }
        repaint();
    }

    private void addBishops() {
        // White bishops at c1, f1
        String[] whiteNotations = {"c1", "f1"};
        for (String notation : whiteNotations) {
            Cell cell = getCellByNotation(notation);
            if (cell != null) {
                Bishop bishop = new Bishop(cell, true);
                add(bishop);
            }
        }

        // Black bishops at c8, f8
        String[] blackNotations = {"c8", "f8"};
        for (String notation : blackNotations) {
            Cell cell = getCellByNotation(notation);
            if (cell != null) {
                Bishop bishop = new Bishop(cell, false);
                add(bishop);
            }
        }
        repaint();
    }

    private void addRooks() {
        // White bishops at a1, h1
        String[] whiteNotations = {"a1", "h1"};
        for (String notation : whiteNotations) {
            Cell cell = getCellByNotation(notation);
            if (cell != null) {
                Rook rook = new Rook(cell, true);
                add(rook);
            }
        }

        // Black bishops at a8, h8
        String[] blackNotations = {"a8", "h8"};
        for (String notation : blackNotations) {
            Cell cell = getCellByNotation(notation);
            if (cell != null) {
                Rook rook = new Rook(cell, false);
                add(rook);
            }
        }
        repaint();
    }

    private void addQueens() {

        Cell whiteQueenCell = getCellByNotation("d1");
        Cell blackQueenCell = getCellByNotation("d8");

        if (whiteQueenCell != null) {
            Queen whiteQueen = new Queen(whiteQueenCell, true);
            add(whiteQueen);
        }

        if (blackQueenCell != null) {
            Queen blackQueen = new Queen(blackQueenCell, false);
            add(blackQueen);
        }
        repaint();
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void clearHighlights() {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                cells[row][col].setHighlighted(false);
            }
        }
    }

    public Cell getCellByNotation(String notation) {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (cells[row][col].getNotation().equalsIgnoreCase(notation)) {
                    return cells[row][col];
                }
            }
        }
        return null;
    }

    public ChessPiece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(ChessPiece piece) {
        this.selectedPiece = piece;
    }

    public void switchTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess Board");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ChessBoardPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
