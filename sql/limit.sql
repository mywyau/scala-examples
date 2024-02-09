
-- LIMIT

-- to limit the number of rows returned by a select statement, you can use the LIMIT and OFFSET clauses.

select column_list
from table1
order by column_list
LIMIT row_count OFFSET offset;

--In this syntax:
--The LIMIT row_count determines the number of rows (row_count) returned by the query.
--The OFFSET offset clause skips the offset rows before beginning to return the rows.

--The following example uses the LIMIT clause to return the first 5 rows in the result set returned by the SELECT clause:

select employee_id, first_name, last_name
from employees
order by first_name
limit 5;

-- now we can combine offset to limit from later/deeper within the table

select employee_id, first_name, last_name
from employees
order by first_name
limit 5 offset 3;   -- this would only return 5 rows but starting from the 4th row in the table since we have offset the search query by 3

-- In MySQL, you can use the shorter form of the LIMIT & OFFSET clauses like this:

select employee_id, first_name, last_name
from employees
order by first_name
limit 3, 5;

--You can use the LIMIT clause to get the top N rows with the highest or lowest value. For example, the following statement gets the top five employees with the highest salaries.
-- if we order first we can then limit the first 5, alternating between desc or asc will give us highest or lowest salaries
SELECT
    employee_id,
    first_name,
    last_name,
    salary
FROM
    employees
ORDER BY
	salary DESC
LIMIT 5;

--Getting the rows with the Nth highest value
--Suppose you have to get employees who have the 2nd highest salary in the company. To do so, you use the LIMIT OFFSET clauses as follows.
-- in this case we limit to only 1 row but offset by 1, this will return the second value within the table. Ordering takes care of the highest/lowest salary
-- however there is potential problem
SELECT
    employee_id,
    first_name,
    last_name,
    salary
FROM
    employees
ORDER BY
	salary DESC
LIMIT 1 OFFSET 1;

-- This query works with the assumption that every employee has a different salary. It will fail if there are two employees who have the same highest salary.
-- Also, if you have two or more employees who have the same 2nd highest salary, the query just returns the first one.
-- To fix this issue, you can get the second highest salary first using the following statement.

SELECT DISTINCT
    salary
FROM
    employees
ORDER BY salary DESC
LIMIT 1 , 1;

-- And pass the result to another query:

SELECT
    employee_id, first_name, last_name, salary
FROM
    employees
WHERE
    salary = 17000;

-- we can even do a combination of select in select from sql zoo  - this is known as subquerying

SELECT
    employee_id, first_name, last_name, salary
FROM
    employees
WHERE
    salary = (SELECT DISTINCT
                 salary
             FROM
                 employees
             ORDER BY salary DESC
             LIMIT 1 , 1;);  -- this would be the same as setting the value to the second highest distinct salary since the nested select will pull out the 17,000/2nd row

























































