package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class Clipboard {
	
	@FunctionalInterface
	private interface DocumentAction {
		void perform(int off, int len, Document doc);
	}
	
	private String clipboard = null;
	private JFrame frame;
	private ILocalizationProvider provider;
	private MultipleDocumentModel multiDocModel;
	
	private Action cutAction;
	private Action copyAction;
	private Action pasteAction;
	
	private abstract class CopyCutPasteAction extends JNotepadPPAction {
		
		DocumentAction da;
		
		public CopyCutPasteAction(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel, 
				DocumentAction da) {
			super(frame, provider, multiDocModel);
			this.da = da;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Caret caret = caret();
			Document document = document();
			int dot = caret.getDot();
			int mark = caret.getMark();
			int len = Math.abs(dot - mark);
			int off = Math.min(dot, mark);
			da.perform(off, len, document);
		}
	}
	
	private CaretListener cl = e -> {
		Caret caret = caret();
		int dot = caret.getDot();
		int mark = caret.getMark();
		int len = Math.abs(dot - mark);
		boolean enable = len > 0;
		copyAction.setEnabled(enable);
		cutAction.setEnabled(enable);
	};
	
	private DocumentAction x = (off, len, doc) -> {
		try {
			clipboard = doc.getText(off, len);
			doc.remove(off, len);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		pasteAction.setEnabled(true);
	};
	private DocumentAction c = (off, len, doc) -> {
		try {
			clipboard = doc.getText(off, len);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		pasteAction.setEnabled(true);
	};
	private DocumentAction v = (off, len, doc) -> {
		try {
			doc.remove(off, len);
			doc.insertString(off, clipboard, null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	};
	

	public Clipboard(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel) {
		this.frame = Objects.requireNonNull(frame);
		this.provider = Objects.requireNonNull(provider);
		this.multiDocModel = Objects.requireNonNull(multiDocModel);
		initActions();
	}
	
	private void updateActions(SingleDocumentModel currentModel) {
		if (currentModel == null) {
			cutAction.setEnabled(false);
			copyAction.setEnabled(false);
			pasteAction.setEnabled(false);
			return;
		}
		
		pasteAction.setEnabled(clipboard != null);
		cl.caretUpdate(null);
	}
	
	private Caret caret() {
		return textComponent().getCaret();
	}
	
	private JTextComponent textComponent() {
		return multiDocModel.getCurrentDocument().getTextComponent();
	}
	
	private Document document() {
		return textComponent().getDocument();
	}

	private void initActions() {
		cutAction = new CopyCutPasteAction(frame, provider, multiDocModel, x) {
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_cut");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_cut");
				this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
				this.setEnabled(false);
			}
		};
		
		copyAction = new CopyCutPasteAction(frame, provider, multiDocModel, c) {
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_copy");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_copy");
				this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
				this.setEnabled(false);
			}
		};
		
		pasteAction = new CopyCutPasteAction(frame, provider, multiDocModel, v) {
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_paste");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_paste");
				this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
				this.setEnabled(false);
			}
		};
		
		multiDocModel.addMultipleDocumentListener(new MultipleDocumentAdapter() {
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				updateActions(currentModel);
				if (previousModel != null)
					previousModel.getTextComponent().removeCaretListener(cl);
				if (currentModel != null)
					currentModel.getTextComponent().addCaretListener(cl);
			}
		});
	}

	public Action getCutAction() {
		return cutAction;
	}

	public Action getCopyAction() {
		return copyAction;
	}

	public Action getPasteAction() {
		return pasteAction;
	}
	
	

	

}
