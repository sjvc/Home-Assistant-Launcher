# Home-Assistant-Launcher
Home Assistant launcher is a simple Android app to use your Home Assistant web UI with some improvements:

## Home-Assistant version
Home Assistant may change its DOM structure on new releases, so this WebView may not work correctly if it happens. 
Latest commit is tested and fully working under **Home Assistant v0.78.3**.

## Lock Screen
If you want to keep the access to Home Assistant password protected in your Android device, you have to logout and login everytime. Home Assistant launcher uses a simple lock screen, so you only have to enter your pin, or use your fingerprint (if your device supports it). It's fast, and reliable. For this feature we are using this library: https://github.com/amirarcane/lock-screen

The app will prompt for pin everytime you open the app after:
- You closed it
- The app is on background for more than 10 seconds

<img src="https://github.com/sjvc/Home-Assistant-Launcher/blob/master/screenshots/lock-screen.png?raw=true" width="300" />

## Hide administrator menu items
You can hide administrator menu items from the drawer menu, so you can let your family members use home assistant without the risk they change any configuration parameter.

<img src="https://github.com/sjvc/Home-Assistant-Launcher/blob/master/screenshots/no-admin-drawer.png?raw=true" width="300" />

## Back key behavior
If you are using Home Assistant through your web browser, Android back key behaves like browser back key. So, if you navigated through several tabs, pressing back key takes you to previous visited tab. Android apps don't work that way (it should close the app). So this is the back key behavior:

- If a "more info" popup is showing, popup will close.
- Else, if section "Overview" is not showing, you will be back to "Overview".
- Else, you will exit the app.
