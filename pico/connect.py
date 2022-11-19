from lib.at_client import at_client
from lib import wifi
from lib.at_client import io_util

# Read settings.json
ssid, password, atSign = io_util.read_settings()
del io_util  # Make space in memory

# Connect to wifi
print(f'Connecting to WiFi {ssid}...')
wifi.init_wlan(ssid, password)
del ssid, password, wifi  # Make space in memory

# Connect and pkam authenticate into secondary
atClient = at_client.AtClient(atSign, writeKeys=False)
atClient.pkam_authenticate(verbose=True)
del at_client
