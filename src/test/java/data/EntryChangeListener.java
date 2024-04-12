package data;

import java.awt.*;

public interface EntryChangeListener {
    public void entryChanged(String[] colNames, Object[] values, boolean isNewValue, String pkName, Object pkValue, Frame instance);
}
