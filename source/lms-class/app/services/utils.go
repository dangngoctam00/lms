package services

import (
	"lms-class/ent"
	"log"
)

func Commit(tx *ent.Tx) {
	if err := tx.Commit(); err != nil {
		if err := tx.Rollback(); err != nil {
			log.Fatalln("Error while trying roll back exception: ", err)
		}
	}
}
