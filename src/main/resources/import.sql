delete from authentications;
--delete from prescription_drug;
delete from quantityExecutions;
delete from quantityPrescriptions;
delete from prescriptions;
delete from drugs;
delete from activeSubstances;
delete from prescriptionExecutions;
delete from doctors;
delete from pharmacists;
delete from patients;

insert into doctors (id, first_name, last_name, AMKA, AFM, email, password, street, number, city, country, zipcode)
values (2000, 'John', 'Papadopoulos', '15020201111', '144441111', 'john@doctor.com', 'd1111', null, null, null, null, null);
insert into doctors (id, first_name, last_name, AMKA, AFM, email, password, street, number, city, country, zipcode)
values (2001, 'Max', 'Tasopoulos', '15020202222', '144442222', 'max@doctor.com', 'd2222', null, null, null, null, null);
insert into doctors (id, first_name, last_name, AMKA, AFM, email, password, street, number, city, country, zipcode)
values (2002, 'Christina', 'Xoleva', '15020203333', '144443333', 'chris@doctor.com', 'd3333', null, null, null, null, null);

insert into patients (id, first_name, last_name, AMKA, email, password, street, number, city, country, zipcode)
values (3000, 'Spyros', 'Alexandrou', '12121201112', 'spy@patient.com', 'ip1111', null, null, null, null, null);
insert into patients (id, first_name, last_name, AMKA, email, password, street, number, city, country, zipcode)
values (3001, 'Laoura', 'Narges', '12121202222', 'laoura@patient.com"', 'ip2222', null, null, null, null, null);

insert into pharmacists (id, first_name, last_name, AFM, email, password, street, number, city, country, zipcode)
values (4000, 'Maria', 'Damiges', '133331111', 'maria@pharmacist.com', 'ph1111', null, null, null, null, null);
insert into pharmacists (id, first_name, last_name, AFM, email, password, street, number, city, country, zipcode)
values (4001, 'Don', 'Volikas', '133332222', 'don@pharmacist.com', 'ph2222', null, null, null, null, null);

insert into authentications (id, username, password) values (1, 'john@doctor.com', 'd1111');
insert into authentications (id, username, password) values (2, 'maria@pharmacist.com', 'ph1111');
insert into authentications (id, username, password) values (9999, 'admin@admin.com', 'ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad');

insert into activeSubstances (id, substanceName, expectedQuantity) values (5000, 'Paraketamol', 10);
insert into activeSubstances (id, substanceName, expectedQuantity) values (5001, 'Nabilon', 2);
insert into activeSubstances (id, substanceName, expectedQuantity) values (5002, 'Povidoni', 100);
insert into activeSubstances (id, substanceName, expectedQuantity) values (5003, 'Iron', 7);

insert into drugs (id, drugName, drugPrice, MedicineCategory, activeSubstance_id) values (6000, 'Depon', 5.44, 'ORIGINALS', 5000);
insert into drugs (id, drugName, drugPrice, MedicineCategory, activeSubstance_id) values (6001, 'Lourofen', 8.44, 'GENERICS', 5001);
insert into drugs (id, drugName, drugPrice, MedicineCategory, activeSubstance_id) values (6002, 'Betedin', 2.88, 'GENERICS', 5002);

--PARTIALLY_EXECUTED in 9000
insert into prescriptionExecutions (id, pharmacistAFM, executionDate, summaryCost, executionFlag, pharmacist_id)
values (9000, '133331111', now(), 6.3432, 'PARTIALLY', 4000);
insert into prescriptionExecutions (id, pharmacistAFM, executionDate, summaryCost, executionFlag, pharmacist_id)
values (9001, '133331111', now(), 0.2176, 'EXECUTED', 4000); --busted

insert into prescriptions (id, doctorToPrescription, patientToPrescription, prescription_execution, doctorAMKA, patientAMKA, diagnosis, creationDate)
values (7000, 2000, 3000, 9000, '15020201111', '12121201112', 'headacke', now());
insert into prescriptions (id, doctorToPrescription, patientToPrescription, prescription_execution, doctorAMKA, patientAMKA, diagnosis, creationDate)
values (7001, 2001, 3001, null, '15020202222', '12121202222', 'osfialgia', now());
insert into prescriptions (id, doctorToPrescription, patientToPrescription, prescription_execution, doctorAMKA, patientAMKA, diagnosis, creationDate)
values (7002, 2002, 3000, null, '15020203333', '12121201112', 'brokenLeg', now());
insert into prescriptions (id, doctorToPrescription, patientToPrescription, prescription_execution, doctorAMKA, patientAMKA, diagnosis, creationDate)
values (7003, 2002, 3000, null, '15020203333', '12121201112', 'toothache', now());
insert into prescriptions (id, doctorToPrescription, patientToPrescription, prescription_execution, doctorAMKA, patientAMKA, diagnosis, creationDate)
values (7200, 2001, 3001, 9001, '15020202222', '12121202222', 'hipothermia', now()); --busted


insert into quantityPrescriptions (Id, quantityPrescription,  drug_id, prescription_id) values (8000, 4, 6000,  7000);
insert into quantityPrescriptions (Id, quantityPrescription,  drug_id, prescription_id) values (8001, 8, 6001, 7000);
insert into quantityPrescriptions (Id, quantityPrescription,  drug_id, prescription_id) values (8002, 12, 6002, 7001);
insert into quantityPrescriptions (Id, quantityPrescription,  drug_id, prescription_id) values (8003, 2, 6000, 7002);
insert into quantityPrescriptions (Id, quantityPrescription,  drug_id, prescription_id) values (8004, 9, 6000, 7003);
insert into quantityPrescriptions (Id, quantityPrescription,  drug_id, prescription_id) values (8200, 100, 6000, 7200); --busted



insert into quantityExecutions (Id, quantityExecutionPieces, drug_id, prescriptionExecution_id) values(1100, 4, 6000, 9000); --prescription 7000
insert into quantityExecutions (Id, quantityExecutionPieces, drug_id, prescriptionExecution_id) values(1101, 7, 6001, 9000); --prescription 7000

insert into quantityExecutions (Id, quantityExecutionPieces, drug_id, prescriptionExecution_id) values(1200, 100, 6000, 9001); --prescription 7200
