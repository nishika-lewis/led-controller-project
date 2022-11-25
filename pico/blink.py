def main():
    from machine import Pin
    from time import sleep

    led = Pin("LED", Pin.OUT) # Denotes the on-board LED.

    # Blink the LED five times.
    for i in range(10):
        led.toggle()
        sleep(0.5)


if __name__ == '__main__':
    main()
