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
        addTestPawns();
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

    private void addTestPawns() {
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

        Cell startingCell = getCellByNotation("d4");
        if (startingCell != null) {
            Pawn whitePawn = new Pawn(startingCell, true);
            add(whitePawn);
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
