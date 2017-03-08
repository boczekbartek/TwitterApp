package org.eclipse.wb.swing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import twitterApp.FileTools;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class ProFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private String pathClassi;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProFrame frame = new ProFrame();
					System.out.println("pro frame");
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ProFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Alert alert = new Alert("Are you sure?");
				alert.setVisible(true);

			}
		});
		pathClassi = null;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 377);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Paste below a path to file of your training data");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JTextPane txtpnNote = new JTextPane();
		txtpnNote.setEditable(false);
		txtpnNote.setFont(new Font("Tahoma", Font.ITALIC, 13));
		txtpnNote.setText("NOTE: THis should be the path to file that contain data labelled as in this example:\r\nI like mountains! They make me so happy and relaxed!      1 /new line/\r\n1 - positive, 0 - negative, 2 - neutral");
		JButton btnNewButton = new JButton("Next");
		btnNewButton.setVisible(false);
		JButton btnTrain = new JButton("Train!");
		btnTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setVisible(true);
				String path = textField.getText();
				try {
					String aa = FileTools.divideIntoFIles(path);
					System.out.println(aa);
					String output = FileTools.train(aa);
					txtpnNote.setText("Yout classifier was saved there: \n" + path + "\\" + (String) output);
					pathClassi = output;
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (null != pathClassi){
					String [] a = {pathClassi};
					Home1.main(a);
				}else {
					Home1.main(null);
				}
				ProFrame.this.dispose();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel, Alignment.LEADING)
						.addComponent(txtpnNote, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(85)
					.addComponent(btnTrain, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton)
					.addContainerGap(179, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(42)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addGap(16)
					.addComponent(txtpnNote, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
					.addGap(26)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnTrain, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
					.addGap(36))
		);
		contentPane.setLayout(gl_contentPane);
	}

}