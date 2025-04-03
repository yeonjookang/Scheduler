-- User 테이블이 비어있을 때만 INSERT
INSERT INTO User (email, password, name, create_at, modify_at)
SELECT * FROM (
                  SELECT 'hong@example.com' AS email, 'password123' AS password, '홍길동' AS name, NOW() AS create_at, NOW() AS modify_at UNION ALL
                  SELECT 'kim@example.com', 'securepass456', '김길동',NOW(), NOW() UNION ALL
                  SELECT 'lee@example.com', 'mypassword789', '이길동',NOW(), NOW()
              ) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM User);

-- Schedule 테이블이 비어있을 때만 INSERT
INSERT INTO Schedule (user_id, title, content, create_at, modify_at)
SELECT * FROM (
                  SELECT 1 AS user_id, '회의 준비' AS title, '회의자료를 작성하고 공유합니다.' AS content, NOW() AS create_at, NOW() AS modify_at UNION ALL
                  SELECT 1,  '운동', '헬스장에서 운동하기', NOW(), NOW() UNION ALL
                  SELECT 2,'스터디 모임', 'Java 공부 스터디', NOW(), NOW()
              ) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM Schedule);


