
-- The ORDERBY clause is an optional clause of the select statement ASC or DESC

select select_list
from table_name
order by sort_expression [ASC|DESC]

select select_table
from table_name
order by
    sort-expression_1 [ASC| DESC];
    sort-expression_2 [ASC| DESC];

-- Note that if you don’t specify the ORDER BY clause, the SELECT statement will not sort the result set.
-- It means that the rows in the result set don’t have a specific order.


-- sort by hire date

select
	employee_id,
	first_name,
	last_name,
	hire_date,
	salary
from employees
order by
    hire_date

--  sort by hire date desc (latest to earliest)

select
	employee_id,
	first_name,
	last_name,
	hire_date,
	salary
from employees
order by
    hire_date desc;






































































