package exceptions;

// kann verwendet werden, wenn nur Filme erwuenscht sind, bei denen ALLE Daten vorhanden sind
// aktuell werden von den Readern bei fehlenden Daten leere String-Werte ("") in den jeweiligen Attributen gesetzt
// Beispiel: Information.prodCompany = ""
// in der movie.dtd Datei laesst sich sehen, welche Daten moeglicherweise fehlen (mit #IMPLIED gekennzeichnet)

public class MissingValueException extends Exception {
	private static final long serialVersionUID = 1L;

	public MissingValueException(String error) {
        super(error);
    }
}
