package ui;

import model.Book;
import model.BooksCollection;
import model.Event;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;

// The code for the main screen was based off of the AlarmControllerUI
// class in the AlarmSystem project:
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem/blob/main/src/main/
// ca/ubc/cpsc210/alarm/ui/AlarmControllerUI
// Represents Book Tracker's main screen
public class BookTrackerUI extends JFrame {
    private final JPanel homePanel;
    private BooksCollection booksCollection = new BooksCollection();
    private final static int WIDTH = 600;
    private final static int HEIGHT = 400;

    // EFFECTS: Constructs main panel with all buttons and no background
    public BookTrackerUI() {
        homePanel = new JPanel();
        addWindowListener(new PrintChangesAction());
        setContentPane(homePanel);
        setVisible(true);
        addButtons();
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    // MODIFIES: this
    // EFFECTS: Creates buttons with corresponding action listeners and
    // adds them to the main menu
    private void addButtons() {
        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(new AddAction());
        homePanel.add(addButton);
        JButton removeButton = new JButton("Remove Book");
        removeButton.addActionListener(new RemoveAction());
        homePanel.add(removeButton);
        JButton viewButton = new JButton("View Books");
        viewButton.addActionListener(new ViewAction());
        homePanel.add(viewButton);
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(new LoadAction());
        homePanel.add(loadButton);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveAction());
        homePanel.add(saveButton);
    }

    // Code for constructing ActionListener class was based off of this tutorial:
    // https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html
    // All code related to JOptionPanes was based off of this tutorial:
    // https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
    // Represents all behaviour related to adding a book
    private class AddAction implements ActionListener {

        // MODIFIES: this, booksCollection
        // EFFECTS: prompts user to answer questions relating to book,
        // and creates book and adds it to the collection if all
        // necessary information is provided
        @Override
        public void actionPerformed(ActionEvent e) {
            String author = null;
            String genre = null;
            int pages = -1;
            String title = promptForValidAnswer("Book title?", "Enter book title:");
            if (title != null) {
                author = promptForValidAnswer("Author?", "Enter book author:");
            }
            if (author != null) {
                genre = promptForValidAnswer("Genre?", "Enter book genre:");
            }
            if (genre != null) {
                pages = promptForValidInt();
            }
            if (pages > 0) {
                int finished = JOptionPane.showConfirmDialog(homePanel, "Finished?",
                        "Have you finished this book?", JOptionPane.YES_NO_OPTION);
                if (finished == 0 || finished == 1) {
                    booksCollection.addToCollection(new Book(title, author, genre, pages, (finished == 0)));
                    JOptionPane.showMessageDialog(homePanel,
                            title + " has been successfully added!");
                }
            }
        }

        // EFFECTS: prompts user to submit answer to given question until
        // a valid answer is provided
        private String promptForValidAnswer(String question, String title) {
            String answer = JOptionPane.showInputDialog(homePanel, question, title,
                    JOptionPane.QUESTION_MESSAGE);
            while (answer != null && answer.isBlank()) {
                answer = prompt(question, title);
            }
            return answer;
        }

        // The following tutorial was followed to convert a string to an integer:
        // https://www.freecodecamp.org/news/java-string-to-int-how-to-convert-a-string-to-an-integer/
        // EFFECTS: prompts user to submit answer to given question until
        // a valid integer answer is provided
        private int promptForValidInt() {
            String question = "Pages?";
            String title = "Enter book pages:";
            String str = JOptionPane.showInputDialog(homePanel, question, title,
                    JOptionPane.QUESTION_MESSAGE);
            int answer = -1;
            while (str != null && (str.isBlank() || answer < 1)) {
                try {
                    answer = Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    str = prompt(question, title);
                }
                if (answer < 1) {
                    str = prompt(question, title);
                }
            }
            return answer;
        }

        // EFFECTS: displays invalid input message and question message,
        // returns the user's answer to question message
        private String prompt(String question, String title) {
            invalidInputMsg();
            return JOptionPane.showInputDialog(homePanel,
                    question,
                    title,
                    JOptionPane.QUESTION_MESSAGE);
        }

        // EFFECTS: displays message indicating that input is invalid
        private void invalidInputMsg() {
            JOptionPane.showMessageDialog(homePanel, "Invalid Input!");
        }
    }

    // Represents all behaviour related to removing a book
    private class RemoveAction implements ActionListener, ListSelectionListener {
        private JFrame bgFrame;
        private JList<String> titleList;

        // The code for displaying the books is based off of this tutorial:
        // https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing
        // /examples/components/SplitPaneDemoProject/src/components/SplitPaneDemo.java
        // MODIFIES: this, booksCollection
        // EFFECTS: displays books to be removed, and removes selected book
        @Override
        public void actionPerformed(ActionEvent e) {
            bgFrame = new JFrame("Select book to remove:");
            titleList = new JList<>(booksCollection.getAllTitles());
            titleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            titleList.addListSelectionListener(this);
            JScrollPane scrollPane = new JScrollPane(titleList);
            bgFrame.add(scrollPane);
            bgFrame.setSize(new Dimension(WIDTH / 2, HEIGHT / 2));
            bgFrame.setVisible(true);
        }

        // MODIFIES: this, booksCollection
        // EFFECTS: removes selected book if user confirms
        @Override
        public void valueChanged(ListSelectionEvent e) {
            removeBook(booksCollection.getBook(titleList.getSelectedIndex()));
        }

        // The following tutorial was used to force close the remove window:
        // https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
        // MODIFIES: this, booksCollection
        // EFFECTS: removes selected book if user confirms and closes the remove books menu,
        // otherwise nothing changes
        private void removeBook(Book b) {
            int selection = JOptionPane.showConfirmDialog(bgFrame,
                    "Are you sure you would like to remove "
                            + b.getTitle() + " from the tracker?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (selection == 0) {
                booksCollection.removeBook(b);
                JOptionPane.showMessageDialog(bgFrame,
                        b.getTitle() + " has been successfully removed.");
                bgFrame.setVisible(false);
                bgFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(bgFrame,
                        b.getTitle() + " was not removed.");
            }
        }
    }

    // Represents behaviour related to choosing a category of books to view
    private class ViewAction implements ActionListener {

        // The following tutorial was used to create a pop-up window with 3 custom options:
        // https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
        // MODIFIES: this
        // EFFECTS: displays a menu of categories of books that can be viewed
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] selections = {"All", "Finished", "To Be Read"};
            int n = JOptionPane.showOptionDialog(homePanel,
                    "Which category of books would you like to view?",
                    "View", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, selections, selections[0]);
            if (n == 0) {
                new TitleView(booksCollection.getBooks());
            } else if (n == 1) {
                new TitleView(booksCollection.getFinished());
            } else if (n == 2) {
                new TitleView(booksCollection.getTBR());
            }
        }
    }

    // The code for this class was primarily based off of code from this tutorial:
    // https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/
    // javase/tutorial/uiswing/examples/components/SplitPaneDemoProject/src/components/SplitPaneDemo.java
    // Represents a split window view of books and their information
    private static class TitleView implements ListSelectionListener {

        private JList<String> titleList;
        private JPanel infoPane;
        private final JLabel titleLabel = new JLabel();
        private final JLabel authorLabel = new JLabel();
        private final JLabel genreLabel = new JLabel();
        private final JLabel pagesLabel = new JLabel();
        private final JLabel finishedLabel = new JLabel();
        private final ArrayList<Book> selectedBooks;

        // EFFECTS: constructs the split view
        private TitleView(ArrayList<Book> selectedBooks) {
            this.selectedBooks = selectedBooks;
            setupTitleList(getTitles(selectedBooks));
            setupInfoPane();
            setupSplitPane();
        }

        // MODIFIES: this
        // EFFECTS: creates list view of book titles to be displayed
        private void setupTitleList(String[] titles) {
            titleList = new JList<>(titles);
            titleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            titleList.addListSelectionListener(this);
            JScrollPane titlesScroll = new JScrollPane(titleList);
            titlesScroll.setMinimumSize(new Dimension(75, 100));
        }

        // MODIFIES: this
        // EFFECTS: creates panel for book information
        private void setupInfoPane() {
            infoPane = new JPanel();
            infoPane.setMinimumSize(new Dimension(75, 100));
            infoPane.add(titleLabel);
            infoPane.add(authorLabel);
            infoPane.add(genreLabel);
            infoPane.add(pagesLabel);
            infoPane.add(finishedLabel);
            infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.Y_AXIS));
        }

        // MODIFIES: this
        // EFFECTS: creates the split pane
        private void setupSplitPane() {
            JFrame bgFrame = new JFrame();
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, titleList, infoPane);
            splitPane.setOneTouchExpandable(true);
            splitPane.setDividerLocation(100);
            Dimension d = new Dimension(WIDTH / 2, HEIGHT / 2);
            splitPane.setPreferredSize(d);
            bgFrame.setVisible(true);
            bgFrame.add(splitPane);
            bgFrame.setSize(d);
        }

        // MODIFIES: this
        // EFFECTS: displays information for selected book
        @Override
        public void valueChanged(ListSelectionEvent e) {
            updateBookInfo(selectedBooks.get(titleList.getSelectedIndex()));
        }

        // MODIFIES: this
        // EFFECTS: changes labels to display information for selected book
        private void updateBookInfo(Book b) {
            titleLabel.setText("Title: " + b.getTitle());
            authorLabel.setText("Author: " + b.getAuthor());
            genreLabel.setText("Genre: " + b.getGenre());
            pagesLabel.setText("Pages: " + b.getPages());
            finishedLabel.setText("Is Finished: " + b.isFinished());
        }

        // EFFECTS: returns an array with all the Book titles in the list
        private String[] getTitles(ArrayList<Book> selectedBooks) {
            String[] titles = new String[selectedBooks.size()];
            for (int i = 0; i < selectedBooks.size(); i++) {
                titles[i] = selectedBooks.get(i).getTitle();
            }
            return titles;
        }
    }

    // The following code was based off of the WorkRoomApp class in the JsonSerializationDemo project:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/ui/WorkRoomApp.java
    // Represents all behaviour related to loading from a save
    private class LoadAction implements ActionListener {
        private static final String JSON_STORE = "./data/booksCollection.json";
        private final JsonReader jsonReader = new JsonReader(JSON_STORE);

        // MODIFIES: this, booksCollection
        // EFFECTS: asks user if they would like to load from save, if yes, loads from save
        @Override
        public void actionPerformed(ActionEvent e) {
            int selection = JOptionPane.showConfirmDialog(homePanel,
                    "Would you like to load from the previous save?", "Load?", JOptionPane.YES_NO_OPTION);
            if (selection == 0) {
                try {
                    loadBooksCollection();
                    JOptionPane.showMessageDialog(homePanel, "Loaded from last save in " + JSON_STORE);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(homePanel, "Unable to read from file: " + JSON_STORE);
                }
            }
        }

        // MODIFIES: this, booksCollection
        // EFFECTS: loads workroom from file, if unable to do so, throws IOException
        private void loadBooksCollection() throws IOException {
            booksCollection = jsonReader.read();
        }
    }

    // The following code was based off of the WorkRoomApp class in the JsonSerializationDemo project:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/ui/WorkRoomApp.java
    // Represents all behaviour related to writing to a file
    private class SaveAction implements ActionListener {
        private static final String JSON_STORE = "./data/booksCollection.json";
        private final JsonWriter jsonWriter = new JsonWriter(JSON_STORE);

        // EFFECTS: asks user if they would like to save, if yes, saves collection
        @Override
        public void actionPerformed(ActionEvent e) {
            int selection = JOptionPane.showConfirmDialog(homePanel,
                    "Would you like to save the current books?", "Save?", JOptionPane.YES_NO_OPTION);
            if (selection == 0) {
                try {
                    saveBookCollection();
                    JOptionPane.showMessageDialog(homePanel,
                            "Books in tracker have been saved to " + JSON_STORE);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(homePanel, "Unable to write to file: " + JSON_STORE);
                }
            }
        }

        // EFFECTS: saves the workroom to file, if unable to do so, throws IOException
        private void saveBookCollection() throws IOException {
            jsonWriter.open();
            jsonWriter.write(booksCollection);
            jsonWriter.close();
        }
    }

    // The following tutorial was used for the creation of this class:
    // https://docs.oracle.com/javase/tutorial/uiswing/events/windowlistener.html
    // Represents all behaviour related to printing out event log when main menu is closed.
    private static class PrintChangesAction implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        // The following code was based off of the ScreenPrinter class
        // in the Alarm System project:
        // https://github.students.cs.ubc.ca/CPSC210/AlarmSystem/blob/main/src/main/ca/ubc/cpsc210/alarm/ui/
        // ScreenPrinter.java
        // EFFECTS: prints out all events in event log to the console
        @Override
        public void windowClosing(WindowEvent e) {
            EventLog changeLog = EventLog.getInstance();
            for (Event next : changeLog) {
                System.out.println(next.getDate() + ": " + next.getDescription());
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
}

