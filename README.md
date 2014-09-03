# Eigenschaften #
- sortiert - Objekte werden anhand der Comparable Implementation sortiert, die Sortierung kann nachträglich aktualisiert werden
- unique - Objekte werden anhand der equals/hashCode Implementation eindeutig gehalten
- begrenzt - Die Liste wird mit einer Begrenzung erstellt, diese kann Schrittweise um diesen Wert vergrößert oder verkleinert werden. Abfragen der Liste werden nur Objekte innerhalb dieser Begrenzung betrachtet. 
- synchronized - Thread-Safe zugriff
- kopierbar - über clone kann die collection kopiert werden