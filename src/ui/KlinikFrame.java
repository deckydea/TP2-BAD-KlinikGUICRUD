package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import connection.DatabaseManager;
import model.Pasien;
import utils.DateUtils;

public class KlinikFrame {
	private JFrame frame;
	private List<Pasien> dataPasien;
	private int currentIndex;

	private JTextField txtName, txtAddress, txtNIK, txtDateOfBirth;
	private JLabel lblNo, lblName, lblNIK, lblDateOfBirth, lblAddress;
	private JButton btnCreate, btnUpdate, btnDelete, btnExit;

	private JTable table;
	private DefaultTableModel tableModel;
	private TableRowSorter<DefaultTableModel> rowSorter;

	// Database
	final DatabaseManager dbManager;

	public KlinikFrame() {
		frame = new JFrame("Aplikasi Klinik");
		frame.setSize(600, 800);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		dataPasien = new ArrayList<>();
		currentIndex = -1;

		// Create a main panel with GridBagLayout
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		// Panel for input fields
		JPanel panelInput = new JPanel(new GridBagLayout());

		lblNo = new JLabel("");
		lblName = new JLabel("Nama Pasien:");
		txtName = new JTextField();
		lblNIK = new JLabel("NIK:");
		txtNIK = new JTextField();
		lblDateOfBirth = new JLabel("Tanggal Lahir (YYYY-MM-DD):");
		txtDateOfBirth = new JTextField();
		lblAddress = new JLabel("Alamat:");
		txtAddress = new JTextField();

		gbc.gridx = 0;
		gbc.gridy = 0;
		panelInput.add(lblName, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		txtName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		panelInput.add(txtName, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		panelInput.add(lblNIK, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		txtNIK.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		panelInput.add(txtNIK, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		panelInput.add(lblDateOfBirth, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		txtDateOfBirth.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		panelInput.add(txtDateOfBirth, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		panelInput.add(lblAddress, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		txtAddress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		panelInput.add(txtAddress, gbc);

		// Panel for buttons
		JPanel panelButtons = new JPanel(new FlowLayout());

		btnCreate = new JButton("Tambah");
		btnUpdate = new JButton("Update");
		btnDelete = new JButton("Hapus");
		btnExit = new JButton("Keluar");

		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tambahDataPasien();
			}
		});

		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDataPasien();
			}
		});

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hapusDataPasien();
			}
		});

		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		panelButtons.add(btnCreate);
		panelButtons.add(btnUpdate);
		panelButtons.add(btnDelete);
		panelButtons.add(btnExit);

		// Add the input and buttons panels to the main panel
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		mainPanel.add(panelInput, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		mainPanel.add(panelButtons, gbc);

		// Panel for the table
		JPanel panelTable = new JPanel(new BorderLayout());
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(new String[] { "No", "Nama Pasien", "NIK", "Tanggal Lahir", "Alamat" });

		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);

		panelTable.add(scrollPane, BorderLayout.CENTER);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					currentIndex = selectedRow;
					updateUI();
				}
			}
		});

		// Create a TableRowSorter
		rowSorter = new TableRowSorter<>(tableModel);
		table.setRowSorter(rowSorter);

		// Add a search/filter field
		JTextField searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(150, 30));

		// Add a listener to filter the table
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				filterTable(searchField.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filterTable(searchField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				filterTable(searchField.getText());
			}
		});

		JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelSearch.add(new JLabel("Cari: "));
		panelSearch.add(searchField);

		// Add the search field to the main panel
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		mainPanel.add(panelSearch, gbc);

		// Add the main panel to the frame
		frame.add(mainPanel, BorderLayout.NORTH);
		frame.add(panelTable, BorderLayout.CENTER);

		frame.setVisible(true);

		// Initialize DatabaseManager
		dbManager = new DatabaseManager();

		// load pasien from database
		dataPasien = dbManager.loadPasienData();
		
		// show data to the UI
		refreshTable();
	}
	
	private void refreshTable() {
        tableModel.setRowCount(0);
        //Insert row
        for (Pasien pasien : dataPasien) {
			tableModel.addRow(new String[] { Integer.toString(tableModel.getRowCount() + 1), pasien.getName(), pasien.getNik(),
					DateUtils.formatDate(pasien.getDateOfBirth()), pasien.getAddress() });
        }
    }

	private void tambahDataPasien() {
		String nama = txtName.getText();
		String alamat = txtAddress.getText();
		String nik = txtNIK.getText();
		Date tanggalLahir = DateUtils.parseDate(txtDateOfBirth.getText());

		if (isNIKUnique(nik)) {
			Pasien pasien = new Pasien(nama, alamat, nik, tanggalLahir);

			// Add pasien
			dbManager.createDataPasien(pasien);

			//Insert row
			tableModel.addRow(new String[] { Integer.toString(tableModel.getRowCount() + 1), nama, nik,
					DateUtils.formatDate(tanggalLahir), alamat });
			
			dataPasien.add(pasien);

			clearInputFields();
			JOptionPane.showMessageDialog(frame,
					"Pasien " + pasien.getName() + "(" + pasien.getNik() + ") berhasil ditambahkan.");

		} else {
			JOptionPane.showMessageDialog(frame, "NIK sudah ada dalam data pasien.");
		}
	}

	private void updateDataPasien() {
		if (currentIndex >= 0 && currentIndex < dataPasien.size()) {
			String name = txtName.getText();
			String address = txtAddress.getText();
			String nik = txtNIK.getText();
			Date dateOfBirth = DateUtils.parseDate(txtDateOfBirth.getText());

			Pasien pasien = dataPasien.get(currentIndex);
			pasien.setName(name);
			pasien.setAddress(address);
			pasien.setNik(nik);
			pasien.setDateOfBirth(dateOfBirth);

			// Update Pasien
			dbManager.updateDataPasien(pasien);

			// Set value
			tableModel.setValueAt(name, currentIndex, 1);
			tableModel.setValueAt(nik, currentIndex, 2);
			tableModel.setValueAt(DateUtils.formatDate(dateOfBirth), currentIndex, 3);
			tableModel.setValueAt(address, currentIndex, 4);

			JOptionPane.showMessageDialog(frame,
					"Pasien " + pasien.getName() + "(" + pasien.getNik() + ") berhasil diupdate.");

			updateUI();
		} else {
			JOptionPane.showMessageDialog(frame, "Tidak ada data pasien yang dipilih.");
		}
	}

	private void hapusDataPasien() {
		if (currentIndex >= 0 && currentIndex < dataPasien.size()) {
			Pasien pasien = dataPasien.get(currentIndex);
			dataPasien.remove(currentIndex);
			tableModel.removeRow(currentIndex);

			// Delete pasien
			dbManager.deleteDataPasien(pasien.getId());

			if (currentIndex >= dataPasien.size()) {
				currentIndex = dataPasien.size() - 1;
			}

			updateUI();
			clearInputFields();
			
			JOptionPane.showMessageDialog(frame,
					"Pasien " + pasien.getName() + "(" + pasien.getNik() + ") berhasil dihapus.");
		} else {
			JOptionPane.showMessageDialog(frame, "Tidak ada data pasien yang dipilih.");
		}
	}

	private void updateUI() {
		if (currentIndex >= 0 && currentIndex < dataPasien.size()) {
			Pasien pasien = dataPasien.get(currentIndex);
			lblNo.setText(Integer.toString(currentIndex + 1));
			txtName.setText(pasien.getName());
			txtAddress.setText(pasien.getAddress());
			txtNIK.setText(pasien.getNik());
			txtDateOfBirth.setText(DateUtils.formatDate(pasien.getDateOfBirth()));
		} else {
			lblNo.setText("");
			txtName.setText("");
			txtAddress.setText("");
			txtNIK.setText("");
			txtDateOfBirth.setText("");
		}
	}

	private boolean isNIKUnique(String nik) {
		for (Pasien pasien : dataPasien) {
			if (pasien.getNik().equals(nik)) {
				return false;
			}
		}
		return true;
	}

	private void filterTable(String text) {
		if (text.trim().length() == 0) {
			rowSorter.setRowFilter(null);
		} else {
			rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
		}
	}

	private void clearInputFields() {
		txtName.setText("");
		txtAddress.setText("");
		txtNIK.setText("");
		txtDateOfBirth.setText("");
	}
}