INSERT INTO roles (role)
SELECT 'admin'
WHERE 
	NOT EXISTS (SELECT role FROM roles r
				WHERE r.role = 'registered_user');

INSERT INTO roles (role)
SELECT 'registered_user'
WHERE 
	NOT EXISTS (SELECT role FROM roles r
				WHERE r.role = 'registered_user');