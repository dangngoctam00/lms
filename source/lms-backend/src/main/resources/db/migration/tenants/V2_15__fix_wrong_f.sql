ALTER TABLE "class_session"
    DROP CONSTRAINT "class_session_unit_fk";
ALTER TABLE "class_session"
    ADD CONSTRAINT "class_session_unit_fk" FOREIGN KEY ("unit_id") REFERENCES "unit_class" ("id");