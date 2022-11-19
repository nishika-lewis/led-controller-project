from machine import Pin
from time import sleep


def main():
    led = Pin("LED", Pin.OUT)

    for i in range(10):
        led.toggle()
        sleep(0.5)


if __name__ == '__main__':
    main()
