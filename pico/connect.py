import gc


def main():
    # Read user settings.
    ssid, password, picoAtSign, appAtSign = read_settings()

    # Connect to Wi-Fi.
    from lib import wifi
    print(f'Connecting to WiFi {ssid}...')
    wifi.init_wlan(ssid, password)
    del ssid, password, wifi

    # Connect and pkam authenticate into secondary.
    from lib.at_client import at_client
    atClient = at_client.AtClient(picoAtSign, writeKeys=True)

    # Explicitly garbage collect to avoid memory errors.
    gc.collect()
    atClient.pkam_authenticate(verbose=True)
    del at_client
    gc.collect()

    # Set up LED display matrix.
    from led_matrix import display_text, init_matrix
    matrix = init_matrix()

    # Set up public keys for each atSign and read in their values.
    picoKey = 'led'
    appKey = 'instructions'
    ledStatus = int(atClient.get_public(picoKey, picoAtSign))
    previousText = atClient.get_public(appKey, appAtSign)

    # Loop forever.
    while True:
        # Get display text from the app atsign's public key.
        text = atClient.get_public(appKey, appAtSign)

        # Whenever the text changes, we know the user has chosen
        # a new option. Display the text on the LED matrix.
        if text != previousText:
            # Update the LED status to let the app know it can continue.
            ledStatus ^= 1
            atClient.put_public(picoKey, str(ledStatus))

            # Display LED text w/o any leading or trailing whitespace.
            display_text(matrix, text.strip())
            previousText = text


# Reads in Wi-Fi settings and atSign names from settings.json.
def read_settings():
    import ujson
    with open('settings.json') as f:
        info = ujson.loads(f.read())
        return (info['ssid'], info['password'],
                info['picoAtSign'].replace('@', ''),
                info['appAtSign'].replace('@', ''))


if __name__ == '__main__':
    main()
