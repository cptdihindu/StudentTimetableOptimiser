# Student Timetable Optimiser

Console-based Java application for managing class data and generating student timetables.

## Getting Started

This project uses a simple folder structure without build tools.

## Run (manual)

Compile and run using the JDK from the project root.

## How to Run the Application on Windows

Option 1:
Double-click start.bat.

Option 2:
Open Command Prompt or Windows Terminal in the project folder and run:

start.bat

The batch file will compile the Java files into the out folder and then start the console application.

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

## Console Colours

The application uses ANSI colours when the terminal supports them.
For best results on Windows, run the app in Windows Terminal or PowerShell.
If strange symbols appear, disable colours from:
Main Menu -> Configuration -> Toggle colour output

## Numbered Selections

The app uses numbered selection menus where possible (topics, campuses, semesters, and more)
to reduce typing errors. You can still enter custom values when required.
