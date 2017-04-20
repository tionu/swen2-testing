Testen Sie die Klasse Briefschreibung. Diese greift auf die Klassen "AerzteService" und "AdressService" zu. Diese beiden Klassen sind etwas unsauber programmiert und m�ssen noch angepasst werden. Au�erdem stehen die anzusprechenden Services aktuell noch nicht bereit.

Mocken Sie daher diese beiden Klassen mithilfe vom Mockito-Framework.

�berpr�fen Sie mit den Tests,

- dass der E-Mail-Header richtige Daten liefert

- dass der Brief-Header richtige Daten liefert

- dass beim E-Mail-Header keine Verbindung zum Aerzteservice aufgebaut wird

- dass beim Brief-Header eine Verbindung zum Aerzteservice aufbaut und diese auch wieder schlie�t.

- dass die Aktionen connect - abrufen - disconnect in der richtigen Reihenfolge ausgef�hrt werden

- dass bei einem falschen Host f�r den AdressService der Verbindungsaufbau insgesamt 3x versucht wird, bevor der Verbindungsaufbau abgebrochen wird.

Ein Refactoring des Codes ist immer sinnvoll :-)