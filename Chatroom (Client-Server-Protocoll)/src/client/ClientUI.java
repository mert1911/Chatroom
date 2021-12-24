package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class ClientUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String defaultName = " <username> ";
	private static final int h = 500, w = 400;

	private final Font textfont = new Font("Arial", Font.PLAIN, 14);
	private final Font roomfont = new Font("Arial", Font.PLAIN, 12);
	private final Font labelfont = new Font("Arial", Font.BOLD, 14);
	private final Color bg = new Color(238, 216, 174);
	private final Color bg2 = new Color(255, 255, 224);
	private final Color bg3 = new Color(255, 160, 122);

	private ChatClient client;
	private JTextArea chatroom = new JTextArea();
	private JTextField username = new JTextField(defaultName);
	private JTextArea userInput = new JTextArea();
	private JScrollPane chatroomScroller;
	private JButton send = new JButton(" send ");
	private JPanel chatPanel;
	private JPanel inputTextPanel;
	private JPanel loginPanel;

	public ClientUI(ChatClient client) {
		super("Client UI");

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ClientUI.this.setVisible(false);
				ClientUI.this.dispose();
				ClientUI.this.client.exit();
			}
		});

		this.client = client;
		this.initUI();
		this.pack();
		this.setLocation(500, h);
		this.setVisible(true);
	}

	private void initUI() {
		Container c = this.getContentPane();
		createLoginPanel();
		c.add(this.loginPanel); // initial nur Login zu sehen.
	}

	/*
	 * Login Panel mit Username und connect-Button
	 */
	public void createLoginPanel() {
		JLabel l1 = new JLabel("Username:");
		l1.setFont(this.labelfont);
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		this.username.setFont(this.textfont);

		CaretListener cLis = new CaretListener() {
			boolean b = true;

			public void caretUpdate(CaretEvent e) {
				if (b) {
					((JTextField) e.getSource()).setText("");
					b = false;
				}
			}
		};

		this.username.addCaretListener(cLis);
		ActionListener connectLis = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = ClientUI.this.username.getText();
				if (name.equals(ClientUI.defaultName)) {
					ClientUI.this.showInfoMessage("Bitte einen Usernamen angeben");
				} else {
					ClientUI.this.client.register(name);
				}
			}
		};

		this.username.addActionListener(connectLis);
		this.username.setAlignmentX(CENTER_ALIGNMENT);
		this.username.setHorizontalAlignment(SwingConstants.CENTER);
		this.username.setBackground(this.bg2);

		JButton submit = new JButton("connect");
		submit.addActionListener(connectLis);
		submit.setBackground(this.bg3);

		JPanel p1 = new JPanel();
		p1.setBackground(this.bg);
		p1.setLayout(new GridLayout(3, 1, 0, 20));
		p1.setPreferredSize(new Dimension(150, 200));
		Border outer = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Login",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null);
		Border inner = BorderFactory.createEmptyBorder(20, 10, 20, 10);
		Border combined = BorderFactory.createCompoundBorder(outer, inner);
		p1.setBorder(combined);
		p1.add(l1);
		p1.add(this.username);
		p1.add(submit);

		this.loginPanel = new JPanel();
		this.loginPanel.setBackground(this.bg);
		this.loginPanel.add(p1);
		this.loginPanel.setPreferredSize(new Dimension(w, h));
		this.loginPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 50, 100));
	}

	/*
	 * Panel fuer den Chatroom zusammenbauen (Anzeige und Eingabe
	 */
	void showChatPanels() {
		createChatPanel();
		this.getContentPane().remove(this.loginPanel);
		this.getContentPane().add(this.chatPanel, BorderLayout.CENTER);
		createInputTextPanel();
		this.getContentPane().add(this.inputTextPanel, BorderLayout.SOUTH);
		this.revalidate();
		this.repaint();
	}

	/*
	 * Panel mit TextArea fuer die Anzeige aller ankommenden Nachrichten
	 */
	public void createChatPanel() {
		this.chatroom.setEditable(false);
		this.chatroom.setFont(this.roomfont);
		this.chatroom.setLineWrap(true);
		this.chatroom.setWrapStyleWord(true);
		this.chatroom.setBackground(bg2);

		this.chatroomScroller = new JScrollPane(chatroom);
		this.chatroomScroller.setBackground(bg2);
		this.chatroomScroller.setPreferredSize(new Dimension(w - 20, h - 120));

		this.chatPanel = new JPanel();
		this.chatPanel.add(this.chatroomScroller);
		this.chatPanel.setPreferredSize(new Dimension(w, h - 120));
		this.chatPanel.setBackground(bg);
		Border outer = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Chat",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null);
		this.chatPanel.setBorder(outer);
	}

	/*
	 * Panel mit Textarea zum Eingeben der Nachrichten und Button zum Abschicken
	 * einer Nachricht
	 */
	public void createInputTextPanel() {

		this.userInput.setFont(this.textfont);
		Border outer = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Input",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null);
		Border inner = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border combined = BorderFactory.createCompoundBorder(outer, inner);
		this.userInput.setBorder(combined);
		this.userInput.setBackground(bg2);
		this.userInput.setLineWrap(true);
		this.userInput.setWrapStyleWord(true);

		JScrollPane inputScroller = new JScrollPane(this.userInput);
		this.send.setFont(this.labelfont);
		this.send.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		this.send.setBackground(Color.GRAY);
		this.send.setForeground(Color.white);
		this.send.setPreferredSize(new Dimension(80, 80));

		this.inputTextPanel = new JPanel();
		this.inputTextPanel.setLayout(new BoxLayout(this.inputTextPanel, BoxLayout.X_AXIS));
		this.inputTextPanel.setBackground(this.bg);
		this.inputTextPanel.add(inputScroller);
		this.inputTextPanel.add(this.send);
		this.inputTextPanel.setPreferredSize(new Dimension(w - 100, 80));
		this.send.addActionListener(new SendTextListener());
	}

	/*
	 * Nachricht im Textfenster hinzufuegen und ggf. zum Ende scrollen
	 */
	public void addMessage(String author, String content) {
		this.chatroom.append(author + ":\n" + content + "\n\n");
		this.chatroom.setCaretPosition(this.chatroom.getText().length());
	}

	/*
	 * Nachricht in einem JOption Pane anzeigen
	 */
	public void showInfoMessage(String content) {
		JOptionPane.showMessageDialog(ClientUI.this, content);
	}

	/*
	 * Listener für den Send Button: Nachricht an ChatClient zur Uebertragung an
	 * den Server weitergeben
	 */
	private class SendTextListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ClientUI.this.client.sendMessage(ClientUI.this.userInput.getText());
			ClientUI.this.userInput.setText("");
		}
	}
}
