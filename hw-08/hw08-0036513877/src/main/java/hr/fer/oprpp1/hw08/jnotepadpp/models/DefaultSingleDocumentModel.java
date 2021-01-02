package hr.fer.oprpp1.hw08.jnotepadpp.models;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	private Path filePath;
	private JTextArea textComponent = new JTextArea();
	private boolean modified = false;
	private List<SingleDocumentListener> listeners = new LinkedList<>();
	
	public DefaultSingleDocumentModel() {
		this(null, "");
	}
	
	public DefaultSingleDocumentModel(Path filePath, String content) {
		this.setFilePath(filePath);
		
		textComponent.setText(Objects.requireNonNull(content));
		textComponent.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {setModified(true);}

			@Override
			public void removeUpdate(DocumentEvent e) {setModified(true);}

			@Override
			public void changedUpdate(DocumentEvent e) {}
			
		});
		
	}
	
	private void notifyModified() {
		listeners.forEach(l -> l.documentModifyStatusUpdated(this));
	}
	
	private void notifyPathChange() {
		listeners.forEach(l -> l.documentFilePathUpdated(this));
	} 
	
	@Override
	public JTextArea getTextComponent() {
		return textComponent;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(Path path) {
		this.filePath = path == null ? path : 
					path.toAbsolutePath().normalize();
		notifyPathChange();
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		notifyModified();
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(Objects.requireNonNull(l));
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(Objects.requireNonNull(l));
	}
	
	
	
}
