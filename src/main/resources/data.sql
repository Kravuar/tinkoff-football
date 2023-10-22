-----------------------------------------Users--------------------------------------------
INSERT INTO Users VALUES ('123notNoob321', 'GrandMaster');
INSERT INTO Users VALUES ('21CanYouDoSomeForMe', 'Drake');
INSERT INTO Users VALUES ('yJeNeTot', 'Tinkoff');
INSERT INTO Users VALUES ('qwerty', 'Oleg');
INSERT INTO Users VALUES ('roflanPapich', 'Papich');
INSERT INTO Users VALUES ('roflanBatya', 'Arthas');
INSERT INTO Users VALUES ('Itali', 'hetEro_phobE');
INSERT INTO Users VALUES ('BottomG', 'Aleksandr_Pistoletov');
INSERT INTO Users VALUES ('TopG61', 'Andrew_Tate');
INSERT INTO Users VALUES ('1BukinTrap777Machine100', 'Gennady');
------------------------------------------------------------------------------------------


-----------------------------------------Teams------------------------------------------—
INSERT INTO Teams VALUES (1, 'Команда А', 0, 3, 4);
INSERT INTO Teams VALUES (1, 'Чемпионы Людей', 1, 6, 5);
INSERT INTO Teams VALUES (1, 'No Homo', 1, 7, 10);
INSERT INTO Teams VALUES (0, 'Братья звери', 0, 9, 8);
INSERT INTO Teams VALUES (1, 'Любители шишек', 1, 5, 6);
------------------------------------------------------------------------------------------


-------------------------------------Tournaments------------------------------------
INSERT INTO Tournaments VALUES ('2016-11-24 16:00:00', 4, 4, '2016-11-24 16:20:00', 2, '1000$ - СЮДА', 6);
INSERT INTO Tournaments VALUES ('2022-02-20 12:10:01', 2, 1, '2022-02-24 5:00:00', 3, 'Турнир чемпионов по шашкам', 10);
INSERT INTO Tournaments VALUES ('2023-10-21 21:19:39', 4, 0, '2023-10-22 20:30:00', 0, 'Турик для чилла', 7);
INSERT INTO Tournaments VALUES ('2023-10-21 22:01:01', 4, 2, '2023-10-22 04:20:00', 1, 'Раздача заданий на спавне', 1);
------------------------------------------------------------------------------------------


------------------------------Tournament_Participants----------------------------
INSERT INTO Tournament_Participants VALUES (1, 1);
INSERT INTO Tournament_Participants VALUES (2, 1);
INSERT INTO Tournament_Participants VALUES (5, 1);
INSERT INTO Tournament_Participants VALUES (3, 1);

INSERT INTO Tournament_Participants VALUES (4, 2);

INSERT INTO Tournament_Participants VALUES (4, 4);
INSERT INTO Tournament_Participants VALUES (2, 4);
------------------------------------------------------------------------------------------


-----------------------------------------Matches-----------------------------------------
INSERT INTO Matches VALUES (2, 2, null, 2, 12, 1, 9, 8, 1);
INSERT INTO Matches VALUES (2, 1, null, 2, 100, 0, 6, 7, 1);
INSERT INTO Matches VALUES (2, 0, '1000$', 2, 69, 96, 9, 6, 1);
INSERT INTO Matches VALUES (5, 1, 'Французская Обжарка', 1, 2, 2, 1, 4, 4);
---------------------------------------------------------------------------------------------