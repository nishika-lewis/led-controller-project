from lib.max7219 import Matrix8x8
from machine import Pin, SPI
from time import sleep


# Initialize the SPI hardware and LED display matrix.
def init_matrix():
    spi = SPI(0, baudrate=10000000, polarity=1,
              phase=0, sck=Pin(2), mosi=Pin(3))
    ss = Pin(5, Pin.OUT)

    # Create a 32x8 matrix display instance with the lowest brightness.
    display = Matrix8x8(spi, ss, 4)
    display.brightness(0)  # Value can range from [0, 15].

    # Clear the display.
    display.fill(0)
    display.show()
    sleep(0.5)

    return display


# Display the scrolling text once, starting from the rightmost matrix.
def display_text(display: Matrix8x8, text: str):
    columns = len(text) * 8  # Number of LED columns needed.

    for i in range(32, -columns, -1):
        # Write new text to the buffer and display it on the LEDs.
        display.text(text, i, 0, 1)
        display.show()
        sleep(0.1)  # Set the scrolling speed here.

        # Clear the previous text.
        display.fill(0)

    display.show()  # Clear leftover text from longer characters.
