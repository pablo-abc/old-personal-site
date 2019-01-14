CREATE TABLE IF NOT EXISTS author (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS blog_post (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  introduction VARCHAR(300) NOT NULL,
  content TEXT NOT NULL
);

INSERT INTO blog_post (title, introduction, content)
VALUES
  ('Another test post',
  'This is a test post and its introduction',
  'This is the content. It is very long.
And it has a return.
In order to test split.');
