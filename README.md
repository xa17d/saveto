# Save To…

Save shares to file from any app.

Save To… - Share into a file
Share data from any app to save it into a file.

Exporting data from any app into a file made simple. Just open the share options, select "Save To…" and choose the target file. Done. No permissions required.

Very first release of "Save To…". Adds the app to the "sharing" and "open with" options on your device. When opened, allows to store the shared data into a file. It can then be viewed in the Gallery or another app.

## Code and Release

Before committing run:
```bash
./debugCheck.sh
```

For releasing run:
```bash
./release.sh
```

## Signing

Signing is located in `.signing` folder. It is added to `.gitignore`.
Extract `release.jks` (and `private_key.pepk`) from Password Manager to `.signing`.

Create `.signing/keystore.properties`:
```properties
keyAlias=releaseKey
keyPassword=[PASSWORD FROM PASSWORD MANAGER]
storeFile=.signing/release.jks
storePassword=[SAME PASSWORD FROM PASSWORD MANAGER]
```

## Package-Structure

- `at.xa1.saveto`: main package with the most fundamental classes (like `MainActivity` and `MainCoordinator`)
  - `common`: generic, non-app-specific classes
    - `android`: generic, non-app-specific classes that are strongly related to Android.
  - `di`: Dependency Injection.
  - `feature`: sub packages contain pieces of functionality, that don't depend on other features.
  - `ui`: User interface components that are not feature specific.

## Screenshots

* Make sure status bar is empty
* Make sure battery, network and wifi are full (emulator settings).
* Use gesture navigation to hide navigation buttons (android settings)
* Set time to: 04:01  (android settings)
* Use tests in `PlayStoreScreenshots` for individual screens
  * Uncomment `Thread.sleep` in `waitForScreenshot` function to have a chance in making screenshots.

## Backlog

- [x] Return OK Result when finished successfully
- [x] Show KB copied
- [x] Show Success
- [x] Show Error
- [x] Propose Filename
- [x] Support Plain Text
- [x] Make Scrollable
- [x] Dependency Injection
- [x] List open source licenses
- [x] Settings
- [x] Intro
- [x] Screenshots
- [x] Feature graphic
- [x] https://xa1.at/saveto/ site
- [x] Review package and file structure
- [x] show version
- [ ] Code Review (total)
- [ ] Shouldn't be possible to open from recents.
- [ ] Abort (back button)

**Future Version:**
- [ ] Support multiple files
