package com.fotocapture.dms_backend.dto;

import java.util.List;

public class BatchDocTypesResponse {
    public static class DocTypeBrief {
        private Integer id;
        private String name;
        public DocTypeBrief() {}
        public DocTypeBrief(Integer id, String name) { this.id = id; this.name = name; }
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    private List<DocTypeBrief> available;
    private List<Integer> selected;

    public BatchDocTypesResponse() {}

    public List<DocTypeBrief> getAvailable() { return available; }
    public void setAvailable(List<DocTypeBrief> available) { this.available = available; }
    public List<Integer> getSelected() { return selected; }
    public void setSelected(List<Integer> selected) { this.selected = selected; }
}
