# ESP8266WifiDisplay
Example code for an ESP8266 to display contents from a webpage to an I2C LCD.

ESP8266 Code builds with Arduino IDE with the ESP8266 core installed. 

Requires the ESP8266-I2C-LCD1602 library available at https://github.com/agnunez/ESP8266-I2C-LCD1602
to enable setting the I2C pins.

Default used here is SDA on D3 and SCL on D4 on the Wemos board. Other boards may vary, check the pinout.

GPIO pins are supposed to be 5v tolerant so no level shifting is required.

## PHP Sample Script
The PHP example script ESP8266LCD.php is preformatted to match a 20x4 LCD screen. When using another size, adjust the ESP code and PHP script to match. 
More complex page parsing could be done, but this is easy.

## spark-wifi-display
This is a java webservice that allows a user to change the message using a web browser. 

It uses [Spark](http://sparkjava.com/) a Java microservice framework and a [Thymeleaf](http://www.thymeleaf.org/) template to display the message update page. 

* To build `./gradlew build fatJar` 
* Run with `java -jar spark-wifi-display-all-1.0.0.jar`
* Exit using CTRL+C or through task manager

Sample Message:

```
2017-08-01 21:53:15
Line #1
some words
more messages!!
```

Default URLs:
* [Edit Page](http://localhost:4567/) http://localhost:4567/
* [Current Message](http://localhost:4567/message) http://localhost:4567/message

# Screenshots

Spark Wifi Display Edit Page

![Spark Wifi Display Edit Page Screenshot](spark-wifi-display-edit-page.png?raw=true "Spark Wifi Display Edit Page")

Wifi Display

![Wifi Display](wifi-display.jpg?raw=true "Wifi Display")
