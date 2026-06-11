# AI Prompts Short Log

Prompt 001 - Set up the Student Timetable Optimiser as a Java console-only application. Define the required package layers (`ui`, `controller`, `service`, `model`, `io`), list the main features such as CSV import, class management, timetable generation, validation, and export, and create a rule that all AI prompts must be logged in `AI_PROMPT_LOG.md`.



Prompt 002 - Create the initial project structure under `src/main/java`. Add placeholder classes for the models, services, controllers, CSV importer, timetable exporter, console menu, and `Main.java`, with correct package declarations and a very basic application start flow.



Prompt 003 - Build the first working console navigation system. Add a main menu, class data menu, timetable management menu, export option, help/about option, input validation for menu choices, and safe exit behaviour, while keeping most features as placeholders.



Prompt 004 - Expand the placeholder console UI into a fuller flow. Add screens for importing class data, browsing and viewing class records, searching, editing, deleting, generating timetables, managing timetables, exporting, and help/about text, without implementing the real business logic yet.



Prompt 005 - Improve console screen transitions. Clear the console between major screens, add pause prompts after actions, and make navigation feel cleaner so users do not see old menu output mixed with new screens.



Prompt 006 - Add cancel and back support throughout the app. Update required input screens so users can type `0` to cancel, return to the previous menu safely, and avoid getting stuck in long input flows.



Prompt 007 - Add a Windows batch file for easier running. Create a script that compiles Java source files and launches the console app, so the project can be run without manually typing all compile commands.



Prompt 008 - Implement the core model classes. Add fields, constructors, getters, setters, and readable summary/detail methods for classes such as `ClassRecord`, `Timetable`, `TimetableEntry`, `Preference`, `Topic`, `Availability`, and `SearchCriteria`.



Prompt 009 - Implement CSV importing. Read CSV files, validate required headers, support quoted fields with commas, parse topic and availability values, parse dates/times/location, create `ClassRecord` objects, and collect row-level import warnings.



Prompt 010 - Improve console clear-screen behaviour for Windows terminals. Avoid printing broken ANSI escape text or excessive blank lines, and make the clear-screen helper safer across different terminal environments.



Prompt 011 - Implement `ClassService` for class record storage. Support importing records through `CSVImporter`, updating duplicate class records, browsing, viewing details, editing fields, deleting records, searching by basic fields, and tracking import counts and errors.



Prompt 012 - Connect `ClassService` to the Class Data Management UI. Replace placeholder messages with real import, browse, view, edit, delete, and search preparation logic while preserving validation, cancel handling, and user-friendly messages.



Prompt 013 - Improve CSV selection so users do not need to type full file paths. List available CSV files from the `CSVs` folder, allow selecting by number, allow manual absolute/relative paths, and support filename shortcuts.



Prompt 014 - Add application configuration using `app-config.properties`. Store the CSV folder path, travel time minutes, and colour setting; create defaults automatically; show configuration in the menu; and make CSV import use the configured folder.



Prompt 015 - Add safe ANSI colour support. Create console colour helper methods, detect whether ANSI is supported, apply colours to menu options and messages, and allow colour output to be toggled from the configuration menu.



Prompt 016 - Fix remaining clear-screen and colour display issues. Remove fallback behaviour that creates huge empty gaps, avoid raw ANSI escape code text in unsupported terminals, and keep the UI readable with or without colours.



Prompt 017 - Implement `SearchService` and connect the search UI. Allow users to search imported class records by topic, campus, semester, class type, day, or advanced manual criteria, then view matching record details.



Prompt 018 - Implement timetable validation rules. Add checks for time clashes, lecture overlap rules, travel time between campuses, same-day class movement, and readable validation warnings that can be reused by timetable generation and editing.



Prompt 019 - Implement the first version of `TimetableService`. Generate a timetable from selected topics, semester, campus filter, lecture overlap setting, preferences, and imported class records; store generated timetables in memory; and provide browse/view/delete helpers.



Prompt 020 - Connect timetable generation and management to the console UI. Let users generate timetables from the menu, choose semester/topics/campuses/preferences, confirm settings, browse saved timetables, view details, and delete timetables.



Prompt 021 - Replace manual typing with numbered selections across the app. Use numbered menus for topics, campuses, semesters, preferences, class edit fields, search values, and timetable choices while keeping custom input where useful.



Prompt 022 - Allow multiple CSV import selection. Update the CSV import screen so users can enter comma-separated file numbers, import multiple CSVs in order, aggregate import counts and warnings, and still support manual file paths.



Prompt 023 - Implement timetable edit/swap. Let users select a generated timetable, choose one entry, view replacement class instances for the same topic and class type, validate the swap, and optionally save a swap with warnings.



Prompt 024 - Improve timetable generation result output. Add result objects for success, partial success, and failure; show missing classes, validation summary, generation warnings, rejection reasons, suggestions, and generation statistics.



Prompt 025 - Fix duplicate rejection reasons and improve backtracking. Deduplicate repeated warning messages caused by duplicate CSV date ranges, try alternative class combinations before giving up, prefer complete timetables over partial ones, and keep rejection reasons readable.



Prompt 026 - Analyse whether timetable export is actually working. Inspect `TimetableExporter` and the export menu, identify that export is only a placeholder, and explain that no CSV file is being written yet.



Prompt 027 - Make timetable export work. Implement CSV writing in `TimetableExporter`, include timetable/class fields in the export, escape CSV values properly, create output folders if needed, connect the export menu, and rebuild the jar.



Prompt 028 - Analyse generated timetable output and the CSV files to fix false partial results. Discover that some topics require mixed campuses, remove the incorrect same-topic campus restriction, and keep real clash/travel-time validation.



Prompt 029 - Change export behaviour to use a root `exports` folder. Stop asking users to type a full export path, automatically create `exports/<timetable name>.csv`, sanitise unsafe filename characters, and show the generated path before confirming.



Prompt 030 - Ensure all required class types are included for each selected course. Treat lectures, tutorials, practicals, workshops, labs, and similar class types as required where they exist in the CSV data, and report missing types clearly if they cannot fit.



Prompt 031 - Improve timetable generation with a global search. Search across all required class types for all selected courses together, instead of finalising one topic before checking the next, so earlier choices can be changed to avoid later clashes.



Prompt 032 - Explain how users can confirm that all classes are included. Point to `SUCCESS`, `Missing required classes: 0`, `Status: Complete timetable`, and recommend adding a required-class checklist for easier visual confirmation.



Prompt 033 - Review whether all menu options and preferences work. Check class management, timetable management, export, configuration, and preference handling; identify that some preferences work while same-campus, spread, and compact options need real optimisation logic.



Prompt 034 - Implement real preference optimisation. Fix preference priority so earlier selections have stronger weight, score complete timetables, implement same-campus preference, evenly-spread days, compact-days scoring, and show optimisation score and summary.



Prompt 035 - Rebuild the runnable jar after the app locked the old jar file. Detect that Java was still using `StudentTimetableOptimiser.jar`, retry the build after the app was closed, and confirm the updated jar was created successfully.



Prompt 036 - Improve the overall console UI style. Add boxed section headers, aligned menu options, clearer prompts, status labels, cleaner help text, and remove corrupted divider symbols from timetable display output.



Prompt 037 - Apply UI ideas from an article about ANSI escape codes. Use ANSI constants for bold, italic, underline, bright colours, combined text styles, and add a clean ASCII-art startup banner while keeping colour output optional.



Prompt 038 - Create a short prompt evidence file. Refer to the long `AI_PROMPT_LOG.md` and this ChatGPT conversation, then create `AI_PROMPTS_SHORT.md` with brief but useful prompt summaries separated by two empty lines.
