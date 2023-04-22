/*
 * Quiero ver las 5 primeras mascotas  sin repetir razas y que ninguna de esas mascotas sea de tipo perro
 * que ya se les asigno una cita médica con un doctor especialista y que a su vez no tengan ningún tratamiento
 * asignado aún. Y a su vez que todos esos animales ya tengan  un dueño que viva en la misma calle donde se
 * encuentra la clínica (en calle 34 que se yo)
 * Que wl nombre del dueño empiece por A
 * */


CREATE OR REPLACE PROCEDURE report (IN race VARCHAR(100),
	IN expertise VARCHAR(100),
	IN adopterAddress VARCHAR(100),
	IN adopterName VARCHAR(100))
BEGIN
    WITH cte_zero_treatments AS ( -- Gets medical appointments without treatments
        SELECT ma.id AS appointmentID, t.id, COUNT(t.id) as treatmentCount
        FROM treatment t
        RIGHT JOIN medical_appointment ma ON ma.id = t.medical_appointment_id
        GROUP BY ma.id
        HAVING treatmentCount = 0
    )
    SELECT p.*
    FROM pet p
    JOIN medical_appointment ma ON ma.pet_id = p.id
    JOIN doctor d ON ma.doctor_id = d.id
    LEFT JOIN adoption a ON a.pet_id = p.id
    LEFT JOIN adopter ater ON a.adopter_id = ater.id
    join cte_zero_treatments cte_zero_treat ON cte_zero_treat.appointmentID = ma.id
    WHERE p.race <> race AND d.expertise = expertise OR (ater.address LIKE adopterAddress AND ater.name LIKE adopterName)
    GROUP BY p.race
    ORDER BY p.id LIMIT 5;
END;