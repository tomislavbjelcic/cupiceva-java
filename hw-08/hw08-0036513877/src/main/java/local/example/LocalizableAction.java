package local.example;

import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

public abstract class LocalizableAction extends AbstractAction {
	
	protected ILocalizationProvider provider;
	protected String key;
	
	public LocalizableAction(ILocalizationProvider provider, String key) {
		this.provider = Objects.requireNonNull(provider);
		this.key = Objects.requireNonNull(key);
		
		String translation = provider.getString(key);
		this.putValue(Action.NAME, translation);
		provider.addLocalizationListener(() -> this.putValue(Action.NAME, provider.getString(key)));
	}
	
	
	
}
