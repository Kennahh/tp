package astra.gpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import astra.exception.GpaInputException;

public class GpaList {
    private final List<GpaEntry> entries = new ArrayList<>();

    public void add(GpaEntry e) {
        assert e != null : "GpaEntry to add must not be null";
        entries.add(e);
        assert entries.size() > 0 : "Entries size should increase after add";
    }

    public GpaEntry remove(int indexOneBased) {
        assert indexOneBased > 0 : "Index must be 1-based positive";
        if (indexOneBased <= 0 || indexOneBased > entries.size()) {
            throw new GpaInputException("Invalid GPA entry index: " + indexOneBased);
        }
        GpaEntry removed = entries.remove(indexOneBased - 1);
        assert removed != null : "Removed GPA entry should not be null";
        return removed;
    }

    public List<GpaEntry> toList() {
        assert entries != null : "Internal entries list should be initialized";
        return Collections.unmodifiableList(entries);
    }

    public int size() {
        return entries.size();
    }

    public void clear() {
        entries.clear();
        assert entries.isEmpty() : "Entries should be empty after clear";
    }

    public double computeGpa() {
        double num = 0.0;
        int denom = 0;
        for (GpaEntry e : entries) {
            assert e != null : "GPA entry in list should not be null";
            assert e.getMc() >= 0 : "MC should be non-negative";
            if (e.isSu()) {
                continue;
            }
            double gp = e.gradePoints();
            if (Double.isNaN(gp)) {
                continue;
            }
            num += gp * e.getMc();
            denom += e.getMc();
        }
        if (denom == 0) {
            assert num == 0.0 : "Numerator should be zero if denominator is zero";
            return 0.0;
        }
        double result = num / denom;
        assert result >= 0.0 && result <= 5.0 : "Computed GPA should be between 0.0 and 5.0";
        return result;
    }
}
