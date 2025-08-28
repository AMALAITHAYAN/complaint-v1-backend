package com.fotocapture.dms_backend.util;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TemplateResolver {
    private TemplateResolver() {}

    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\[([A-Za-z0-9_]+)]");

    public static String resolve(String template, Map<String, Object> metadata) {
        if (template == null) return "";
        LocalDate today = LocalDate.now();

        return replaceTokens(template, token -> {
            switch (token) {
                case "YYYY": return String.valueOf(today.getYear());
                case "MM": return pad2(today.getMonthValue());
                case "DD": return pad2(today.getDayOfMonth());
                case "RandomId": return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
                default:
                    Object v = metadata != null ? metadata.get(token) : null;
                    return v == null ? "" : String.valueOf(v);
            }
        });
    }

    private static String replaceTokens(String template, TokenSupplier supplier) {
        Matcher m = TOKEN_PATTERN.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String token = m.group(1);
            String replacement = supplier.valueFor(token);
            m.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String pad2(int n) { return n < 10 ? "0"+n : String.valueOf(n); }

    private interface TokenSupplier {
        String valueFor(String token);
    }
}
