
-- retrieve data
SELECT * FROM emp_basic;


-- Insert data
INSERT INTO emp_basic VALUES
  ('simon', 'romero', 'simoncito@email.com', '123 street abc, apt 2', 'cali', '2024-11-05'),
  ('tobias', 'romero', 'gordi@email.com', '456 street aws, apt 5', 'cali', '2012-10-05');

-- Filter rows
SELECT email FROM emp_basic WHERE email LIKE '%email.com';
