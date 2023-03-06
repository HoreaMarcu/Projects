//includes the LCD library
#include <TimerOne.h>
#include <LiquidCrystal.h>

int b1;
int b2;
int b3;
int b4;

LiquidCrystal lcd(7, 6, 5, 4, 3, 2);

float resolutionADC = .0049;
float resolutionSensor = .01;

const byte HOURGLASS_TOP[8] = {
  0b11111,
  0b11111,
  0b01110,
  0b00100,
  0b01010,
  0b10001,
  0b11111,
  0b00000
};

const byte HOURGLASS_MIDDLE[8] = {
  0b11111,
  0b10001,
  0b01110,
  0b00100,
  0b01110,
  0b10001,
  0b11111,
  0b00000
};

const byte HOURGLASS_BOTTOM[8] = {
  0b11111,
  0b10001,
  0b01010,
  0b00100,
  0b01110,
  0b11111,
  0b11111,
  0b00000
};

volatile byte currentImage = 0;
volatile bool enableTimer = false;
volatile unsigned long currentTime;
volatile bool timerInterrupt = false; 
volatile int value = 0;


void setup() {
  // Sets the no. of rows and columns of the LCD
  lcd.createChar(0, HOURGLASS_TOP);
  lcd.createChar(1, HOURGLASS_MIDDLE);
  lcd.createChar(2, HOURGLASS_BOTTOM);
  

  pinMode(13, OUTPUT);
  pinMode(20, INPUT);
  pinMode(21, INPUT);
  digitalWrite(20, HIGH);
  digitalWrite(21, HIGH);
  pinMode(4, INPUT_PULLUP);
  pinMode(5, INPUT_PULLUP);
  pinMode(6, INPUT_PULLUP);
  pinMode(8, INPUT_PULLUP);
  lcd.begin(16, 2);
  attachInterrupt(digitalPinToInterrupt(20), butonUnu, RISING);
  attachInterrupt(digitalPinToInterrupt(21), butonDoi, CHANGE);

  Timer1.initialize(1000000);
}
void loop() {

  b1 = digitalRead(8);
  //b2 = digitalRead(13);
  // b3 = digitalRead(6);
  // b4 = digitalRead(7);
  if (enableTimer) {
    lcd.clear();
    displayValue();
    displayImage();
    if(currentTime > 0)currentTime--;
    else currentTime = 100;
    
    if (currentImage == 2) {
      currentImage = 0;
    } else {
      currentImage++;
    }
    delay(1000);
  }
  if(b1==0){
    timerInterrupt = !timerInterrupt;
    value = 0;
  }
  if(timerInterrupt == true){
    Timer1.attachInterrupt(timerFunction);
  }

          
}
void butonUnu(){
  timerInterrupt = false;
  lcd.clear();
  currentTime = 100;
  currentImage = 0;
  enableTimer = !enableTimer;
}
void displayValue() {
  lcd.setCursor(0,0);
  lcd.print(currentTime);
}
void displayImage() {
  lcd.setCursor(0,1);
  lcd.write(currentImage);
}

void butonDoi(){
  timerInterrupt = false;
  enableTimer = false;
  lcd.clear();
  lcd.print("Temp: ");
  float temp = readTempInCelsius(10,0);
  lcd.print(temp);
  delay(200);
}



float readTempInCelsius(int count, int pin) {
 // read temp. count times from the analog pin
 float sumTemp = 0;
 for (int i =0; i < count; i++) {
 int reading = analogRead(pin);
 float voltage = reading * resolutionADC;
 // subtract the DC offset and converts the value in
 //degrees (C)
 float tempCelsius = (voltage - 0.5) / resolutionSensor ;
 sumTemp = sumTemp + tempCelsius; // accumulates the
 //readings
 }
 return sumTemp / (float)count; // return the average value
}


void timerFunction(void){
  if(timerInterrupt == true){
    enableTimer = false;
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print("Timer func: ");
    lcd.print(value);
    value++;
    if(value % 2 == 1){
      digitalWrite(13, HIGH);
    }
    else{
      digitalWrite(13, LOW);
    }
  }

}
