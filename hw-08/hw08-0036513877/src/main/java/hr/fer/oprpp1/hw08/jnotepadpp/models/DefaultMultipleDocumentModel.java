package hr.fer.oprpp1.hw08.jnotepadpp.models;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	
	private List<SingleDocumentModel> docs = new ArrayList<>();
	private List<MultipleDocumentListener> listeners = new LinkedList<>();
	private SingleDocumentModel currentDocument;
	
	public DefaultMultipleDocumentModel() {
		
		ChangeListener cl = changeEvent -> {
			int sel = this.getSelectedIndex();
			System.out.println("Selected: " + sel);
		};
		this.addChangeListener(cl);
		
	}
	
	private void notifyCurrentDocumentChanged(SingleDocumentModel prev, 
			SingleDocumentModel curr) {
		listeners.forEach(l -> l.currentDocumentChanged(prev, curr));
	}
	
	private void notifyDocumentAdded(SingleDocumentModel model) {
		listeners.forEach(l -> l.documentAdded(model));
	}
	
	private void notifyDocumentRemoved(SingleDocumentModel model) {
		listeners.forEach(l -> l.documentRemoved(model));
	}
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return docs.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel newDoc = new DefaultSingleDocumentModel();
		SingleDocumentModel oldDoc = currentDocument;
		docs.add(newDoc);
		currentDocument = newDoc;
		this.addTab("lol"+(this.getNumberOfDocuments()-1), newDoc.getTextComponent());
		this.setSelectedIndex(this.getNumberOfDocuments()-1);
		this.notifyDocumentAdded(newDoc);
		this.notifyCurrentDocumentChanged(oldDoc, newDoc);
		return newDoc;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		int idx = docs.indexOf(model);
		if (idx == -1)
			throw new IllegalArgumentException("Nepostojeći dokument");
		
		docs.remove(idx);
		this.removeTabAt(idx);
		this.notifyDocumentRemoved(model);
		if (model != currentDocument)
			return;
		
		int count = this.getNumberOfDocuments();
		SingleDocumentModel newModel = null;
		int newIdx = idx==count ? idx-1 : idx;
		if (count > 0) {
			newModel = this.getDocument(newIdx);
		}
		this.setSelectedIndex(newIdx);
		currentDocument = newModel;
		this.notifyCurrentDocumentChanged(model, newModel);
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(Objects.requireNonNull(l));
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(Objects.requireNonNull(l));
	}

	@Override
	public int getNumberOfDocuments() {
		return docs.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return docs.get(index);
	}
	
	
	
}
