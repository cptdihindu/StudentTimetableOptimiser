# Student Timetable Optimiser

A Java console application for importing university class CSV files, managing class records, generating clash-free student timetables, and exporting completed timetables as CSV files.

The application is designed as a console-only project. It uses numbered menus, readable validation messages, ANSI colour styling where supported, and a small loading spinner for longer tasks.

## Features

- Import one or more class CSV files from a configured folder or a custom path.
- Browse, search, view, edit, and delete imported class records.
- Generate complete timetables for selected semesters and topics.
- Include all required class types for each selected course, such as lectures, tutorials, workshops, laboratories, and practicals.
- Check time clashes, lecture-overlap rules, and travel-time issues.
- Score timetable options using user preferences such as preferred campus, mornings, afternoons, days, same-campus preference, spread schedules, and compact schedules.
- Browse, view, edit by swapping class instances, and delete generated timetables.
- Export timetables automatically to the root `exports` folder using the timetable name as the CSV file name.
- Configure CSV folder path, travel time minutes, and colour output from inside the app.

## Requirements

- Java Development Kit (JDK) 8 or newer.
- Windows Terminal, PowerShell, or Command Prompt.

Windows Terminal or modern PowerShell is recommended for the best colour display.

## Project Structure

```text
StudentTimetableOptimiser/
|-- CSVs/                         Sample/importable class CSV files
|-- exports/                      Generated timetable CSV exports
|-- src/main/java/
|   |-- controller/               Console screen controllers
|   |-- io/                       CSV import and timetable export logic
|   |-- model/                    Data models
|   |-- service/                  Business logic and validation
|   |-- ui/                       Console UI helpers
|   `-- Main.java                 Application entry point
|-- app-config.properties         Runtime configuration
|-- compile.bat                   Build script for Windows
|-- AI_PROMPTS_SHORT.md           Short AI prompt evidence log
`-- README.md
```

## Build

Open a terminal in the project root and run:

```powershell
.\compile.bat
```

The script compiles all Java source files into `build/classes` and creates:

```text
StudentTimetableOptimiser.jar
```

If the build cannot replace the JAR, close any running instance of the app and run `.\compile.bat` again.

## Run

After building, run:

```powershell
java -jar StudentTimetableOptimiser.jar
```

The app starts in the main menu. A typical workflow is:

1. Go to `Class Data Management`.
2. Import CSV files from the configured folder.
3. Go to `Timetable Generation`.
4. Select semester, topics, campus filter, lecture overlap rule, and preferences.
5. Review the generated timetable and validation summary.
6. Export the timetable from `Export Timetable`.

## CSV Input

CSV files are imported from the folder configured in `app-config.properties`.

Default:

```properties
csv.folder.path=CSVs
```

The app can import:

- One CSV file by number.
- Multiple CSV files using comma-separated numbers.
- All available CSV files using `all`.
- A custom absolute or relative CSV path.

Relative paths are resolved from the folder where the app is launched.

## Configuration

The app uses `app-config.properties` in the project root. If the file is missing, it is created automatically.

Main settings:

```properties
csv.folder.path=CSVs
travel.time.minutes=5
colors.enabled=true
```

These settings can also be changed from:

```text
Main Menu -> Configuration
```

## Timetable Generation

The generator attempts to build a complete timetable using one valid class instance for each required class type in every selected course.

A timetable is considered complete when:

- Time clashes are `0`.
- Travel-time issues are `0`.
- Missing required classes are `0`.
- The validation status says `Complete timetable`.

If a complete timetable cannot be built, the app reports partial success or failure with missing class types, friendly rejection reasons, and suggestions.

## Export

Timetables are exported to:

```text
exports/<timetable-name>.csv
```

The app creates the `exports` folder automatically if needed. Unsafe filename characters are cleaned before the CSV is written.

## Console UI

The app uses a styled Java console interface:

- ASCII title banners.
- Coloured status badges.
- Coloured section headers.
- Numbered menus.
- Clear prompts and cancellation support.
- Loading spinner for longer tasks.

If colour output is hard to read or unsupported, disable it from:

```text
Main Menu -> Configuration -> Toggle colour output
```

## Notes

- This is an in-memory console app. Generated timetables exist while the app is running and can be exported to CSV.
- The project intentionally avoids Maven or Gradle to keep the build simple for coursework-style submission.
- `AI_PROMPTS_SHORT.md` provides a compact record of the AI prompts used during development.
