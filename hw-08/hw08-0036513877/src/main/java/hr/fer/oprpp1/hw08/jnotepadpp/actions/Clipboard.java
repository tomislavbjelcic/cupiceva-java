package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.Objects;
import java.util.function.UnaryOperator;

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
		void perform(int off, int len, Document doc, UnaryOperator<String> caseChanger);
	}
	
	private String clipboard = null;
	private JFrame frame;
	private ILocalizationProvider provider;
	private MultipleDocumentModel multiDocModel;
	
	private Action cutAction;
	private Action copyAction;
	private Action pasteAction;
	private Action changeCaseAction;
	private Action toLowerCaseAction;
	private Action toUpperCaseAction;
	private Action invertCaseAction;
	
	private Action[] highlightDependentActions;
	
	private abstract class ChangeDocumentAction extends JNotepadPPAction {
		
		DocumentAction da;
		UnaryOperator<String> caseChanger;
		
		public ChangeDocumentAction(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel, 
				DocumentAction da, UnaryOperator<String> caseChanger) {
			super(frame, provider, multiDocModel);
			this.da = da;
			this.caseChanger = caseChanger;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Caret caret = caret();
			Document document = document();
			int dot = caret.getDot();
			int mark = caret.getMark();
			int len = Math.abs(dot - mark);
			int off = Math.min(dot, mark);
			da.perform(off, len, document, caseChanger);
		}
	}
	
	private UnaryOperator<String> transformToLowercase
		= s -> s.toLowerCase(new Locale(provider.getCurrentLanguage()));
	private UnaryOperator<String> transformToUppercase
		= s -> s.toUpperCase(new Locale(provider.getCurrentLanguage()));
	private UnaryOperator<String> transformToInvertCase = s -> {
		char[] chars = s.toCharArray();
		int clen = chars.length;
		StringBuilder sb = new StringBuilder(clen);
		Locale l = new Locale(provider.getCurrentLanguage());
		for (int i=0; i<clen; i++) {
			char ch = chars[i];
			String chstr = String.valueOf(ch);
			String chstrinv = null;
			if (Character.isLowerCase(ch))
				chstrinv = chstr.toUpperCase(l);
			else if (Character.isUpperCase(ch))
				chstrinv = chstr.toLowerCase(l);
			else
				chstrinv = chstr;
			chars[i] = chstrinv.charAt(0);
		}
		return new String(chars);
	};
		
	private CaretListener cl = e -> {
		Caret caret = caret();
		int dot = caret.getDot();
		int mark = caret.getMark();
		int len = Math.abs(dot - mark);
		boolean enable = len > 0;
		toggleHighlightDependentAction(enable);
	};
	
	private DocumentAction cut = (off, len, doc, caseChanger) -> {
		try {
			clipboard = doc.getText(off, len);
			doc.remove(off, len);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		pasteAction.setEnabled(true);
	};
	private DocumentAction copy = (off, len, doc, caseChanger) -> {
		try {
			clipboard = doc.getText(off, len);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		pasteAction.setEnabled(true);
	};
	private DocumentAction paste = (off, len, doc, caseChanger) -> {
		try {
			doc.remove(off, len);
			doc.insertString(off, clipboard, null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	};
	private DocumentAction changeCase = (off, len, doc, caseChanger) -> {
		try {
			String highlighted = doc.getText(off, len);
			String lang = provider.getCurrentLanguage();
			Locale l = new Locale(lang);
			String caseChangedStr = caseChanger.apply(highlighted);
			doc.remove(off, len);
			doc.insertString(off, caseChangedStr, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			toggleHighlightDependentAction(false);
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
	
	private void toggleHighlightDependentAction(boolean enable) {
		for (Action ac : highlightDependentActions)
			ac.setEnabled(enable);
	}

	private void initActions() {
		cutAction = new ChangeDocumentAction(frame, provider, multiDocModel, cut, null) {
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_cut");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_cut");
				this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
				this.setEnabled(false);
			}
		};
		
		copyAction = new ChangeDocumentAction(frame, provider, multiDocModel, copy, null) {
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_copy");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_copy");
				this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
				this.setEnabled(false);
			}
		};
		
		pasteAction = new ChangeDocumentAction(frame, provider, multiDocModel, paste, null) {
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_paste");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_paste");
				this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
				this.setEnabled(false);
			}
		};
		
		changeCaseAction = new ChangeDocumentAction(frame, provider, multiDocModel, null, null) {
			
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "change_case");
				this.setEnabled(false);
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {}
		};
		
		toLowerCaseAction = new ChangeDocumentAction(frame, provider, multiDocModel, changeCase, transformToLowercase) {
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_tolowercase");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_tolowercase");
				this.setEnabled(false);
			}
		};
		
		toUpperCaseAction = new ChangeDocumentAction(frame, provider, multiDocModel, changeCase, transformToUppercase) {
			
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_touppercase");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_touppercase");
				this.setEnabled(false);
			}
		};
		
		invertCaseAction = new ChangeDocumentAction(frame, provider, multiDocModel, changeCase, transformToInvertCase) {
			
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_invertcase");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_invertcase");
				this.setEnabled(false);
			}
		};
		
		highlightDependentActions = new Action[]
				{cutAction, copyAction, changeCaseAction, 
						toLowerCaseAction, toUpperCaseAction, invertCaseAction};
		
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
	
	public Action getToLowerCaseAction() {
		return toLowerCaseAction;
	}

	public Action getChangeCaseAction() {
		return changeCaseAction;
	}
	
	public Action getToUpperCaseAction() {
		return toUpperCaseAction;
	}
	
	public Action getInvertCaseAction() {
		return invertCaseAction;
	}

}
