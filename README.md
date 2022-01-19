# Save To…

Save shares to file from any app.

Save To… - Share into a file
Share data from any app to save it into a file.

Exporting data from any app into a file made simple. Just open the share options, select "Save To…" and choose the target file. Done. No permissions required.

Very first release of "Save To…". Adds the app to the "sharing" and "open with" options on your device. When opened, allows to store the shared data into a file. It can then be viewed in the Gallery or another app.

## Package-Structure

- `at.xa1.saveto`: main package with the most fundamental classes (like `MainActivity` and `MainCoordinator`)
  - `common`: generic, non-app-specific classes
    - `android`: generic, non-app-specific classes that are strongly related to Android.
  - `di`: Dependency Injection.
  - `feature`: sub packages contain pieces of functionality, that don't depend on other features.
  - `ui`: User interface components that are not feature specific.

## Screenshots

* Make sure status bar is empty
* Make sure battery, network and wifi are full.
* Use gesture navigation to hide navigation buttons
* Set time to: 04:01
* Use tests in `PlayStoreScreenshots` for individual screens

## Backlog

- [x] Return OK Result when finished successfully
- [x] Show KB copied
- [x] Show Success
- [ ] Show Error
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
- [ ] Code Review (total)
- [ ] Shouldn't be possible to open from recents.

**Future Version:**
- [ ] Support multiple files
