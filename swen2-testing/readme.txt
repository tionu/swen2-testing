Testen Sie die Klasse Briefschreibung. Diese greift auf die Klassen "AerzteService" und "AdressService" zu. Diese beiden Klassen sind etwas unsauber programmiert und müssen noch angepasst werden. Außerdem stehen die anzusprechenden Services aktuell noch nicht bereit.

Mocken Sie daher diese beiden Klassen mithilfe vom Mockito-Framework.

Überprüfen Sie mit den Tests,

- dass der E-Mail-Header richtige Daten liefert

- dass der Brief-Header richtige Daten liefert

- dass beim E-Mail-Header keine Verbindung zum Aerzteservice aufgebaut wird

- dass beim Brief-Header eine Verbindung zum Aerzteservice aufbaut und diese auch wieder schließt.

- dass die Aktionen connect - abrufen - disconnect in der richtigen Reihenfolge ausgeführt werden

- dass bei einem falschen Host für den AdressService der Verbindungsaufbau insgesamt 3x versucht wird, bevor der Verbindungsaufbau abgebrochen wird.

Ein Refactoring des Codes ist immer sinnvoll :-)