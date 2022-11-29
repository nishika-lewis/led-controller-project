import gc


def main():
    # Read settings.json
    from lib.at_client import io_util
    ssid, password, atSign = io_util.read_settings()
    del io_util

    # Connect to wifi.
    from lib import wifi
    print(f'Connecting to WiFi {ssid}...')
    wifi.init_wlan(ssid, password)
    del ssid, password, wifi

    # Connect and pkam authenticate into secondary.
    from lib.at_client import at_client
    atClient = at_client.AtClient(atSign, writeKeys=False)

    # Explicitly garbage collect to avoid memory errors.
    gc.collect()
    atClient.pkam_authenticate(verbose=True)
    del at_client
    gc.collect()

    # Set up LED display matrix.
    from led_matrix import display_text, init_matrix
    matrix = init_matrix()

    # Set up atsigns/public keys and read in their values.
    appKey = 'instructions'
    picoKey = 'led'
    appAtSign = '@computer0'
    picoAtSign = '@maximumcomputer'
    ledStatus = int(atClient.get_public(picoKey, picoAtSign))
    previousText = atClient.get_public(appKey, appAtSign)

    # Loop forever.
    while True:
        # Get display text from the app atsign's public key.
        text = atClient.get_public(appKey, appAtSign)

        # Whenever the text changes, we know the user has chosen
        # a new option. Display the text on the LED matrix.
        if text != previousText:
            display_text(matrix, text.strip())
            previousText = text

            # Update the LED status to let the app know it can continue.
            ledStatus ^= 1
            atClient.put_public(picoKey, str(ledStatus))


if __name__ == '__main__':
    main()
