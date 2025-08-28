package com.fotocapture.dms_backend.service;

import com.fotocapture.dms_backend.dto.*;

public interface BatchService {
    BatchResponse create(BatchCreateUpdateRequest req);
    BatchResponse get(Integer id);
    PageResponse<BatchListItem> list(String q, int page, int size);
    BatchResponse update(Integer id, BatchCreateUpdateRequest req);
    void delete(Integer id);
    BatchDocTypesResponse docTypesForBatch(Integer batchId);
}
