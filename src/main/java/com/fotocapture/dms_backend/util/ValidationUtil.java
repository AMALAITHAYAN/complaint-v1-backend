package com.fotocapture.dms_backend.util;

import com.fotocapture.dms_backend.dto.IndexingFieldDTO;
import com.fotocapture.dms_backend.exception.BadRequestException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValidationUtil {
    private ValidationUtil() {}

    private static final Set<String> ALLOWED_TYPES =
            new HashSet<>(Arrays.asList("text","number","date","options","boolean"));

    private static final Set<String> BASE_TOKENS =
            new HashSet<>(Arrays.asList("Department","DocType","YYYY","MM","DD","RandomId"));

    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\[([A-Za-z0-9_]+)]");

    public static void validateIndexingFields(List<IndexingFieldDTO> fields) {
        if (fields == null || fields.isEmpty()) return; // allowed, UI might add later
        Set<String> names = new HashSet<>();
        for (IndexingFieldDTO f : fields) {
            if (f.getName() == null || f.getName().trim().isEmpty())
                throw new BadRequestException("Indexing field 'name' is required");

            if (!names.add(f.getName()))
                throw new BadRequestException("Duplicate indexing field name: " + f.getName());

            if (f.getType() == null || !ALLOWED_TYPES.contains(f.getType()))
                throw new BadRequestException("Unsupported field type: " + f.getType());

            if ("options".equals(f.getType())) {
                if (f.getOptions() == null || f.getOptions().isEmpty())
                    throw new BadRequestException("Field '" + f.getName() + "': options list required");
            }
        }
    }

    public static void validateTemplates(String folderTemplate,
                                         String fileTemplate,
                                         List<IndexingFieldDTO> fields) {
        Set<String> allowed = new HashSet<>(BASE_TOKENS);
        if (fields != null) {
            for (IndexingFieldDTO f : fields) {
                allowed.add(f.getName());
            }
        }
        validateTemplate("folderTemplate", folderTemplate, allowed);
        validateTemplate("fileTemplate", fileTemplate, allowed);
    }

    private static void validateTemplate(String label, String template, Set<String> allowed) {
        if (template == null || template.trim().isEmpty())
            throw new BadRequestException(label + " is required");

        Matcher m = TOKEN_PATTERN.matcher(template);
        while (m.find()) {
            String token = m.group(1);
            if (!allowed.contains(token)) {
                throw new BadRequestException(
                        label + " contains unknown token [" + token + "]. Allowed tokens: " + allowed);
            }
        }
    }

    public static List<IndexingFieldDTO> filterVisible(List<IndexingFieldDTO> fields) {
        if (fields == null) return Collections.emptyList();
        List<IndexingFieldDTO> out = new ArrayList<>();
        for (IndexingFieldDTO f : fields) {
            if (f.isVisible()) out.add(f);
        }
        return out;
    }
}
