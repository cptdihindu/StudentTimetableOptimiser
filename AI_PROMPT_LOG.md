**Documentation Rule:** For every future prompt, include the full original prompt text in the log under **Original Prompt Used**, followed by files changed, summary, human review notes, and status.

## Prompt 001 - Project Setup and Documentation Rules

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Prompt Summary:** Initial project rules and documentation setup.
**Original Prompt Used:**
You are assisting with the implementation of a Java console application called Student Timetable Optimiser.

Important project rules:

1. This must be a Java console application only.
2. Do not use Swing, JavaFX, web frameworks, mobile frameworks, databases, or GUI tools.
3. The application must use console input and output only.
4. The application should follow this layered structure:

   * ui package for console menus and user input/output
   * controller package for handling user actions
   * service package for business logic
   * model package for data classes
   * io package for CSV import and timetable export
5. The application must support:

   * importing class data from CSV
   * browsing class records
   * viewing class details
   * searching class records
   * editing class records
   * deleting class records
   * generating timetables
   * checking time clashes
   * checking campus travel-time rules
   * managing generated timetables
   * exporting timetables as CSV
6. Keep the code beginner-friendly, readable, and suitable for a university assignment.
7. Use clear class names, method names, comments where useful, and avoid over-engineering.
8. Do not generate JUnit test code unless I explicitly ask. JUnit tests must be written manually later.
9. Do not add features outside the assignment requirements unless I specifically request them.

Documentation rule:
Create and maintain a markdown file named AI_PROMPT_LOG.md in the project root.

Every time I give you an implementation prompt, update AI_PROMPT_LOG.md with a new entry containing:

* Prompt number
* Date
* AI tool used: GitHub Copilot in IntelliJ IDEA
* Original prompt or short summary of the prompt
* Files created or modified
* Summary of code generated or edited
* Human review notes placeholder
* Status

Use this format:

## Prompt 001 - Project Setup and Documentation Rules

**Date:** [enter current date]
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Prompt Summary:** Initial project rules and documentation setup.
**Files Created/Modified:**

* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
No Java code generated yet. Documentation rules were created for future implementation prompts.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft / Reviewed / Modified

For every future prompt, continue the numbering:
Prompt 002, Prompt 003, Prompt 004, and so on.

First task:
Create the AI_PROMPT_LOG.md file in the project root and add the first entry using the format above.

Note: Initial project rules and documentation setup prompt was used to instruct Copilot to maintain AI_PROMPT_LOG.md for all future implementation prompts.
**Files Created/Modified:**

* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
No Java code generated yet. Documentation rules were created for future implementation prompts.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft / Reviewed / Modified

## Prompt 002 - Initial Project Structure

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Prompt Summary:** Created initial Java console project structure with placeholder classes and a basic menu start.
**Original Prompt Used:**
Create the initial Java project structure for the Student Timetable Optimiser console application.

Project rules:
- This is a Java console application only.
- Do not use Swing, JavaFX, web frameworks, GUI tools, databases, Maven, or Gradle yet.
- Keep everything beginner-friendly and suitable for a university Java assignment.
- Use the standard source folder structure: src/main/java.

Tasks:
1. Create this folder structure if it does not already exist:

src/main/java
src/main/java/model
src/main/java/service
src/main/java/io
src/main/java/ui
src/main/java/controller

2. Inside src/main/java, create Main.java.

3. Inside the package folders, create placeholder Java classes only. Do not add full business logic yet.

Create these files:

src/main/java/model/Topic.java
src/main/java/model/Availability.java
src/main/java/model/ClassRecord.java
src/main/java/model/Timetable.java
src/main/java/model/TimetableEntry.java
src/main/java/model/Preference.java
src/main/java/model/SearchCriteria.java

src/main/java/service/ClassService.java
src/main/java/service/TimetableService.java
src/main/java/service/SearchService.java
src/main/java/service/ValidationService.java

src/main/java/io/CSVImporter.java
src/main/java/io/TimetableExporter.java

src/main/java/ui/ConsoleMenu.java

src/main/java/controller/ClassController.java
src/main/java/controller/TimetableController.java

4. Add correct package declarations to every file.

Examples:
- files inside model must start with package model;
- files inside service must start with package service;
- files inside io must start with package io;
- files inside ui must start with package ui;
- files inside controller must start with package controller;

5. Main.java should contain a basic main method that creates a ConsoleMenu object and starts the application.

Example behaviour:
- When the program runs, it should call ConsoleMenu.start().
- ConsoleMenu.start() should print a simple welcome message and main menu placeholder.
- Do not implement full menu logic yet.

6. ConsoleMenu.java should include:
- a public class ConsoleMenu
- a public void start() method
- a private void printHeader() method
- a private void printMainMenu() method
- simple console output only

7. Do not create JUnit tests. Do not generate any test code.

8. After completing this task, update AI_PROMPT_LOG.md by adding a new entry:

Prompt 002 - Initial Project Structure

Include:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Prompt Summary
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Note: Prompt asked Copilot to create the initial Java project structure, including src/main/java, model, service, io, ui, controller packages, placeholder Java classes, Main.java, ConsoleMenu.java, and documentation updates.
**Files Created/Modified:**

* README.md
* src/main/java/Main.java
* src/main/java/ui/ConsoleMenu.java
* src/main/java/model/Topic.java
* src/main/java/model/Availability.java
* src/main/java/model/ClassRecord.java
* src/main/java/model/Timetable.java
* src/main/java/model/TimetableEntry.java
* src/main/java/model/Preference.java
* src/main/java/model/SearchCriteria.java
* src/main/java/service/ClassService.java
* src/main/java/service/TimetableService.java
* src/main/java/service/SearchService.java
* src/main/java/service/ValidationService.java
* src/main/java/io/CSVImporter.java
* src/main/java/io/TimetableExporter.java
* src/main/java/ui/ConsoleMenu.java
* src/main/java/controller/ClassController.java
* src/main/java/controller/TimetableController.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Created source folders, a basic `Main` entry point, a placeholder console menu, and empty model/service/io/controller classes. Added a minimal README.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 014 - Add App Configuration and Configurable CSV Folder

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 8 - Add application configuration file and configurable CSV folder path.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app imports university class data from CSV files and stores class records during the current running session.
The app uses console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.
Do not create JUnit tests.
Do not generate test code.

Reason for this change:
Currently CSV import assumes CSV files are in a fixed folder such as CSVs.
That is not flexible enough.
The user should be able to configure the CSV folder path.
The path can be absolute or relative.
The app should save this path in a configuration file.
Later, this same configuration file can store app preferences such as travel time minutes.

Main goal:
Add an app configuration system using a simple properties file.
When the app launches, if the configuration file does not exist, the app should create it automatically with default values.
The user should be able to view and edit the configuration from the main menu.
The CSV import screen should use the configured CSV folder path to list available CSV files.

Configuration file:
Create and use a file in the project root named:

app-config.properties

Default contents:

csv.folder.path=CSVs
travel.time.minutes=30

Important:
- If app-config.properties does not exist when the app launches, create it automatically.
- If a property is missing, add the default value.
- Do not crash if the file is missing or partially incomplete.

Files to create:
- src/main/java/service/AppConfigService.java

Files to modify:
- src/main/java/Main.java
- src/main/java/ui/ConsoleMenu.java
- src/main/java/controller/ClassController.java
- src/main/java/ui/ConsoleUtils.java
- AI_PROMPT_LOG.md

Modify only if required:
- src/main/java/service/ClassService.java
- src/main/java/io/CSVImporter.java

Do not modify unrelated timetable logic.
Do not create JUnit tests.

1. Implement AppConfigService.java

Package:
service

Fields:
- private final String CONFIG_FILE_NAME = "app-config.properties";
- private Properties properties;

Use:
java.util.Properties
java.nio.file.Path
java.nio.file.Paths
java.nio.file.Files
java.io.InputStream
java.io.OutputStream

Required default values:
- csv.folder.path = CSVs
- travel.time.minutes = 30

Required public methods:

public AppConfigService()
- Initialise properties.
- Call loadOrCreateConfig().

public void loadOrCreateConfig()
Behaviour:
- If app-config.properties does not exist in the current working directory, create it with default values.
- If it exists, load it.
- If csv.folder.path is missing or blank, set it to CSVs.
- If travel.time.minutes is missing or blank, set it to 30.
- Save the file after adding missing defaults.

public String getCsvFolderPath()
- Return csv.folder.path.
- If missing or blank, return CSVs.

public boolean setCsvFolderPath(String path)
Behaviour:
- Accept absolute or relative folder paths.
- If path is null or blank, return false.
- Resolve the path.
- Verify that the directory exists.
- Verify that it is a directory.
- If valid:
  - save the path exactly as the user entered it, trimmed.
  - save the config file.
  - return true.
- If invalid:
  - do not save.
  - return false.

public Path resolvePath(String pathText)
Behaviour:
- If pathText is absolute, return it normalized.
- If pathText is relative, resolve it against the current working directory and normalize it.
- Example:
  CSVs
  should resolve to:
  [current project folder]\CSVs
- Example:
  ..\SharedCSVs
  should resolve correctly relative to the current working directory.

public Path getResolvedCsvFolderPath()
- Return resolvePath(getCsvFolderPath()).

public int getTravelTimeMinutes()
- Read travel.time.minutes.
- If invalid, return 30.

public boolean setTravelTimeMinutes(int minutes)
- Accept positive values only.
- Save to config.
- Return true if saved, false if invalid.

public String getConfigSummary()
Return a readable summary like:
Configuration:
- CSV folder path: CSVs
- Resolved CSV folder path: D:\...\StudentTimetableOptimiser\CSVs
- Travel time minutes: 30

private void saveConfig()
- Save app-config.properties.

2. App startup behaviour

Update Main.java or ConsoleMenu.java so AppConfigService is created when the app starts.

Important:
This should create app-config.properties automatically if it does not exist.

Use one shared AppConfigService instance for the whole application session.

Suggested:
- ConsoleMenu has:
  private AppConfigService appConfigService;
  private ClassService classService;
  private ClassController classController;
- ConsoleMenu constructor creates:
  appConfigService = new AppConfigService();
  classService = new ClassService();
  classController = new ClassController(classService, appConfigService);

Update ClassController constructor to accept AppConfigService.

3. Add Configuration option to main menu

Update main menu to include a configuration option:

Main Menu
[1] Class Data Management
[2] Timetable Generation
[3] Timetable Management
[4] Export Timetable
[5] Configuration
[6] Help / About
[0] Exit

Update choice handling accordingly.

4. Add Configuration menu/screen

In ConsoleMenu.java, add a configuration screen.

Screen title:
APPLICATION CONFIGURATION

Menu:
[1] View current configuration
[2] Set CSV folder path
[3] Set travel time minutes
[0] Back to main menu

For now, travel time setting can be edited and saved, but timetable validation will use it later.

4.1 View current configuration

Print appConfigService.getConfigSummary()

Also print clear path help:
Path input help:
- Absolute path example:
  D:\Shortcuts\Documents\CSVs
- Relative path example:
  CSVs
- Another relative path example:
  ..\SharedCSVs
Relative paths are resolved from the folder where you run the app.

Pause before returning.

4.2 Set CSV folder path

Show title:
SET CSV FOLDER PATH

Show clear instructions:
You can enter an absolute or relative folder path.

Absolute path example:
D:\Shortcuts\Documents\#MyProjects\#IsuruJavaApp\StudentTimetableOptimiser\CSVs

Relative path examples:
CSVs
..\SharedCSVs

Important explanation:
Relative paths are resolved from the current working directory where the app is launched.

Show current CSV folder path and resolved path.

Ask:
Enter new CSV folder path or 0 to cancel:

Before saving:
- Resolve the path and show:
Resolved path:
[resolved path]

Verify:
- directory exists
- path is a directory

If invalid:
Show error:
The folder does not exist or is not a directory. Configuration was not saved.

If valid:
Ask:
Save this CSV folder path? Y/N or 0 to cancel:

If yes:
Call appConfigService.setCsvFolderPath(input)
Show success:
CSV folder path saved successfully.

If no or cancel:
Show:
Configuration update cancelled.

4.3 Set travel time minutes

Show title:
SET TRAVEL TIME MINUTES

Show current value.

Ask:
Enter travel time in minutes or 0 to cancel:

Rules:
- Must be a positive integer.
- If invalid, show error and ask again or return cleanly.
- Save using appConfigService.setTravelTimeMinutes(minutes)

Show success when saved.

5. Update CSV import screen to use configured CSV folder

When user selects:
Class Data Management -> Import class data from CSV

Behaviour:
- Use appConfigService.getResolvedCsvFolderPath()
- If the configured CSV folder exists, list .csv files from that folder.
- If the configured CSV folder does not exist, show warning:
  Configured CSV folder does not exist:
  [resolved path]
  You can update it from Main Menu -> Configuration, or import a custom CSV file manually.

- If configured folder exists but contains no CSV files, show warning:
  No CSV files were found in the configured CSV folder:
  [resolved path]
  You can still import a custom CSV file manually.

- If CSV files exist, display:
Available CSV files in configured folder:
[1] file1.csv
[2] file2.csv

Then ask:
Enter CSV number, custom CSV file path, or 0 to cancel:

6. CSV import path input rules

The import screen must clearly explain how to input paths:

Print:
Input options:
- Type a number from the list to import that CSV file.
- Type an absolute CSV file path, for example:
  D:\Shortcuts\Documents\Classes\COMP1002.csv
- Type a relative CSV file path, for example:
  CSVs\COMP1002.csv
- Relative paths are resolved from the folder where you launched the app.
- Type 0 to cancel.

7. Custom CSV path import

If the user types a number:
- Use the selected CSV from the configured folder.

If the user types a path:
- Accept absolute path.
- Accept relative path.
- If user types only a file name and a file with that name exists inside the configured CSV folder, use that file.
- Otherwise resolve the typed path relative to current working directory.

Then call classService.importFromCsv(finalPath.toString()).

8. Keep import result behaviour

After importing, still show:
CSV import completed.
New records imported: X
Existing records updated: Y
Total class records stored: Z

If import fails, show classService.getLastErrorMessage().

If warnings exist, show them.

9. Update help/about screen

Mention:
The CSV folder path can be changed from Configuration.
The app creates app-config.properties automatically if it is missing.
CSV folder paths can be absolute or relative.

10. Code quality

- Keep code beginner-friendly.
- Use clear method names.
- Use java.nio.file.Path and Files.
- Use DirectoryStream or simple file listing to find CSV files.
- Sort CSV file list alphabetically if simple to do.
- Do not use external libraries.
- Do not use GUI file chooser.
- Make sure app still compiles.
- Make sure start.bat and compile.bat still work.

11. Documentation

Update README.md if it exists with a small section:

## Application Configuration

The app uses app-config.properties in the project root.
If the file does not exist, it is created automatically when the app starts.

Example:
csv.folder.path=CSVs
travel.time.minutes=30

The CSV folder path can be edited from the app:
Main Menu -> Configuration -> Set CSV folder path

The path can be absolute or relative.
Relative paths are resolved from the folder where the app is launched.

12. AI prompt log

Update AI_PROMPT_LOG.md by adding a new entry:

## Prompt 014 - Add App Configuration and Configurable CSV Folder

Include these sections:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.

**Files Created/Modified:**

* src/main/java/service/AppConfigService.java
* src/main/java/ui/ConsoleMenu.java
* src/main/java/controller/ClassController.java
* README.md
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Added AppConfigService for creating and loading app-config.properties with defaults, updated the main and configuration menus to view and edit settings, switched CSV import to use the configured folder with input guidance and resolution rules, and documented configuration usage.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 015 - Fix ANSI Display and Add Safe Console Colours

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Fix console UI colours and remove weird ANSI escape text in the Student Timetable Optimiser.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app is run on Windows using:
java -jar StudentTimetableOptimiser.jar

Problem:
The console is showing weird text such as:
<[3J<[H<[2J

This happens because ANSI escape codes for clear screen are being printed in terminals that do not support them correctly.

Also, the UI is currently all white. I want the console UI to use simple colours where supported, but it must not show weird escape characters in unsupported terminals.

Files to modify:
- src/main/java/ui/ConsoleUtils.java
- src/main/java/ui/ConsoleMenu.java
- src/main/java/controller/ClassController.java
- src/main/java/controller/TimetableController.java
- README.md
- AI_PROMPT_LOG.md

Modify only if needed:
- start.bat
- compile.bat

Do not implement SearchService in this step.
Do not implement timetable logic in this step.
Do not create JUnit tests.

Main goal:
1. Remove weird ANSI escape text.
2. Add safe colour support.
3. Keep the UI readable even when colours are not supported.
4. Keep clear-screen behaviour safe.

Important:
Do not print ANSI codes unless colour support is enabled.
The app should work cleanly in:
- IntelliJ terminal
- PowerShell
- Windows Terminal
- Command Prompt

1. Update ConsoleUtils.java

Add a colour support system.

Fields:
private static boolean colorsEnabled = true;

Add methods:
public static void setColorsEnabled(boolean enabled)
public static boolean isColorsEnabled()

Add ANSI constants:
RESET
BOLD
RED
GREEN
YELLOW
BLUE
CYAN
MAGENTA
WHITE

But only use them through helper methods.

Add helper method:
private static String color(String text, String ansiCode)

Behaviour:
- If colorsEnabled is true, return ansiCode + text + RESET.
- If colorsEnabled is false, return text only.

Add public helper methods:
public static String successText(String text)
public static String errorText(String text)
public static String warningText(String text)
public static String infoText(String text)
public static String titleText(String text)
public static String menuOptionText(String text)

Suggested colours:
- success: GREEN
- error: RED
- warning: YELLOW
- info: CYAN
- title: CYAN + BOLD
- menu option: YELLOW

Update existing print methods:
printSuccess should use success colour.
printError should use error colour.
printWarning should use warning colour.
printSectionTitle should use title colour.
Menu options can use menuOptionText where suitable.

2. Fix clearScreen()

Replace the current ANSI-only clearScreen() method.

Use this safer behaviour:

public static void clearScreen() {
  if (supportsAnsi()) {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  } else {
    for (int i = 0; i < 40; i++) {
      System.out.println();
    }
  }
}

Add:
private static boolean supportsAnsi()

Behaviour:
- Return true if environment variable WT_SESSION exists. This means Windows Terminal.
- Return true if environment variable TERM exists and is not blank.
- Return true if System.console() is not null and OS is not Windows.
- Return false for old Windows Command Prompt by default.

Important:
Do not use "\033[3J" because it is causing weird visible text in some terminals.
Only use "\033[H\033[2J" when ANSI is supported.
If ANSI is not supported, print blank lines instead.

3. Add colour toggle in app configuration if simple

If AppConfigService already exists, add a property:
colors.enabled=true

Default:
colors.enabled=true

Add methods:
public boolean isColorsEnabled()
public boolean setColorsEnabled(boolean enabled)

When the app starts:
- Read colors.enabled from config.
- Call ConsoleUtils.setColorsEnabled(appConfigService.isColorsEnabled())

If this becomes too complex, keep colorsEnabled default true but disable ANSI colours automatically when supportsAnsi() is false.

Best behaviour:
- If ANSI is not supported, do not print colour codes.
- If ANSI is supported and colors.enabled=true, print colours.

4. Add Configuration menu option for colours

In Configuration menu, add:

[4] Toggle colour output

When selected:
- Show current colour status: Enabled or Disabled
- Ask: Enable colour output? Y/N or 0 to cancel:
- Save the setting if AppConfigService supports it.
- Call ConsoleUtils.setColorsEnabled(newValue)
- Show success message.

Updated Configuration menu:
[1] View current configuration
[2] Set CSV folder path
[3] Set travel time minutes
[4] Toggle colour output
[0] Back to main menu

5. Improve visual style

Apply colours lightly:
- Main titles should be cyan/bold.
- Success messages should be green.
- Errors should be red.
- Warnings should be yellow.
- Menu option numbers can be yellow.
- Normal explanations should remain plain white.

Do not overdo it.
Do not add complicated ASCII colour art.
Keep it professional and readable for an assignment.

6. Fix app start display

The welcome ASCII screen should not show duplicated headers.
It should show:
- ASCII title once
- App name
- Press Enter to continue

Then main menu should show cleanly.

7. Update README.md

Add a note:

## Console Colours

The application uses ANSI colours when the terminal supports them.
For best results on Windows, run the app in Windows Terminal or PowerShell.
If strange symbols appear, disable colours from:
Main Menu -> Configuration -> Toggle colour output

8. Update AI_PROMPT_LOG.md

Add a new entry:

## Prompt 015 - Fix ANSI Display and Add Safe Console Colours

Include:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.

9. Make sure the project compiles.

After the changes, the app should:
- Not print weird ANSI code text.
- Use colours only when safe.
- Let the user disable colours.
- Still work in plain white if the terminal does not support colours.

**Files Created/Modified:**

* src/main/java/ui/ConsoleUtils.java
* src/main/java/ui/ConsoleMenu.java
* src/main/java/controller/ClassController.java
* src/main/java/controller/TimetableController.java
* src/main/java/service/AppConfigService.java
* README.md
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Added safe ANSI detection and a color toggle, updated menu rendering to use color helpers, wired the color setting into configuration, and documented color support in the README.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 016 - Fix Clear Screen Gap on Windows

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Fix ConsoleUtils.clearScreen().

Problem: it prints many blank lines and creates a huge gap.

In ConsoleUtils.java, replace clearScreen with this:

public static void clearScreen() {
  try {
    String os = System.getProperty("os.name").toLowerCase();

    if (os.contains("win")) {
      new ProcessBuilder("cmd", "/c", "cls")
          .inheritIO()
          .start()
          .waitFor();
    } else {
      System.out.print("\033[H\033[2J");
      System.out.flush();
    }
  } catch (Exception e) {
    System.out.println();
    System.out.println();
    System.out.println();
  }
}

Remove any fallback that prints 40 or 50 blank lines.
Do not use \033[3J.
Update AI_PROMPT_LOG.md as Prompt 016 - Fix Clear Screen Gap on Windows.
Do not change backend logic or create tests.

**Files Created/Modified:**

* src/main/java/ui/ConsoleUtils.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Replaced the clear screen logic with a Windows cls call and a minimal fallback to avoid large blank-line gaps.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 017 - Implement SearchService and Connect Search UI

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 9 - Implement SearchService and connect it to the Search Class Records UI.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app imports university class data from CSV files and stores class records during the current running session.
The app uses console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.
Do not create JUnit tests.
Do not generate test code.

Main goal:
Make this menu option real:
Class Data Management -> Search class records

Files to modify:
- src/main/java/service/SearchService.java
- src/main/java/controller/ClassController.java
- src/main/java/model/SearchCriteria.java
- AI_PROMPT_LOG.md

Modify only if needed:
- src/main/java/service/ClassService.java
- src/main/java/ui/ConsoleUtils.java

Do not implement timetable generation, validation, or export in this step.

1. Implement SearchService.java

Package:
service

Required method:

public ArrayList<ClassRecord> search(ArrayList<ClassRecord> records, SearchCriteria criteria)

Behaviour:
- If records is null, return an empty ArrayList.
- If criteria is null or criteria.isEmpty(), return a copy of all records.
- Search should support multiple criteria.
- A record must match all entered criteria to be included.
- Matching should be case-insensitive.
- Matching should allow partial text matches.
  Example: campus "ton" should match "Tonsley".
- Blank criteria fields should be ignored.

Search fields:
- topicCode
- topicName
- attendanceMode
- campus
- semester
- availabilityNumber
- classType
- classInstance
- firstClassDate
- lastClassDate
- day
- startTime
- endTime
- building
- room

For startTime and endTime:
- Compare using display text such as 09:00.
- Partial match is acceptable.

Add helper methods:
- private boolean matches(String recordValue, String searchValue)
- private boolean matchesTime(LocalTime time, String searchValue)
- private boolean isBlank(String value)

Use simple loops, not complicated stream code.

2. Update SearchCriteria.java if needed

Make sure SearchCriteria has:
- getters and setters for all search fields
- isEmpty()
- toString()

toString() should only show entered criteria.

3. Connect search in ClassController.java

Update the Search Class Records screen.

Behaviour:
- If no class records exist, show warning:
  No class records have been imported yet.
- Show title:
  SEARCH CLASS RECORDS
- Show:
  Tip: Leave a field blank to ignore it. Type 0 at any field to cancel and go back.

Ask for these criteria:
- Topic code
- Topic name
- Campus
- Semester
- Class type
- Day
- Start time
- End time
- Building
- Room

All fields are optional.
But if the user types 0 at any prompt, cancel the search and return to the Class Data Management menu.

Create a SearchCriteria object and set the entered values.

Call:
SearchService searchService = new SearchService();
ArrayList<ClassRecord> results = searchService.search(classService.getAllClassRecords(), criteria);

Display:
Search criteria:
[criteria.toString()]

If results are empty:
No matching class records found.

If results exist:
Search results:
1. COMP1701 Game Design | Tonsley | S2 | Tutorial | Instance 1
2. ...

Then ask:
View full details of a result? Enter result number or 0 to go back:

If user enters 0, return to menu.
If invalid, show error and pause.
If valid, show the selected result's getFullDetails().

Important:
The result number is based on the search result list, not the original class record list.

4. Code quality

- Keep the code beginner-friendly.
- Do not crash on invalid input.
- Keep cancel support.
- Make sure imported records remain stored after searching.
- Do not create JUnit tests.
- Make sure the project compiles.

5. Documentation

Update AI_PROMPT_LOG.md by adding:

## Prompt 017 - Implement SearchService and Connect Search UI

Include:
- Date
- AI Tool Used
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under Original Prompt Used, include the full original prompt text from this message, not only a summary.

**Files Created/Modified:**

* src/main/java/service/SearchService.java
* src/main/java/controller/ClassController.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Implemented multi-criteria class record searching with partial matches and time checks, and connected the search UI to display results and selected record details.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 018 - Implement ValidationService

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 10 - Implement ValidationService for timetable rules.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app imports class records from CSV files and will generate timetables later.
The app uses console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.
Do not create JUnit tests.
Do not generate test code.

Main goal:
Implement validation rules needed before timetable generation.

Files to modify:
- src/main/java/service/ValidationService.java
- AI_PROMPT_LOG.md

Modify only if needed:
- src/main/java/model/ClassRecord.java
- src/main/java/service/AppConfigService.java

Do not implement timetable generation in this step.
Do not connect to UI yet unless needed for compilation.

Validation rules to support:
1. Time clash checking
2. Lecture overlap allowed/not allowed
3. Same campus back-to-back rule
4. Different campus 30-minute travel rule
5. Online class travel exemption
6. Flinders City Campus mixing rule for the same topic

Important assignment rules:
- Two classes clash if they overlap on the same base day.
- Same campus classes can be back-to-back.
- Different physical campuses need at least 30 minutes travel time.
- Online classes do not require campus travel time.
- Bedford Park and Tonsley can be mixed for the same topic.
- Flinders City Campus cannot be mixed with Bedford Park or Tonsley for the same topic.
- Different topics can use different campuses.
- Lecture overlap is allowed only if the user chooses that option.

1. Implement ValidationService.java

Package:
service

Required public methods:

public boolean hasTimeClash(ClassRecord a, ClassRecord b, boolean allowLectureOverlap)

Behaviour:
- Return false if either record is null.
- Compare base day using ClassRecord.getBaseDay().
- If base days are different, return false.
- If allowLectureOverlap is true and either class is a lecture, return false.
- Otherwise, return true if times overlap.
- Time overlap rule:
  a starts before b ends AND b starts before a ends.
- Back-to-back classes on the same day are not a time clash.
  Example:
  10:00 - 11:00 and 11:00 - 12:00 should not clash.

public boolean hasAnyTimeClash(ArrayList<ClassRecord> records, boolean allowLectureOverlap)

Behaviour:
- Check every pair of records.
- Return true if any pair clashes.

public boolean hasEnoughTravelTime(ClassRecord a, ClassRecord b, int requiredMinutes)

Behaviour:
- Return true if either record is null.
- Return true if base days are different.
- Return true if either record is online.
- Return true if campuses are the same, ignoring case.
- For different physical campuses:
  - Check gap between end of earlier class and start of later class.
  - If classes overlap, return false.
  - Return true only if gap is at least requiredMinutes.
- Back-to-back different-campus classes are invalid unless gap >= requiredMinutes.

public boolean hasAnyTravelTimeIssue(ArrayList<ClassRecord> records, int requiredMinutes)

Behaviour:
- Check every pair of records.
- Return true if any pair does not have enough travel time.

public boolean isValidCampusMixForSameTopic(ArrayList<ClassRecord> records)

Behaviour:
- Check records grouped by topic code.
- For each topic:
  - If that topic has Flinders City Campus and also Bedford Park or Tonsley, return false.
  - Bedford Park and Tonsley together are allowed.
  - Online should not cause this rule to fail.
- Return true if all topic campus mixes are valid.

public boolean isTimetableValid(ArrayList<ClassRecord> records, boolean allowLectureOverlap, int requiredTravelMinutes)

Behaviour:
- Return false if hasAnyTimeClash is true.
- Return false if hasAnyTravelTimeIssue is true.
- Return false if isValidCampusMixForSameTopic is false.
- Otherwise return true.

public ArrayList<String> getValidationWarnings(ArrayList<ClassRecord> records, boolean allowLectureOverlap, int requiredTravelMinutes)

Behaviour:
- Return a list of readable warning messages.
- Add messages for:
  - time clashes
  - travel time problems
  - invalid Flinders City Campus mixing
- If no problems, return empty list.
- Warning examples:
  "Time clash: COMP1701 Tutorial Wednesday 10:00 - 11:00 overlaps with COMP1711 Workshop Wednesday 10:30 - 12:00"
  "Travel issue: Tonsley class ending at 11:00 and Bedford Park class starting at 11:10 has less than 30 minutes travel time."
  "Campus rule issue: Topic COMP1701 mixes Flinders City Campus with Bedford Park or Tonsley."

2. Helper methods

Add private helper methods where useful:
- private boolean isSameBaseDay(ClassRecord a, ClassRecord b)
- private boolean sameCampus(ClassRecord a, ClassRecord b)
- private long minutesBetween(LocalTime end, LocalTime start)
- private String safe(String value)
- private boolean isBlank(String value)
- private String getRecordShortName(ClassRecord record)

3. Travel time logic details

For same-day classes:
- Determine which class happens earlier.
- If a ends before or at b starts, gap = minutes between a.endTime and b.startTime.
- If b ends before or at a starts, gap = minutes between b.endTime and a.startTime.
- If neither, they overlap, so travel time fails for different campuses.
- Same campus does not need travel gap.
- Online classes do not need travel gap.

4. Time null safety

If startTime or endTime is null:
- Avoid crashing.
- Treat as not clashing for hasTimeClash.
- Treat as valid for travel time.
- Add no warning for that pair.

5. Campus matching

Use case-insensitive trimmed comparison.
Treat these campus names as physical campuses:
- Bedford Park
- Tonsley
- Flinders City Campus

Online classes:
Use ClassRecord.isOnline().

6. Code quality

- Use ArrayList.
- Use HashSet or HashMap where useful.
- Keep code beginner-friendly.
- Use simple loops.
- Do not use external libraries.
- Do not create JUnit tests.
- Make sure project compiles.

7. Documentation

Update AI_PROMPT_LOG.md by adding:

## Prompt 018 - Implement ValidationService

Include:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under Original Prompt Used, include the full original prompt text from this message, not only a summary.

**Files Created/Modified:**

* src/main/java/service/ValidationService.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Implemented timetable validation rules for time clashes, travel time gaps, campus mixing restrictions, and readable warnings using simple helper methods.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 019 - Implement TimetableService

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 11 - Implement TimetableService for timetable generation and storage.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app imports university class data from CSV files, stores class records in memory, and helps students generate valid timetables.
The app uses console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.
Do not create JUnit tests.
Do not generate test code.

Main goal:
Implement TimetableService so the app can generate and store timetables using imported ClassRecord data.

Files to modify:
- src/main/java/service/TimetableService.java
- AI_PROMPT_LOG.md

Modify only if needed:
- src/main/java/model/Timetable.java
- src/main/java/model/TimetableEntry.java
- src/main/java/model/Preference.java
- src/main/java/model/ClassRecord.java
- src/main/java/service/ValidationService.java

Do not connect to UI yet.
Do not implement CSV export yet.
Do not create tests.

Important:
Keep the first version simple and reliable.
The generator does not need to find every possible timetable.
It should try to build one valid timetable using the assignment rules.

Required fields in TimetableService:
- private ArrayList<Timetable> timetables;
- private ValidationService validationService;
- private int autoNameCounter;
- private String lastErrorMessage;
- private ArrayList<String> lastGenerationWarnings;

Constructor:
public TimetableService()
- initialise timetables
- initialise validationService
- autoNameCounter starts at 1
- initialise lastGenerationWarnings

Required public methods:

1. public Timetable generateTimetable(
        String timetableName,
        String semester,
        ArrayList<String> selectedTopicCodes,
        ArrayList<String> selectedCampuses,
        boolean allowLectureOverlap,
        ArrayList<Preference> preferences,
        ArrayList<ClassRecord> availableRecords,
        int requiredTravelMinutes
)

Behaviour:
- Clear lastErrorMessage and lastGenerationWarnings.
- Validate input:
  - selectedTopicCodes must not be null or empty.
  - selectedCampuses must not be null or empty.
  - availableRecords must not be null or empty.
  - semester must not be blank.
- If timetableName is blank, generate automatic unique name:
  Timetable_1, Timetable_2, etc.
- If timetableName is not blank, make sure it is unique.
  If already exists, set lastErrorMessage and return null.

Filtering:
- Filter availableRecords by:
  - selected semester
  - selected topic codes
  - selected campuses
- Semester matching:
  - If selected semester is "Both", allow S1 and S2 records.
  - Otherwise match case-insensitive.
  - Accept "Semester 1" as matching "S1".
  - Accept "Semester 2" as matching "S2".
- Topic matching should be case-insensitive.
- Campus matching should be case-insensitive.
- Online records can be included even if selected campus does not include Online, if their topic and semester match.

Generation approach:
- For each selected topic, the timetable should try to include one suitable class instance for each class type.
- Group filtered records by topic code, then class type.
- For each topic and class type:
  - choose one ClassRecord that does not break validation with already selected records.
  - prefer records that score higher according to preferences.
- Add chosen records as TimetableEntry objects.
- If no valid record can be found for a required topic/class type:
  - add a warning explaining what could not be selected.
  - continue if possible.
- After selecting records, run full validation:
  validationService.getValidationWarnings(...)
- Add those warnings to lastGenerationWarnings.
- If validationService.isTimetableValid(...) is false:
  - set lastErrorMessage to "Unable to generate a valid timetable with the selected options."
  - return null.
- If valid:
  - create Timetable object.
  - set timetable name, semester, allowLectureOverlap, preferences.
  - add selected entries.
  - store timetable in timetables.
  - return the timetable.

2. public ArrayList<Timetable> getAllTimetables()
- Return a copy of the timetables list.

3. public boolean hasTimetables()
- Return true if timetables is not empty.

4. public int getTimetableCount()
- Return timetables.size().

5. public Timetable getTimetableByIndex(int displayIndex)
- 1-based index.
- Return null if invalid.

6. public Timetable getTimetableByName(String name)
- Case-insensitive matching.
- Return null if not found.

7. public String getBrowseSummary()
- If no timetables exist, return:
  "No timetables have been generated yet."
- Otherwise return numbered summaries:
  1. Timetable_1 | Semester 2 | 3 topics | 3 days

8. public String getFullDetailsByIndex(int displayIndex)
- Return timetable full details or "Invalid timetable number."

9. public boolean deleteTimetableByIndex(int displayIndex)
- Remove timetable using 1-based index.
- Return true if deleted.

10. public boolean isTimetableNameUnique(String name)
- Return true if no timetable already uses that name.
- Blank names do not need uniqueness check.

11. public String generateAutomaticTimetableName()
- Generate Timetable_1, Timetable_2, etc.
- Must avoid names that already exist.

12. public String getLastErrorMessage()
- Return lastErrorMessage.

13. public ArrayList<String> getLastGenerationWarnings()
- Return copy of warnings.

Preference scoring:
Add a private method:
private int scoreRecordByPreferences(ClassRecord record, ArrayList<Preference> preferences)

Simple scoring:
- If preferences is null or empty, return 0.
- Higher ranked preferences should have more value.
- If preference name matches campus, add points.
- If preference is "Mornings", add points if start time is before 12:00.
- If preference is "Afternoons", add points if start time is 12:00 or later.
- If preference is a weekday like Mondays, Tuesdays, etc., add points if base day matches.
- Keep it simple.

Selection helper:
Add private helper:
private ClassRecord chooseBestValidRecord(
    ArrayList<ClassRecord> options,
    ArrayList<ClassRecord> selectedRecords,
    boolean allowLectureOverlap,
    int requiredTravelMinutes,
    ArrayList<Preference> preferences
)

Behaviour:
- Try options sorted by preference score, highest first.
- For each option:
  - create temporary selected list with option added.
  - check validationService.isTimetableValid(...)
  - return first valid option.
- If none are valid, return null.

Other helper methods:
- private ArrayList<ClassRecord> filterRecords(...)
- private boolean matchesSemester(String recordSemester, String selectedSemester)
- private boolean matchesAnyIgnoreCase(String value, ArrayList<String> choices)
- private boolean isBlank(String value)
- private ArrayList<String> normaliseStringList(ArrayList<String> list)

Code quality:
- Use ArrayList and HashMap.
- Use simple loops.
- Avoid advanced Java features.
- Keep code readable.
- Do not use external libraries.
- Do not create JUnit tests.
- Make sure the project compiles.

Documentation:
Update AI_PROMPT_LOG.md by adding:

## Prompt 019 - Implement TimetableService

Include:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under Original Prompt Used, include the full original prompt text from this message, not only a summary.

**Files Created/Modified:**

* src/main/java/service/TimetableService.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Implemented timetable generation, filtering, preference scoring, validation checks, and in-memory storage with summaries and lookup helpers.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 020 - Connect TimetableService to Timetable UI

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 12 - Connect TimetableService to the Timetable UI.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app imports class data from CSV files, stores class records in memory, and generates timetables.
The app uses console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.
Do not create JUnit tests.
Do not generate test code.

Main goal:
Connect TimetableService to the UI so timetable generation, browsing, viewing, and deleting work from the console menu.

Files to modify:
- src/main/java/Main.java
- src/main/java/ui/ConsoleMenu.java
- src/main/java/controller/TimetableController.java
- AI_PROMPT_LOG.md

Modify only if needed:
- src/main/java/controller/ClassController.java
- src/main/java/service/TimetableService.java
- src/main/java/service/ClassService.java
- src/main/java/service/AppConfigService.java
- src/main/java/ui/ConsoleUtils.java
- src/main/java/model/Preference.java
- src/main/java/model/Timetable.java

Do not implement CSV export in this step.
Do not implement timetable edit/swap in this step.
Do not create tests.

Important design:
Use one shared ClassService, one shared TimetableService, and one shared AppConfigService for the whole app session.
Do not create a new TimetableService every time the menu opens, because generated timetables would be lost.

1. Update service setup

ConsoleMenu should have:
- private ClassService classService;
- private TimetableService timetableService;
- private AppConfigService appConfigService;
- private ClassController classController;
- private TimetableController timetableController;

In the ConsoleMenu constructor:
- create appConfigService
- create classService
- create timetableService
- pass classService and appConfigService to ClassController
- pass timetableService, classService, and appConfigService to TimetableController

2. Update TimetableController constructor

TimetableController should have:
- private TimetableService timetableService;
- private ClassService classService;
- private AppConfigService appConfigService;

Constructor:
public TimetableController(TimetableService timetableService, ClassService classService, AppConfigService appConfigService)

If any service is null, create fallback service objects.

3. Connect Main Menu -> Timetable Generation

When the user selects Main Menu option Timetable Generation, call the same generate timetable screen used by Timetable Management -> Generate new timetable.

4. Connect Timetable Management menu

Timetable Management menu should support:

[1] Generate new timetable
[2] Browse saved timetables
[3] View timetable details
[4] Edit timetable
[5] Delete timetable
[0] Back to main menu

In this step:
- [1] Generate new timetable should work.
- [2] Browse saved timetables should work.
- [3] View timetable details should work.
- [4] Edit timetable can remain placeholder: "Timetable edit/swap will be implemented in a later step."
- [5] Delete timetable should work.

5. Generate timetable screen

When user chooses Generate Timetable:

- Clear screen.
- Show title: GENERATE TIMETABLE.
- If no class records have been imported, show warning:
  "No class records have been imported yet. Please import CSV data first."
  Then pause and return.

Collect these inputs:
- Timetable name, optional. Blank means automatic name.
- Semester, required.
- Topics, separated by commas, required.
- Campuses, separated by commas, required.
- Allow lecture overlap, Y/N.
- Preferences, separated by commas, optional.

Input help:
For topics:
"Enter topic codes separated by commas, for example COMP1002, COMP1701"

For campuses:
"Enter campuses separated by commas, for example Tonsley, Bedford Park"
Also mention:
"Use the campus names as they appear in the imported class data."

For preferences:
"Examples: Mornings, Afternoons, Tonsley, Mondays, Compact classes"

Use cancel-aware input.
Typing 0 at any input should cancel generation and return to the previous menu.

Convert:
- Topics input into ArrayList<String>
- Campuses input into ArrayList<String>
- Preferences input into ArrayList<Preference>
  - Assign ranking based on order:
    first preference ranking 1, second ranking 2, etc.

Get available records:
classService.getAllClassRecords()

Get travel time:
appConfigService.getTravelTimeMinutes()

Call:
Timetable generated = timetableService.generateTimetable(
    timetableName,
    semester,
    selectedTopicCodes,
    selectedCampuses,
    allowLectureOverlap,
    preferences,
    classService.getAllClassRecords(),
    appConfigService.getTravelTimeMinutes()
);

6. Show generation summary before confirming

Before calling generateTimetable, show:

Timetable generation request:
- Name: [entered name or Automatic name]
- Semester: ...
- Topics: ...
- Campuses: ...
- Allow lecture overlap: Yes/No
- Preferences: ...
- Travel time minutes: ...

Ask:
Generate timetable with these settings? Y/N or 0 to cancel:

If N or 0:
Show "Timetable generation cancelled."

If Y:
Call TimetableService.

7. Generation result display

If generated timetable is not null:
Show success:
"Timetable generated successfully."

Then show:
generated.getFullDetails()

If timetableService.getLastGenerationWarnings() is not empty:
Show:
Generation warnings:
- warning 1
- warning 2

If generated timetable is null:
Show error:
timetableService.getLastErrorMessage()

If warnings exist, print them too.

Pause before returning.

8. Browse saved timetables

When user selects Browse saved timetables:

- Clear screen.
- Show title: BROWSE SAVED TIMETABLES.
- Print timetableService.getBrowseSummary().
- Pause before returning.

9. View timetable details

When user selects View timetable details:

- Clear screen.
- Show title: VIEW TIMETABLE DETAILS.
- If no timetables exist, show warning:
  "No timetables have been generated yet."
  Pause and return.
- Print timetableService.getBrowseSummary().
- Ask:
  "Enter timetable number or 0 to cancel:"
- If cancelled, show "View timetable cancelled."
- If invalid/non-number, show error.
- If valid, print timetableService.getFullDetailsByIndex(number).
- Pause before returning.

10. Delete timetable

When user selects Delete timetable:

- Clear screen.
- Show title: DELETE TIMETABLE.
- If no timetables exist, show warning.
- Print timetableService.getBrowseSummary().
- Ask:
  "Enter timetable number or 0 to cancel:"
- If cancelled, show "Delete timetable cancelled."
- If valid, show selected timetable full details.
- Ask:
  "Warning: This timetable will be deleted. Confirm delete? Y/N or 0 to cancel:"
- If Y:
  Call timetableService.deleteTimetableByIndex(number)
  If true, show success:
  "Timetable deleted successfully."
  If false, show error:
  "Invalid timetable number."
- If N or 0:
  Show "Delete timetable cancelled."
- Pause before returning.

11. Helper methods

Add helper methods inside TimetableController if useful:
- private ArrayList<String> parseCommaSeparatedList(String input)
- private ArrayList<Preference> parsePreferences(String input)
- private String displayList(ArrayList<String> list)
- private void printWarnings(ArrayList<String> warnings)
- private Integer readNumberOrCancel(Scanner scanner, String prompt)

12. Code quality

- Keep code beginner-friendly.
- Use simple ArrayLists.
- Do not use advanced Java features unnecessarily.
- Keep cancel support.
- Do not crash on invalid input.
- Make sure generated timetables remain available after returning to menus.
- Make sure project compiles.

13. Documentation

Update AI_PROMPT_LOG.md by adding:

## Prompt 020 - Connect TimetableService to Timetable UI

Include:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under Original Prompt Used, include the full original prompt text from this message, not only a summary.

**Files Created/Modified:**

* src/main/java/ui/ConsoleMenu.java
* src/main/java/controller/TimetableController.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Connected the timetable UI to shared services, implemented generation inputs and summaries, and wired browse/view/delete flows to TimetableService with warnings and cancel handling.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 021 - Add Numbered Selection Inputs Across UI

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 13 - Reduce manual typing by adding numbered selection inputs across the app.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app uses console input/output only.
The app already has class import, browse, view, search, edit/delete, config, and timetable generation features.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Do not create JUnit tests.
Do not generate test code.

Main goal:
Reduce manual typing as much as possible.
Wherever the app already knows available options, show numbered choices and let the user select numbers instead of typing full text.

Examples:
Instead of:
Semester: S2

Use:
[1] Semester 1 / S1
[2] Semester 2 / S2
[3] Both
[0] Cancel
Select semester:

Instead of:
Campuses: Tonsley, Bedford Park

Use:
Available campuses:
[1] Bedford Park
[2] Tonsley
[3] Flinders City Campus
[4] Online
[5] Other / custom campus
[0] Cancel
Select campus numbers separated by commas, for example 1,2:

The user should still be able to use custom input only when needed, such as:
- custom campus name
- custom CSV file path
- custom timetable name
- custom export path

Files to modify:
- src/main/java/ui/ConsoleUtils.java
- src/main/java/controller/ClassController.java
- src/main/java/controller/TimetableController.java
- src/main/java/ui/ConsoleMenu.java
- AI_PROMPT_LOG.md

Modify only if needed:
- src/main/java/service/ClassService.java
- src/main/java/service/SearchService.java
- src/main/java/service/TimetableService.java

Do not implement timetable edit/swap in this step.
Do not implement CSV export in this step.

1. Add reusable numbered selection helpers to ConsoleUtils

Add methods such as:

public static Integer readSingleNumberOrCancel(Scanner scanner, String prompt, int min, int max)

Behaviour:
- Ask for a number.
- If user enters 0, return null.
- If non-number, show error and ask again.
- If outside min/max, show error and ask again.
- Return selected number.

public static ArrayList<Integer> readMultipleNumbersOrCancel(Scanner scanner, String prompt, int min, int max)

Behaviour:
- Accept input like:
  1
  1,2
  1, 2, 3
- If user enters 0, return null.
- Validate all numbers.
- Do not allow numbers outside min/max.
- Remove duplicate numbers.
- Return selected numbers.

public static void printNumberedOptions(ArrayList<String> options)

Behaviour:
- Print:
  [1] option 1
  [2] option 2

public static String chooseFromOptionsOrCancel(Scanner scanner, String title, ArrayList<String> options)

Behaviour:
- Print options.
- Ask user to select one number.
- Return selected option text.
- Return null if cancelled.

public static ArrayList<String> chooseMultipleFromOptionsOrCancel(Scanner scanner, String title, ArrayList<String> options)

Behaviour:
- Print options.
- Ask user to select numbers separated by commas.
- Return selected option texts.
- Return null if cancelled.

Keep these methods beginner-friendly.

2. Add data extraction helpers where useful

In ClassService, add methods if not already present:

public ArrayList<String> getAvailableTopicCodes()
- Return unique topic codes from imported records, sorted alphabetically if simple.

public ArrayList<String> getAvailableTopicDisplayNames()
- Return unique "COMP1002 Fundamentals of Artificial Intelligence" style names.

public ArrayList<String> getAvailableCampuses()
- Return unique campuses from imported records.

public ArrayList<String> getAvailableSemesters()
- Return unique semesters from imported records.

public ArrayList<String> getAvailableClassTypes()
- Return unique class types from imported records.

public ArrayList<String> getAvailableDays()
- Return unique base days from imported records.

These will support selection menus.

3. Update timetable generation UI

Replace manual semester/topic/campus/preference input where possible.

Semester:
Show:
[1] Semester 1 / S1
[2] Semester 2 / S2
[3] Both
[0] Cancel

Return:
1 -> S1
2 -> S2
3 -> Both

Topics:
Instead of typing topic codes manually, show imported topics as numbered options.

Example:
Available topics:
[1] COMP1002 Fundamentals of Artificial Intelligence
[2] COMP1701 Game Design
[3] COMP1711 Database Modelling
[0] Cancel

Prompt:
Select topic numbers separated by commas, for example 1,3:

Convert selected topics to topic codes before calling TimetableService.

Campuses:
Show campuses from imported records if available.
Also include common campus options if not already present:
- Bedford Park
- Tonsley
- Flinders City Campus
- Online
- Other / custom campus

Prompt:
Select campus numbers separated by commas, for example 1,2:

If user selects Other / custom campus:
Ask:
Enter custom campus name or 0 to cancel:

Preferences:
Show numbered list:
[1] Bedford Park
[2] Tonsley
[3] Flinders City Campus
[4] All at the same campus
[5] Mornings
[6] Afternoons
[7] Mondays
[8] Tuesdays
[9] Wednesdays
[10] Thursdays
[11] Fridays
[12] Evenly spread classes across days
[13] Compact classes to as few days as possible
[0] No preferences / Cancel

Prompt:
Select preference numbers in priority order, separated by commas.
Example: 2,5,13 means Tonsley first, then Mornings, then Compact classes.

If user enters 0, use no preferences, not cancel the whole timetable generation.
But if your existing cancel system needs a separate cancel option, clearly show:
[0] No preferences
[C] Cancel
If supporting C is too much, use 0 as no preferences.

Lecture overlap:
Use:
[1] Yes
[2] No
[0] Cancel

Timetable name:
This can remain text input because it is custom.
Blank means automatic name.

4. Update search UI

For search fields where options are known, use numbered selection where possible.

Search screen should offer:
[1] Search by topic
[2] Search by campus
[3] Search by semester
[4] Search by class type
[5] Search by day
[6] Advanced manual search
[0] Back

For topic/campus/semester/class type/day:
- Show available numbered options from imported records.
- Search using selected option.
- Display results.

Advanced manual search can keep the existing optional text fields.

5. Update edit class record UI

Record selection already uses numbers, keep it.

Field selection already uses numbers, keep it.

For fields like campus, semester, class type, day:
- After choosing the field, show available known values as numbered options plus custom option.

Example for campus:
[1] Bedford Park
[2] Tonsley
[3] Flinders City Campus
[4] Online
[5] Custom value
[0] Cancel

If custom selected, ask user to type value.

For startTime/endTime, keep manual text input because times are custom.

6. Update configuration menu where useful

Configuration can mostly stay as text input because paths are custom.
But for colour toggle:
Use:
[1] Enable colours
[2] Disable colours
[0] Cancel

7. Keep custom path import support

Do not remove custom CSV file path import.
The CSV import screen should still allow:
- selecting a CSV number from configured folder
- typing absolute CSV path
- typing relative CSV path

But it should clearly explain the options.

8. Code quality

- Keep the UI beginner-friendly.
- Do not over-engineer.
- Do not break existing features.
- Do not remove cancel/back support.
- Make sure imported class records remain stored.
- Make sure generated timetables remain stored.
- Make sure the project compiles.

9. Documentation

Update README.md if useful:
Mention that the app uses numbered selections to reduce typing errors.

Update AI_PROMPT_LOG.md by adding:

## Prompt 021 - Add Numbered Selection Inputs Across UI

Include:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under Original Prompt Used, include the full original prompt text from this message, not only a summary.

**Files Created/Modified:**

* src/main/java/ui/ConsoleUtils.java
* src/main/java/controller/ClassController.java
* src/main/java/controller/TimetableController.java
* src/main/java/ui/ConsoleMenu.java
* src/main/java/service/ClassService.java
* README.md
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Added reusable numbered selection helpers, exposed available data lists from ClassService, and updated timetable generation, search, edit, and configuration screens to use numbered options with custom fallbacks.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 022 - Allow Multiple CSV Selection

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
currently only 1 CSV file number can input to import. make it comma seperate to multiple CSVs import same time according to the comma number order

**Files Created/Modified:**

* src/main/java/controller/ClassController.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Updated CSV import selection to accept comma-separated numbers and import multiple CSV files sequentially, aggregating counts and warnings while preserving selection order.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 022 - Implement Timetable Edit Swap

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 14 - Implement timetable edit by swapping class instances.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app imports class data from CSV files, generates timetables, and manages saved timetables during the current session.
The app uses console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.
Do not create JUnit tests.
Do not generate test code.

Main goal:
Make this menu option real:
Timetable Management -> Edit timetable

Required behaviour:
The user should be able to edit a generated timetable by swapping one selected class with another class instance from:
- the same topic
- the same class type
- a different class instance if possible

The swap must be checked using validation rules.
If the swap causes time clashes or travel-time issues, show warnings and ask for confirmation before saving.

Files to modify:
- src/main/java/service/TimetableService.java
- src/main/java/controller/TimetableController.java
- AI_PROMPT_LOG.md

Modify only if needed:
- src/main/java/model/Timetable.java
- src/main/java/model/TimetableEntry.java
- src/main/java/model/ClassRecord.java
- src/main/java/service/ValidationService.java
- src/main/java/service/ClassService.java
- src/main/java/ui/ConsoleUtils.java

Do not implement CSV export in this step.

1. Add swap support to TimetableService

Add this public method:

public boolean swapTimetableEntry(
    int timetableDisplayIndex,
    int entryDisplayIndex,
    ClassRecord replacementRecord,
    boolean allowInvalidSwap,
    int requiredTravelMinutes
)

Behaviour:
- timetableDisplayIndex is 1-based.
- entryDisplayIndex is 1-based.
- If timetable number is invalid, set lastErrorMessage and return false.
- If entry number is invalid, set lastErrorMessage and return false.
- If replacementRecord is null, set lastErrorMessage and return false.
- The replacement must have the same topic code as the current entry, ignoring case.
- The replacement must have the same class type as the current entry, ignoring case.
- The replacement should ideally have a different class instance.
- Create a temporary list of records from the timetable entries, replacing the selected entry with replacementRecord.
- Validate temporary list using ValidationService:
  - time clash
  - travel time
  - campus mix rule
- If the temporary list is invalid and allowInvalidSwap is false:
  - do not save the swap.
  - store validation warnings in lastGenerationWarnings or a new lastValidationWarnings list.
  - set lastErrorMessage to "Swap causes timetable validation issues."
  - return false.
- If valid, or invalid but allowInvalidSwap is true:
  - update the TimetableEntry to use replacementRecord.
  - save warnings if any.
  - return true.

Also add:

public ArrayList<ClassRecord> findReplacementOptions(
    Timetable timetable,
    int entryDisplayIndex,
    ArrayList<ClassRecord> allClassRecords
)

Behaviour:
- Return possible replacement records from allClassRecords.
- Must match same topic code.
- Must match same class type.
- Should exclude the exact same class instance/day/time/location where possible.
- If entry index invalid, return empty list.
- Use simple loops.
- Return options in a readable order if simple.

Add or reuse:
public ArrayList<String> getLastGenerationWarnings()
public String getLastErrorMessage()

2. Add helper methods if needed in TimetableService

Suggested helpers:
- private ArrayList<ClassRecord> getRecordsFromTimetable(Timetable timetable)
- private boolean sameText(String a, String b)
- private boolean sameClassRecord(ClassRecord a, ClassRecord b)
- private TimetableEntry getEntryByIndex(Timetable timetable, int entryDisplayIndex)

3. Connect Edit Timetable screen in TimetableController

When user selects:
Timetable Management -> Edit timetable

Behaviour:
- Clear screen.
- Show title: EDIT TIMETABLE.
- If no timetables exist, show warning and return.
- Print timetableService.getBrowseSummary().
- Ask user to select timetable number or 0 to cancel.
- Show selected timetable full details.
- Show numbered timetable entries:
  [1] COMP1701 Game Design | Tonsley | S2 | Tutorial | Instance 1
  [2] COMP1701 Game Design | Tonsley | S2 | Workshop | Instance 1
- Ask user to select entry number or 0 to cancel.
- Find replacement options using:
  timetableService.findReplacementOptions(selectedTimetable, entryNumber, classService.getAllClassRecords())

If no replacements:
- Show warning:
  "No replacement class instances were found for the same topic and class type."
- Pause and return.

If replacements exist:
- Print numbered replacement options.
- Ask user to select replacement number or 0 to cancel.

Show selected replacement full details.

Before saving:
- Create a temporary check if possible or call swap once with allowInvalidSwap=false.
- If swap succeeds:
  Show success:
  "Timetable entry swapped successfully."
  Show updated timetable details.
- If swap fails because validation issues:
  Show error/warnings from timetableService.
  Ask:
  "This swap may cause clashes or travel-time issues. Save anyway? Y/N or 0 to cancel:"
  If user says Y:
    Call swapTimetableEntry again with allowInvalidSwap=true.
    If true, show warning:
    "Swap saved with validation warnings."
    Show updated timetable details.
  If N or 0:
    Show "Swap cancelled."
- If swap fails for invalid topic/class type or other error:
  Show error and do not ask save anyway.

Important:
Do not let the user swap Tutorial with Workshop.
Do not let the user swap COMP1002 class with COMP1701 class.
Only same topic and same class type replacements should appear.

4. UI selection style

Use numbered selections as much as possible.
Do not ask user to manually type topic code or class type for this feature.
Use imported data and generated timetable data.

5. Validation warnings

When a swap has validation issues, display warnings clearly:
Validation warnings:
- warning 1
- warning 2

Use ValidationService warning messages if available.

6. Code quality

- Keep code beginner-friendly.
- Use ArrayList.
- Use simple loops.
- Do not use external libraries.
- Do not create tests.
- Keep cancel/back support.
- Do not break timetable generation, browse, view, or delete.
- Make sure the project compiles.

7. Documentation

Update AI_PROMPT_LOG.md by adding:

## Prompt 022 - Implement Timetable Edit Swap

Include:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under Original Prompt Used, include the full original prompt text from this message, not only a summary.

**Files Created/Modified:**

* src/main/java/service/TimetableService.java
* src/main/java/controller/TimetableController.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Implemented timetable entry swapping with validation checks and replacement lookups, and wired the edit screen to select entries, pick replacements, and confirm swaps when warnings occur.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 013 - Improve CSV File Selection

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Improve CSV import file selection so the user does not need to type the full CSV file path.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app is run from the project root using:
java -jar StudentTimetableOptimiser.jar

The project has a folder named:
CSVs

Example CSV file path:
D:\Shortcuts\Documents\#MyProjects\#IsuruJavaApp\StudentTimetableOptimiser\CSVs\COMP1002 Fundamentals of Artificial Intelligence.csv

Problem:
Currently the user must type the full CSV file path.
This is inconvenient.
Java Scanner input does not provide normal terminal tab-completion inside the app.
I want the app to support easier CSV file selection.

Files to modify:
- src/main/java/controller/ClassController.java
- src/main/java/service/ClassService.java
- src/main/java/io/CSVImporter.java
- src/main/java/ui/ConsoleUtils.java
- AI_PROMPT_LOG.md

Modify only if needed:
- src/main/java/ui/ConsoleMenu.java

Do not create JUnit tests.
Do not implement timetable logic in this step.

Requirements:

1. Support relative file paths

The CSV importer should accept:
- full absolute paths
- relative paths from the current working directory

Examples:
CSVs\COMP1002 Fundamentals of Artificial Intelligence.csv
CSVs/COMP1002 Fundamentals of Artificial Intelligence.csv

Use java.nio.file.Path and Files.exists where useful.

2. Add default CSV folder support

Use a default folder named:
CSVs

When the user opens:
Class Data Management -> Import class data from CSV

The app should look for CSV files inside the CSVs folder in the project root.

3. Show available CSV files

If the CSVs folder exists and contains .csv files, display them as a numbered list:

Available CSV files in CSVs folder:
[1] COMP1002 Fundamentals of Artificial Intelligence.csv
[2] COMP1701 Game Design.csv
[3] COMP1711 Database Modelling.csv

Then ask:
Enter CSV number, file path, or 0 to cancel:

4. Allow selecting by number

If the user enters 1, 2, 3, etc:
- convert that number into the matching CSV file path from the CSVs folder
- pass that path to classService.importFromCsv()

5. Allow typing a path manually

If the user types something that is not a number:
- treat it as a file path
- pass it to classService.importFromCsv()

This should support:
- full path
- relative path from project root
- relative path using CSVs folder

6. Optional helpful shortcut

If the user types only a file name like:
COMP1002 Fundamentals of Artificial Intelligence.csv

and that file exists inside the CSVs folder, automatically use:
CSVs/COMP1002 Fundamentals of Artificial Intelligence.csv

7. If CSVs folder does not exist

Show warning:
No CSVs folder was found in the project root.
You can still enter a full CSV file path manually.

Then ask:
Enter CSV file path or 0 to cancel:

8. If CSVs folder exists but has no CSV files

Show warning:
No CSV files were found in the CSVs folder.
You can still enter a full CSV file path manually.

Then ask:
Enter CSV file path or 0 to cancel:

9. Keep cancel support

Typing 0 should cancel and return to the Class Data Management menu.

10. Add helper methods if useful

In ClassController or ConsoleUtils, add beginner-friendly helper methods such as:
- private ArrayList<Path> getCsvFilesFromDefaultFolder()
- private Path resolveCsvSelection(String input, ArrayList<Path> availableCsvFiles)
- private void printAvailableCsvFiles(ArrayList<Path> csvFiles)

Use java.nio.file.Path, Files, and DirectoryStream if useful.

11. Import result should stay the same

After importing, still show:
CSV import completed.
New records imported: X
Existing records updated: Y
Total class records stored: Z

If import fails, still show the error message.

12. Code quality

- Keep the code beginner-friendly.
- Do not use advanced libraries.
- Do not use external dependencies.
- Do not use GUI file chooser.
- Do not use Swing or JavaFX.
- Do not break existing absolute path import.
- Make sure the project compiles.

13. Documentation

Update AI_PROMPT_LOG.md by adding a new entry:

## Prompt 013 - Improve CSV File Selection

Include these sections:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.

**Files Created/Modified:**

* src/main/java/controller/ClassController.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Added CSV file selection helpers that list available CSVs from the default CSVs folder, allow numbered selection or manual path input, support filename shortcuts, and preserve existing import result messaging with cancel handling.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 011 - Implement ClassService

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 6 - Implement ClassService for class data storage and management.

Project context:
This is a Java console application called Student Timetable Optimiser.
The application imports university class data from CSV files and stores class records during the current program session.
The app must use console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.
Do not create JUnit tests.
Do not generate test code.

Important:
This step is only for ClassService business logic.
Do not connect this to the UI yet unless required to fix compilation.
Do not implement timetable generation yet.
Do not implement validation rules yet.
Do not implement CSV export yet.
Do not modify model classes unless required to fix compilation.

Current project structure:
src/main/java/service/ClassService.java
src/main/java/io/CSVImporter.java
src/main/java/model/ClassRecord.java
src/main/java/model/SearchCriteria.java
AI_PROMPT_LOG.md

Files to modify:
- src/main/java/service/ClassService.java
- AI_PROMPT_LOG.md

Main goal:
Implement ClassService so it can store, import, browse, view, edit, and delete ClassRecord objects in memory.

Required fields in ClassService:
- private ArrayList<ClassRecord> classRecords;
- private int lastImportNewCount;
- private int lastImportUpdatedCount;
- private ArrayList<String> lastImportWarnings;
- private String lastErrorMessage;

Constructor:
- public ClassService()
  - initialise classRecords as an empty ArrayList
  - initialise lastImportWarnings as an empty ArrayList

Required public methods:

1. public boolean importFromCsv(String filePath)

Behaviour:
- Create a CSVImporter object.
- Call importer.importFromFile(filePath).
- If importer.wasSuccessful() is false:
  - set lastErrorMessage from importer.getLastErrorMessage()
  - copy importer.getRowWarnings() into lastImportWarnings
  - return false
- If importer succeeds:
  - call importRecords(importedRecords)
  - copy importer.getRowWarnings() into lastImportWarnings
  - set lastErrorMessage to an empty string
  - return true

2. public void importRecords(ArrayList<ClassRecord> importedRecords)

Behaviour:
- Reset lastImportNewCount and lastImportUpdatedCount to 0.
- If importedRecords is null, do nothing.
- For each imported ClassRecord:
  - If an existing record has the same identity, update the existing record's time and location.
  - Increase lastImportUpdatedCount.
  - Otherwise add the new record to classRecords.
  - Increase lastImportNewCount.

Use ClassRecord.hasSameIdentity(ClassRecord other) for duplicate checking.
Use ClassRecord.updateTimeAndLocationFrom(ClassRecord other) for updating.

3. public ArrayList<ClassRecord> getAllClassRecords()

Behaviour:
- Return a new ArrayList copy of classRecords.
- Do not return the original internal list directly.

4. public int getClassRecordCount()

Return classRecords.size().

5. public boolean hasClassRecords()

Return true if classRecords is not empty.

6. public ClassRecord getClassRecordByIndex(int displayIndex)

Behaviour:
- displayIndex should be 1-based for user display.
- If displayIndex is 1, return the first record.
- If index is invalid, return null.

7. public String getBrowseSummary()

Behaviour:
- If there are no class records, return:
  "No class records have been imported yet."
- Otherwise return a numbered list of class summaries.
Example:
1. COMP1701 Game Design | Tonsley | S2 | Workshop | Instance 1
2. COMP1701 Game Design | Tonsley | S2 | Tutorial | Instance 2

Use ClassRecord.getSummary().

8. public String getFullDetailsByIndex(int displayIndex)

Behaviour:
- Get class record by 1-based index.
- If invalid, return:
  "Invalid class record number."
- Otherwise return ClassRecord.getFullDetails().

9. public boolean deleteClassRecordByIndex(int displayIndex)

Behaviour:
- Remove a record using 1-based display index.
- Return true if deleted.
- Return false if invalid index.

10. public boolean editClassRecordField(int displayIndex, String fieldName, String newValue)

Behaviour:
- Get class record by 1-based index.
- If invalid, set lastErrorMessage to "Invalid class record number." and return false.
- If fieldName or newValue is null/blank, set lastErrorMessage and return false.
- Support editing these fields:
  topicCode
  topicName
  attendanceMode
  campus
  semester
  availabilityNumber
  classType
  classInstance
  firstClassDate
  lastClassDate
  day
  startTime
  endTime
  building
  room
- fieldName matching should be case-insensitive.
- Also support friendly field names:
  "topic code" -> topicCode
  "topic name" -> topicName
  "attendance mode" -> attendanceMode
  "availability number" -> availabilityNumber
  "class type" -> classType
  "class instance" -> classInstance
  "first class date" -> firstClassDate
  "last class date" -> lastClassDate
  "start time" -> startTime
  "end time" -> endTime
- For startTime and endTime, parse newValue using LocalTime.parse(newValue).
  Expected format: HH:mm, for example 09:00 or 14:30.
  If parsing fails, set lastErrorMessage to "Invalid time format. Use HH:mm." and return false.
- If fieldName is unknown, set lastErrorMessage to "Unknown field name." and return false.
- Return true if updated.

11. public ArrayList<ClassRecord> findByTopicCode(String topicCode)

Behaviour:
- Return records where topicCode matches ignoring case.
- If topicCode is null/blank, return an empty list.

12. public ArrayList<ClassRecord> findByCampus(String campus)

Behaviour:
- Return records where campus matches ignoring case.
- If campus is null/blank, return an empty list.

13. public ArrayList<ClassRecord> findBySemester(String semester)

Behaviour:
- Return records where semester matches ignoring case.
- If semester is null/blank, return an empty list.

14. public int getLastImportNewCount()

Return lastImportNewCount.

15. public int getLastImportUpdatedCount()

Return lastImportUpdatedCount.

16. public ArrayList<String> getLastImportWarnings()

Return a new ArrayList copy of lastImportWarnings.

17. public String getLastErrorMessage()

Return lastErrorMessage.

Helper methods:
- private boolean matchesIgnoreCaseTrim(String value, String search)
- private boolean isBlank(String value)

Code quality:
- Use ArrayList<ClassRecord>.
- Use java.time.LocalTime for time parsing.
- Keep code readable and beginner-friendly.
- Do not use streams if simple loops are easier to understand.
- Do not use external libraries.
- Do not add database or file saving.
- Do not create JUnit tests.
- Make sure the full project compiles after this change.

Documentation:
Update AI_PROMPT_LOG.md by adding a new entry:

## Prompt 011 - Implement ClassService

Include these sections:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.

**Files Created/Modified:**

* src/main/java/service/ClassService.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Implemented in-memory class record storage with CSV import support, duplicate update handling, browse/detail/delete helpers, editable fields with time parsing, simple search methods, and tracking of import counts/warnings and last error messages.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 010 - Improve Console Clear Screen Behaviour

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Fix the console clear screen behaviour so old menu content is actually removed from the visible terminal before showing a new screen.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app is run from Windows PowerShell / Windows Terminal using:
java -jar StudentTimetableOptimiser.jar

Problem:
When the user selects a menu option, the next submenu appears below the previous menu instead of starting on a clean screen.
The current clearScreen() method is not working properly in PowerShell. It seems to only move or hide previous output instead of giving a clean screen.

Files to modify:
- src/main/java/ui/ConsoleUtils.java
- src/main/java/ui/ConsoleMenu.java
- src/main/java/controller/ClassController.java
- src/main/java/controller/TimetableController.java
- AI_PROMPT_LOG.md

Do not implement backend logic in this step.
Do not create JUnit tests.
Do not modify model or CSV importer logic unless required to fix compilation.

Tasks:

1. Replace the existing ConsoleUtils.clearScreen() method with a stronger terminal clear method.

Use this implementation:

public static void clearScreen() {
  try {
    String os = System.getProperty("os.name").toLowerCase();

    if (os.contains("win")) {
      // ANSI clear screen + clear scrollback + move cursor to home.
      System.out.print("\033[3J\033[H\033[2J");
      System.out.flush();

      // Fallback spacing for terminals that do not fully support ANSI clear.
      for (int i = 0; i < 3; i++) {
        System.out.println();
      }
    } else {
      System.out.print("\033[3J\033[H\033[2J");
      System.out.flush();
    }
  } catch (Exception e) {
    // Basic fallback if ANSI clearing is not supported.
    for (int i = 0; i < 50; i++) {
      System.out.println();
    }
  }
}

2. Make sure clearScreen() is called BEFORE printing every new full screen/menu:
- initial main menu
- main menu after returning from submenus
- class data management menu
- timetable management menu
- timetable generation screen
- export timetable screen
- help/about screen
- all class data screens
- all timetable management screens

3. Avoid duplicate headers:
Currently the app prints the large ASCII title and then immediately prints another title header.
Update the UI so the large ASCII welcome header appears only once when the app starts.
After that, normal menus should show only one clean section header.

Expected app start:
- Clear screen
- Show large ASCII welcome header once
- Pause or continue to main menu depending on existing design
- Then show main menu cleanly

Expected main menu:
==================================================
    STUDENT TIMETABLE OPTIMISER
==================================================
Main Menu
[1] Class Data Management
[2] Timetable Generation
[3] Timetable Management
[4] Export Timetable
[5] Help / About
[0] Exit

Do not print the ASCII art again every time unless the app is restarted.

4. Add a small helper method if useful:
public static void clearAndPrintSection(String title)

It should:
- call clearScreen()
- print the section title cleanly

5. Important message behaviour:
- Do not clear the screen immediately after printing an error, warning, success, or cancellation message.
- Let the user read the message.
- After the user presses Enter in pause(), the next screen can clear.

6. Keep the code beginner-friendly and readable.
Avoid advanced libraries.
Do not use external dependencies.

7. Documentation:
Update AI_PROMPT_LOG.md by adding a new entry:

## Prompt 010 - Improve Console Clear Screen Behaviour

Include these sections:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.

**Files Created/Modified:**

* src/main/java/ui/ConsoleUtils.java
* src/main/java/ui/ConsoleMenu.java
* src/main/java/controller/ClassController.java
* src/main/java/controller/TimetableController.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Replaced `clearScreen()` with a stronger ANSI clear (including scrollback clear) that behaves better in Windows Terminal/PowerShell, added `clearAndPrintSection(title)` to standardize screen setup, updated all UI screens to clear before printing titles, and adjusted startup flow so the large ASCII header appears once at launch before the main menu.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 003 - Basic Console Menu Navigation

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Prompt Summary:** Implemented console menu navigation with main and submenu loops using Scanner and placeholder actions.
**Original Prompt Used:**
Step 2 - Build the basic console menu navigation for the Student Timetable Optimiser.

Project context:
This is a Java console application for a university assignment.
The app must use console input and output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.

Current project structure:
src/main/java/Main.java
src/main/java/ui/ConsoleMenu.java
src/main/java/controller/ClassController.java
src/main/java/controller/TimetableController.java
src/main/java/model/
src/main/java/service/
src/main/java/io/

Task:
Update the console UI so the application has working menu navigation.

Files to modify:
- src/main/java/Main.java
- src/main/java/ui/ConsoleMenu.java
- src/main/java/controller/ClassController.java
- src/main/java/controller/TimetableController.java
- AI_PROMPT_LOG.md

Requirements:

1. Main.java
Make sure Main.java creates a ConsoleMenu object and calls start().

Expected behaviour:
public static void main(String[] args) {
	ConsoleMenu menu = new ConsoleMenu();
	menu.start();
}

2. ConsoleMenu.java
Implement a working console menu loop using Scanner.

The main menu should display:

==================================================
		STUDENT TIMETABLE OPTIMISER
==================================================

Main Menu
[1] Class Data Management
[2] Timetable Generation
[3] Timetable Management
[4] Export Timetable
[5] Help / About
[0] Exit

Enter your choice:

Rules:
- Use Scanner for console input.
- Keep asking for menu choices until the user selects 0.
- Use switch statements or clear if/else logic.
- Handle invalid input gracefully.
- If the user enters text instead of a number, show an error and continue.
- Do not crash on invalid input.
- Add helper methods where useful.

Suggested methods inside ConsoleMenu:
- public void start()
- private void printHeader()
- private void printMainMenu()
- private int readIntChoice()
- private void handleMainMenuChoice(int choice)
- private void pause()

3. Class Data Management menu
When the user selects [1] from the main menu, show this submenu:

--------------------------------------------------
CLASS DATA MANAGEMENT
--------------------------------------------------
[1] Import class data from CSV
[2] Browse imported classes
[3] View class details
[4] Search class records
[5] Edit class record
[6] Delete class record
[0] Back to main menu

Enter your choice:

For now, these options should only print placeholder messages.

Example:
"Import class data feature will be implemented in a later step."

Then pause and return to the class data menu.

Place the class data menu handling mainly in ClassController.java.

ClassController should include:
- public void showClassDataMenu(Scanner scanner)
- private void printClassDataMenu()
- private int readIntChoice(Scanner scanner)
- private void handleClassDataChoice(int choice)
- private void pause(Scanner scanner)

4. Timetable Generation
When the user selects [2] from the main menu, print:

"Timetable generation feature will be implemented in a later step."

Then pause and return to the main menu.

5. Timetable Management menu
When the user selects [3] from the main menu, show this submenu:

--------------------------------------------------
TIMETABLE MANAGEMENT
--------------------------------------------------
[1] Generate new timetable
[2] Browse saved timetables
[3] View timetable details
[4] Edit timetable
[5] Delete timetable
[0] Back to main menu

Enter your choice:

For now, these options should only print placeholder messages.

Place timetable management menu handling mainly in TimetableController.java.

TimetableController should include:
- public void showTimetableMenu(Scanner scanner)
- private void printTimetableMenu()
- private int readIntChoice(Scanner scanner)
- private void handleTimetableChoice(int choice)
- private void pause(Scanner scanner)

6. Export Timetable
When the user selects [4] from the main menu, print:

"Export timetable feature will be implemented in a later step."

Then pause and return to the main menu.

7. Exit
When the user selects [0], print:

"Thank you for using Student Timetable Optimiser. Goodbye!"

Then end the program cleanly.

8. Code quality
- Use clear method names.
- Avoid duplicated code where possible, but keep it beginner-friendly.
- Do not implement CSV import, timetable generation, search, edit, delete, or export logic yet.
- Only menu navigation and placeholders are required in this step.
- Do not create JUnit tests.
- Do not generate test code.

9. Documentation
After completing this task, update AI_PROMPT_LOG.md by adding:

## Prompt 003 - Basic Console Menu Navigation

Include:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Prompt Summary
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Note: Prompt asked Copilot to implement basic console menu navigation with main menu, class data management menu, timetable management menu, placeholder actions, input validation, pause handling, and clean exit.
**Files Created/Modified:**

* src/main/java/ui/ConsoleMenu.java
* src/main/java/controller/ClassController.java
* src/main/java/controller/TimetableController.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Added a main menu loop with input validation, wired class and timetable submenus via controllers, and included placeholder messages with pause prompts.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 004 - Complete Console UI Flow

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 3 - Complete the console UI flow for the Student Timetable Optimiser.

Project context:
This is a Java console application for a university assignment.
The app must use console input and output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.

Important:
This step is UI-only.
Do not implement real CSV import, search, edit, delete, timetable generation, timetable validation, or export logic yet.
Use placeholder messages where backend functionality is not ready.
Do not create JUnit tests.
Do not generate test code.

Current project structure:
src/main/java/Main.java
src/main/java/ui/ConsoleMenu.java
src/main/java/controller/ClassController.java
src/main/java/controller/TimetableController.java
src/main/java/model/
src/main/java/service/
src/main/java/io/
AI_PROMPT_LOG.md

Main goal:
Make the console application feel complete from a user interface point of view.
The user should be able to navigate through all required screens, enter sample inputs, confirm actions, see placeholder success/error messages, and return to previous menus.

Files to modify:
- src/main/java/ui/ConsoleMenu.java
- src/main/java/controller/ClassController.java
- src/main/java/controller/TimetableController.java
- AI_PROMPT_LOG.md

Optional new file:
- src/main/java/ui/ConsoleUtils.java

If useful, create ConsoleUtils.java to avoid repeated UI helper code.

Required UI improvements:

1. Add a clean application header

Create a header that prints when the application starts:

==================================================
     ____  _             _            _   
    / ___|| |_ _   _  __| | ___ _ __ | |_ 
    \___ \| __| | | |/ _` |/ _ \ '_ \| __|
     ___) | |_| |_| | (_| |  __/ | | | |_ 
    |____/ \__|\__,_|\__,_|\___|_| |_|\__|

        STUDENT TIMETABLE OPTIMISER
==================================================

Keep it readable. If this ASCII art causes formatting issues, use a simpler header.

2. Improve the main menu

Main menu should display:

==================================================
        STUDENT TIMETABLE OPTIMISER
==================================================

Main Menu
[1] Class Data Management
[2] Timetable Generation
[3] Timetable Management
[4] Export Timetable
[5] Help / About
[0] Exit

Enter your choice:

3. Add shared UI helper methods

Create reusable helper methods either in ConsoleMenu or ConsoleUtils:

- printLine()
- printSectionTitle(String title)
- printSuccess(String message)
- printError(String message)
- printWarning(String message)
- pause(Scanner scanner)
- readIntChoice(Scanner scanner)
- readRequiredText(Scanner scanner, String prompt)
- readOptionalText(Scanner scanner, String prompt)
- readYesNo(Scanner scanner, String prompt)

Rules:
- readIntChoice should not crash if the user enters text.
- readRequiredText should keep asking until the user enters something.
- readOptionalText can return an empty string.
- readYesNo should only accept Y or N, case-insensitive.

4. Complete Class Data Management UI flow

When user selects Class Data Management, show:

--------------------------------------------------
CLASS DATA MANAGEMENT
--------------------------------------------------
[1] Import class data from CSV
[2] Browse imported classes
[3] View class details
[4] Search class records
[5] Edit class record
[6] Delete class record
[0] Back to main menu

Each option should open a proper UI screen with sample prompts and placeholders.

4.1 Import class data screen

Screen title:
IMPORT CLASS DATA FROM CSV

Prompts:
Enter CSV file path:

Placeholder behaviour:
- If blank, show error: "CSV file path cannot be empty."
- If not blank, show: "CSV import logic will be implemented in a later step."
- Show placeholder result:
  New records imported: 0
  Existing records updated: 0

4.2 Browse imported classes screen

Screen title:
BROWSE IMPORTED CLASSES

Placeholder output:
No class records have been imported yet.
This feature will display imported class summaries in a later step.

4.3 View class details screen

Screen title:
VIEW CLASS DETAILS

Prompts:
Enter class record number or topic code:

Placeholder behaviour:
Show:
Full class detail view will be implemented in a later step.

4.4 Search class records screen

Screen title:
SEARCH CLASS RECORDS

Prompts:
Topic code:
Topic name:
Campus:
Semester:
Class type:
Day:

All prompts can be optional for now.

Placeholder behaviour:
Show:
Search functionality will be implemented in a later step.
Entered criteria:
- Topic code: ...
- Topic name: ...
- Campus: ...
- Semester: ...
- Class type: ...
- Day: ...

4.5 Edit class record screen

Screen title:
EDIT CLASS RECORD

Prompts:
Enter class record number or topic code:

Then show placeholder selected class:
COMP1701 Game Design | Tonsley | S2 | Tutorial | Instance 1

Show edit options:
[1] Day
[2] Time
[3] Location
[4] Campus
[0] Cancel

Prompt:
Enter field to edit:

Then ask for new value if not cancelled.

Before saving, call readYesNo:
Warning: You are about to edit this class record. Are you sure? Y/N:

If Y:
Show success:
Class record update logic will be implemented in a later step.

If N:
Show warning:
Edit cancelled.

4.6 Delete class record screen

Screen title:
DELETE CLASS RECORD

Prompts:
Enter class record number or topic code:

Show placeholder selected class:
COMP1701 Game Design | Tonsley | S2 | Tutorial | Instance 1
Day: Wednesday
Time: 12:00 - 13:00
Location: Tonsley T1, 1.08 Lecture Room

Ask confirmation:
Warning: This class record will be deleted. Confirm delete? Y/N:

If Y:
Show success:
Class record delete logic will be implemented in a later step.

If N:
Show warning:
Delete cancelled.

5. Complete Timetable Generation UI flow

When user selects Timetable Generation from main menu, show a full input screen.

Screen title:
GENERATE TIMETABLE

Prompts:
Timetable name (leave blank for automatic name):
Semester:
Topics, separated by commas:
Campuses, separated by commas:
Allow lecture overlap? Y/N:
Preferences, separated by commas:

Rules:
- Semester is required.
- Topics are required.
- Campuses are required.
- Timetable name and preferences are optional.
- Use readYesNo for lecture overlap.

After collecting inputs, show a summary:

Timetable generation request:
- Name: [entered name or Automatic name]
- Semester: ...
- Topics: ...
- Campuses: ...
- Allow lecture overlap: Yes/No
- Preferences: ...

Then ask:
Generate timetable with these settings? Y/N:

If Y:
Show success:
Timetable generation logic will be implemented in a later step.

If N:
Show warning:
Timetable generation cancelled.

6. Complete Timetable Management UI flow

When user selects Timetable Management, show:

--------------------------------------------------
TIMETABLE MANAGEMENT
--------------------------------------------------
[1] Generate new timetable
[2] Browse saved timetables
[3] View timetable details
[4] Edit timetable
[5] Delete timetable
[0] Back to main menu

6.1 Generate new timetable
This should call the same timetable generation UI screen used by the main menu option.

6.2 Browse saved timetables

Screen title:
BROWSE SAVED TIMETABLES

Placeholder:
No timetables have been generated yet.
This feature will show saved timetables in a later step.

6.3 View timetable details

Screen title:
VIEW TIMETABLE DETAILS

Prompt:
Enter timetable name or number:

Placeholder:
Timetable detail view will be implemented in a later step.

6.4 Edit timetable

Screen title:
EDIT TIMETABLE

Prompt:
Enter timetable name or number:

Show placeholder current class:
COMP1701 Tutorial instance 1 [Monday, 09:00 - 11:00]

Show replacement options:
[1] COMP1701 Tutorial instance 2 [Tuesday, 09:00 - 11:00]
[2] COMP1701 Tutorial instance 3 [Wednesday, 14:00 - 16:00]
[0] Cancel

Prompt:
Select replacement option:

Before saving, ask:
Warning: This swap may affect clashes or travel-time rules. Continue? Y/N:

If Y:
Show success:
Timetable edit logic will be implemented in a later step.

If N:
Show warning:
Timetable edit cancelled.

6.5 Delete timetable

Screen title:
DELETE TIMETABLE

Prompt:
Enter timetable name or number:

Ask:
Warning: This timetable will be deleted. Confirm delete? Y/N:

If Y:
Show success:
Timetable delete logic will be implemented in a later step.

If N:
Show warning:
Delete cancelled.

7. Complete Export Timetable UI flow

When user selects Export Timetable from main menu:

Screen title:
EXPORT TIMETABLE

Prompts:
Enter timetable name or number:
Enter export file path, for example output/timetable.csv:

Rules:
- Timetable name/number is required.
- Export file path is required.
- If the file path does not end with .csv, show warning:
  Only CSV export is planned for this version.

Ask:
Export this timetable? Y/N:

If Y:
Show success:
Timetable export logic will be implemented in a later step.

If N:
Show warning:
Export cancelled.

8. Add Help / About screen

When user selects Help / About:

Screen title:
HELP / ABOUT

Show:
Student Timetable Optimiser is a Java console application.
It helps students import university class data from CSV files and generate convenient timetables.
The app is planned to support class data management, timetable generation, clash checking, travel-time validation, and CSV export.

Rules:
- This is a console application only.
- No graphical interface is used.
- Some features are currently placeholders and will be connected to real logic in later steps.

9. Code quality requirements

- Keep UI code simple and readable.
- Use clear method names.
- Avoid very long methods where possible.
- Use placeholders instead of real backend logic.
- Do not use advanced libraries.
- Do not use ANSI colours unless the code safely works without them.
- Do not implement real business logic yet.
- Do not create or modify JUnit test files.

10. Documentation requirement

After completing this task, update AI_PROMPT_LOG.md by adding a new entry:

## Prompt 004 - Complete Console UI Flow

Include these sections:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.
**Files Created/Modified:**

* src/main/java/ui/ConsoleMenu.java
* src/main/java/controller/ClassController.java
* src/main/java/controller/TimetableController.java
* src/main/java/ui/ConsoleUtils.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Expanded the console UI flow with shared helper utilities, full main menu navigation, detailed class data screens, timetable generation and management screens, export flow, and help/about text with placeholders.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 005 - Clear Console Between Menu Screens

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Fix the console UI so each menu screen clears the previous output before displaying the next screen.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app uses console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Do not implement backend logic in this step.
Do not create JUnit tests.

Problem:
When the user selects a menu option, the submenu opens below the previous menu, making the console messy.
I want each new screen or submenu to appear cleanly, as if the previous content was cleared.

Files to modify:
- src/main/java/ui/ConsoleUtils.java
- src/main/java/ui/ConsoleMenu.java
- src/main/java/controller/ClassController.java
- src/main/java/controller/TimetableController.java
- AI_PROMPT_LOG.md

Task:
1. Add a reusable clearScreen() method to ConsoleUtils.

The method should try to clear the console using ANSI escape codes:

public static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
}

2. Add a fallback comment explaining that some IDE consoles may not fully support clearing, but Windows Terminal, PowerShell, Command Prompt, and many external terminals should work better.

3. Use ConsoleUtils.clearScreen() before displaying:
- main menu
- class data management menu
- timetable management menu
- timetable generation screen
- export timetable screen
- help/about screen
- import class data screen
- browse imported classes screen
- view class details screen
- search class records screen
- edit class record screen
- delete class record screen
- browse saved timetables screen
- view timetable details screen
- edit timetable screen
- delete timetable screen

4. Important behaviour:
- Do not clear the screen immediately after showing an error message. Let the user read the message first.
- Use pause() after placeholder messages or error messages when needed.
- After the user presses Enter to continue, then the next screen can clear.
- Do not create an infinite loop.
- Do not break existing menu navigation.

5. Keep all UI code beginner-friendly and readable.

6. After this change, when a user selects a menu option:
- the old menu should disappear
- the selected submenu/screen should appear cleanly
- after completing the screen and pressing Enter, the app should return cleanly to the correct menu

7. Documentation:
Update AI_PROMPT_LOG.md by adding a new entry:

## Prompt 005 - Clear Console Between Menu Screens

Include these sections:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.
**Files Created/Modified:**

* src/main/java/ui/ConsoleUtils.java
* src/main/java/ui/ConsoleMenu.java
* src/main/java/controller/ClassController.java
* src/main/java/controller/TimetableController.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Added a clear-screen helper using ANSI escape codes and cleared the console before each major menu or screen while keeping pauses for error and placeholder messages.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 005 - CLI Color Styling

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
although this is a CLI ui. you should use different colors for the text to make it attractive and professional

and i want to check the CLI UI as well
**Files Created/Modified:**

* src/main/java/ui/ConsoleUtils.java
* src/main/java/ui/ConsoleMenu.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Added optional ANSI color support with safe detection, colored headers and status messages, and kept output readable when colors are not supported.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 006 - Add Cancel and Back Support to Input Screens

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Fix the console UI input flow so the user can go back or cancel from input screens without using Ctrl + C.

Project context:
This is a Java console application called Student Timetable Optimiser.
The app uses console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Do not implement backend logic in this step.
Do not create JUnit tests.

Problem:
Some screens ask for required user input, such as semester, timetable name, CSV file path, topic code, timetable name/number, or export file path.
If the user wants to go back, there is no normal way to cancel the input screen.
The user must press Ctrl + C to force terminate the program.
This must be fixed.

Files to modify:
- src/main/java/ui/ConsoleUtils.java
- src/main/java/ui/ConsoleMenu.java
- src/main/java/controller/ClassController.java
- src/main/java/controller/TimetableController.java
- AI_PROMPT_LOG.md

Main requirement:
Every input screen must clearly tell the user they can type 0 to go back/cancel.
Typing 0 should cancel the current screen and return to the correct previous menu.
Do not make the program exit unless the user selects 0 from the main menu.

Update the input helper methods:

1. In ConsoleUtils.java, update or create these methods:

- public static String readRequiredTextOrCancel(Scanner scanner, String prompt)
- public static String readOptionalTextOrCancel(Scanner scanner, String prompt)
- public static Boolean readYesNoOrCancel(Scanner scanner, String prompt)

Behaviour:

readRequiredTextOrCancel:
- Display the prompt with "or 0 to cancel".
- If the user types 0, return null.
- If the user types blank, show an error and ask again.
- Otherwise return the entered text.

readOptionalTextOrCancel:
- Display the prompt with "or 0 to cancel".
- If the user types 0, return null.
- If the user types blank, return an empty string.
- Otherwise return the entered text.

readYesNoOrCancel:
- Display the prompt with "Y/N or 0 to cancel".
- If the user types 0, return null.
- If the user types Y or y, return true.
- If the user types N or n, return false.
- Otherwise show an error and ask again.

2. Update every input screen to use cancel-aware helper methods.

Examples:

Import CSV screen:
- Prompt: Enter CSV file path or 0 to cancel:
- If null is returned, show "Import cancelled." and return to Class Data Management menu.

View class details:
- Prompt: Enter class record number or topic code or 0 to cancel:
- If null, show "View class details cancelled." and return.

Search class records:
- Allow the user to type 0 at any field to cancel the whole search screen.
- If cancelled, show "Search cancelled." and return.

Edit class record:
- User can type 0 when asked for record/topic.
- User can type 0 when selecting field to edit.
- User can type 0 when entering new value.
- User can type 0 at confirmation.
- In all cases, return to Class Data Management menu with a clear cancelled message.

Delete class record:
- User can type 0 when asked for record/topic.
- User can type 0 at confirmation.
- Return to Class Data Management menu.

Timetable Generation screen:
Prompts:
- Timetable name is optional, but typing 0 should cancel.
- Semester is required, but typing 0 should cancel.
- Topics are required, but typing 0 should cancel.
- Campuses are required, but typing 0 should cancel.
- Allow lecture overlap accepts Y/N/0.
- Preferences are optional, but typing 0 should cancel.
- Confirmation accepts Y/N/0.

If the user cancels at any point, show:
"Timetable generation cancelled."
Then return to the previous menu.

Export Timetable screen:
- User can type 0 for timetable name/number.
- User can type 0 for export file path.
- User can type 0 at confirmation.
- Return to main menu with "Export cancelled."

Timetable Management screens:
- Browse saved timetables can just show placeholder and return.
- View timetable details must allow 0 cancel.
- Edit timetable must allow 0 cancel at every input or selection.
- Delete timetable must allow 0 cancel.

3. Menu choice behaviour:
Keep 0 as Back in submenus and Exit only in main menu.

Main menu:
[0] Exit

Submenus:
[0] Back to main menu

Input screens:
typing 0 means Cancel current screen and go back to previous menu.

4. Make instructions visible:
At the top of each input screen, print:
"Tip: Type 0 at any input prompt to cancel and go back."

5. Clear screen compatibility:
Keep the previous clearScreen() method if already added.
Do not clear error messages immediately before the user can read them.
Use pause() after cancellation or important messages if needed.

6. Code quality:
- Keep the code beginner-friendly and readable.
- Avoid advanced Java features.
- Avoid unnecessary exceptions.
- Do not add real backend logic.
- Do not create or modify JUnit test files.

7. Documentation:
Update AI_PROMPT_LOG.md by adding a new entry:

## Prompt 006 - Add Cancel and Back Support to Input Screens

Include these sections:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.
**Files Created/Modified:**

* src/main/java/ui/ConsoleUtils.java
* src/main/java/ui/ConsoleMenu.java
* src/main/java/controller/ClassController.java
* src/main/java/controller/TimetableController.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Added cancel-aware input helpers and updated all input screens to allow 0 for cancel, including clear cancel messages and a visible tip line.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 007 - Add Windows Start Batch File

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Create a Windows batch file to compile and run the Student Timetable Optimiser Java console application.

Project context:
This is a plain Java console application.
The project does not use Maven or Gradle.
The main class is Main.java located in src/main/java/Main.java.
The app uses packages such as ui, controller, model, service, and io.
The compiled files should go into an out folder.
Do not create JUnit tests.
Do not modify Java business logic unless needed for the batch file to work.

Files to create/modify:
- start.bat
- README.md
- AI_PROMPT_LOG.md

Task:
1. Create a file named start.bat in the project root.

The batch file should:
- Clear the terminal screen.
- Print a title such as Student Timetable Optimiser Launcher.
- Check whether Java is installed using java -version.
- If Java is not found, show a clear error message telling the user to install Java JDK 17 or newer.
- Create the out folder if it does not exist.
- Compile all Java files from src/main/java into the out folder.
- Run the Main class using java -cp out Main.
- If compilation fails, show an error message and pause.
- When the app exits, pause so the terminal does not close immediately.

Use this batch file content:

@echo off
title Student Timetable Optimiser
cls

echo ==================================================
echo        Student Timetable Optimiser Launcher
echo ==================================================
echo.

echo Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo.
    echo ERROR: Java was not found on this computer.
    echo Please install Java JDK 17 or newer and try again.
    echo.
    pause
    exit /b 1
)

echo Java found.
echo.

echo Creating output folder...
if not exist out mkdir out

echo Compiling Java source files...
javac -d out ^
src/main/java/Main.java ^
src/main/java/ui/*.java ^
src/main/java/controller/*.java ^
src/main/java/model/*.java ^
src/main/java/service/*.java ^
src/main/java/io/*.java

if errorlevel 1 (
    echo.
    echo ERROR: Compilation failed.
    echo Please check the Java code for errors.
    echo.
    pause
    exit /b 1
)

echo.
echo Compilation successful.
echo Starting application...
echo.

java -cp out Main

echo.
echo Application closed.
pause

2. Update README.md with a simple section explaining how to run the app:

## How to Run the Application on Windows

Option 1:
Double-click start.bat.

Option 2:
Open Command Prompt or Windows Terminal in the project folder and run:

start.bat

The batch file will compile the Java files into the out folder and then start the console application.

3. Update AI_PROMPT_LOG.md by adding a new entry:

## Prompt 007 - Add Windows Start Batch File

Include these sections:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.
**Files Created/Modified:**

* start.bat
* README.md
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Added a Windows batch launcher that checks for Java, compiles sources into out, runs the app, and added Windows run instructions to the README.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 008 - Implement Model Classes

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 4 - Implement model classes for the Student Timetable Optimiser.

Project context:
This is a Java console application for a university assignment.
The application imports university class data from CSV files and helps students generate timetables.
The app must use console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.
Do not create JUnit tests.
Do not generate test code.

Important:
This step is only for model/data classes.
Do not implement CSV import logic yet.
Do not implement timetable generation logic yet.
Do not implement search logic yet.
Do not implement validation logic yet.
Do not modify UI menus unless required to fix compilation.

Current project structure:
src/main/java/Main.java
src/main/java/model/Topic.java
src/main/java/model/Availability.java
src/main/java/model/ClassRecord.java
src/main/java/model/Timetable.java
src/main/java/model/TimetableEntry.java
src/main/java/model/Preference.java
src/main/java/model/SearchCriteria.java
src/main/java/service/
src/main/java/io/
src/main/java/ui/
src/main/java/controller/
AI_PROMPT_LOG.md

Files to modify:
- src/main/java/model/Topic.java
- src/main/java/model/Availability.java
- src/main/java/model/ClassRecord.java
- src/main/java/model/Timetable.java
- src/main/java/model/TimetableEntry.java
- src/main/java/model/Preference.java
- src/main/java/model/SearchCriteria.java
- AI_PROMPT_LOG.md

General model class rules:
- Use package model; at the top of every model file.
- Use private fields.
- Provide constructors.
- Provide getters and setters.
- Provide useful toString() methods.
- Keep validation light inside models.
- Do not use Lombok.
- Do not use records.
- Use normal Java classes so the code is easy for beginners to understand.
- Use java.time.LocalTime for start and end times.
- Store dates such as "29 Jul" and "16 Sep" as String for now to keep parsing simple.
- Store day as String because sample data may include values like "Monday (once-only)" or "Thursday (fortnightly)".
- Add comments only where helpful.

1. Topic.java

Fields:
- private String topicCode;
- private String topicName;

Constructors:
- no-argument constructor
- constructor with topicCode and topicName

Methods:
- getters and setters
- getDisplayName() returning "COMP1701 Game Design" style text
- toString()

2. Availability.java

Fields:
- private String attendanceMode;
- private String campus;
- private String semester;
- private String availabilityNumber;

Constructors:
- no-argument constructor
- constructor with all fields

Methods:
- getters and setters
- getDisplayText() returning "In person - Tonsley - S2 - 1" style text
- toString()

3. ClassRecord.java

This is the main data class. One imported CSV row should become one ClassRecord.

Fields:
- private String topicCode;
- private String topicName;
- private String attendanceMode;
- private String campus;
- private String semester;
- private String availabilityNumber;
- private String classType;
- private String classInstance;
- private String firstClassDate;
- private String lastClassDate;
- private String day;
- private LocalTime startTime;
- private LocalTime endTime;
- private String building;
- private String room;

Constructors:
- no-argument constructor
- constructor with all fields

Methods:
- getters and setters for all fields
- getTopicDisplayName() returning topicCode + " " + topicName
- getTimeDisplay() returning "09:00 - 11:00" style text
- getLocationDisplay() returning "building, room" if both exist
- getSummary() returning a short line like:
  COMP1701 Game Design | Tonsley | S2 | Tutorial | Instance 1
- getFullDetails() returning a multi-line string with all fields clearly labelled
- isLecture() returning true if classType contains "lecture", ignoring case
- isOnline() returning true if campus, building, or room contains "online", ignoring case
- getBaseDay() returning the main weekday only:
  If day is "Monday (once-only)", return "Monday".
  If day is "Thursday (fortnightly)", return "Thursday".
  If day is blank or null, return "".
- hasSameIdentity(ClassRecord other) for duplicate checking.
  It should return true if these fields match, ignoring case and trimming spaces:
  topicCode
  topicName
  attendanceMode
  campus
  semester
  availabilityNumber
  classType
  classInstance
  firstClassDate
  lastClassDate
  day
  This supports the assignment rule where same records update time and location instead of creating duplicates.
- updateTimeAndLocationFrom(ClassRecord other)
  This should copy startTime, endTime, building, and room from the other record.
- toString() should return getSummary().

Important:
Use helper methods inside ClassRecord if needed:
- private boolean equalsIgnoreCaseTrim(String a, String b)

4. TimetableEntry.java

This represents one selected class inside a timetable.

Fields:
- private ClassRecord classRecord;

Constructors:
- no-argument constructor
- constructor with ClassRecord

Methods:
- getter and setter
- getSummary() returning classRecord.getSummary() or "No class selected"
- toString()

5. Preference.java

This represents one user timetable preference.

Fields:
- private String preferenceName;
- private int ranking;

Constructors:
- no-argument constructor
- constructor with preferenceName and ranking

Methods:
- getters and setters
- toString() returning something like "1. Mornings"

6. Timetable.java

This represents a generated timetable.

Fields:
- private String timetableName;
- private String semester;
- private ArrayList<TimetableEntry> entries;
- private ArrayList<Preference> preferences;
- private boolean allowLectureOverlap;

Constructors:
- no-argument constructor
  - initialise entries and preferences as empty ArrayLists
- constructor with timetableName, semester, allowLectureOverlap
  - initialise entries and preferences as empty ArrayLists

Methods:
- getters and setters
- addEntry(TimetableEntry entry)
- removeEntry(TimetableEntry entry)
- addPreference(Preference preference)
- getEntryCount()
- getTopicCount()
  - count unique topic codes from entries
- getDayCount()
  - count unique base days from entries
- getSummary()
  - return something like:
    Timetable_1 | Semester 2 | 3 topics | 3 days
- getFullDetails()
  - return a multi-line string showing timetable name, semester, lecture overlap setting, preferences, and all entries
- toString() returning getSummary()

Use java.util.ArrayList and java.util.HashSet where needed.

7. SearchCriteria.java

This stores optional search filters entered by the user.

Fields:
- private String topicCode;
- private String topicName;
- private String attendanceMode;
- private String campus;
- private String semester;
- private String availabilityNumber;
- private String classType;
- private String classInstance;
- private String firstClassDate;
- private String lastClassDate;
- private String day;
- private String startTime;
- private String endTime;
- private String building;
- private String room;

Constructors:
- no-argument constructor

Methods:
- getters and setters
- isEmpty()
  - returns true if all fields are null or blank
- toString()
  - return a readable list of entered criteria
  - skip blank fields

8. Code quality:
- Make sure all files compile.
- Keep imports clean.
- Do not create unused complicated methods.
- Do not add real app logic outside model classes.
- Do not create JUnit tests.

9. Documentation:
Update AI_PROMPT_LOG.md by adding a new entry:

## Prompt 008 - Implement Model Classes

Include these sections:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.
**Files Created/Modified:**

* src/main/java/model/Topic.java
* src/main/java/model/Availability.java
* src/main/java/model/ClassRecord.java
* src/main/java/model/Timetable.java
* src/main/java/model/TimetableEntry.java
* src/main/java/model/Preference.java
* src/main/java/model/SearchCriteria.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Implemented model classes with fields, constructors, getters/setters, display helpers, and summary/detail methods while keeping logic lightweight.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft


## Prompt 009 - Implement CSV Importer

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 5 - Implement CSVImporter for the Student Timetable Optimiser.

Project context:
This is a Java console application for a university assignment.
The application imports university class data from CSV files and converts each valid CSV row into a ClassRecord object.
The app must use console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.
Do not create JUnit tests.
Do not generate test code.

Important:
This step is only for CSV import/parsing logic.
Do not implement ClassService storage logic yet.
Do not implement UI connection yet.
Do not implement timetable generation yet.
Do not implement validation rules yet.
Do not modify UI menus unless required to fix compilation.

Current project structure:
src/main/java/io/CSVImporter.java
src/main/java/model/ClassRecord.java
src/main/java/model/Topic.java
src/main/java/model/Availability.java
src/main/java/model/Timetable.java
src/main/java/model/TimetableEntry.java
src/main/java/model/Preference.java
src/main/java/model/SearchCriteria.java
AI_PROMPT_LOG.md

Files to modify:
- src/main/java/io/CSVImporter.java
- AI_PROMPT_LOG.md

CSV format:
The expected CSV columns are:
Topic
Availability
Class
Class instance
Date
Day
Time
Location

Some sample CSV files may use Room instead of Location for the final column.
Treat Room and Location as equivalent.

Example CSV row:
COMP1701 Game Design, In person - Tonsley - S2 - 1, Workshop, 1, 29 Jul - 16 Sep, Wednesday, 10:00 - 11:00, Tonsley T1, 1.08 Lecture Room

That row should become:
topicCode = COMP1701
topicName = Game Design
attendanceMode = In person
campus = Tonsley
semester = S2
availabilityNumber = 1
classType = Workshop
classInstance = 1
firstClassDate = 29 Jul
lastClassDate = 16 Sep
day = Wednesday
startTime = 10:00
endTime = 11:00
building = Tonsley T1
room = 1.08 Lecture Room

Main task:
Implement CSVImporter.java so it can read a CSV file path and return imported ClassRecord objects.

Required public methods in CSVImporter:

1. public ArrayList<ClassRecord> importFromFile(String filePath)

Behaviour:
- Check that filePath is not null or blank.
- Check that filePath ends with .csv, ignoring case.
- Check that the file exists.
- Read the CSV file.
- Validate that the header contains the required columns.
- Accept Location or Room as the final location column.
- Parse each valid row into a ClassRecord.
- Skip completely blank lines.
- If a row is invalid, skip that row and record an error message.
- Return the successfully imported ClassRecord list.
- Store import result information inside the CSVImporter object so the caller can ask for it later.

2. public boolean wasSuccessful()

Return true if:
- the file existed
- the header was valid
- at least one valid record was imported
- there were no fatal errors

3. public String getLastErrorMessage()

Return a readable error message if the import failed.
Examples:
- "File path cannot be empty."
- "Only CSV files are supported."
- "File not found."
- "Invalid CSV format. Missing required column: Topic"
- "No valid records were imported."

4. public ArrayList<String> getRowWarnings()

Return warnings for skipped or partially invalid rows.
Example:
- "Row 5 skipped: Invalid time format."

5. public int getImportedCount()

Return the number of successfully parsed records.

Parsing requirements:

1. Header parsing
- Header names should be trimmed.
- Header matching should be case-insensitive.
- Required columns:
  Topic
  Availability
  Class
  Class instance
  Date
  Day
  Time
  Location OR Room

2. CSV parsing
Use a simple CSV parser method that supports commas inside quoted fields.

Do not just split by comma because locations may include commas.
Implement a private helper method:
private ArrayList<String> parseCsvLine(String line)

Rules:
- Commas outside quotes split columns.
- Commas inside double quotes should stay inside the value.
- Remove surrounding double quotes from values.
- Trim values.
- Keep the implementation beginner-friendly.

3. Topic parsing
Create a private helper:
private String[] parseTopic(String topicText)

Rules:
- First word is topic code.
- Remaining text is topic name.
- If no topic name exists, topicName should be empty string.
Example:
"COMP1701 Game Design" -> ["COMP1701", "Game Design"]

4. Availability parsing
Create a private helper:
private String[] parseAvailability(String availabilityText)

Expected format:
"In person - Tonsley - S2 - 1"

Rules:
- Split by " - "
- attendanceMode = first part if available
- campus = second part if available
- semester = third part if available
- availabilityNumber = fourth part if available
- Missing parts should become empty strings.
- Trim all parts.

5. Date parsing
Create a private helper:
private String[] parseDateRange(String dateText)

Expected format:
"29 Jul - 16 Sep"

Rules:
- Split by " - "
- firstClassDate = first part if available
- lastClassDate = second part if available
- If only one date exists, use it as firstClassDate and leave lastClassDate empty.
- Trim all parts.

6. Time parsing
Create a private helper:
private LocalTime[] parseTimeRange(String timeText)

Expected format:
"10:00 - 11:00"

Rules:
- Split by " - "
- Parse using java.time.LocalTime.
- Return LocalTime start and end.
- Support normal 24-hour times like 09:00, 10:00, 14:30.
- If invalid, throw an IllegalArgumentException with a readable message.

7. Location parsing
Create a private helper:
private String[] parseLocation(String locationText)

Rules:
- If location contains a comma, split at the first comma only.
  building = text before first comma
  room = text after first comma
- If no comma exists:
  building = full location text
  room = empty string
- Trim both values.

8. Record creation
Create a private helper:
private ClassRecord createClassRecordFromRow(ArrayList<String> rowValues, Map<String, Integer> columnIndexes)

Use the helper parsing methods above and return a ClassRecord.

9. Error/warning behaviour
- Fatal errors should set lastErrorMessage.
- Invalid individual rows should be skipped and added to rowWarnings.
- Do not crash the whole import because one row is bad.
- If the file has only header and no valid data rows, set lastErrorMessage to "No valid records were imported."

10. Code quality:
- Use java.io.BufferedReader or java.nio.file.Files.
- Use java.nio.file.Path where useful.
- Use ArrayList<ClassRecord>.
- Use Map<String, Integer> for column indexes.
- Keep code readable.
- Add helpful comments for tricky parsing methods.
- Do not use external CSV libraries.
- Do not use OpenCSV.
- Do not use Apache Commons CSV.
- Do not implement duplicate checking in CSVImporter. Duplicate update logic will be in ClassService later.
- Do not create JUnit tests.

11. Documentation:
Update AI_PROMPT_LOG.md by adding a new entry:

## Prompt 009 - Implement CSV Importer

Include these sections:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.
**Files Created/Modified:**

* src/main/java/io/CSVImporter.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Implemented CSV import with header validation, a simple quoted CSV parser, row-level warnings, and helpers to parse topic, availability, date, time, and location into ClassRecord objects.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 012 - Connect ClassService to Class Data UI

**Date:** May 28, 2026
**AI Tool Used:** GitHub Copilot in IntelliJ IDEA
**Original Prompt Used:**
Step 7 - Connect ClassService to the Class Data Management UI.

Project context:
This is a Java console application called Student Timetable Optimiser.
The application imports university class data from CSV files and stores class records during the current program session.
The app must use console input/output only.
Do not use Swing, JavaFX, GUI tools, web frameworks, databases, Maven, or Gradle.
Keep the code beginner-friendly, readable, and easy to explain.
Do not create JUnit tests.
Do not generate test code.

Important:
This step connects existing ClassService logic to the existing Class Data Management UI.
Do not implement timetable generation yet.
Do not implement timetable validation yet.
Do not implement CSV export yet.
Do not implement SearchService yet, except simple placeholder search if already exists.
Do not add database or permanent save/load logic.

Current relevant files:
src/main/java/Main.java
src/main/java/ui/ConsoleMenu.java
src/main/java/ui/ConsoleUtils.java
src/main/java/controller/ClassController.java
src/main/java/service/ClassService.java
src/main/java/io/CSVImporter.java
src/main/java/model/ClassRecord.java
AI_PROMPT_LOG.md

Files to modify:
- src/main/java/Main.java
- src/main/java/ui/ConsoleMenu.java
- src/main/java/controller/ClassController.java
- AI_PROMPT_LOG.md

Modify only if required for compilation:
- src/main/java/service/ClassService.java
- src/main/java/ui/ConsoleUtils.java

Main goal:
The Class Data Management menu should now use the real ClassService for:
1. Import class data from CSV
2. Browse imported classes
3. View class details
4. Edit class record
5. Delete class record

Search can remain placeholder for now unless basic simple search is already easy to connect.

Required design:
Use one shared ClassService instance for the whole application session.
Do not create a new ClassService every time the menu opens, because imported data would be lost.

1. Main.java / ConsoleMenu.java service setup

Update the application so one ClassService object is created and passed into ClassController.

Suggested design:
- ConsoleMenu has a private ClassService classService field.
- ConsoleMenu constructor creates:
  classService = new ClassService();
  classController = new ClassController(classService);
- Or Main.java creates ClassService and passes it into ConsoleMenu.
Choose the simpler beginner-friendly approach.

Important:
The same ClassService instance must be used for all class data menu operations.

2. Update ClassController constructor

ClassController should have:
- private ClassService classService;
- public ClassController(ClassService classService)

If classService is null, create a new ClassService as fallback.

3. Connect Import class data from CSV screen

When the user selects:
Class Data Management -> Import class data from CSV

Behaviour:
- Clear screen.
- Show title: IMPORT CLASS DATA FROM CSV
- Show tip: Type 0 to cancel and go back.
- Ask for CSV file path using cancel-aware input.
- If cancelled, show "Import cancelled." and return to Class Data Management menu.
- Call classService.importFromCsv(filePath).
- If result is true:
  Show success message:
  "CSV import completed."
  Show:
  New records imported: X
  Existing records updated: Y
  Total class records stored: Z
- If result is false:
  Show error:
  classService.getLastErrorMessage()
- If classService.getLastImportWarnings() is not empty:
  Show warnings under:
  "Import warnings:"
  Print each warning as "- warning text"
- Pause before returning to Class Data Management menu.

4. Connect Browse imported classes screen

When user selects:
Class Data Management -> Browse imported classes

Behaviour:
- Clear screen.
- Show title: BROWSE IMPORTED CLASSES
- Print classService.getBrowseSummary()
- Pause before returning.

Expected if no records:
No class records have been imported yet.

Expected if records exist:
1. COMP1701 Game Design | Tonsley | S2 | Workshop | Instance 1
2. COMP1701 Game Design | Tonsley | S2 | Tutorial | Instance 2

5. Connect View class details screen

When user selects:
Class Data Management -> View class details

Behaviour:
- Clear screen.
- Show title: VIEW CLASS DETAILS
- If no class records exist, show warning and return after pause.
- Print classService.getBrowseSummary() first so user can see record numbers.
- Ask:
  "Enter class record number or 0 to cancel:"
- If cancelled, show "View class details cancelled."
- If user enters non-number, show error and return to the same screen or menu cleanly.
- If valid number:
  Print classService.getFullDetailsByIndex(number)
- Pause before returning.

6. Connect Edit class record screen

When user selects:
Class Data Management -> Edit class record

Behaviour:
- Clear screen.
- Show title: EDIT CLASS RECORD
- If no class records exist, show warning and return after pause.
- Print classService.getBrowseSummary()
- Ask:
  "Enter class record number or 0 to cancel:"
- If cancelled, show "Edit cancelled."

Then show selected class full details.

Show edit field options:
[1] Topic Code
[2] Topic Name
[3] Attendance Mode
[4] Campus
[5] Semester
[6] Availability Number
[7] Class Type
[8] Class Instance
[9] First Class Date
[10] Last Class Date
[11] Day
[12] Start Time
[13] End Time
[14] Building
[15] Room
[0] Cancel

Ask:
"Select field to edit or 0 to cancel:"

Map selected number to ClassService field names:
1 -> topicCode
2 -> topicName
3 -> attendanceMode
4 -> campus
5 -> semester
6 -> availabilityNumber
7 -> classType
8 -> classInstance
9 -> firstClassDate
10 -> lastClassDate
11 -> day
12 -> startTime
13 -> endTime
14 -> building
15 -> room

Ask:
"Enter new value or 0 to cancel:"

For start time and end time, show note:
"Use HH:mm format, for example 09:00 or 14:30."

Before updating, ask confirmation:
"Warning: You are about to edit this class record. Are you sure? Y/N or 0 to cancel:"

If user selects N or 0:
Show "Edit cancelled."

If user selects Y:
Call classService.editClassRecordField(recordNumber, fieldName, newValue)
If true:
Show success:
"Class record updated successfully."
Then show updated full details.
If false:
Show error:
classService.getLastErrorMessage()

Pause before returning.

7. Connect Delete class record screen

When user selects:
Class Data Management -> Delete class record

Behaviour:
- Clear screen.
- Show title: DELETE CLASS RECORD
- If no class records exist, show warning and return after pause.
- Print classService.getBrowseSummary()
- Ask:
  "Enter class record number or 0 to cancel:"
- If cancelled, show "Delete cancelled."
- If invalid number, show error and pause.

If valid number:
- Show selected class full details.
- Ask:
  "Warning: This class record will be deleted. Confirm delete? Y/N or 0 to cancel:"

If user selects N or 0:
Show "Delete cancelled."

If user selects Y:
Call classService.deleteClassRecordByIndex(recordNumber)
If true:
Show success:
"Class record deleted successfully."
If false:
Show error:
"Invalid class record number."

Pause before returning.

8. Search class records screen

For this step, search can remain as placeholder:
"Search functionality will be connected in the next step."

But it should still allow 0 cancel and should not crash.

9. Ensure imported data remains available

After importing a CSV file:
- Return to Class Data Management menu.
- Select Browse imported classes.
- The imported class records should still be visible.
This confirms ClassService is shared correctly.

10. Code quality requirements

- Keep UI code beginner-friendly.
- Use ConsoleUtils helper methods where possible.
- Do not duplicate too much code, but clarity is more important than over-optimising.
- Use clear method names.
- Do not crash on invalid input.
- Do not use advanced Java features unnecessarily.
- Make sure the project compiles.

11. Documentation

Update AI_PROMPT_LOG.md by adding a new entry:

## Prompt 012 - Connect ClassService to Class Data UI

Include these sections:
- Date
- AI Tool Used: GitHub Copilot in IntelliJ IDEA
- Original Prompt Used
- Files Created/Modified
- AI-Generated/Edited Code Summary
- Human Review Notes: [To be completed by student after reviewing the output.]
- Status: Draft

Important:
Under **Original Prompt Used**, include the full original prompt text from this message, not only a summary.

**Files Created/Modified:**

* src/main/java/ui/ConsoleMenu.java
* src/main/java/controller/ClassController.java
* AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Connected a shared `ClassService` instance to the class data UI, replaced placeholders with real import/browse/view/edit/delete flows, added edit field selection mapping, and kept search as a safe placeholder while preserving cancel/invalid input handling.

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft

## Prompt 023 - Fix Campus/Preference Input and Enhance Timetable Generation with Detailed Results

**Date:** June 10, 2026
**AI Tool Used:** GitHub Copilot
**Prompt Summary:** Fixed campus filter and preference input handling to treat empty/0 as no filter, enhanced timetable generation algorithm with detailed rejection reasons, improved timetable display with better formatting, added generation status tracking (SUCCESS/PARTIAL SUCCESS/FAILED), and included validation summaries and helpful suggestions.

**Original Prompt Used:**

See original user request in workspace - full implementation of comprehensive timetable generation improvements including:
- Fix campus filter input handling (blank and 0 mean no filter)
- Fix preference input handling (0 and empty mean no preferences)
- Improve generation status messages (SUCCESS, PARTIAL SUCCESS, FAILED)
- Add missing classes section display
- Add detailed generation warnings with rejection reasons
- Add validation summary after timetable output
- Add helpful suggestions
- Improve timetable sorting and display
- Improve once-only and fortnightly day handling
- Improve generator algorithm to try alternative combinations
- Add generation debug/details summary
- Keep code structure clean with helper result classes
- Manual testing checklist verification

**Files Created/Modified:**

* src/main/java/model/RejectionReason.java (created)
* src/main/java/model/GenerationWarning.java (created)
* src/main/java/model/ValidationSummary.java (created)
* src/main/java/model/TimetableGenerationResult.java (created)
* src/main/java/controller/TimetableController.java (modified)
* src/main/java/service/TimetableService.java (modified)
* src/main/java/service/ValidationService.java (modified)
* src/main/java/model/Timetable.java (modified)
* AI_PROMPT_LOG.md (modified)

**AI-Generated/Edited Code Summary:**

1. **Created helper result classes:**
   - RejectionReason: Holds instance number and rejection reason
   - GenerationWarning: Holds topic code, class type, and rejection reasons
   - ValidationSummary: Holds validation counts and status
   - TimetableGenerationResult: Comprehensive result with timetable, status, warnings, missing classes, validation summary, suggestions, and statistics

2. **Fixed input handling in TimetableController:**
   - readCampusSelection(): Treats empty input and '0' as no filter (empty ArrayList), 'cancel' as cancellation (null)
   - readPreferenceSelection(): Treats empty input and '0' as no preferences (empty ArrayList), 'cancel' as cancellation (null)
   - Both use custom parsing to distinguish between no-selection and cancellation

3. **Enhanced ValidationService:**
   - Added getDetailedRejectionReason() to provide specific rejection reasons
   - Added helper methods for gap calculation and text comparison

4. **Implemented new generation algorithm in TimetableService:**
   - Created generateTimetableWithDetails() returning TimetableGenerationResult
   - Collects detailed rejection reasons for each failed class option
   - Tracks statistics: candidate records, selected records, rejections by reason
   - Determines status: SUCCESS, PARTIAL SUCCESS, or FAILED
   - Provides missing classes list and helpful suggestions
   - Sorts timetable by day order (Mon-Sun) then start time

5. **Improved timetable display:**
   - Redesigned getTimetableTableDisplay() with multi-line entry formatting
   - Full room/location names now readable without aggressive truncation
   - Better visual separation between entries

6. **Updated TimetableController display logic:**
   - Uses generateTimetableWithDetails() for comprehensive result display
   - Shows status message with color coding
   - Displays missing classes, detailed warnings with rejection reasons
   - Shows validation summary and generation details
   - Provides helpful suggestions

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Draft


## Prompt 023 - Fix Campus/Preference Input and Enhance Timetable Generation with Detailed Results

**Date:** June 10, 2026
**AI Tool Used:** GitHub Copilot
**Prompt Summary:** Fixed campus filter and preference input, enhanced timetable generation with detailed rejection reasons, improved display, and added status tracking.

**Files Created/Modified:**
- src/main/java/model/RejectionReason.java
- src/main/java/model/GenerationWarning.java
- src/main/java/model/ValidationSummary.java
- src/main/java/model/TimetableGenerationResult.java
- src/main/java/controller/TimetableController.java
- src/main/java/service/TimetableService.java
- src/main/java/service/ValidationService.java
- src/main/java/model/Timetable.java
- AI_PROMPT_LOG.md

**AI-Generated/Edited Code Summary:**
Implemented comprehensive timetable generation improvements including: helper result classes for detailed tracking, fixed campus and preference input handling, enhanced generation algorithm with rejection reason tracking, improved timetable display formatting, and comprehensive status/validation display in UI.

**Status:** Draft

## Prompt 024 - Fix Duplicate Rejection Reasons and Implement Backtracking Algorithm

**Date:** June 10, 2026
**AI Tool Used:** GitHub Copilot in VS Code
**Prompt Summary:** Fixed duplicate rejection reasons appearing multiple times in generation warnings and implemented backtracking algorithm to explore alternative class instance combinations instead of greedily accepting the first valid option.

**Original Prompt Used:**
The timetable generation output is now mostly correct, but two issues remain.

Important: Record this prompt in the Section 2 prompt engineering evidence because this is an AI-assisted code change.

Issue 1: Duplicate rejection reasons
The console output repeats identical rejection reasons several times, for example:
Instance 1 rejected: invalid campus combination...
Instance 1 rejected: invalid campus combination...

This likely happens because the same class instance appears in multiple CSV rows/date ranges.

Fix:
- Deduplicate rejection reasons before displaying them.
- Do not remove the actual internal checking, only clean the displayed warnings.
- Use a display deduplication key such as:
  topicCode + classType + classInstance + reasonType + message text
- Keep different reasons for the same instance if they are actually different.
- After the fix, each identical instance/reason message should appear only once.

Issue 2: Backtracking / optimisation still appears greedy
Observed example:
- The generator selects COMP1103 Workshop at Flinders City Campus.
- Then it cannot select COMP1103 Tutorial because tutorials are at Tonsley.
- A better generator should try alternative COMP1103 workshop instances, such as Tonsley workshop, before deciding the timetable is partial.

Fix:
- Improve the timetable generator so it searches combinations of class instances instead of permanently accepting the first valid option.
- For each topic and required class type, group candidates by class instance.
- Try alternative candidate combinations using backtracking or another simple search method.
- Prefer a complete timetable over a partial timetable.
- If multiple complete timetables exist, choose the one with the best preference score.
- If no complete timetable exists, choose the best partial timetable and show missing classes with reasons.
- Required rules must be checked before preference scoring:
  1. same-topic campus rule
  2. time clash rule, respecting lecture overlap setting
  3. travel-time rule
  4. semester and campus filter rules
- Do not generate JUnit test code.

Also make sure the normal travel-time setting remains 30 minutes for final assignment behaviour. Testing with 5 minutes is okay, but the default rule should be 30 minutes.

**Files Created/Modified:**

* src/main/java/model/GenerationWarning.java
* src/main/java/service/TimetableService.java
* src/main/java/service/ValidationService.java
* src/main/java/controller/TimetableController.java

**AI-Generated/Edited Code Summary:**
1. **GenerationWarning.java**: Added HashSet<String> deduplicationKeys to track unique rejection combinations using instance + reason text as the key. The addRejectionReason() method now only adds rejection reasons that haven't been seen before, eliminating duplicates while preserving different reasons.

2. **TimetableService.java**: Implemented backtracking algorithm with tryBacktrackForCombinationForTopic() recursive method that explores alternative combinations of class instances for each topic before giving up. Created inner class BacktrackingResult to track selection statistics. Refactored generation loop to call backtracking for each topic, tracking rejection counts by category (clash, travel time, campus rule, semester/filter).

3. **ValidationService.java**: Added getReadableCampusName() helper method to map internal campus key codes (CITY, BEDFORD, TONSLEY) to user-friendly names in rejection reasons. Updated campus mixing rule error messages to use readable campus names instead of internal enums.

4. **TimetableController.java**: Added displayCampusFilter() to show "No filter / All campuses allowed" when no campus filter is active. Added readValidTimetableName() method to validate timetable names (only letters, numbers, spaces, hyphens, underscores). Updated PARTIAL SUCCESS message to use new format. Updated suggestion method call to pass allowLectureOverlap parameter for context-aware suggestions.

**Application Improvements:**
- Duplicate rejection reasons no longer clutter generation warnings
- Algorithm now explores alternative class combinations before declaring a class impossible
- Campus filter display is clearer for users
- Timetable names are validated against unusual characters
- Context-aware suggestions (won't suggest enabling lecture overlap if already enabled)
- Travel time default remains 30 minutes as specified
- Rejection reason categorization and counting now accurate
- Validation summary properly populated with actual missing class counts

**Human Review Notes:**
[To be completed by student after reviewing the output.]

**Status:** Completed
