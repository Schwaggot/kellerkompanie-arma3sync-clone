package fr.soe.a3s.ui.tools.tfar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import fr.soe.a3s.dao.FileAccessMethods;
import fr.soe.a3s.exception.WritingException;
import fr.soe.a3s.ui.Facade;
import fr.soe.a3s.ui.tools.WizardDialog;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class SecondPageTFARInstallerPanel extends WizardDialog {

	private JLabel labelOperationsPerformed;
	private JLabel labelCopyPlugin;
	private JLabel labelCopyPluginValue;
	private JLabel labelCopyUserconfig;
	private JLabel labelCopyUserconfigValue;
	private JLabel labelMessage1;
	private JLabel labelMessage2;
	private JButton buttonCopyPlugin;
	private JButton buttonCopyUserconfig;
	private String armA3Directory;
	private String ts3Directory;
	private final FirstPageTFARInstallerPanel firstPageTFARInstallerPanel;
	private static String statusOK = "OK";
	private static String statusFail = "Fail";

	public SecondPageTFARInstallerPanel(Facade facade,
			FirstPageTFARInstallerPanel firstPageTFARInstallerPanel,
			String title, String description, Image image) {
		super(facade, title, description, image);

		this.firstPageTFARInstallerPanel = firstPageTFARInstallerPanel;
		buttonFist.setText("Back");
		buttonSecond.setText("Close");
		getRootPane().setDefaultButton(buttonSecond);

		JPanel centerPanel = new JPanel();
		this.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setBorder(BorderFactory
				.createEtchedBorder(BevelBorder.LOWERED));
		centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		Box vBox = Box.createVerticalBox();
		centerPanel.add(vBox);
		vBox.add(Box.createVerticalStrut(20));
		{
			labelOperationsPerformed = new JLabel(
					"The following operations have been performed:");
			Box hBox = Box.createHorizontalBox();
			hBox.add(labelOperationsPerformed);
			hBox.add(Box.createHorizontalGlue());
			vBox.add(hBox);
		}
		vBox.add(Box.createVerticalStrut(10));
		{
			labelCopyPlugin = new JLabel(
					"  - TFAR plugin copied into TS3\\plugins directory. ");
			labelCopyPluginValue = new JLabel();
			Box hBox = Box.createHorizontalBox();
			hBox.add(labelCopyPlugin);
			hBox.add(Box.createHorizontalStrut(5));
			hBox.add(labelCopyPluginValue);
			hBox.add(Box.createHorizontalStrut(20));
			buttonCopyPlugin = new JButton("View");
			hBox.add(buttonCopyPlugin);
			hBox.add(Box.createHorizontalGlue());
			vBox.add(hBox);

		}
		vBox.add(Box.createVerticalStrut(10));
		{
			labelCopyUserconfig = new JLabel(
					"  - TFAR userconfig copied into ArmA 3 directory.");
			labelCopyUserconfigValue = new JLabel();
			Box hBox = Box.createHorizontalBox();
			hBox.add(labelCopyUserconfig);
			hBox.add(Box.createHorizontalStrut(5));
			hBox.add(labelCopyUserconfigValue);
			hBox.add(Box.createHorizontalStrut(20));
			buttonCopyUserconfig = new JButton("View");
			hBox.add(buttonCopyUserconfig);
			hBox.add(Box.createHorizontalGlue());
			vBox.add(hBox);
		}
		vBox.add(Box.createVerticalStrut(20));
		{
			labelMessage1 = new JLabel();
			Box hBox = Box.createHorizontalBox();
			hBox.add(labelMessage1);
			hBox.add(Box.createHorizontalGlue());
			vBox.add(hBox);
		}
		vBox.add(Box.createVerticalStrut(10));
		{
			labelMessage2 = new JLabel();
			Box hBox = Box.createHorizontalBox();
			hBox.add(labelMessage2);
			hBox.add(Box.createHorizontalGlue());
			vBox.add(hBox);
			vBox.add(Box.createVerticalStrut(10));
		}

		this.pack();
		this.setMinimumSize(new Dimension(
				facade.getMainPanel().getBounds().width,
				this.firstPageTFARInstallerPanel.getBounds().height));
		this.setPreferredSize(new Dimension(
				facade.getMainPanel().getBounds().width,
				this.firstPageTFARInstallerPanel.getBounds().height));
		this.pack();
		this.setLocationRelativeTo(facade.getMainPanel());

		buttonCopyPlugin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				buttonViewPluginPerformed();
			}
		});
		buttonCopyUserconfig.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonViewUserconfigPerformed();
			}
		});
	}

	public void init(String armA3Directory, String ts3Directory,
			String tfarUserconfigDirectory, String tfarPuginPath) {

		boolean copyUserconfig = false;
		boolean copyPlugin = false;
		this.armA3Directory = armA3Directory;
		this.ts3Directory = ts3Directory;

		/* Check write permissions on ArmA 3 directory */
		boolean writeOK = Files.isWritable(FileSystems.getDefault().getPath(
				armA3Directory));
		if (!writeOK) {
			JOptionPane.showMessageDialog(facade.getMainPanel(),
					"Can't to write on: " + armA3Directory + "\n"
							+ "Check files permissions.", "Error",
					JOptionPane.ERROR_MESSAGE);
			copyUserconfig = false;
		} else {
			/* Copy userconfig to ArmA 3 */
			File sourceLocation = new File(tfarUserconfigDirectory);
			File targetLocation = new File(armA3Directory + "/userconfig");
			targetLocation.mkdirs();
			try {
				FileAccessMethods.copyDirectory(sourceLocation, targetLocation);
				copyUserconfig = true;
			} catch (IOException e) {
				e.printStackTrace();
				copyUserconfig = false;
				JOptionPane.showMessageDialog(
						facade.getMainPanel(),
						"Failed to write on: " + armA3Directory + "\n"
								+ e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		/* Check write permissions on TS3 directory */
		writeOK = Files.isWritable(FileSystems.getDefault().getPath(
				ts3Directory));
		if (!writeOK) {
			JOptionPane.showMessageDialog(facade.getMainPanel(),
					"Can't to write on: " + ts3Directory + "\n"
							+ "Check files permissions.", "Error",
					JOptionPane.ERROR_MESSAGE);
			copyPlugin = false;
		} else {
			/* Copy plugin to TS3 */
			File sourceLocation = new File(tfarPuginPath);
			File targetLocation = new File(ts3Directory + "/plugins");
			try {
				FileAccessMethods.copyDirectory(sourceLocation, targetLocation);
				copyPlugin = true;
			} catch (IOException e) {
				e.printStackTrace();
				copyPlugin = false;
				JOptionPane.showMessageDialog(
						facade.getMainPanel(),
						"Failed to write on: " + ts3Directory + "\n"
								+ e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		labelCopyUserconfigValue.setFont(labelCopyUserconfigValue.getFont()
				.deriveFont(Font.BOLD));
		if (copyUserconfig) {
			labelCopyUserconfigValue.setText(statusOK);
			labelCopyUserconfigValue.setForeground(new Color(45, 125, 45));
		} else {
			labelCopyUserconfigValue.setText(statusFail);
			labelCopyUserconfigValue.setForeground(Color.RED);
		}

		labelCopyPluginValue.setFont(labelCopyPluginValue.getFont().deriveFont(
				Font.BOLD));
		if (copyPlugin) {
			labelCopyPluginValue.setText(statusOK);
			labelCopyPluginValue.setForeground(new Color(45, 125, 45));
		} else {
			labelCopyPluginValue.setText(statusFail);
			labelCopyPluginValue.setForeground(Color.RED);
		}

		if (copyUserconfig && copyPlugin) {
			String message1 = "You can now run your game with @task_force_radio.";
			String message2 = "Don't forget to run TS3 with administrator priviledges.";
			labelMessage1.setText(message1);
			labelMessage2.setText(message2);
			labelMessage2
					.setFont(labelMessage2.getFont().deriveFont(Font.BOLD));
		}
	}

	@Override
	public void buttonFistPerformed() {
		this.firstPageTFARInstallerPanel.setVisible(true);
		this.dispose();
	}

	@Override
	public void buttonSecondPerformed() {
		try {
			configurationService.write();
		} catch (WritingException e) {
		} finally {
			dispose();
		}
	}

	private void buttonViewPluginPerformed() {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.OPEN)) {
				try {
					desktop.open(new File(ts3Directory + "/plugins"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void buttonViewUserconfigPerformed() {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.OPEN)) {
				try {
					desktop.open(new File(armA3Directory + "/userconfig"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
