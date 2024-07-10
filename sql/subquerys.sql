-- subqueries are nested queries

SELECT
    *
FROM
    departments
WHERE
    location_id = 1700;

-- Second, find all employees that belong to the location 1700 by using the department id list of the previous query:

SELECT
    employee_id, first_name, last_name
FROM
    employees
WHERE
    department_id IN (1 , 3, 8, 10, 11)
ORDER BY first_name , last_name;

--   This solution has two problems. To start with, you have looked at the departments table to check which department belongs to the location 1700.
--   However, the original question was not referring to any specific departments; it referred to the location 1700.
--   Because of the small data volume, you can get a list of department easily. However, in the real system with high volume data, it might be problematic.
--   Another problem was that you have to revise the queries whenever you want to find employees who locate in a different location.
--   A much better solution to this problem is to use a subquery.
--   By definition, a subquery is a query nested inside another query such as SELECT, INSERT, UPDATE, or DELETE statement. In this tutorial, we are focusing on the subquery used with the SELECT statement.
--   In this example, you can rewrite combine the two queries above as follows:

select employees, first_name, last_name  -- outer query
from employees
where department_id IN (
                        SELECT department_id   -- inner query
                        FROM departments
                        WHERE location_id = 1700
                      );
ORDER BY first_name , last_name;

-- this is known as inner query or inner select, the query which contains the subquery is known as the outer query

--  You can use a subquery in many places such as:
--
--  With the IN or NOT IN operator
--  With comparison operators
--  With the EXISTS or NOT EXISTS operator
--  With the ANY or ALL operator
--  In the FROM clause
--  In the SELECT clause

