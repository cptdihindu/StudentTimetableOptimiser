package service;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class AppConfigService {
    private static final String CSV_FOLDER_KEY = "csv.folder.path";
    private static final String TRAVEL_TIME_KEY = "travel.time.minutes";
    private static final String COLORS_ENABLED_KEY = "colors.enabled";
    private static final String DEFAULT_CSV_FOLDER = "CSVs";
    private static final int DEFAULT_TRAVEL_TIME = 30;
    private static final boolean DEFAULT_COLORS_ENABLED = true;

    private final String CONFIG_FILE_NAME = "app-config.properties";
    private Properties properties;

    public AppConfigService() {
        this.properties = new Properties();
        loadOrCreateConfig();
    }

    public void loadOrCreateConfig() {
        Path configPath = Paths.get(CONFIG_FILE_NAME);
        boolean shouldSave = false;

        if (Files.exists(configPath)) {
            try (InputStream inputStream = Files.newInputStream(configPath)) {
                properties.load(inputStream);
            } catch (Exception ex) {
                properties.clear();
                properties.setProperty(CSV_FOLDER_KEY, DEFAULT_CSV_FOLDER);
                properties.setProperty(TRAVEL_TIME_KEY, String.valueOf(DEFAULT_TRAVEL_TIME));
                properties.setProperty(COLORS_ENABLED_KEY, String.valueOf(DEFAULT_COLORS_ENABLED));
                saveConfig();
                return;
            }
        } else {
            properties.setProperty(CSV_FOLDER_KEY, DEFAULT_CSV_FOLDER);
            properties.setProperty(TRAVEL_TIME_KEY, String.valueOf(DEFAULT_TRAVEL_TIME));
            properties.setProperty(COLORS_ENABLED_KEY, String.valueOf(DEFAULT_COLORS_ENABLED));
            saveConfig();
            return;
        }

        String csvFolder = properties.getProperty(CSV_FOLDER_KEY);
        if (isBlank(csvFolder)) {
            properties.setProperty(CSV_FOLDER_KEY, DEFAULT_CSV_FOLDER);
            shouldSave = true;
        }

        String travelTime = properties.getProperty(TRAVEL_TIME_KEY);
        if (isBlank(travelTime)) {
            properties.setProperty(TRAVEL_TIME_KEY, String.valueOf(DEFAULT_TRAVEL_TIME));
            shouldSave = true;
        }

        String colorsEnabled = properties.getProperty(COLORS_ENABLED_KEY);
        if (isBlank(colorsEnabled) || !isBooleanValue(colorsEnabled)) {
            properties.setProperty(COLORS_ENABLED_KEY, String.valueOf(DEFAULT_COLORS_ENABLED));
            shouldSave = true;
        }

        if (shouldSave) {
            saveConfig();
        }
    }

    public String getCsvFolderPath() {
        String value = properties.getProperty(CSV_FOLDER_KEY);
        if (isBlank(value)) {
            return DEFAULT_CSV_FOLDER;
        }
        return value.trim();
    }

    public boolean setCsvFolderPath(String path) {
        if (isBlank(path)) {
            return false;
        }

        String trimmed = path.trim();
        Path resolved = resolvePath(trimmed);
        if (resolved == null || !Files.exists(resolved) || !Files.isDirectory(resolved)) {
            return false;
        }

        properties.setProperty(CSV_FOLDER_KEY, trimmed);
        saveConfig();
        return true;
    }

    public Path resolvePath(String pathText) {
        if (isBlank(pathText)) {
            return null;
        }

        try {
            Path path = Paths.get(pathText.trim());
            if (path.isAbsolute()) {
                return path.normalize();
            }

            Path currentFolder = Paths.get("").toAbsolutePath();
            return currentFolder.resolve(path).normalize();
        } catch (InvalidPathException ex) {
            return null;
        }
    }

    public Path getResolvedCsvFolderPath() {
        Path resolved = resolvePath(getCsvFolderPath());
        if (resolved == null) {
            return resolvePath(DEFAULT_CSV_FOLDER);
        }
        return resolved;
    }

    public int getTravelTimeMinutes() {
        String value = properties.getProperty(TRAVEL_TIME_KEY);
        if (isBlank(value)) {
            return DEFAULT_TRAVEL_TIME;
        }

        try {
            int minutes = Integer.parseInt(value.trim());
            if (minutes > 0) {
                return minutes;
            }
        } catch (NumberFormatException ex) {
            return DEFAULT_TRAVEL_TIME;
        }

        return DEFAULT_TRAVEL_TIME;
    }

    public boolean isColorsEnabled() {
        String value = properties.getProperty(COLORS_ENABLED_KEY);
        if (isBlank(value)) {
            return DEFAULT_COLORS_ENABLED;
        }

        String trimmed = value.trim().toLowerCase();
        if (trimmed.equals("true")) {
            return true;
        }
        if (trimmed.equals("false")) {
            return false;
        }

        return DEFAULT_COLORS_ENABLED;
    }

    public boolean setColorsEnabled(boolean enabled) {
        properties.setProperty(COLORS_ENABLED_KEY, String.valueOf(enabled));
        saveConfig();
        return true;
    }

    public boolean setTravelTimeMinutes(int minutes) {
        if (minutes <= 0) {
            return false;
        }

        properties.setProperty(TRAVEL_TIME_KEY, String.valueOf(minutes));
        saveConfig();
        return true;
    }

    public String getConfigSummary() {
        StringBuilder builder = new StringBuilder();
        builder.append("Configuration:");
        builder.append("\n- CSV folder path: ").append(getCsvFolderPath());
        builder.append("\n- Resolved CSV folder path: ").append(getResolvedCsvFolderPath());
        builder.append("\n- Travel time minutes: ").append(getTravelTimeMinutes());
        builder.append("\n- Colour output enabled: ")
            .append(isColorsEnabled() ? "Yes" : "No");
        return builder.toString();
    }

    private void saveConfig() {
        Path configPath = Paths.get(CONFIG_FILE_NAME);
        try (OutputStream outputStream = Files.newOutputStream(configPath)) {
            properties.store(outputStream, "Student Timetable Optimiser Configuration");
        } catch (Exception ex) {
            // Keep startup resilient if config saving fails.
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isBooleanValue(String value) {
        if (value == null) {
            return false;
        }
        String trimmed = value.trim().toLowerCase();
        return trimmed.equals("true") || trimmed.equals("false");
    }
}
