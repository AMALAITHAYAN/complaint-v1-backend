package com.fotocapture.dms_backend.dto;

import java.util.List;

public class IndexingFieldDTO {
    private String name;         // e.g., "passportNumber"   (token-safe key)
    private String displayName;  // e.g., "Passport #"
    private String type;         // text | number | date | options | boolean
    private boolean required;
    private boolean visible = true;
    private String defaultValue; // may be null
    private List<String> options; // only for type=options
    private boolean unique;
    private String regex;        // optional validation pattern
    private Integer min;         // optional for length/range
    private Integer max;

    public IndexingFieldDTO() {}

    // Getters/Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public boolean isUnique() { return unique; }
    public void setUnique(boolean unique) { this.unique = unique; }

    public String getRegex() { return regex; }
    public void setRegex(String regex) { this.regex = regex; }

    public Integer getMin() { return min; }
    public void setMin(Integer min) { this.min = min; }

    public Integer getMax() { return max; }
    public void setMax(Integer max) { this.max = max; }
}
