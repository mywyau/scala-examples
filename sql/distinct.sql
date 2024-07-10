
-- DISTINCT

-- distinct can be combined on a select to filter for duplicates

select distinct salary
from employees
order by salary desc;

-- multiple columns

-- e.g. if this table returns duplicates then we can make both columns distinct

select job_id, salary
from employees
order by
    job_id
    salary desc;

select distinct
    job_id, salary
from employees
order by
    job_id
    salary desc;







































































