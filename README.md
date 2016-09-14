# ESP8266WifiDisplay
Example code for an ESP8266 to display contents from a webpage to an I2C LCD.

ESP8266 Code builds with Arduino IDE with the ESP8266 core installed. 

Requires the ESP8266-I2C-LCD1602 library available at https://github.com/agnunez/ESP8266-I2C-LCD1602
to enable setting the I2C pins.

Default used here is SDA on D3 and SCL on D4 on the Wemos board. Other boards may vary, check the pinout.

GPIO pins are supposed to be 5v tolerant so no level shifting is required.

PHP example script is preformatted to match a 20x4 LCD screen. When using another size, adjust the ESP code and PHP script to match. 
More complex page parsing could be done, but there is likely little benefit to the increased complexity.
