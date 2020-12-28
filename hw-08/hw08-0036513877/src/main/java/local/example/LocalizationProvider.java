package local.example;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {
	
	private static LocalizationProvider instance = new LocalizationProvider();
	private String language;
	private ResourceBundle bundle;
	
	private static final String DEFAULT_LANG = "en";
	private static final String RES = "local.example.prijevodi";
	
	private LocalizationProvider() {
		setLang(DEFAULT_LANG);
	}
	
	public static LocalizationProvider getInstance() {
		return instance;
	}
	
	public void setLanguage(String language) {
		if (this.language.equals(language))
			return;
		setLang(language);
		fire();
	}
	
	private void setLang(String language) {
		this.language = Objects.requireNonNull(language);
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle(RES, locale);
	}
	
	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}
	
	
	
}
