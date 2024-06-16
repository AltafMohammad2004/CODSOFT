import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem extends JFrame {
    private List<Student> students;
    private DefaultListModel<String> studentListModel;
    private JList<String> studentList;
    private JTextField nameField;
    private JTextField rollNumberField;
    private JTextField gradeField;
    private JTextField searchField;
    private JButton updateButton;
    private JButton deleteButton;

    public StudentManagementSystem() {
        students = new ArrayList<>();
        studentListModel = new DefaultListModel<>();
        studentList = new JList<>(studentListModel);
        nameField = new JTextField(15);
        rollNumberField = new JTextField(15);
        gradeField = new JTextField(15);
        searchField = new JTextField(15);
        updateButton = new JButton("Update Student");
        deleteButton = new JButton("Delete Student");

        setTitle("Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Roll Number:"));
        inputPanel.add(rollNumberField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeField);
        inputPanel.add(new JLabel("Search by Roll Number:"));
        inputPanel.add(searchField);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Student");
        JButton searchButton = new JButton("Search Student");
        JButton displayButton = new JButton("Display All Students");
        JButton saveButton = new JButton("Save to File");
        JButton loadButton = new JButton("Load from File");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(updateButton);

        add(new JScrollPane(studentList), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new AddStudentAction());
        deleteButton.addActionListener(new DeleteStudentAction());
        searchButton.addActionListener(new SearchStudentAction());
        displayButton.addActionListener(new DisplayStudentsAction());
        saveButton.addActionListener(new SaveToFileAction());
        loadButton.addActionListener(new LoadFromFileAction());
        updateButton.addActionListener(new UpdateStudentAction());

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private class AddStudentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String rollNumber = rollNumberField.getText();
            String grade = gradeField.getText();

            if (name.isEmpty() || rollNumber.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student student = new Student(name, rollNumber, grade);
            students.add(student);
            studentListModel.addElement(student.toString());
            clearFields();
        }
    }

    private class DeleteStudentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String rollNumber = rollNumberField.getText();
            students.removeIf(student -> student.getRollNumber().equals(rollNumber));
            refreshStudentList();
            clearFields();
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Student deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class SearchStudentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String rollNumber = searchField.getText();
            for (Student student : students) {
                if (student.getRollNumber().equals(rollNumber)) {
                    JOptionPane.showMessageDialog(null, student.toString(), "Student Found", JOptionPane.INFORMATION_MESSAGE);
                    nameField.setText(student.getName());
                    rollNumberField.setText(student.getRollNumber());
                    gradeField.setText(student.getGrade());
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class UpdateStudentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String rollNumber = rollNumberField.getText();
            for (Student student : students) {
                if (student.getRollNumber().equals(rollNumber)) {
                    student.setName(nameField.getText());
                    student.setGrade(gradeField.getText());
                    refreshStudentList();
                    clearFields();
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "Student updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Student not found for update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class DisplayStudentsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshStudentList();
        }
    }

    private class SaveToFileAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                    oos.writeObject(students);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class LoadFromFileAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    students = (List<Student>) ois.readObject();
                    refreshStudentList();
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void refreshStudentList() {
        studentListModel.clear();
        for (Student student : students) {
            studentListModel.addElement(student.toString());
        }
    }

    private void clearFields() {
        nameField.setText("");
        rollNumberField.setText("");
        gradeField.setText("");
        searchField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem frame = new StudentManagementSystem();
            frame.setVisible(true);
        });
    }
}
