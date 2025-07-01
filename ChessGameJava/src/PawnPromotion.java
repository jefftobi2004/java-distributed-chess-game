import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PawnPromotion extends JDialog {

    private final boolean isWhite;
    private final Cell promotionCell;
    private final ChessBoardPanel chessBoard;

    public PawnPromotion(boolean isWhite, Cell promotionCell, ChessBoardPanel chessBoard) {

        super(SwingUtilities.getWindowAncestor(chessBoard), "Pawn Promotion", Dialog.ModalityType.APPLICATION_MODAL);

        this.isWhite = isWhite;
        this.promotionCell = promotionCell;
        this.chessBoard = chessBoard;

        setSize(420, 120);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addPromotionButton("Queen", isWhite ? "/ChessSprites/wQ.png" : "/ChessSprites/bQ.png");
        addPromotionButton("Rook", isWhite ? "/ChessSprites/wR.png" : "/ChessSprites/bR.png");
        addPromotionButton("Bishop", isWhite ? "/ChessSprites/wB.png" : "/ChessSprites/bB.png");
        addPromotionButton("Knight", isWhite ? "/ChessSprites/wN.png" : "/ChessSprites/bN.png");

        setLocationRelativeTo(chessBoard);
        setVisible(true);
    }

    private void addPromotionButton(String pieceName, String imagePath) {
        JButton button = new JButton();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImage);
            button.setIcon(icon);
        } catch (Exception e) {
            button.setText(pieceName); // if loading the image fails, we'll just display a text
        }

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promotePawn(pieceName);
                dispose(); // close promotion window
            }
        });
        add(button);
    }

    private void promotePawn(String pieceName) {
        // Remove the pawn
        ChessPiece pawn = promotionCell.getOccupyingPiece();
        if (pawn != null) {
            chessBoard.remove(pawn);
            promotionCell.setOccupyingPiece(null);
        }

        // Add the new piece
        ChessPiece newPiece = switch (pieceName) {
            case "Queen" -> new Queen(promotionCell, isWhite);
            case "Rook" -> new Rook(promotionCell, isWhite);
            case "Bishop" -> new Bishop(promotionCell, isWhite);
            case "Knight" -> new Knight(promotionCell, isWhite);
            default -> null;
        };

        if (newPiece != null) {
            chessBoard.add(newPiece);
            promotionCell.setOccupyingPiece(newPiece);
            chessBoard.repaint();
        }
    }

    public static void main(String[] args) {
        new PawnPromotion(false, null, null);
    }
}
