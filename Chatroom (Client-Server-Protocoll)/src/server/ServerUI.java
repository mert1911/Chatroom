package server;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ServerUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton s1, s2;
	private ChatServer server;

	public ServerUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.createComponents();

		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				ServerUI.this.server.stopServer();
			}
		});

		this.setLocation(100, 100);
		this.pack();
	}

	private void createComponents() {
		Container c = this.getContentPane();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		this.s1 = new JButton("Start Server");
		this.s2 = new JButton("Stop Server");
		this.s2.setEnabled(false);

		ActionListener sbLis = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = ((JButton) e.getSource()).getText();
				if (s.equals("Start Server")) {
					ServerUI.this.s1.setEnabled(false);
					ServerUI.this.s2.setEnabled(true);
					ServerUI.this.server = new ChatServer();
				} else {
					ServerUI.this.s1.setEnabled(true);
					ServerUI.this.s2.setEnabled(false);
					ServerUI.this.server.stopServer();
					ServerUI.this.server = null;
				}
				ServerUI.this.repaint();
			}
		};

		this.s1.addActionListener(sbLis);
		this.s2.addActionListener(sbLis);
		this.s1.setBackground(new Color(152, 251, 152));
		this.s2.setBackground(new Color(240, 128, 128));
		Font font = new Font("Arial", Font.BOLD, 14);
		this.s1.setFont(font);
		this.s2.setFont(font);
		panel.setPreferredSize(new Dimension(300, 50));
		panel.add(this.s1);
		panel.add(this.s2);
		c.add(panel);
	}

}
