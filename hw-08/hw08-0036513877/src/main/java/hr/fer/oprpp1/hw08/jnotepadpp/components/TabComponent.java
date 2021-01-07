package hr.fer.oprpp1.hw08.jnotepadpp.components;

import java.awt.FlowLayout;
import java.nio.file.Path;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hr.fer.oprpp1.hw08.jnotepadpp.icons.SaveIcons;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class TabComponent extends JPanel {
	
	private static final String UNNAMED_DOCNAME = "(unnamed)";
	private SingleDocumentModel model;
	private JLabel saveIconLabel;
	private JLabel fileNameLabel;
	private CloseTabButton closeBtn;
	
	public TabComponent(SingleDocumentModel model) {
		this.model = model;
		saveIconLabel = new JLabel(SaveIcons.GREEN_SAVE_ICON);
		fileNameLabel = new JLabel(getFileNameFromModel(model));
		closeBtn = new CloseTabButton();
		initComponent();
		initListeners();
	}
	
	private void initComponent() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
		this.add(saveIconLabel);
		this.add(fileNameLabel);
		this.add(closeBtn);
		this.setOpaque(false);
	}
	
	private void initListeners() {
		SingleDocumentListener l = new SingleDocumentListener() {

			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				ImageIcon saveIcon = getIconFromModel(model);
				saveIconLabel.setIcon(saveIcon);
			}

			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				String filePathStr = getFileNameFromModel(model);
				fileNameLabel.setText(filePathStr);
			}
			
		};
		model.addSingleDocumentListener(l);
	}
	
	public void setCloseTabAction(Action ac) {
		closeBtn.setAction(ac);
	}
	
	private static String getFileNameFromModel(SingleDocumentModel model) {
		Path modelFilePath = model.getFilePath();
		return modelFilePath == null ? UNNAMED_DOCNAME : modelFilePath.getFileName().toString();
	}
	
	private static ImageIcon getIconFromModel(SingleDocumentModel model) {
		ImageIcon saveIcon = model.isModified() ? SaveIcons.RED_SAVE_ICON : SaveIcons.GREEN_SAVE_ICON;
		return saveIcon;
	}
}
