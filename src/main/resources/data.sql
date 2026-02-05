INSERT INTO member (name, created_at, updated_at) VALUES ('test1', NOW(), NOW());
INSERT INTO member (name, created_at, updated_at) VALUES ('테스터1', NOW(), NOW());

INSERT INTO post (title, content, member_id, view_count, created_at, updated_at) VALUES ('첫 번째 게시글', '안녕하세요. 첫 번째 게시글입니다.', 1, 0, NOW(), NOW());
INSERT INTO post (title, content, member_id, view_count, created_at, updated_at) VALUES ('두 번째 게시글', 'Spring Boot 프로젝트 예제입니다.', 2, 10, NOW(), NOW());
