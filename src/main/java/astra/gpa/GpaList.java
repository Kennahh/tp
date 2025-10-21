package astra.gpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import astra.exception.GpaInputException;

public class GpaList {
    private final List<GpaEntry> entries = new ArrayList<>();

    public void add(GpaEntry e) {
        entries.add(e);
    }

    public GpaEntry remove(int indexOneBased) {
        if (indexOneBased <= 0 || indexOneBased > entries.size()) {
            throw new GpaInputException("Invalid GPA entry index: " + indexOneBased);
        }
        return entries.remove(indexOneBased - 1);
    }

    public List<GpaEntry> toList() {
        return Collections.unmodifiableList(entries);
    }

    public int size() {
        return entries.size();
    }

    public void clear() {
        entries.clear();
    }

    public double computeGpa() {
        double num = 0.0;
        int denom = 0;
        for (GpaEntry e : entries) {
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
            return 0.0;
        }
        return num / denom;
    }
}
