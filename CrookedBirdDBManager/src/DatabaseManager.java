import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.crookedbird.engine.database.GameDataTable;
import com.crookedbird.engine.database.GameDatabase;

public class DatabaseManager extends JFrame { // implements WindowListener {

	private JTabbedPane tpane;
	private GameDatabase gdb;

	public static void main(String[] args) {
		new DatabaseManager();
	}

	public DatabaseManager() {
		super();

		// init window

		// addWindowListener(this);

		setTitle("CrookedBird DB Manager");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// setMinimumSize(size);
		// setPreferredSize(size);
		// setSize(size);
		setVisible(true);

		// load data
		gdb = new GameDatabase();
		gdb.load();

		// init tabs
		tpane = new JTabbedPane();
		tpane.setVisible(true);
		// tpane.setMinimumSize(size);
		// tpane.setPreferredSize(size);
		// tpane.setSize(size);

		// pop tabs
		for (GameDataTable gdtb : gdb.getTables()) {
			addTab(gdtb);
		}

		// add new componants
		add(tpane);
		pack();
	}

	public void addTab(final GameDataTable gdtb) {
		final DatabaseManager dm = this;

		JPanel p = new JPanel();

		final JTable t = new JTable(gdtb);
		t.setVisible(true);

		gdtb.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				String title = tpane.getTitleAt(tpane.getSelectedIndex());
				if (title.charAt(title.length() - 1) != '*') {
					tpane.setTitleAt(tpane.getSelectedIndex(), title + "*");
				}
			}
		});

		final JScrollPane sp = new JScrollPane(t);
		sp.setVisible(true);
		p.add(sp);

		JPanel pb = new JPanel(); // panelbuttons
		pb.setLayout(new BoxLayout(pb, BoxLayout.Y_AXIS));
		p.add(pb);

		JPanel pbr = new JPanel(); // panelbuttonsrow
		pb.add(pbr);

		JPanel pbc = new JPanel(); // panelbuttonscol
		pb.add(pbc);

		JPanel pbd = new JPanel(); // panelbuttonsdata
		pb.add(pbd);

		JPanel pbs = new JPanel(); // panelbuttonssave
		pb.add(pbs);

		JButton btn;
		// Add row
		btn = new JButton("Add New Row");
		btn.setVisible(true);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] vals = new Object[gdtb.getColumnCount()];
				vals[0] = gdtb.getRowCount() + 1;
				gdtb.addRow(Arrays.asList(vals));
				t.revalidate();
			}
		});
		pbr.add(btn);

		// Delete row
		btn = new JButton("Delete Row");
		btn.setVisible(true);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = t.getSelectedRow();
				if (row >= 0) {
					gdtb.deleteRow(row);
					t.revalidate();
				}
			}
		});
		pbr.add(btn);

		// Add col
		btn = new JButton("Add New Col");
		btn.setVisible(true);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final AddInputDialog r = new AddInputDialog();
				r.setInputName("Col");

				r.addAddInputActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						gdtb.addColumn(r.getInputText());
						t.revalidate();
					}
				});

				r.setVisible(true);
			}
		});
		pbc.add(btn);

		// Delete col
		btn = new JButton("Delete Col");
		btn.setVisible(true);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int col = t.getSelectedColumn();
				if (col > 0) {
					gdtb.deleteColumn(col);
					t.revalidate();
				}
			}
		});
		pbc.add(btn);

		// Add table
		btn = new JButton("Add New Table");
		btn.setVisible(true);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final AddInputDialog r = new AddInputDialog();
				r.setInputName("Table");

				r.addAddInputActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dm.addTab(gdb.newTable(r.getInputText()));
					}
				});

				r.setVisible(true);
			}
		});
		pbd.add(btn);

		// Delete table
		btn = new JButton("Delete Table");
		btn.setVisible(true);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tpane.getTabCount() > 1) {
					gdb.deleteTable(gdtb.getName());
					tpane.removeTabAt(tpane.getSelectedIndex());
				}
			}
		});
		pbd.add(btn);

		// Save
		btn = new JButton("Save All");
		btn.setVisible(true);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gdb.save();

				for (int i = 0; i < tpane.getTabCount(); i++) {

					String title = tpane.getTitleAt(i);
					if (title.charAt(title.length() - 1) == '*') {
						tpane.setTitleAt(i,
								title.substring(0, title.length() - 1));
					}
				}
			}
		});
		pbs.add(btn);

		tpane.addTab(gdtb.getName(), p);
	}
}
