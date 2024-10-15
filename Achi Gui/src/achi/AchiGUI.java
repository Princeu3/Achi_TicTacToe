/* AchiGUI.java - Updated */

package achi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AchiGUI {

    private JFrame frame;
    private JPanel boardPanel;
    private GameBoard gameBoard;
    private int currentPlayer;
    private boolean movePhase;
    private int moveCount;
    private String player1Name;
    private String player2Name;
    private JLabel statusLabel;

    // Icons for cross and circle
    private ImageIcon crossIcon;
    private ImageIcon circleIcon;

    public AchiGUI() {
        gameBoard = new GameBoard();
        currentPlayer = 1; // Player X starts
        movePhase = false; // Start with drop phase
        moveCount = 0; // Initialize move count
        loadIcons();
        welcomeDialog();
        player1Name = getPlayerName("Player one");
        player2Name = getPlayerName("Player two");
        initializeMainFrame();
        announceMove();
    }

    private void loadIcons() {
        // Load the icons for cross and circle
        crossIcon = new ImageIcon(getClass().getResource("/resources/cross.png"));
        circleIcon = new ImageIcon(getClass().getResource("/resources/circle.png"));
    }

    private void welcomeDialog() {
        JOptionPane.showMessageDialog(frame, "Welcome to the Game of Achi.", "Welcome", JOptionPane.INFORMATION_MESSAGE);
    }

    private void initializeMainFrame() {
        frame = new JFrame("Achi Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use BorderLayout for the frame
        frame.setLayout(new BorderLayout());

        initializeBoardPanel();
        initializeStatusLabel();
        initializeResetButton();

        frame.setPreferredSize(new Dimension(600, 700)); // Adjust dimensions as needed
        frame.pack(); // Adjust frame size based on its components
        frame.setVisible(true);
    }

    private void initializeBoardPanel() {
        boardPanel = new JPanel(new GridLayout(3, 3));
        frame.add(boardPanel, BorderLayout.CENTER);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                initializeCellPanel(row, col);
            }
        }
    }

    private void initializeCellPanel(int row, int col) {
        JPanel cellPanel = new JPanel(new BorderLayout());
        cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cellPanel.setBackground(Color.WHITE);

        cellPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cellClicked(row, col);
            }
        });

        boardPanel.add(cellPanel);
    }

    private void initializeResetButton() {
        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> resetGame());
        frame.add(resetButton, BorderLayout.SOUTH);
    }

    private void initializeStatusLabel() {
        statusLabel = new JLabel(getCurrentPlayerName() + "'s turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(statusLabel, BorderLayout.NORTH);
    }

    private void cellClicked(int row, int col) {
        if (!movePhase) {
            handleDropPhase(row, col);
        } else {
            handleMovePhase(row, col);
        }
    }

    private void handleDropPhase(int row, int col) {
        if (gameBoard.isDropPhaseMoveLegal(row, col)) {
            gameBoard.dropPhasePutPieceAtLocation(row, col, currentPlayer);
            updateCell(row, col, currentPlayer);
            moveCount++;

            if (moveCount >= 8) {
                movePhase = true;
                gameBoard.findEmptyCell();
            }

            checkForWinsAndSwitchPlayer();
        } else {
            statusLabel.setText("Cell is already occupied. Choose another cell.");
        }
    }

    private void handleMovePhase(int row, int col) {
        if (!gameBoard.canPlayerMove(currentPlayer)) {
            statusLabel.setText("No legal moves available. " + getCurrentPlayerName() + " wins by default!");
            handleGameEnd();
            return;
        }

        if (gameBoard.isMovePhaseMoveLegal(row, col, currentPlayer)) {
            int emptyRow = gameBoard.getRowOfEmptyCell();
            int emptyCol = gameBoard.getColOfEmptyCell();

            // Animate the move
            animateMove(row, col, emptyRow, emptyCol, currentPlayer);

            // Update the game board after animation
            gameBoard.movePieceFromLocation(row, col, emptyRow, emptyCol, currentPlayer);

            // Update the GUI cells
            updateCell(row, col, 0);
            updateCell(emptyRow, emptyCol, currentPlayer);

            checkForWinsAndSwitchPlayer();
        } else {
            statusLabel.setText("Invalid move. Try again.");
        }
    }

    private void animateMove(int fromRow, int fromCol, int toRow, int toCol, int player) {
        JPanel fromCell = (JPanel) boardPanel.getComponent(fromRow * 3 + fromCol);
        JPanel toCell = (JPanel) boardPanel.getComponent(toRow * 3 + toCol);

        // Create a label to represent the moving piece
        JLabel pieceLabel = new JLabel();
        pieceLabel.setSize(fromCell.getSize());
        pieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pieceLabel.setVerticalAlignment(SwingConstants.CENTER);

        if (player == 1) {
            pieceLabel.setIcon(crossIcon);
        } else {
            pieceLabel.setIcon(circleIcon);
        }

        // Get locations relative to the screen
        Point fromCellLocation = fromCell.getLocationOnScreen();
        Point toCellLocation = toCell.getLocationOnScreen();

        // Create a JWindow for the animation
        JWindow animationWindow = new JWindow();
        animationWindow.getContentPane().add(pieceLabel);
        animationWindow.setSize(pieceLabel.getSize());
        animationWindow.setLocation(fromCellLocation);
        animationWindow.setVisible(true);

        // Calculate movement increments
        int deltaX = toCellLocation.x - fromCellLocation.x;
        int deltaY = toCellLocation.y - fromCellLocation.y;

        int steps = 20;
        int delay = 15; // milliseconds

        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            int currentStep = 0;

            public void actionPerformed(ActionEvent e) {
                if (currentStep <= steps) {
                    int x = fromCellLocation.x + (deltaX * currentStep) / steps;
                    int y = fromCellLocation.y + (deltaY * currentStep) / steps;
                    animationWindow.setLocation(x, y);
                    currentStep++;
                } else {
                    timer.stop();
                    animationWindow.setVisible(false);
                    animationWindow.dispose();
                }
            }
        });
        timer.start();
    }

    private void announceMove() {
        String playerName = getCurrentPlayerName();
        String playerSymbol = (currentPlayer == 1) ? "X" : "O";
        statusLabel.setText(playerName + "'s turn (" + playerSymbol + ")");
        if (currentPlayer == 1) {
            statusLabel.setForeground(Color.RED);
        } else {
            statusLabel.setForeground(Color.BLUE);
        }
    }

    private void checkForWinsAndSwitchPlayer() {
        if (checkForWins()) return;
        switchPlayerTurn();
        announceMove();
    }

    private boolean checkForWins() {
        if (gameBoard.checkRowWins(currentPlayer) ||
                gameBoard.checkColWins(currentPlayer) ||
                gameBoard.checkDiagonalWins(currentPlayer)) {
            announceWinner();
            return true;
        }
        return false;
    }

    private void switchPlayerTurn() {
        currentPlayer = -currentPlayer;
    }

    private void updateCell(int row, int col, int player) {
        JPanel cellPanel = (JPanel) boardPanel.getComponent(row * 3 + col);
        cellPanel.removeAll(); // Remove any existing symbols

        if (player == 1) {
            // Use cross symbol
            JLabel label = new JLabel();
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setIcon(crossIcon);
            cellPanel.add(label, BorderLayout.CENTER);
        } else if (player == -1) {
            // Use circle symbol
            JLabel label = new JLabel();
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setIcon(circleIcon);
            cellPanel.add(label, BorderLayout.CENTER);
        }

        cellPanel.revalidate();
        cellPanel.repaint();
    }

    private void resetGame() {
        gameBoard = new GameBoard();
        currentPlayer = 1;
        movePhase = false;
        moveCount = 0;

        Component[] components = boardPanel.getComponents();
        for (Component component : components) {
            JPanel cellPanel = (JPanel) component;
            cellPanel.setBackground(Color.WHITE);
            cellPanel.removeAll();
            cellPanel.revalidate();
            cellPanel.repaint();
        }

        announceMove();
    }

    private String getPlayerName(String playerLabel) {
        String inputValue = JOptionPane.showInputDialog(playerLabel + ", what is your name?");
        if (inputValue == null || inputValue.trim().isEmpty()) {
            System.exit(0);
        }
        return inputValue.split(" ")[0];
    }

    private void announceWinner() {
        highlightWinningLine();
        statusLabel.setText(getCurrentPlayerName() + " wins this game!");
        handleGameEnd();
    }

    private void handleGameEnd() {
        int option = JOptionPane.showOptionDialog(frame, "Do you want to play another game?", "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

        if (option == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    private void highlightWinningLine() {
        int[][] winningLine = gameBoard.getWinningLine(currentPlayer);
        if (winningLine != null) {
            for (int[] position : winningLine) {
                int row = position[0];
                int col = position[1];
                JPanel cellPanel = (JPanel) boardPanel.getComponent(row * 3 + col);
                cellPanel.setBackground(Color.YELLOW);
            }
        }
    }

    private String getCurrentPlayerName() {
        return (currentPlayer == 1) ? player1Name : player2Name;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AchiGUI::new);
    }
}
