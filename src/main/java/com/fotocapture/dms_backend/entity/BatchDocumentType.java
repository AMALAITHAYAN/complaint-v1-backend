package com.fotocapture.dms_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "batch_document_type")
@IdClass(BatchDocumentType.PK.class)
public class BatchDocumentType {

    @Id
    @Column(name = "batch_id", nullable = false)
    private Integer batchId;

    @Id
    @Column(name = "document_type_id", nullable = false)
    private Integer documentTypeId;

    public BatchDocumentType() {}
    public BatchDocumentType(Integer batchId, Integer documentTypeId) {
        this.batchId = batchId; this.documentTypeId = documentTypeId;
    }

    public Integer getBatchId() { return batchId; }
    public void setBatchId(Integer batchId) { this.batchId = batchId; }
    public Integer getDocumentTypeId() { return documentTypeId; }
    public void setDocumentTypeId(Integer documentTypeId) { this.documentTypeId = documentTypeId; }

    public static class PK implements Serializable {
        private Integer batchId;
        private Integer documentTypeId;

        public PK() {}
        public PK(Integer batchId, Integer documentTypeId) {
            this.batchId = batchId; this.documentTypeId = documentTypeId;
        }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PK pk = (PK) o;
            return Objects.equals(batchId, pk.batchId) && Objects.equals(documentTypeId, pk.documentTypeId);
        }
        @Override public int hashCode() { return Objects.hash(batchId, documentTypeId); }

        public Integer getBatchId() { return batchId; }
        public void setBatchId(Integer batchId) { this.batchId = batchId; }
        public Integer getDocumentTypeId() { return documentTypeId; }
        public void setDocumentTypeId(Integer documentTypeId) { this.documentTypeId = documentTypeId; }
    }
}
