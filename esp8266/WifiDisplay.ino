// Uses Library https://github.com/agnunez/ESP8266-I2C-LCD1602

#include <LiquidCrystal_I2C.h>
#include <Wire.h> 

#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>

#define REFRESH_DELAY 500
#define LCD_ROWS 4
#define LCD_COLS 20
#define URL "http:// YOUR URL / ESP8266LCD.php"

LiquidCrystal_I2C lcd(0x3F, LCD_COLS, LCD_ROWS); // Check I2C address of LCD, normally 0x27 or 0x3F

const char* ssid = "YOUR WIFI SSID";
const char* password = "YOUR WIFI PASSWORD";
ESP8266WiFiMulti WiFiMulti;

void display(String resp);

void setup()  {
  lcd.begin(0,2);      //Uses SDA D3 and SCL D4 see http://escapequotes.net/wp-content/uploads/2016/02/d1-mini-esp8266-board-sh_fixled.jpg
  lcd.backlight();
  
  lcd.home();
  lcd.print("ESP8266");   
  lcd.setCursor(0, 1);
  lcd.print("WiFi LCD Display");
  
  Serial.begin(115200);
  Serial.println("ESP9266 WiFi LCD Display");
  
  WiFiMulti.addAP(ssid, password);
  while (WiFiMulti.run() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  
  lcd.setCursor(0, 2);
  lcd.print("WiFi Connected");
  lcd.setCursor(0, 3);
  lcd.print(WiFi.localIP());
  
  delay(500);
  lcd.clear();
}

void loop()  {
  if (WiFiMulti.run() == WL_CONNECTED) {
    HTTPClient http;

    Serial.print("[HTTP] begin...\n");
    http.begin(URL);

    Serial.print("[HTTP] GET...\n");
    int httpCode = http.GET();

    if(httpCode > 0) {
      Serial.printf("[HTTP] GET... code: %d\n", httpCode);

      if(httpCode == HTTP_CODE_OK) {
        String payload = http.getString();
        Serial.println(payload);
        
        display(payload);
      }
    } else {
      Serial.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
  }

  delay(REFRESH_DELAY);
}

/**
 * Displays a preformatted page on the lcd
 * Expects the page to be length of (rows * cols)
 */
void display(String resp) {
  for (int i = 0; i < LCD_ROWS; i++) {
    lcd.setCursor(0, i);
    String part = resp.substring(i * LCD_COLS, i * LCD_COLS + LCD_COLS);
    lcd.print(part);
  }
}
