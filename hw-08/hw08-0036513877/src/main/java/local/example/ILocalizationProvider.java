package local.example;

public interface ILocalizationProvider {
	
	void addLocalizationListener(ILocalizationListener listener);
	
	void removeLocalizationListener(ILocalizationListener listener);
	
	String getString(String key);
}
