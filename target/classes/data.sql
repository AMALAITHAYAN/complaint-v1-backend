-- === Seed Departments ===
INSERT INTO department(id, name) VALUES
(1, 'HR'),
(2, 'Finance'),
(3, 'IT')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- === Seed User Presence ===
INSERT INTO user_presence(user_id, last_seen, status) VALUES
(101, NOW(), 'ACTIVE'),
(102, NOW() - INTERVAL 30 MINUTE, 'INACTIVE'),
(103, NOW() - INTERVAL 2 DAY, 'OFFLINE')
ON DUPLICATE KEY UPDATE last_seen = VALUES(last_seen), status = VALUES(status);

-- === Seed Processing Events ===
-- SCAN
INSERT INTO processing_event(occurred_at, stage, action, department_id, user_id, count_value, meta) VALUES
(NOW(), 'SCAN', 'START', 1, 101, 10, NULL),
(NOW(), 'SCAN', 'DONE', 1, 101, 9, NULL),
(NOW(), 'SCAN', 'FAIL', 1, 101, 1, JSON_OBJECT('errorCode','SCANNER_JAM'));

-- INDEX
INSERT INTO processing_event(occurred_at, stage, action, department_id, user_id, count_value, meta) VALUES
(NOW(), 'INDEX', 'DONE', 2, 102, 20, NULL);

-- QA
INSERT INTO processing_event(occurred_at, stage, action, department_id, user_id, count_value, meta) VALUES
(NOW(), 'QA', 'DONE', 3, 103, 15, NULL);

-- EXPORT
INSERT INTO processing_event(occurred_at, stage, action, department_id, user_id, count_value, meta) VALUES
(NOW(), 'EXPORT', 'DONE', 1, 101, 12, NULL),
(NOW(), 'EXPORT_FAIL', 'FAIL', 1, 101, 2, JSON_OBJECT('errorCode','S3_TIMEOUT'));
