-- User, Schedule 테이블 생성
CREATE TABLE IF NOT EXISTS User (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    create_at DATETIME NOT NULL,
    modify_at DATETIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS Schedule (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    create_at DATETIME NOT NULL,
    modify_at DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id)
    );

-- User 테이블이 비어있을 때만 INSERT
INSERT INTO User (name, email, password, create_at, modify_at)
SELECT * FROM (
                  SELECT '홍길동' AS name, 'hong@example.com' AS email, 'password123' AS password, NOW() AS create_at, NOW() AS modify_at UNION ALL
                  SELECT '김영희', 'kim@example.com', 'securepass456', NOW(), NOW() UNION ALL
                  SELECT '이철수', 'lee@example.com', 'mypassword789', NOW(), NOW()
              ) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM User);

-- Schedule 테이블이 비어있을 때만 INSERT
INSERT INTO Schedule (user_id, title, content, create_at, modify_at)
SELECT * FROM (
                  SELECT 1 AS user_id, '회의 준비' AS title, '회의자료를 작성하고 공유합니다.' AS content, NOW() AS create_at, NOW() AS modify_at UNION ALL
                  SELECT 1, '운동', '헬스장에서 운동하기', NOW(), NOW() UNION ALL
                  SELECT 2, '스터디 모임', 'Java 공부 스터디', NOW(), NOW()
              ) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM Schedule);


