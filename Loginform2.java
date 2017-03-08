package org.eclipse.wb.swing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import twitterApp.Account;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JTextField;
import java.awt.Font;

public class LoginForm2 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 326299648994947627L;
	private boolean isConnectedToInternet = true;
	private JLabel lblBrowserInfo;

	private JPanel contentPane;
	private JTextField authPIN;
	private JLabel lblBrowser;
	private JButton btnPIN;
	private JButton btnTryAgain;
	public static Account account;

	/**
	 * Launch the application.
	 */

	private static void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				/* TODO: error handling */ }
		} else {
			/* TODO: error handling */ }
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm2 frame = new LoginForm2();
					
					frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
					frame.setVisible(true);
					frame.authPIN.setVisible(false);
					frame.lblBrowserInfo.setVisible(false);
					//frame.pack();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void openAuthURL() {
		try {
			String oAuthURL = new String(account.generateURL());
			System.out.println(oAuthURL);

			URI uri;

			try {
				uri = new URI(oAuthURL);
				open(uri);
				isConnectedToInternet = true;
				btnPIN.setText("Ok");
				lblBrowser.setVisible(true);
			} catch (URISyntaxException e) {
				
				e.printStackTrace();
				lblBrowser.setText("Error... Could not open browser");
			}

			lblBrowserInfo.setText("<html><p>Please enter PIN code below:</p></html>");
			lblBrowserInfo.setVisible(true);

			authPIN.setVisible(true);
			btnPIN.setVisible(true);
		} catch (Exception e) {
			lblBrowser.setText(
					"<html><body>Brak dostępu do internetu. Sprawdz połącznie i spróbuj ponownie</body></html>");
			lblBrowser.setVisible(true);
			btnPIN.setText("Close");
			btnPIN.setVisible(true);
			isConnectedToInternet = false;
		}
	}

	/**
	 * Create the frame.
	 */
	public LoginForm2() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Alert alert = new Alert("Are you sure?");
				alert.setVisible(true);

			}
		});
		
		//w pliku poniżej należy zapisać swoje consumer i consumer secret key, to są dane właściciela aplikacji, który dzięki nim może byc autoryzowany do
		//dostępu do prywatnych danych użytkownika
		File keys = new File("consumer_key_i_consumer_secret.txt");
		Scanner sc;
		try {
			sc = new Scanner(keys);
			account = new Account(sc.nextLine(), sc.nextLine());
			sc.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Unable to find the file (consumer_key_i_consumer_secret.txt) with Consumer Keys");
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 308, 346);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		lblBrowserInfo = new JLabel();
		lblBrowserInfo.setForeground(Color.BLACK);
		JLabel lblNewLabel = new JLabel("Welcome to Twitter Emotions App! \r\n");

		JLabel lbltoSignIn = new JLabel(
				"<html>To sign in please click \"Log In\" button and follow further instructions.</html>");
		authPIN = new JTextField();
		authPIN.setColumns(10);
		JLabel lblLoginSucceeded = new JLabel("Login successful!!!");
		lblLoginSucceeded.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLoginSucceeded.setVisible(false);
		btnPIN = new JButton("OK");
		btnPIN.setVisible(false);
		lblBrowser = new JLabel("<html>Check out your Internet browser!</html>");
		lblBrowser.setVisible(false);
		JButton btnLogIn = new JButton("Log In");

		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!isConnectedToInternet) {
					lblBrowser.setText("<html>Check out your Internet browser!</html>");
					lblBrowser.setVisible(false);
					btnPIN.setVisible(false);
				} else if (account.authoriseAccount()) {
					System.out.println("Account authorised");
					lblLoginSucceeded.setText("Login Successful!");
					lblLoginSucceeded.setVisible(true);
					
					ModeChoose.main(null);
					//Home1 frame = new Home1(account);
					//frame.setVisible(true);
					LoginForm2.this.dispose();
					// encode data on your side using BASE64
				} else {
					openAuthURL();
				}
			}

		});

		btnTryAgain = new JButton("Try again");

		btnPIN.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (!isConnectedToInternet) {
					System.out.println("Failed to connect to internet");
					System.exit(-1);
				} else {
					String pin = authPIN.getText();
					System.out.println("PIN text" + authPIN.getText());
					lblBrowser.setVisible(false);
					btnLogIn.setEnabled(false);

					if (account.authoriseAccount(pin)) {

						lblLoginSucceeded.setText("Login Successful!");
						lblLoginSucceeded.setVisible(true);
						ModeChoose.main(null);
//						Home1 frame = new Home1(account);
//						frame.setVisible(true);
						LoginForm2.this.dispose();

					} else {

						lblLoginSucceeded.setText("Login Failed");
						lblLoginSucceeded.setVisible(true);
						btnTryAgain.setVisible(true);

					}

				}
			}
		});

		btnTryAgain.setVisible(false);
		btnTryAgain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openAuthURL();
				btnTryAgain.setVisible(false);
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(
						gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addGroup(
										gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(btnLogIn)
												.addGroup(gl_contentPane.createSequentialGroup()
														.addComponent(authPIN, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED).addComponent(
																btnPIN))
												.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
														.addComponent(lbltoSignIn, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
														.addComponent(lblNewLabel, Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE))
												.addComponent(lblBrowser)
												.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
														.addGroup(Alignment.LEADING,
																gl_contentPane.createSequentialGroup()
																		.addComponent(lblLoginSucceeded,
																				GroupLayout.PREFERRED_SIZE, 124,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addComponent(btnTryAgain))
														.addComponent(lblBrowserInfo, Alignment.LEADING,
																GroupLayout.PREFERRED_SIZE, 193,
																GroupLayout.PREFERRED_SIZE)))
										.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_contentPane
				.setVerticalGroup(
						gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup().addGap(29)
										.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 43,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(lbltoSignIn).addGap(18)
										.addComponent(btnLogIn).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblBrowser).addGap(18)
										.addComponent(lblBrowserInfo, GroupLayout.PREFERRED_SIZE, 19,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
												.addComponent(authPIN, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(btnPIN))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblLoginSucceeded, GroupLayout.PREFERRED_SIZE, 25,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(btnTryAgain, GroupLayout.PREFERRED_SIZE, 22,
														GroupLayout.PREFERRED_SIZE))
										.addContainerGap(27, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);
	}
}