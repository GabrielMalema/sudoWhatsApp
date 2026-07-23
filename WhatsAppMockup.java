import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class WhatsAppMockup extends JFrame {

    private final Map<String, List<String>> conversations = new LinkedHashMap<>();
    private String currentContact;

    private DefaultListModel<String> contactListModel;
    private JPanel messagePanel;
    private JScrollPane messageScrollPane;
    private JTextField inputField;
    private JLabel chatHeaderLabel;

    public WhatsAppMockup() {
        setTitle("Chat App");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setupDummyData();

        // --- Left side: contact list ---
        contactListModel = new DefaultListModel<>();
        for (String name : conversations.keySet()) {
            contactListModel.addElement(name);
        }

        JList<String> contactList = new JList<>(contactListModel);
        contactList.setFont(new Font("SansSerif", Font.PLAIN, 16));
        contactList.setFixedCellHeight(50);
        contactList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && contactList.getSelectedValue() != null) {
                openConversation(contactList.getSelectedValue());
            }
        });

        JScrollPane contactScroll = new JScrollPane(contactList);
        contactScroll.setPreferredSize(new Dimension(220, 0));

        JLabel contactsHeader = new JLabel("  Chats");
        contactsHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        contactsHeader.setOpaque(true);
        contactsHeader.setBackground(new Color(0, 92, 75));
        contactsHeader.setForeground(Color.WHITE);
        contactsHeader.setPreferredSize(new Dimension(220, 50));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(contactsHeader, BorderLayout.NORTH);
        leftPanel.add(contactScroll, BorderLayout.CENTER);

        // --- Right side: chat window ---
        chatHeaderLabel = new JLabel("  Select a chat");
        chatHeaderLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        chatHeaderLabel.setOpaque(true);
        chatHeaderLabel.setBackground(new Color(0, 92, 75));
        chatHeaderLabel.setForeground(Color.WHITE);
        chatHeaderLabel.setPreferredSize(new Dimension(0, 50));

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(new Color(229, 221, 213));

        messageScrollPane = new JScrollPane(messagePanel);
        messageScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        inputField.addActionListener(e -> sendMessage());

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(chatHeaderLabel, BorderLayout.NORTH);
        rightPanel.add(messageScrollPane, BorderLayout.CENTER);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(220);
        splitPane.setEnabled(false);

        add(splitPane);

        if (!contactListModel.isEmpty()) {
            contactList.setSelectedIndex(0);
        }
    }

    private void setupDummyData() {
    List<String> thaboChat = new ArrayList<>();
    thaboChat.add("them|Hey, are we still on for tomorrow?");
    thaboChat.add("me|Yeah for sure, what time works?");
    thaboChat.add("them|Let's say 2pm");
    conversations.put("Thabo", thaboChat);

    List<String> naledichat = new ArrayList<>();
    naledichat.add("them|Did you finish the assignment?");
    naledichat.add("me|Almost done, just testing it now");
    conversations.put("Naledi", naledichat);

    List<String> studyGroupChat = new ArrayList<>();
    studyGroupChat.add("them|Anyone free to go over chapter 4?");
    studyGroupChat.add("me|I can do 6pm");
    studyGroupChat.add("them|Perfect, I'll share my notes");
    conversations.put("Study Group", studyGroupChat);

    List<String> momChat = new ArrayList<>();
    momChat.add("them|Have you eaten today?");
    momChat.add("me|Yes mom, don't worry :)");
    conversations.put("Mom", momChat);
}

    private void openConversation(String contactName) {
        currentContact = contactName;
        chatHeaderLabel.setText("  " + contactName);
        messagePanel.removeAll();

        for (String entry : conversations.get(contactName)) {
            String[] parts = entry.split("\\|", 2);
            boolean isMe = parts[0].equals("me");
            addMessageBubble(parts[1], isMe);
        }

        messagePanel.revalidate();
        messagePanel.repaint();
        scrollToBottom();
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty() || currentContact == null) return;

        conversations.get(currentContact).add("me|" + text);
        addMessageBubble(text, true);
        inputField.setText("");

        messagePanel.revalidate();
        messagePanel.repaint();
        scrollToBottom();
    }

    private void addMessageBubble(String text, boolean isMe) {
        JPanel row = new JPanel(new FlowLayout(isMe ? FlowLayout.RIGHT : FlowLayout.LEFT));
        row.setBackground(new Color(229, 221, 213));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel bubble = new JLabel(
                "<html><body style='width: 200px'>" + text
                        + "<br><span style='font-size:9px; color:gray'>"
                        + new SimpleDateFormat("HH:mm").format(new Date())
                        + "</span></body></html>"
        );
        bubble.setOpaque(true);
        bubble.setBackground(isMe ? new Color(220, 248, 198) : Color.WHITE);
        bubble.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        bubble.setFont(new Font("SansSerif", Font.PLAIN, 14));

        row.add(bubble);
        messagePanel.add(row);
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = messageScrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WhatsAppMockup mockup = new WhatsAppMockup();
            mockup.setVisible(true);
        });
    }
}
