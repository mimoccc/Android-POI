Pretty basic structure for a point of interests app.
The web server handle the DB and provides lists of nearest POIs
given the location of the user through the Android app.

Requirements:

Running:
  $ mvn install
  $ cd server
  $ mvn jetty:run

Eclipse setup:
  $ mvn eclipse:eclipse

Rapid development setup:
  Use maven-jetty-plugin.
  $ mvn jetty:run

