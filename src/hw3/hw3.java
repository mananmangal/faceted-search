package hw3;

import java.awt.*;

import hw3.Models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class hw3 {

	ArrayList<String> selectedCategories = new ArrayList<String>();
	ArrayList<String> selectedSubCategories = new ArrayList<String>();
	ArrayList<String> selectedAttributes = new ArrayList<String>();
	ArrayList<String> generatedBusinessIds = new ArrayList<String>();
	ArrayList<String> generatedUserIds = new ArrayList<String>();

	private JFrame frame;
	private JComboBox<String> comboBox;
	private JComboBox<String> searchCombo;
	private JComboBox<String> reviewStarsCombo;
	private JComboBox<String> reviewVotesCombo;
	private JComboBox<String> reviewCountCombo;
	private JComboBox<String> numberFriendsCombo;
	private JComboBox<String> avgStarsCombo;
	private JComboBox<String> numberVotesCombo;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JPanel jPanel4;
	private JPanel jPanel5;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private JScrollPane jScrollPane4;
	private JScrollPane jScrollPane5;
	private JScrollPane jScrollPane6;
	private JScrollPane jScrollPane7;
	private JTable jTable1;
	private JTextField jTextField1;
	private JTextField jTextField2;
	private JTextField reviewStarText;
	private JTextField reviewVoteText;
	private JTextField memberSinceText;
	private JTextField reviewCountText;
	private JTextField numberFriendsText;
	private JTextField avgStarsText;
	private JTextField numberVotesText;
	private JTextArea jTextArea;

	private JLabel lblYelpSearch;
	private JLabel lblNewLabel_1;
	private JLabel lblMemberSince;
	private JLabel lblReview;
	private JLabel lblReviewFrom;
	private JLabel lblReviewTo;
	private JLabel lblReviewStar;
	private JLabel lblReviewVote;
	private JLabel lblReviewCount;
	private JLabel lblNumberFriends;
	private JLabel lblAverageStars;
	private JLabel lblNumberVotes;

	private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
	private org.jdesktop.swingx.JXDatePicker jXDatePicker2;
	private org.jdesktop.swingx.JXDatePicker jXDatePicker3;

	private DBHelper db;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					hw3 window = new hw3();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public hw3() {
		db = new DBHelper();
		db.DBConnect();

		initComponents();
		loadCategories();
	}

	private void initComponents() {

		frame = new JFrame();
		frame.setPreferredSize(new java.awt.Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		jScrollPane2 = new JScrollPane();
		jPanel1 = new JPanel();
		jScrollPane3 = new JScrollPane();
		jTable1 = new JTable();
		jScrollPane4 = new JScrollPane();
		jPanel2 = new JPanel();
		jScrollPane5 = new JScrollPane();
		jPanel3 = new JPanel();
		jPanel5 = new JPanel();
		comboBox = new JComboBox<String>();
		searchCombo = new JComboBox<String>();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		jTextField1 = new JTextField();
		jTextArea = new JTextArea(10, 10);
		jPanel4 = new JPanel();
		jScrollPane6 = new JScrollPane();
		jScrollPane7 = new JScrollPane();
		reviewStarsCombo = new JComboBox<String>();
		reviewStarText = new JTextField();
		reviewVotesCombo = new JComboBox<String>();
		reviewVoteText = new JTextField();
		memberSinceText = new JTextField();
		reviewCountText = new JTextField();
		numberFriendsText = new JTextField();
		avgStarsText = new JTextField();
		numberVotesText = new JTextField();
		reviewCountCombo = new JComboBox<String>();
		numberFriendsCombo = new JComboBox<String>();
		avgStarsCombo = new JComboBox<String>();
		numberVotesCombo = new JComboBox<String>();

		jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
		jXDatePicker2 = new org.jdesktop.swingx.JXDatePicker();
		jXDatePicker3 = new org.jdesktop.swingx.JXDatePicker();

		jTextField2 = new JTextField();

		jScrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		jPanel1.setLayout(new java.awt.GridLayout(0, 1));
		jScrollPane2.setViewportView(jPanel1);

		jTable1.setModel(
				new javax.swing.table.DefaultTableModel(
						new Object[][] { { null, null, null, null }, { null, null, null, null },
								{ null, null, null, null }, { null, null, null, null } },
						new String[] { "Business", "City", "State", "Stars" }) {
					Class[] types = new Class[] { java.lang.String.class, java.lang.String.class,
							java.lang.String.class, java.lang.String.class };

					public Class getColumnClass(int columnIndex) {
						return types[columnIndex];
					}
				});
		jScrollPane3.setViewportView(jTable1);

		jPanel2.setLayout(new java.awt.GridLayout(0, 1));
		jScrollPane4.setViewportView(jPanel2);

		jPanel3.setLayout(new java.awt.GridLayout(0, 1));
		jScrollPane5.setViewportView(jPanel3);

		reviewStarsCombo.setModel(new DefaultComboBoxModel<String>(new String[] { "=", "<", ">" }));
		reviewVotesCombo.setModel(new DefaultComboBoxModel<String>(new String[] { "=", "<", ">" }));
		reviewCountCombo.setModel(new DefaultComboBoxModel<String>(new String[] { "=", "<", ">" }));
		numberFriendsCombo.setModel(new DefaultComboBoxModel<String>(new String[] { "=", "<", ">" }));
		avgStarsCombo.setModel(new DefaultComboBoxModel<String>(new String[] { "=", "<", ">" }));
		numberVotesCombo.setModel(new DefaultComboBoxModel<String>(new String[] { "=", "<", ">" }));

		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "AND", "OR" }));
		searchCombo.setModel(new DefaultComboBoxModel<String>(new String[] { "AND", "OR" }));

		jTextField1.setText("");
		jTextField2.setText("");

		jTextArea.setText("");
		jTextArea.setEditable(false);
		jTextArea.setLineWrap(true);
		jTextArea.setWrapStyleWord(false);

		JButton jButton1 = new JButton("Execute Query");
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateData();
			}
		});

		JButton jButton2 = new JButton("Execute User Query");
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateUserData();
			}
		});

		lblReview = new JLabel("Review");
		lblYelpSearch = new JLabel("Result");
		lblNewLabel_1 = new JLabel("Search for");
		lblMemberSince = new JLabel("Member Since");
		lblReviewFrom = new JLabel("From");
		lblReviewTo = new JLabel("To");
		lblReviewStar = new JLabel("Stars");
		lblReviewVote = new JLabel("Votes");
		lblReviewCount = new JLabel("Review Count");
		lblNumberFriends = new JLabel("Number of Friends");
		lblAverageStars = new JLabel("Average Stars");
		lblNumberVotes = new JLabel("Number of Votes");

		memberSinceText.setEditable(false);

		jPanel4.setLayout(new java.awt.GridLayout(0, 3));
		jPanel4.add(lblMemberSince);
		jPanel4.add(jXDatePicker1);
		jPanel4.add(memberSinceText);
		jPanel4.add(lblReviewCount);
		jPanel4.add(reviewCountCombo);
		jPanel4.add(reviewCountText);
		jPanel4.add(lblNumberFriends);
		jPanel4.add(numberFriendsCombo);
		jPanel4.add(numberFriendsText);
		jPanel4.add(lblAverageStars);
		jPanel4.add(avgStarsCombo);
		jPanel4.add(avgStarsText);
		jPanel4.add(lblNumberVotes);
		jPanel4.add(numberVotesCombo);
		jPanel4.add(numberVotesText);
		jPanel4.add(searchCombo);
		jScrollPane6.setViewportView(jPanel4);

		jPanel5.setLayout(new java.awt.GridLayout(0, 1));
		jPanel5.add(lblReview);
		jPanel5.add(lblReviewFrom);
		jPanel5.add(jXDatePicker2);
		jPanel5.add(lblReviewTo);
		jPanel5.add(jXDatePicker3);
		jPanel5.add(lblReviewStar);
		jPanel5.add(reviewStarsCombo);
		jPanel5.add(reviewStarText);
		jPanel5.add(lblReviewVote);
		jPanel5.add(reviewVotesCombo);
		jPanel5.add(reviewVoteText);
		jScrollPane7.setViewportView(jPanel5);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addComponent(jScrollPane2,
										GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
								.addGroup(layout.createSequentialGroup().addComponent(lblNewLabel_1)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 200,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(jScrollPane7, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout
										.createSequentialGroup()
										.addGroup(layout.createParallelGroup(Alignment.TRAILING)
												.addGroup(
														layout.createSequentialGroup().addComponent(jLabel3).addGap(50))
												.addGroup(layout.createSequentialGroup().addComponent(jLabel2)
														.addGap(40)))
										.addGap(32))))
								.addGroup(layout.createSequentialGroup().addGap(250).addComponent(lblYelpSearch))
								.addGroup(layout.createSequentialGroup().addGap(32).addComponent(jScrollPane3,
										GroupLayout.PREFERRED_SIZE, 490, GroupLayout.PREFERRED_SIZE))
								.addGroup(layout.createSequentialGroup().addGap(100).addComponent(jButton1).addGap(150)
										.addComponent(jButton2))))
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane6, GroupLayout.DEFAULT_SIZE, 800, GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createSequentialGroup().addGap(10).addComponent(jTextArea)).addGap(11)));

		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout
						.createParallelGroup(Alignment.LEADING, false)
						.addGroup(layout.createSequentialGroup()
								.addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 400, 400).addGap(18)
								.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lblNewLabel_1).addComponent(comboBox, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGap(18))
						.addGroup(layout.createSequentialGroup().addComponent(lblYelpSearch).addComponent(jScrollPane3,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(layout.createSequentialGroup().addComponent(jLabel2).addGap(44).addComponent(jLabel3))
						.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(jScrollPane5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, 400)
								.addComponent(jScrollPane4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, 400)
								.addComponent(jScrollPane7, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, 400)))
				.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(jScrollPane6, GroupLayout.DEFAULT_SIZE, 250, 800)
						.addGroup(layout.createSequentialGroup().addComponent(jTextArea).addGap(18)
								.addGroup(layout.createParallelGroup().addComponent(jButton1).addComponent(jButton2))))
				.addContainerGap(72, Short.MAX_VALUE)));

		jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = jTable1.rowAtPoint(evt.getPoint());
				if (!(generatedBusinessIds.size() <= row)) {
					frame.setEnabled(false);
					jTable1.setOpaque(false);
					ArrayList<String> filters = getReviewFilters();
					ReviewsPage rp = new ReviewsPage(generatedBusinessIds.get(row), filters);
					rp.setVisible(true);
					jTextArea.setText(rp.query);
					frame.setEnabled(true);
				}
				if (!(generatedUserIds.size() <= row)) {
					frame.setEnabled(false);
					jTable1.setOpaque(false);
					ReviewsPage rp = new ReviewsPage(generatedUserIds.get(row));
					rp.setVisible(true);
					jTextArea.setText(rp.query);
					frame.setEnabled(true);
				}
			}
		});

		DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
		dtm.setRowCount(0);
		jTable1.setDefaultRenderer(Object.class, new MyTableCellRender());
		jTable1.setGridColor(Color.BLACK);
		frame.getContentPane().setLayout(layout);

		frame.pack();
	}

	private ArrayList<String> getReviewFilters() {
		ArrayList<String> filters = new ArrayList<String>();
		if (jXDatePicker2.getDate() != null)
			filters.add(new SimpleDateFormat("dd-MMM-yy").format(jXDatePicker2.getDate()).toString());
		else
			filters.add("");

		if (jXDatePicker3.getDate() != null)
			filters.add(new SimpleDateFormat("dd-MMM-yy").format(jXDatePicker3.getDate()).toString());
		else
			filters.add("");

		filters.add(reviewStarText.getText());
		filters.add(reviewStarsCombo.getSelectedItem().toString());

		filters.add(reviewVoteText.getText());
		filters.add(reviewVotesCombo.getSelectedItem().toString());

		return filters;
	}

	private void loadCategories() {
		jPanel1.removeAll();
		ReturnString rs = new ReturnString();
		rs = db.getAllCategories();
		ArrayList<String> cats = rs.str;
		for (int i = 0; i < cats.size(); i++) {
			JCheckBox mycheckbox = new JCheckBox();
			mycheckbox.setSize(10, 10);
			mycheckbox.setText(cats.get(i));
			mycheckbox.setForeground(Color.BLACK);
			mycheckbox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					Object source = e.getItemSelectable();
					JCheckBox checkbox = (JCheckBox) source;
					if (e.getStateChange() == ItemEvent.SELECTED) {
						selectedCategories.add(checkbox.getText());

					} else {
						selectedCategories.remove(checkbox.getText());
					}
					if (selectedCategories.size() > 0) {
						loadSubCategories();
						updateData();
					} else {
						jPanel2.removeAll();
						jPanel2.repaint();

						jPanel3.removeAll();
						jPanel3.repaint();

						DefaultTableModel tmodel = new DefaultTableModel();
						jTable1.removeAll();
						jTable1.setModel(tmodel);
						tmodel.addColumn("Business");
						tmodel.addColumn("City");
						tmodel.addColumn("State");
						tmodel.addColumn("Stars");
					}
				}
			});
			jPanel1.add(mycheckbox);
			jTextArea.setText(rs.query);
			frame.pack();
		}
	}

	private void loadSubCategories() {
		ReturnString rs = new ReturnString();
		String condition = comboBox.getSelectedItem().toString();
		rs = db.getSubCategories(selectedCategories, condition);
		ArrayList<String> subs = rs.str;
		jPanel2.removeAll();
		for (int i = 0; i < subs.size(); i++) {
			JCheckBox mycheckbox = new JCheckBox();
			mycheckbox.setSize(10, 10);
			mycheckbox.setText(subs.get(i));
			mycheckbox.setForeground(Color.BLACK);
			mycheckbox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					Object source = e.getItemSelectable();
					JCheckBox checkbox = (JCheckBox) source;
					if (e.getStateChange() == ItemEvent.SELECTED) {
						selectedSubCategories.add(checkbox.getText());

					} else {
						selectedSubCategories.remove(checkbox.getText());
					}
					if (selectedSubCategories.size() > 0) {
						loadAttributes();
						updateData();
					} else {

						jPanel3.removeAll();
						jPanel3.repaint();

						DefaultTableModel tmodel = new DefaultTableModel();
						jTable1.removeAll();
						jTable1.setModel(tmodel);
						tmodel.addColumn("Business");
						tmodel.addColumn("City");
						tmodel.addColumn("State");
						tmodel.addColumn("Stars");
					}
				}
			});
			jPanel2.add(mycheckbox);
			jTextArea.setText(rs.query);
			frame.pack();
		}
	}

	public void loadAttributes() {
		String condition = comboBox.getSelectedItem().toString();
		ReturnAttribute ra = new ReturnAttribute();
		ra = db.getAttributes(selectedSubCategories, selectedCategories, condition);
		ArrayList<Attribute> attrs = ra.attributes;
		jPanel3.removeAll();
		for (int i = 0; i < attrs.size(); i++) {
			JCheckBox mycheckbox = new JCheckBox();
			mycheckbox.setSize(10, 10);
			mycheckbox.setText(attrs.get(i).name);
			mycheckbox.setForeground(Color.BLACK);
			mycheckbox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					Object source = e.getItemSelectable();
					JCheckBox checkbox = (JCheckBox) source;
					if (e.getStateChange() == ItemEvent.SELECTED) {
						selectedAttributes.add(checkbox.getText());

					} else {
						selectedAttributes.remove(checkbox.getText());
					}
					if (selectedAttributes.size() > 0) {
//                        fillTable();
						updateData();
					} else {
						DefaultTableModel tmodel = new DefaultTableModel();
						jTable1.removeAll();
						jTable1.setModel(tmodel);
						tmodel.addColumn("Business");
						tmodel.addColumn("City");
						tmodel.addColumn("State");
						tmodel.addColumn("Stars");
					}
				}
			});
			jPanel3.add(mycheckbox);
			jTextArea.setText(ra.query);
			frame.pack();
		}

	}

	public void updateData() {

		DefaultTableModel tmodel = new DefaultTableModel();
		jTable1.removeAll();
		jTable1.setModel(tmodel);
		tmodel.addColumn("Business");
		tmodel.addColumn("City");
		tmodel.addColumn("State");
		tmodel.addColumn("Stars");

		Date reviewFrom = null;
		Date reviewTo = null;

		if (jXDatePicker2.getDate() != null)
			reviewFrom = new java.sql.Date(jXDatePicker2.getDate().getTime());
		if (jXDatePicker3.getDate() != null)
			reviewTo = new java.sql.Date(jXDatePicker3.getDate().getTime());

		String reviewStars = reviewStarText.getText();
		String reviewVotes = reviewVoteText.getText();

		String starsCondition = reviewStarsCombo.getSelectedItem().toString();
		String votesCondition = reviewVotesCombo.getSelectedItem().toString();

		String condition = comboBox.getSelectedItem().toString();

		ArrayList<Business> businesses = new ArrayList<Business>();
		ReturnBusiness rb = new ReturnBusiness();
		if (selectedAttributes.size() == 0 && selectedSubCategories.size() == 0 && selectedCategories.size() > 0) {
			rb = db.queryBusinessByCategory(selectedCategories, reviewFrom, reviewTo, reviewStars, starsCondition,
					reviewVotes, votesCondition, condition);
		} else if (selectedCategories.size() > 0 && selectedSubCategories.size() > 0
				&& selectedAttributes.size() == 0) {
			rb = db.queryBusinessByCategorySubCategory(selectedCategories, selectedSubCategories, reviewFrom, reviewTo,
					reviewStars, starsCondition, reviewVotes, votesCondition, condition);
		} else if (selectedCategories.size() > 0 && selectedSubCategories.size() > 0 && selectedAttributes.size() > 0) {
			if (reviewFrom != null || reviewTo != null || reviewStars.length() > 0 || reviewVotes.length() > 0) {
				rb = db.advancedQueryBusiness(selectedCategories, selectedSubCategories, selectedAttributes, reviewFrom,
						reviewTo, reviewStars, starsCondition, reviewVotes, votesCondition, condition);
			} else {
				rb = db.queryBusiness(selectedAttributes, selectedCategories, selectedSubCategories, reviewFrom,
						reviewTo, reviewStars, starsCondition, reviewVotes, votesCondition, condition);
			}
		}

		businesses = rb.business;

		generatedUserIds = new ArrayList<String>();
		generatedBusinessIds = new ArrayList<String>();

		for (int i = 0; i < businesses.size(); i++) {
			Business business = businesses.get(i);
			tmodel.addRow(new Object[] { business.name, business.city, business.state, business.stars });
			generatedBusinessIds.add(business.businessId);
		}
		jTextArea.setText(rb.query);
		frame.pack();

	}

	public void updateUserData() {
		DefaultTableModel tmodel = new DefaultTableModel();
		jTable1.removeAll();
		jTable1.setModel(tmodel);
		tmodel.addColumn("User");
		tmodel.addColumn("Yelping_since");
		tmodel.addColumn("Average_stars");

		Date memberSince = new java.sql.Date(jXDatePicker1.getDate().getTime());
		String reviewCount = reviewCountText.getText();
		String numberFriends = numberFriendsText.getText();
		String averageStars = avgStarsText.getText();
		String numberVotes = numberVotesText.getText();

		String reviewCondition = reviewCountCombo.getSelectedItem().toString();
		String friendsCondition = numberFriendsCombo.getSelectedItem().toString();
		String starsCondition = avgStarsCombo.getSelectedItem().toString();
		String votesCondition = numberVotesCombo.getSelectedItem().toString();

		String searchCondition = searchCombo.getSelectedItem().toString();

		ArrayList<User> users = new ArrayList<User>();
		ReturnUser ru = new ReturnUser();
		ru = db.queryUser(memberSince, reviewCount, reviewCondition, numberFriends, friendsCondition, averageStars,
				starsCondition, numberVotes, votesCondition, searchCondition);

		users = ru.users;

		generatedUserIds = new ArrayList<String>();
		generatedBusinessIds = new ArrayList<String>();

		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			tmodel.addRow(new Object[] { user.name, user.yelpingSince, user.averageStars });
			generatedUserIds.add(user.userId);
		}
		jTextArea.setText(ru.query);
		frame.pack();
	}

	class MyTableCellRender extends DefaultTableCellRenderer {

		public MyTableCellRender() {
		}

		final JLabel headerLabel = new JLabel();
		{
			headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.RED));
			headerLabel.setOpaque(true);
			headerLabel.setBackground(Color.WHITE);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			setForeground(Color.black);
			setBackground(Color.white);
			setText(value != null ? value.toString() : "");
			return this;
		}
	}
}
