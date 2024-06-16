import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class Word_Counter extends JFrame {
    private static final Set<String> STOP_WORDS = new HashSet<String>() {{
        add("a");
        add("an");
        add("and");
        add("are");
        add("as");
        add("at");
        add("be");
        add("by");
        add("for");
        add("from");
        add("has");
        add("he");
        add("in");
        add("is");
        add("it");
        add("its");
        add("of");
        add("on");
        add("that");
        add("the");
        add("to");
        add("was");
        add("were");
        add("will");
        add("with");
    }};

    private JTextArea textArea;
    private JButton loadFileButton;
    private JButton countWordsButton;
    private JLabel totalWordsLabel;
    private JLabel uniqueWordsLabel;
    private JTextArea wordFrequencyArea;

    public Word_Counter() {
        setTitle("Word Counter");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        loadFileButton = new JButton("Load File");
        countWordsButton = new JButton("Count Words");
        totalWordsLabel = new JLabel("Total words: 0");
        uniqueWordsLabel = new JLabel("Unique words: 0");
        wordFrequencyArea = new JTextArea();

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 2));
        northPanel.add(loadFileButton);
        northPanel.add(countWordsButton);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(2, 1));
        southPanel.add(totalWordsLabel);
        southPanel.add(uniqueWordsLabel);

        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        add(new JScrollPane(wordFrequencyArea), BorderLayout.EAST);

        loadFileButton.addActionListener(new LoadFileAction());
        countWordsButton.addActionListener(new CountWordsAction());
    }

    private class LoadFileAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String content = readFile(selectedFile.getAbsolutePath());
                if (content != null) {
                    textArea.setText(content);
                } else {
                    JOptionPane.showMessageDialog(null, "Error reading file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class CountWordsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = textArea.getText();
            Map<String, Integer> wordFrequency = countWords(text);
            displayStatistics(wordFrequency);
        }
    }

    private String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            while (fileScanner.hasNextLine()) {
                content.append(fileScanner.nextLine()).append(" ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return content.toString();
    }

    private Map<String, Integer> countWords(String text) {
        Map<String, Integer> wordFrequency = new HashMap<>();
        String[] words = text.split("\\W+");  // Split on non-word characters

        for (String word : words) {
            if (word.isEmpty() || STOP_WORDS.contains(word.toLowerCase())) {
                continue;
            }

            word = word.toLowerCase();
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }

        return wordFrequency;
    }

    private void displayStatistics(Map<String, Integer> wordFrequency) {
        int totalWords = wordFrequency.values().stream().mapToInt(Integer::intValue).sum();
        int uniqueWords = wordFrequency.size();

        totalWordsLabel.setText("Total words: " + totalWords);
        uniqueWordsLabel.setText("Unique words: " + uniqueWords);
        wordFrequencyArea.setText("Word frequencies:\n");
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            wordFrequencyArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Word_Counter frame = new Word_Counter();
            frame.setVisible(true);
        });
    }
}
