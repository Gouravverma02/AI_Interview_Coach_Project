import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ModernInterviewCoach extends Application {

    @Override
    public void start(Stage primaryStage) {
        SwingNode swingNode = new SwingNode();
        createSwingContent(swingNode);

        StackPane root = new StackPane();
        root.getChildren().add(swingNode);

        Scene scene = new Scene(root, 800, 700);
        
        // Modern styling for JavaFX window
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap");
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Interview Coach Pro");
        primaryStage.show();
    }

    private void createSwingContent(SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> {
            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    // Gradient background
                    g2d.setPaint(new GradientPaint(0, 0, new Color(20, 30, 48), getWidth(), getHeight(), new Color(36, 59, 85)));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.dispose();
                }
            };
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            // Header with modern design
            JPanel headerPanel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    // Glass effect
                    g2d.setColor(new Color(255, 255, 255, 15));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    // Border
                    g2d.setColor(new Color(255, 255, 255, 50));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                    g2d.dispose();
                }
            };
            headerPanel.setOpaque(false);
            headerPanel.setPreferredSize(new Dimension(0, 80));

            JLabel title = new JLabel("Interview Coach Pro", JLabel.CENTER);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Poppins", Font.BOLD, 28));
            headerPanel.add(title, BorderLayout.CENTER);

            // Add theme toggle button
            JToggleButton themeToggle = new JToggleButton("🌙");
            themeToggle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            themeToggle.setContentAreaFilled(false);
            themeToggle.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            themeToggle.setForeground(Color.WHITE);
            
            // Add to header
            JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            headerRight.setOpaque(false);
            headerRight.add(themeToggle);
            headerPanel.add(headerRight, BorderLayout.EAST);

            mainPanel.add(headerPanel, BorderLayout.NORTH);

            // Main content area
            JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
            contentPanel.setOpaque(false);
            contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

            // Card creation with glass effect
            JPanel mockCard = createGlassCard("Mock Interview", "⏺ Start Interview", 
                "Practice with AI-driven mock interviews");
            JPanel questionsCard = createGlassCard("Practice Questions", "🔍 Get Questions", 
                "Get tailored questions based on your field");
            JPanel feedbackCard = createGlassCard("Feedback", "📊 View Feedback", 
                "Detailed analysis of your performance");
            JPanel tipsCard = createGlassCard("Pro Tips", "💡 View Tips", 
                "Master the art of interviewing");

            // Add cards to content
            contentPanel.add(mockCard);
            contentPanel.add(questionsCard);
            contentPanel.add(feedbackCard);
            contentPanel.add(tipsCard);

            mainPanel.add(contentPanel, BorderLayout.CENTER);

            // Create the actual SwingNode content
            swingNode.setContent(mainPanel);

            // Theme toggle functionality
            themeToggle.addActionListener(e -> {
                if (themeToggle.isSelected()) {
                    // Switch to light theme
                    mainPanel.setBackground(new Color(240, 240, 240));
                    title.setForeground(Color.DARK_GRAY);
                    themeToggle.setText("☀️");
                    updateCardThemes(contentPanel, false);
                } else {
                    // Switch to dark theme
                    mainPanel.setBackground(new Color(20, 30, 48));
                    title.setForeground(Color.WHITE);
                    themeToggle.setText("🌙");
                    updateCardThemes(contentPanel, true);
                }
            });

            // Initialize API button action
            JButton getQuestionsBtn = (JButton) ((JPanel) questionsCard.getComponent(2)).getComponent(0);
            getQuestionsBtn.addActionListener(e -> {
                JTextArea questionArea = (JTextArea) ((JScrollPane) ((JPanel) questionsCard.getComponent(1)).getComponent(0)).getViewport().getView();
                getQuestionsBtn.setEnabled(false);
                getQuestionsBtn.setText("Loading...");
                
                new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        return callInterviewAPI();
                    }

                    @Override
                    protected void done() {
                        try {
                            String response = get();
                            questionArea.setText(response);
                        } catch (Exception ex) {
                            questionArea.setText("Error: " + ex.getMessage());
                        } finally {
                            getQuestionsBtn.setEnabled(true);
                            getQuestionsBtn.setText("🔍 Get Questions");
                        }
                    }
                }.execute();
            });
        });
    }

    private JPanel createGlassCard(String title, String buttonText, String description) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                // Glass effect
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // Border
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        card.add(titleLabel, BorderLayout.NORTH);

        // Content - with subtle animation on hover
        JTextArea contentArea = new JTextArea(description);
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setOpaque(false);
        contentArea.setForeground(Color.LIGHT_GRAY);
        contentArea.setFont(new Font("Poppins", Font.PLAIN, 14));
        
        // Put content in a scroll pane with glass effect
        JScrollPane scrollPane = new JScrollPane(contentArea) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 10));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        card.add(contentPanel, BorderLayout.CENTER);

        // Button with animation
        JButton cardButton = new JButton(buttonText);
        cardButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        cardButton.setFocusPainted(false);
        cardButton.setContentAreaFilled(false);
        cardButton.setForeground(Color.WHITE);
        cardButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Hover animation
        cardButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cardButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 200, 255, 150), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                cardButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(cardButton);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private void updateCardThemes(JPanel contentPanel, boolean darkMode) {
        Color textColor = darkMode ? Color.WHITE : Color.DARK_GRAY;
        Color contentColor = darkMode ? Color.LIGHT_GRAY : new Color(80, 80, 80);
        Color buttonColor = darkMode ? Color.WHITE : Color.DARK_GRAY;
        
        for (Component comp : contentPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel card = (JPanel) comp;
                // Update title
                JLabel title = (JLabel) card.getComponent(0);
                title.setForeground(textColor);
                
                // Update content
                JPanel contentPanelInCard = (JPanel) card.getComponent(1);
                JScrollPane scrollPane = (JScrollPane) contentPanelInCard.getComponent(0);
                JTextArea content = (JTextArea) scrollPane.getViewport().getView();
                content.setForeground(contentColor);
                
                // Update button
                JPanel buttonPanel = (JPanel) card.getComponent(2);
                JButton button = (JButton) buttonPanel.getComponent(0);
                button.setForeground(buttonColor);
                if (darkMode) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                    ));
                } else {
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                    ));
                }
            }
        }
    }

    private String callInterviewAPI() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer your-api-key-here")
                .POST(HttpRequest.BodyPublishers.ofString(
                    "{\"model\":\"text-davinci-003\",\"prompt\":\"Generate 5 professional interview questions for a software engineering position with brief explanations.\",\"temperature\":0.7,\"max_tokens\":256}"))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception ex) {
            return "Error calling API: " + ex.getMessage();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
