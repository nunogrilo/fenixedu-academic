ALTER TABLE STUDENT_CURRICULAR_PLAN CHANGE KEY_SPECIALIZATION_BRANCH KEY_SPECIALIZATION_BRANCH int(11) default NULL;
UPDATE STUDENT_CURRICULAR_PLAN SET KEY_SPECIALIZATION_BRANCH=NULL, KEY_SECUNDARY_BRANCH=NULL WHERE KEY_DEGREE_CURRICULAR_PLAN = 48;