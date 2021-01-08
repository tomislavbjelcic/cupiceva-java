package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.AbstractAction;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

public abstract class LocalizableAction extends AbstractAction {
	
	private ILocalizationProvider provider;
	private Map<String, String> actionKeysToTranslationKeysMap = new HashMap<>();
	
	public LocalizableAction(ILocalizationProvider provider) {
		this.provider = Objects.requireNonNull(provider);
		this.provider.addLocalizationListener(this::update);
	}
	
	private void update() {
		this.actionKeysToTranslationKeysMap.forEach((k, v) -> {
			String translation = provider.getString(v);
			this.putValue(k, translation);
		});
	}
	
	public void putLocalizedValue(String key, String translationKey) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(translationKey);
		String translation = provider.getString(translationKey);
		this.putValue(key, translation);
		this.actionKeysToTranslationKeysMap.put(key, translationKey);
	}
	
	
}
