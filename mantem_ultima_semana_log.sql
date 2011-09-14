select count(*) from logchecks 
	where time <= to_timestamp(current_date-7 ,'YYYY-MM-DD')

