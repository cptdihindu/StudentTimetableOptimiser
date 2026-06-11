package service;

import model.ClassRecord;
import model.SearchCriteria;

import java.time.LocalTime;
import java.util.ArrayList;

public class SearchService {
    public ArrayList<ClassRecord> search(ArrayList<ClassRecord> records, SearchCriteria criteria) {
        ArrayList<ClassRecord> results = new ArrayList<>();
        if (records == null) {
            return results;
        }
        if (criteria == null || criteria.isEmpty()) {
            return new ArrayList<>(records);
        }

        for (ClassRecord record : records) {
            if (record == null) {
                continue;
            }
            if (matchesAllCriteria(record, criteria)) {
                results.add(record);
            }
        }

        return results;
    }

    private boolean matchesAllCriteria(ClassRecord record, SearchCriteria criteria) {
        return matches(record.getTopicCode(), criteria.getTopicCode())
                && matches(record.getTopicName(), criteria.getTopicName())
                && matches(record.getAttendanceMode(), criteria.getAttendanceMode())
                && matches(record.getCampus(), criteria.getCampus())
                && matches(record.getSemester(), criteria.getSemester())
                && matches(record.getAvailabilityNumber(), criteria.getAvailabilityNumber())
                && matches(record.getClassType(), criteria.getClassType())
                && matches(record.getClassInstance(), criteria.getClassInstance())
                && matches(record.getFirstClassDate(), criteria.getFirstClassDate())
                && matches(record.getLastClassDate(), criteria.getLastClassDate())
                && matches(record.getDay(), criteria.getDay())
                && matchesTime(record.getStartTime(), criteria.getStartTime())
                && matchesTime(record.getEndTime(), criteria.getEndTime())
                && matches(record.getBuilding(), criteria.getBuilding())
                && matches(record.getRoom(), criteria.getRoom());
    }

    private boolean matches(String recordValue, String searchValue) {
        if (isBlank(searchValue)) {
            return true;
        }
        if (recordValue == null) {
            return false;
        }
        String recordText = recordValue.trim().toLowerCase();
        String searchText = searchValue.trim().toLowerCase();
        return recordText.contains(searchText);
    }

    private boolean matchesTime(LocalTime time, String searchValue) {
        if (isBlank(searchValue)) {
            return true;
        }
        if (time == null) {
            return false;
        }
        return matches(time.toString(), searchValue);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

