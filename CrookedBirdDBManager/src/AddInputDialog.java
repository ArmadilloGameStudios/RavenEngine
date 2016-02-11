import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class AddInputDialog extends JDialog {
	private JButton btnAdd = new JButton();
	private JTextField txtInputName = new JTextField(10);
	private JPanel pn = new JPanel(), pnTxts = new JPanel(),
			pnBtns = new JPanel();
	private Dimension size = new Dimension(350, 200);
	private Dimension btnSize = new Dimension(120, 50);
	private String inputName = "";

	public AddInputDialog() {
		setModal(true);
		setTitle("Add " + inputName);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		// setSize(size);
		// setMinimumSize(size);
		setResizable(false);

		txtInputName.setVisible(true);
		pnTxts.add(txtInputName);

		btnAdd.setText("Add " + inputName);
		// btnAdd.setSize(btnSize);
		pnBtns.add(btnAdd);

		add(pn);
		pn.add(pnTxts);
		pn.add(pnBtns);
		

		pack();
	}
	
	public void addAddInputActionListener(ActionListener a) {
		btnAdd.addActionListener(a);
	}
	
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}
	
	public String getInputText() {
		return txtInputName.getText();
	}
}
