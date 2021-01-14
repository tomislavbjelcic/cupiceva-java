package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.oprpp1.custom.collections.Dictionary;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;

/**
 * Implementacija konfigurabilnog Lindermayerovog sustava (L-sustava).
 * 
 * @author Tomislav Bjelčić
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {
	
	/**
	 * Skup (polje) svih podržanih direktiva.
	 */
	private static final String[] DIRECTIVES = {
			"origin",
			"angle",
			"unitLength",
			"unitLengthDegreeScaler",
			"command",
			"axiom",
			"production"
	};
	/**
	 * Skup (polje) svih podržanih akcija koje kornjača na crtaćoj površini može obaviti 
	 * prilikom prikazivanja fraktala kojeg je generirao ovaj L-sustav.
	 */
	private static final String[] ACTIONS = {
			"draw",
			"skip",
			"scale",
			"rotate",
			"push",
			"pop",
			"color"
	};
	
	
	/**
	 * Registrirane produkcije u obliku preslikavanja znak -> razvoj.
	 */
	private Dictionary<Character, String> productions = new Dictionary<>();
	/**
	 * Registrirane akcije koje se dohvaćaju sa pripadnim znakom.
	 */
	private Dictionary<Character, Command> actions = new Dictionary<>();
	
	/**
	 * Duljina jediničnog pomaka kornjače koji će se obaviti ako se obave akcije 
	 * skip ili draw.
	 */
	private double unitLength = 0.1;
	/**
	 * Faktor skaliranja duljine jediničnog pomaka. Koristi se da bi se smanjila duljina 
	 * jediničnog pomaka za veće dubine prikaza fraktala generiranih ovim L-sustavom.
	 */
	private double unitLengthDegreeScaler = 1.0;
	/**
	 * Ishodišna točka iz koje kornjača kreće crtanje fraktala generiranih ovim L-sustavom.
	 */
	private Vector2D origin = new Vector2D(0, 0);
	/**
	 * Početni kut smjera kornjače u stupnjevima. 
	 */
	private double angle = 0;
	/**
	 * Početni niz iz kojeg kreće razvoj sustava.
	 */
	private String axiom = "";
	
	/**
	 * Implementacija Lindermayerovog sustava kojeg je prethodno konfigurirao 
	 * {@code LSystemBuilder} i izgradio pozivom metode {@code build()}.
	 * 
	 * @author Tomislav Bjelčić
	 *
	 */
	private static class LSystemImpl implements LSystem {
		
		/**
		 * Riječnik (preslikavanje) svih produkcija.
		 */
		Dictionary<Character, String> productions;
		/**
		 * Riječnik (preslikavanje) svih akcija. 
		 */
		Dictionary<Character, Command> actions;
		
		/**
		 * Duljina jediničnog pomaka kornjače.
		 */
		double unitLength;
		/**
		 * Faktor skaliranja duljine jediničnog pomaka kornjače.
		 */
		double unitLengthDegreeScaler;
		/**
		 * Ishodišna točka kornjače.
		 */
		Vector2D origin;
		/**
		 * Početni kut smjera kornjače.
		 */
		double angle;
		/**
		 * Početni niz iz kojeg kreće razvoj sustava.
		 */
		String axiom;
		
		/**
		 * Stvara novi L-sustav iz konfigurabilnog L-sustava {@code builder}.
		 * 
		 * @param builder konfigurabilni L-sustav iz kojeg se stvara ovaj L-sustav.
		 */
		LSystemImpl(LSystemBuilderImpl builder) {
			unitLength = builder.unitLength;
			unitLengthDegreeScaler = builder.unitLengthDegreeScaler;
			origin = builder.origin.copy();
			angle = builder.angle;
			axiom = builder.axiom;
			
			productions = builder.productions;
			actions = builder.actions;
		}
		
		/**
		 * Obavlja crtanje fraktala dubine {@code level} generiranog ovim 
		 * L-sustavom na crtaćoj površini {@code painter}.
		 * 
		 * @param level dubina fraktala.
		 * @param painter crtaća površina.
		 * 
		 * @throws NullPointerException ako je {@code painter} {@code null}.
		 * @throws IllegalArgumentException ako je dubina {@code level} negativna.
		 */
		@Override
		public void draw(int level, Painter painter) {
			Objects.requireNonNull(painter, "Predana crtaća površina je null.");
			String generated = generate(level);
			
			double rad = Math.toRadians(angle);
			Vector2D angVec = new Vector2D(
						Math.cos(rad),
						Math.sin(rad)
					);
			Color color = Color.BLACK;
			double offsetLength = unitLength * Math.pow(unitLengthDegreeScaler, level);
			
			TurtleState state = new TurtleState(origin.copy(), angVec, color, offsetLength);
			Context ctx = new Context();
			ctx.pushState(state);
			
			for (int i=0, len=generated.length(); i<len; i++) {
				char ch = generated.charAt(i);
				Command cmd = actions.get(ch);
				
				if (cmd == null)
					continue;
				
				cmd.execute(ctx, painter);
			}
		}

		/**
		 * Generira String zapis fraktala dubine {@code level} koristeći 
		 * produkcije ovog L-sustava.
		 * 
		 * @param level dubina String zapisa fraktala.
		 * 
		 * @return String zapis fraktala dubine {@code level}.
		 * 
		 * @throws IllegalArgumentException ako je dubina {@code level} negativna.
		 */
		@Override
		public String generate(int level) {
			if (level < 0)
				throw new IllegalArgumentException("Predana dubina je negativna.");
			
			StringBuilder prev = new StringBuilder(axiom);
			StringBuilder generated = new StringBuilder();
			
			for (int i=0; i<level; i++) {
				int len = prev.length();
				for (int j=0; j<len; j++) {
					char ch = prev.charAt(j);
					String replacement = productions.get(ch);
					
					if (replacement == null) {
						generated.append(ch);
					} else
						generated.append(replacement);
				}
				
				StringBuilder prevTmp = prev;
				prev = generated;
				generated = prevTmp.delete(0, len);
			}
			
			
			return prev.toString();
		}
		
	}
	
	/**
	 * Izgrađuje novi L-sustav koji je konfiguriran sa ovim L-sustavom.
	 * 
	 * @return novi konfigurirani L-sustav.
	 */
	@Override
	public LSystem build() {
		return new LSystemImpl(this);
	}

	/**
	 * Podešava (konfigurira) ovaj L-sustav iz predanog polja linija teksta 
	 * {@code lines}.
	 * 
	 * @param lines polje linija teksta u određenom formatu s kojima se podešava ovaj 
	 * L-sustav.
	 * 
	 * @return ovaj L-sustav sa ažuriranim postavkama.
	 * 
	 * @throws NullPointerException ako je predano polje {@code null}, ili to polje sadrži barem 
	 * jednu {@code null} referencu.
	 * @throws LSystemBuilderException ako je bar jedna linija u neispravnom formatu.
	 */
	@Override
	public LSystemBuilder configureFromText(String[] lines) {
		Objects.requireNonNull(lines, "Predano polje linija teksta je null.");
		
		for (int i=0, len=lines.length; i<len; i++) {
			String line = lines[i];
			Objects.requireNonNull(line, "Linija " + i + " je null.");
			parseLine(line);
		}
		
		return this;
	}

	/**
	 * Registrira novu akciju kornjače kao String zapis {@code command} povezana sa znakom 
	 * {@code sym}.
	 * 
	 * @param sym znak (simbol) koji se preslikava u akciju.
	 * @param command String zapis akcije koja se registrira.
	 * 
	 * @return ovaj L-sustav sa ažuriranim postavkama.
	 * 
	 * @throws NullPointerException ako je predani zapis akcije {@code null}.
	 * @throws LSystemBuilderException ako je zapis akcije {@code command} u neispravnom formatu.
	 */
	@Override
	public LSystemBuilder registerCommand(char sym, String command) {
		Objects.requireNonNull(command, "Predana komanda je null.");
		
		String stripped = command.strip();
		if (stripped.isEmpty())
			throw new LSystemBuilderException("Prazna komanda za simbol " + sym);
		
		int strippedLen = stripped.length();
		int whiteSpaceIndex = 0;
		for ( ; whiteSpaceIndex<strippedLen; whiteSpaceIndex++) {
			char ch = stripped.charAt(whiteSpaceIndex);
			if (Character.isWhitespace(ch))
				break;
		}
		
		String action = stripped.substring(0, whiteSpaceIndex);
		if (!isAction(action))
			throw new LSystemBuilderException("Nepoznata akcija: " + action);
		
		String rest = stripped.substring(whiteSpaceIndex).strip();
		
		if (rest.isEmpty()) {
			if (action.equals("push")) {
				Command cmd = new PushCommand();
				actions.put(sym, cmd);
				return this;
			}
			if (action.equals("pop")) {
				Command cmd = new PopCommand();
				actions.put(sym, cmd);
				return this;
			}
			throw new LSystemBuilderException("Akcija " + action
					 + " nema argumenata.");
		}
		
		if (action.equals("push") || action.equals("pop"))
			throw new LSystemBuilderException("Akcija " + action
					 + " ne smije imati argumente.");
		
		
		if (action.equals("scale")
			|| action.equals("skip")
			|| action.equals("draw")
			|| action.equals("rotate")) {
			
			double arg = 0;
			try {
				arg = Double.parseDouble(rest);
			} catch (NumberFormatException ex) {
				throw new LSystemBuilderException("Akcija " + action + ": argument " + rest + 
						" mora biti realan broj.");
			}
			
			Command cmd = switch(action) {
				case "scale" -> new ScaleCommand(arg);
				case "skip" -> new SkipCommand(arg);
				case "draw" -> new DrawCommand(arg);
				case "rotate" -> new RotateCommand(arg);
				default -> null;
			};
			actions.put(sym, cmd);
			return this;
		}
		
		if (action.equals("color")) {
			String regex = "(\\p{XDigit}){6}";
			if (!rest.matches(regex))
				throw new LSystemBuilderException("Akcija color: argument " + rest + 
						" nije u ispravnom formatu boje rrggbb.");
			
			String redStr = rest.substring(0,2);
			String greenStr = rest.substring(2,4);
			String blueStr = rest.substring(4,6);
			
			int base = 16;
			int r = Integer.parseInt(redStr, base);
			int g = Integer.parseInt(greenStr, base);
			int b = Integer.parseInt(blueStr, base);
			
			Color color = new Color(r, g, b);
			Command cmd = new ColorCommand(color);
			actions.put(sym, cmd);
			return this;
		}
		
		// do ove linije neće doći
		throw new LSystemBuilderException(
				"Metoda registerCommand ("
				+ sym + ", " + command + ")");
		//return this;
	}

	/**
	 * Registrira novu produkciju oblika {@code sym -> production} ovog L-sustava.<br>
	 * Produkcija razvija (zamjenjuje) predani znak {@code sym} u niz {@code production}.
	 * 
	 * @param sym znak (simbol) koji se razvija.
	 * @param production znakovni niz koji će zamijeniti znak {@code sym} prilikom 
	 * primjene produkcije.
	 * 
	 * @return ovaj L-sustav sa ažuriranim postavkama.
	 * 
	 * @throws NullPointerException ako je predani zamjenski niz {@code null}.
	 * @throws LSystemBuilderException ako su u zamjenskom nizu sve bjeline.
	 */
	@Override
	public LSystemBuilder registerProduction(char sym, String production) {
		Objects.requireNonNull(production, "Zamjenski niz simbola je null.");
		
		String stripped = production.strip();
		if (stripped.isEmpty())
			throw new LSystemBuilderException("Prazan zamjenski niz za simbol " + sym);
		
		productions.put(sym, production);
		return this;
	}
	

	/**
	 * Postavlja kut smjera kornjače iz kojeg će kornjača krenuti iscrtavati 
	 * fraktal generiran ovim L-sustavom.
	 * 
	 * @param angle početni kut smjera kornjače.
	 * 
	 * @return ovaj L-sustav sa ažuriranim postavkama.
	 */
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;
		return this;
	}

	/**
	 * Postavlja početni niz iz kojeg kreće razvoj ovog L-sustava.
	 * 
	 * @param axiom početni niz.
	 * 
	 * @return ovaj L-sustav sa ažuriranim postavkama.
	 * 
	 * @throws NullPointerException ako je predani početni niz {@code null}.
	 */
	@Override
	public LSystemBuilder setAxiom(String axiom) {
		this.axiom = Objects.requireNonNull(axiom, 
				"Predani početni niz je null.");
		return this;
	}

	/**
	 * Postavlja ishodišnu točku iz koje će kornjača krenuti iscrtavati fraktal 
	 * generiran ovim L-sustavom.
	 * 
	 * @param x x-koordinata ishodišne točke.
	 * @param y y-koordinata ishodišne točke.
	 * 
	 * @return ovaj L-sustav sa ažuriranim postavkama.
	 */
	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		origin = new Vector2D(x, y);
		return this;
	}

	/**
	 * Postavlja duljinu jediničnog pomaka kornjače koji će se obaviti ako se obave akcije 
	 * skip ili draw.
	 * 
	 * @param unitLength duljina jediničnog pomaka kornjače.
	 * 
	 * @return ovaj L-sustav sa ažuriranim postavkama.
	 */
	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;
		return this;
	}

	/**
	 * Postavlja faktor skaliranja duljine jediničnog pomaka.<br>
	 * Koristi se da bi se smanjila duljina 
	 * jediničnog pomaka za veće dubine prikaza fraktala generiranih ovim L-sustavom.<br>
	 * Računanje duljine jediničnog pomaka za dubinu {@code d} se obavlja po formuli: 
	 * {@code unitLength = unitLength * unitLengthDegreeScaler ^ d}.
	 * 
	 * @param faktor skaliranja duljine jediničnog pomaka.
	 * 
	 * @return ovaj L-sustav sa ažuriranim postavkama.
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;
		return this;
	}
	
	/**
	 * Pomoćna metoda koja provjerava je li predani String direktive {@code directive} podržan.
	 * 
	 * @param directive
	 * @return {@code true} ako je direktiva podržana, inače {@code false}.
	 */
	private static boolean isDirective(String directive) {
		for (String d : DIRECTIVES) {
			if (directive.equals(d))
				return true;
		}
		return false;
	}
	
	/**
	 * Pomoćna metoda koja provjerava je li predani String akcije {@code action} 
	 * podržan.
	 * 
	 * @param action
	 * @return {@code true} ako je akcija podržana, inače {@code false}.
	 */
	private static boolean isAction(String action) {
		for (String a : ACTIONS) {
			if (action.equals(a))
				return true;
		}
		return false;
	}
	
	/**
	 * Pomoćna metoda koja se poziva iz metode {@code configureFromText} za svaku 
	 * liniju konfiguracijskog teksta {@code line}.
	 * 
	 * @param line linija konfiguracijskog teskta.
	 * 
	 * @throws LSystemBuilderException ako je linija u neispravnom formatu.
	 */
	private void parseLine(String line) {
		String stripped = line.strip();
		if (stripped.isEmpty())
			return;
		
		int strippedLen = stripped.length();
		int whiteSpaceIndex = 0;
		for ( ; whiteSpaceIndex<strippedLen; whiteSpaceIndex++) {
			char ch = stripped.charAt(whiteSpaceIndex);
			if (Character.isWhitespace(ch))
				break;
		}
		String directive = stripped.substring(0, whiteSpaceIndex);
		if (!isDirective(directive))
			throw new LSystemBuilderException("Nepoznata direktiva: " + directive);
		
		String rest = stripped.substring(whiteSpaceIndex).strip();
		if (rest.isEmpty())
			throw new LSystemBuilderException("Direktiva " + directive + " bez argumenata.");
		
		String regex = "\\s+";
		
		if (directive.equals("origin")) {
			String[] args = rest.split(regex);
			if (args.length != 2)
				throw new LSystemBuilderException("Direktiva origin nema točno 2 argumenta.");
			
			String first = args[0];
			String second = args[1];
			double x, y;
			
			String current = null;
			try {
				current = first;
				x = Double.parseDouble(current);
				current = second;
				y = Double.parseDouble(current);
			} catch (NumberFormatException ex) {
				throw new LSystemBuilderException("Direktiva origin: argument " + current + 
						" mora biti realan broj.");
			}
			setOrigin(x, y);
			return;
		}
		
		if (directive.equals("angle")) {
			double angDeg;
			try {
				angDeg = Double.parseDouble(rest);
			} catch (NumberFormatException ex) {
				throw new LSystemBuilderException("Direktiva angle: argument " + rest + 
						" mora biti realan broj.");
			}
			
			setAngle(angDeg);
			return;
		}
		
		if (directive.equals("unitLength")) {
			double unitLen;
			try {
				unitLen = Double.parseDouble(rest);
			} catch (NumberFormatException ex) {
				throw new LSystemBuilderException("Direktiva unitLength: argument " + rest + 
						" mora biti realan broj.");
			}
			
			setUnitLength(unitLen);
			return;
		}
		
		if (directive.equals("unitLengthDegreeScaler")) {
			StringBuilder sbRest = new StringBuilder(rest);
			for (int i=0; i<sbRest.length(); i++) {
				int ch = sbRest.charAt(i);
				if (ch == '/') {
					sbRest.replace(i, i+1, " / ");
					i++;
				}
			}
			
			rest = sbRest.toString();
			String[] splitted = rest.split(regex);
			int splittedLen = splitted.length;
			if (splittedLen != 1 && splittedLen != 3)
				throw new LSystemBuilderException(
						"Direktiva unitLengthDegreeScaler: neispravni argumenti "
						 + rest);
			
			double scaler = 0;
			if (splittedLen == 1) {
				String scalerStr = splitted[0];
				try {
					scaler = Double.parseDouble(scalerStr);
				} catch (NumberFormatException ex) {
					throw new LSystemBuilderException(
							"Direktiva unitLengthDegreeScaler: argument " + scalerStr + 
							" mora biti realan broj.");
				}
			}
			if (splittedLen == 3) {
				String leftStr = splitted[0];
				String op = splitted[1];
				String rightStr = splitted[2];
				if (!op.equals("/"))
					throw new LSystemBuilderException(
							"Direktiva unitLengthDegreeScaler: "
							+ op + " nije prepoznat.");
				double left, right;
				
				String current = null;
				try {
					current = leftStr;
					left = Double.parseDouble(current);
					current = rightStr;
					right = Double.parseDouble(current);
				} catch (NumberFormatException ex) {
					throw new LSystemBuilderException(
							"Direktiva unitLengthDegreeScaler: argument " + current + 
							" mora biti realan broj.");
				}
				scaler = left / right;
			}
			
			setUnitLengthDegreeScaler(scaler);
			return;
		}
		
		if (directive.equals("command")) {
			char sym = rest.charAt(0);
			
			if (rest.length() < 2)
				throw new LSystemBuilderException(
						"Direktiva command: iza simbola " + sym
						 + " nema akcije.");
			
			char shouldBeWhitespace = rest.charAt(1);
			if (!Character.isWhitespace(shouldBeWhitespace))
				throw new LSystemBuilderException(
						"Direktiva command: iza simbola " + sym
						 + " mora biti praznina.");
			
			String action = rest.substring(1).strip();
			registerCommand(sym, action);
			return;
		}
		
		if (directive.equals("axiom")) {
			setAxiom(rest);
			return;
		}
		
		if (directive.equals("production")) {
			char sym = rest.charAt(0);

			if (rest.length() < 2)
				throw new LSystemBuilderException(
						"Direktiva production: iza simbola " + sym
						+ " nema zamjenskog niza simbola.");

			char shouldBeWhitespace = rest.charAt(1);
			if (!Character.isWhitespace(shouldBeWhitespace))
				throw new LSystemBuilderException(
						"Direktiva production: iza simbola " + sym
						+ " mora biti praznina.");

			String prod = rest.substring(1).strip();
			registerProduction(sym, prod);
			return;
		}
		
		// do ove linije neće doći
		throw new LSystemBuilderException("Metoda parseLine (" + line + ")");
	}

}
