package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Objects;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	
	private ILocalizationProvider provider;
	private ILocalizationListener singleListener = () -> {
		lastKnownLanguage = provider.getCurrentLanguage();
		fire();
	};
	private String lastKnownLanguage;
	private boolean connected;
	
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		this.provider = Objects.requireNonNull(provider);
	}

	public void connect() {
		if (connected)
			return;
		connected = true;
		provider.addLocalizationListener(singleListener);
		String providerLang = provider.getCurrentLanguage();
		if (!Objects.equals(providerLang, lastKnownLanguage))
			fire();
	}
	
	public void disconnect() {
		if (!connected)
			return;
		connected = false;
		provider.removeLocalizationListener(singleListener);
	}

	@Override
	public String getString(String key) {
		return provider.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return lastKnownLanguage;
	}
	
	
	
}
