package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.JFrame;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class CloseTabAction extends JNotepadPPAction {
	
	private SingleDocumentModel associatedSingleDocModel;
	
	public CloseTabAction(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel, 
			SingleDocumentModel associatedSingleDocModel) {
		super(frame, provider, multiDocModel);
		this.associatedSingleDocModel = Objects.requireNonNull(associatedSingleDocModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		multiDocModel.closeDocument(associatedSingleDocModel);
	}

	@Override
	protected void initAction() {
		
	}
	
}
