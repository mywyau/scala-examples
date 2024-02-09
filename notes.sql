

select name from world where gdp > 1000000000

SELECT population FROM world WHERE name = 'Germany'

SELECT name, population FROM world WHERE name IN ('Sweden', 'Norway', 'Denmark');

SELECT name, area
FROM world
WHERE area BETWEEN 200000 AND 250000

--  <> not equals, and take from LEFT()

--  The capital of Sweden is Stockholm. Both words start with the letter 'S'.
--  Show the name and the capital where the first letters of each match. Don't include countries where the name and the capital are the same word.

SELECT name, capital
FROM world
WHERE LEFT(name, 1) = LEFT(capital, 1) AND name <> capital


-- create a table in mysql

CREATE TABLE courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(50) NOT NULL,
);

CREATE TABLE trainings (
    employee_id INT;
    course_id INT;
    taken_date DATE;
    PRIMARY KEY (employee_id, course_id);
)

-- simple calculations can be made in the select query
SELECT
    first_name,
    last_name,
    salary,
    salary * 1.05
FROM
    employees;

-- Aliases


SELECT
    first_name,
    last_name,
    salary,
    (salary * 1.05) AS new_alias_salary
FROM
    employees;


























































