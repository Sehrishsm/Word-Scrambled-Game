
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class GameFrame extends JFrame {

    private final WordManager wordManager = new WordManager();
    private final Preferences preferences = Preferences.userNodeForPackage(GameFrame.class);

    private final Color background = new Color(18, 25, 39);
    private final Color panelColor = new Color(29, 40, 58);
    private final Color tileColor = new Color(48, 64, 88);
    private final Color accentColor = new Color(20, 166, 160);

    private JLabel timerLabel;
    private JLabel scoreLabel;
    private JLabel highScoreLabel;
    private JLabel wordLabel;
    private JLabel feedbackLabel;
    private JLabel hintLabel;
    private JLabel roundLabel;
    private JTextField answerField;

    private JButton checkButton;
    private JButton hintButton;
    private JButton nextButton;

    private Timer gameTimer;

    private String currentWord;
    private int score = 0;
    private int highScore;
    private int timeLeft = 60;
    private int round = 0;

    private boolean hintUsed = false;
    private boolean roundFinished = false;

    private final Set<String> usedWords = new HashSet<>();

    public GameFrame() {
        setTitle("Word Scramble Game");
        setSize(650, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        highScore = preferences.getInt("highScore", 0);

        getContentPane().setBackground(background);
        setLayout(new BorderLayout());

        createHeader();
        createGamePanel();
        createBottomPanel();

        startNewGame();
    }

    private void createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(background);
        header.setBorder(new EmptyBorder(22, 30, 15, 30));

        JLabel title = new JLabel("WORD SCRAMBLE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Unscramble the letters and test your vocabulary");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(155, 170, 190));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(background);
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 8, 0));
        statsPanel.setBackground(background);

        timerLabel = createStatLabel("TIME", "60s");
        scoreLabel = createStatLabel("SCORE", "0");
        highScoreLabel = createStatLabel("BEST", String.valueOf(highScore));

        statsPanel.add(timerLabel);
        statsPanel.add(scoreLabel);
        statsPanel.add(highScoreLabel);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(statsPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private JLabel createStatLabel(String title, String value) {
        JLabel label = new JLabel(
                "<html><div style='text-align:center;'>" +
                        "<span style='font-size:10px;color:#A0B0C5;'>" +
                        title + "</span><br>" +
                        "<span style='font-size:19px;color:white;'>" +
                        value + "</span></div></html>");

        label.setOpaque(true);
        label.setBackground(panelColor);
        label.setBorder(new EmptyBorder(10, 16, 10, 16));
        return label;
    }

    private void createGamePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(background);
        mainPanel.setBorder(new EmptyBorder(15, 35, 10, 35));

        roundLabel = new JLabel("ROUND " + (round + 1));
        roundLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        roundLabel.setForeground(accentColor);
        roundLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instruction = new JLabel("Unscramble this word");
        instruction.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        instruction.setForeground(new Color(180, 192, 208));
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        wordLabel = new JLabel("READY?");
        wordLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        wordLabel.setForeground(Color.WHITE);
        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel wordCard = new JPanel(new GridBagLayout());
        wordCard.setBackground(panelColor);
        wordCard.setPreferredSize(new Dimension(540, 160));
        wordCard.setMaximumSize(new Dimension(540, 160));
        wordCard.setMinimumSize(new Dimension(540, 160));
        wordCard.add(wordLabel);

        answerField = new JTextField();
        answerField.setFont(new Font("Segoe UI", Font.PLAIN, 19));
        answerField.setForeground(Color.WHITE);
        answerField.setCaretColor(Color.WHITE);
        answerField.setBackground(tileColor);
        answerField.setHorizontalAlignment(JTextField.CENTER);
        answerField.setBorder(new EmptyBorder(12, 15, 12, 15));
        answerField.setMaximumSize(new Dimension(400, 48));

        answerField.addActionListener(this::checkAnswer);

        checkButton = createButton("CHECK ANSWER", accentColor);
        checkButton.addActionListener(this::checkAnswer);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        feedbackLabel.setForeground(Color.WHITE);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hintLabel = new JLabel(" ");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hintLabel.setForeground(new Color(190, 200, 215));
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(roundLabel);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(instruction);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(wordCard);
        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(answerField);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(checkButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(feedbackLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(hintLabel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        bottomPanel.setBackground(background);

        hintButton = createButton("GET A HINT", tileColor);
        nextButton = createButton("NEXT WORD", tileColor);
        JButton restartButton = createButton("RESTART", tileColor);

        hintButton.addActionListener(e -> showHint());
        nextButton.addActionListener(e -> loadNextWord());
        restartButton.addActionListener(e -> startNewGame());

        bottomPanel.add(hintButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(restartButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(13, 20, 13, 20));

        return button;
    }

    private void startNewGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }

        score = 0;
        round = 0;
        timeLeft = 60;
        usedWords.clear();

        updateStats();

        checkButton.setEnabled(true);
        hintButton.setEnabled(true);
        nextButton.setEnabled(true);
        answerField.setEnabled(true);

        loadNextWord();

        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            updateStats();

            if (timeLeft <= 0) {
                endGame();
            }
        });

        gameTimer.start();
    }

    private void loadNextWord() {
        if (timeLeft <= 0) {
            return;
        }

        if (usedWords.size() == 10) {
            usedWords.clear();
        }

        do {
            currentWord = wordManager.getRandomWord();
        } while (usedWords.contains(currentWord));

        usedWords.add(currentWord);

        String scrambled = wordManager.scrambleWord(currentWord);

        wordLabel.setText(spacedLetters(scrambled));
        answerField.setText("");

        feedbackLabel.setText(" ");
        hintLabel.setText(" ");

        hintUsed = false;
        roundFinished = false;

        round++;
        roundLabel.setText("ROUND " + round);
        checkButton.setEnabled(true);
        hintButton.setEnabled(true);
        answerField.setEnabled(true);

        answerField.requestFocusInWindow();
    }

    private String spacedLetters(String word) {
        StringBuilder result = new StringBuilder();

        for (char letter : word.toCharArray()) {
            result.append(letter).append("   ");
        }

        return result.toString().trim();
    }

    private void checkAnswer(ActionEvent event) {
        if (roundFinished || timeLeft <= 0) {
            return;
        }

        String answer = answerField.getText().trim();

        if (answer.isEmpty()) {
            feedbackLabel.setForeground(new Color(255, 190, 90));
            feedbackLabel.setText("Please enter an answer first.");
            return;
        }

        if (answer.equalsIgnoreCase(currentWord)) {
            int points = hintUsed ? 5 : 10;
            score += points;

            feedbackLabel.setForeground(new Color(65, 210, 145));
            feedbackLabel.setText("Correct! +" + points + " points");

            roundFinished = true;
            checkButton.setEnabled(false);
            hintButton.setEnabled(false);
            answerField.setEnabled(false);

            updateStats();
        } else {
            feedbackLabel.setForeground(new Color(255, 110, 110));
            feedbackLabel.setText("Not quite! Try again.");
        }
    }

    private void showHint() {
        if (roundFinished || hintUsed || timeLeft <= 0) {
            return;
        }

        hintUsed = true;

        hintLabel.setText("Hint: " + wordManager.getHint(currentWord));
        feedbackLabel.setForeground(new Color(255, 190, 90));
        feedbackLabel.setText("Hint used. Correct answers earn 5 points.");

        hintButton.setEnabled(false);
    }

    private void updateStats() {
        timerLabel.setText(
                "<html><div style='text-align:center;'>" +
                        "<span style='font-size:10px;color:#A0B0C5;'>TIME</span><br>" +
                        "<span style='font-size:19px;color:white;'>" +
                        timeLeft + "s</span></div></html>");

        scoreLabel.setText(
                "<html><div style='text-align:center;'>" +
                        "<span style='font-size:10px;color:#A0B0C5;'>SCORE</span><br>" +
                        "<span style='font-size:19px;color:white;'>" +
                        score + "</span></div></html>");

        if (score > highScore) {
            highScore = score;
            preferences.putInt("highScore", highScore);
        }
    }

    private void endGame() {
        gameTimer.stop();

        checkButton.setEnabled(false);
        hintButton.setEnabled(false);
        nextButton.setEnabled(false);
        answerField.setEnabled(false);

        wordLabel.setText("TIME'S UP!");
        feedbackLabel.setForeground(accentColor);
        feedbackLabel.setText("Game over! Final score: " + score);
        hintLabel.setText("Press RESTART to play again.");

        updateStats();
        highScoreLabel.setText(
                "<html><div style='text-align:center;'>" +
                        "<span style='font-size:10px;color:#A0B0C5;'>BEST</span><br>" +
                        "<span style='font-size:19px;color:white;'>" +
                        highScore + "</span></div></html>");
    }
}
