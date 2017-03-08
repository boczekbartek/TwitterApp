package org.eclipse.wb.swing;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitterApp.Account;
import twitterApp.PieChart;
import twitterApp.SentimentClassifier;
import twitterApp.Tools;
import twitterApp.Value;

import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextPane;
import java.awt.Desktop;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Cursor;

public class Home1 extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Account account;
	/**
	 * @wbp.nonvisual location=275,299
	 */
	private JTextField textField;
	private JLabel lblNewLabel;
	private JTextField textField_1;
	public String ownClassifierPath = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home1 frame = new Home1(LoginForm2.account);
					if (args != null && args.length > 0) {
						System.out.println(args[0]);
						frame.ownClassifierPath = args[0];
					}

					frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
					System.out.println("aaaaaaddddsss");

					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private int[] classifyTweets(List<Status> tweets, SentimentClassifier sentClassifier) {
		try {
			File file = new File("tempTweets.txt");

			PrintStream ps = new PrintStream(file);
			ps.println(tweets.get(0).getUser().getScreenName());
			int g = 0, pos = 0, neg = 0, neu = 0;

			for (Status st : tweets) {
				g++;
				ps.println("-----------------------------------------\n***" + g + "***\n");

				String text = st.getText();
				ps.println("Text: " + text);
				// klasyfikacja
				String sent = sentClassifier.classify(st.getText());
				ps.println("Sentiment: " + sent);
				if (sent.equals("pos"))
					pos++;
				else if (sent.equals("neg"))
					neg++;
				else
					neu++;
			}
			ps.println("Pos: " + pos + "Neg: " + neg + "Neu: " + neu);
			ps.close();

			file.deleteOnExit();
			int[] arr = { pos, neg, neu };
			return arr;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			int[] arr = { 0, 0, 0 };
			return arr;
		}

	}

	/**
	 * Create the frame.
	 */
	public Home1(Account _account) {
		setMaximumSize(new Dimension(700, 800));
		setMinimumSize(new Dimension(400, 500));
		setSize(new Dimension(500, 600));
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Alert alert = new Alert("Are you sure?");
				alert.setVisible(true);

			}
		});

		account = _account;

		setBounds(100, 100, 419, 540);

		// chartPanel.add(chart.getChartPanel());
		//
		// chartPanel.setVisible(true);
		//
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(panel,
				Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(panel,
				Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE));

		textField = new JTextField();
		textField.setColumns(10);

		lblNewLabel = new JLabel(
				"<html>Enter a username and amount of tweets below to see emotions charts of timeline!</html>");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setVisible(true);
		JLabel lblFault = new JLabel("Incorrect username, try again!");
		lblFault.setForeground(new Color(255, 99, 71));
		lblFault.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblFault.setVisible(false);

		JTextPane txtpnUnfortunatelyTwitterAllows = new JTextPane();
		txtpnUnfortunatelyTwitterAllows.setForeground(new Color(0, 0, 0));
		txtpnUnfortunatelyTwitterAllows.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtpnUnfortunatelyTwitterAllows.setText(
				"Unfortunately Twitter allows to download only 1500 tweets in one query due to its servers limitations.");
		txtpnUnfortunatelyTwitterAllows.setEditable(false);
		txtpnUnfortunatelyTwitterAllows.setVisible(false);
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		JButton btnClickHereTo = new JButton(
				"<html>Click here to open txt file with tweets and its sentiments!</html>");
		btnClickHereTo.setVisible(false);
		// BUUUUTTOONNNNNN

		JButton btnShowMeResults = new JButton("Show me results!");
		btnShowMeResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					lblNewLabel.setVisible(true);
					lblFault.setVisible(false);
					txtpnUnfortunatelyTwitterAllows.setVisible(false);
					String username = textField.getText();
					String tweetsAmount = textField_1.getText();
					System.out.println("twam:" + tweetsAmount);

					Integer twAm = Integer.parseInt(tweetsAmount);
					System.out.println(twAm);

					List<Status> tweets = Tools.getUserTweets(account.getTwitter(), username, twAm);
					// List<Status> tweets = Tools.searchWords(account,
					// username, twAm);

					SentimentClassifier sentClassifier;
					if (null != ownClassifierPath) {
						sentClassifier = new SentimentClassifier(ownClassifierPath);

					} else {
						sentClassifier = new SentimentClassifier();
					}
					int[] arr = classifyTweets(tweets, sentClassifier);

					List<Value> list = new ArrayList<Value>();

					list.add(new Value("Negative", arr[1]));
					list.add(new Value("Neutral", arr[2]));
					list.add(new Value("Positive", arr[0]));

					String chartName = username + " emotions chart";
					PieChart chart = new PieChart("TwitterApp", chartName, list);
					chart.pack();
					chart.setLocation(500, 100);
					chart.setVisible(true);
					btnClickHereTo.setVisible(true);
				} catch (TwitterException e) {
					lblFault.setText("Incorrect username, try again!");
					lblFault.setVisible(true);
				} catch (IOException e) {
					lblFault.setText("Wrong tweets number!");
					lblFault.setVisible(true);
					e.printStackTrace();
					txtpnUnfortunatelyTwitterAllows.setVisible(true);
				} catch (NumberFormatException e) {
					lblFault.setText("Wrong format of tweets number!");
					lblFault.setVisible(true);
					e.printStackTrace();
					txtpnUnfortunatelyTwitterAllows.setVisible(true);
				}
			}
		});

		btnShowMeResults.setForeground(new Color(138, 43, 226));
		btnShowMeResults.setFont(new Font("Calibri", Font.BOLD | Font.ITALIC, 22));

		JLabel lblNewLabel_1 = new JLabel("Username:");

		JLabel lblNewLabel_2 = new JLabel("Tweets:");

		btnClickHereTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = new File(
						"C://Users//Bartek//Documents//PROGRAMY//Java//Workspace//TwitteAnalyseApp//tempTweets.txt");
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					txtpnUnfortunatelyTwitterAllows.setText("Error! File not found. Please try again later...");
					txtpnUnfortunatelyTwitterAllows.setVisible(true);
				}
			}
		});

		JButton btnNewButton = new JButton("Change Mode");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ModeChoose.main(null);
				Home1.this.dispose();
			}
		});

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panel.createSequentialGroup().addGap(115).addComponent(lblNewLabel_1).addGap(110)
										.addComponent(lblNewLabel_2).addContainerGap(87, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup().addGap(72).addGroup(gl_panel
								.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup()
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, 179,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap())
								.addGroup(gl_panel.createSequentialGroup()
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel.createSequentialGroup().addGap(10).addComponent(
														btnShowMeResults, GroupLayout.PREFERRED_SIZE, 236,
														GroupLayout.PREFERRED_SIZE))
												.addComponent(lblFault, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
														249, Short.MAX_VALUE))
										.addGap(82))
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 303,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap())))
						.addGroup(gl_panel.createSequentialGroup().addGap(59)
								.addComponent(txtpnUnfortunatelyTwitterAllows, GroupLayout.PREFERRED_SIZE, 274,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(70, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup().addGap(39)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(btnNewButton)
										.addComponent(btnClickHereTo, GroupLayout.PREFERRED_SIZE, 325,
												GroupLayout.PREFERRED_SIZE))
								.addContainerGap(39, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panel.createSequentialGroup().addGap(29)
										.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 38,
												GroupLayout.PREFERRED_SIZE)
										.addGap(29)
										.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblNewLabel_1).addComponent(lblNewLabel_2))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
												.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 38,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(textField, GroupLayout.PREFERRED_SIZE, 38,
														GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblFault, GroupLayout.PREFERRED_SIZE, 43,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnShowMeResults, GroupLayout.PREFERRED_SIZE, 91,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(txtpnUnfortunatelyTwitterAllows, GroupLayout.PREFERRED_SIZE, 74,
												GroupLayout.PREFERRED_SIZE)
										.addGap(27)
										.addComponent(btnClickHereTo, GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(btnNewButton).addContainerGap()));
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);

		

	}
}