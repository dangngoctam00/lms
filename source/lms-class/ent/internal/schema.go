// Code generated by ent, DO NOT EDIT.

//go:build tools
// +build tools

// Package internal holds a loadable version of the latest schema.
package internal

const Schema = `{"Schema":"lms-class/ent/schema","Package":"lms-class/ent","Schemas":[{"name":"Exam","config":{"Table":""},"edges":[{"name":"quizzes","type":"Quiz"}],"fields":[{"name":"title","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":0,"MixedIn":false,"MixinIndex":0}},{"name":"context","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":1,"MixedIn":false,"MixinIndex":0}},{"name":"contextId","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":2,"MixedIn":false,"MixinIndex":0}},{"name":"isPublished","type":{"Type":1,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":3,"MixedIn":false,"MixinIndex":0}},{"name":"havingDraft","type":{"Type":1,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":4,"MixedIn":false,"MixinIndex":0}},{"name":"lastPublishedAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":5,"MixedIn":false,"MixinIndex":0}},{"name":"updatedAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"position":{"Index":6,"MixedIn":false,"MixinIndex":0}}],"annotations":{"EntSQL":{"table":"exam"}}},{"name":"ExamHistory","config":{"Table":""},"fields":[{"name":"history_time","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"default":true,"default_kind":19,"immutable":true,"position":{"Index":0,"MixedIn":false,"MixinIndex":0}},{"name":"operation","type":{"Type":6,"Ident":"enthistory.OpType","PkgPath":"github.com/flume/enthistory","PkgName":"enthistory","Nillable":false,"RType":{"Name":"OpType","Ident":"enthistory.OpType","Kind":24,"PkgPath":"github.com/flume/enthistory","Methods":{"String":{"In":[],"Out":[{"Name":"string","Ident":"string","Kind":24,"PkgPath":"","Methods":null}]},"Value":{"In":[],"Out":[{"Name":"Value","Ident":"driver.Value","Kind":20,"PkgPath":"database/sql/driver","Methods":null},{"Name":"error","Ident":"error","Kind":20,"PkgPath":"","Methods":null}]},"Values":{"In":[],"Out":[{"Name":"","Ident":"[]string","Kind":23,"PkgPath":"","Methods":null}]}}}},"enums":[{"N":"INSERT","V":"INSERT"},{"N":"UPDATE","V":"UPDATE"},{"N":"DELETE","V":"DELETE"}],"immutable":true,"position":{"Index":1,"MixedIn":false,"MixinIndex":0}},{"name":"ref","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"optional":true,"immutable":true,"position":{"Index":2,"MixedIn":false,"MixinIndex":0}},{"name":"updated_by","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"immutable":true},{"name":"title","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":4,"MixedIn":false,"MixinIndex":0}},{"name":"context","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":5,"MixedIn":false,"MixinIndex":0}},{"name":"contextId","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":6,"MixedIn":false,"MixinIndex":0}},{"name":"isPublished","type":{"Type":1,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":7,"MixedIn":false,"MixinIndex":0}},{"name":"havingDraft","type":{"Type":1,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":8,"MixedIn":false,"MixinIndex":0}},{"name":"lastPublishedAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":9,"MixedIn":false,"MixinIndex":0}},{"name":"updatedAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"position":{"Index":10,"MixedIn":false,"MixinIndex":0}}],"annotations":{"EntSQL":{"table":"exam_history"}}},{"name":"Question","config":{"Table":""},"fields":[{"name":"context","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":0,"MixedIn":false,"MixinIndex":0}},{"name":"contextId","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":1,"MixedIn":false,"MixinIndex":0}},{"name":"position","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":2,"MixedIn":false,"MixinIndex":0}},{"name":"questionType","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":3,"MixedIn":false,"MixinIndex":0}},{"name":"data","type":{"Type":3,"Ident":"json.RawMessage","PkgPath":"encoding/json","PkgName":"json","Nillable":true,"RType":{"Name":"RawMessage","Ident":"json.RawMessage","Kind":23,"PkgPath":"encoding/json","Methods":{"MarshalJSON":{"In":[],"Out":[{"Name":"","Ident":"[]uint8","Kind":23,"PkgPath":"","Methods":null},{"Name":"error","Ident":"error","Kind":20,"PkgPath":"","Methods":null}]},"UnmarshalJSON":{"In":[{"Name":"","Ident":"[]uint8","Kind":23,"PkgPath":"","Methods":null}],"Out":[{"Name":"error","Ident":"error","Kind":20,"PkgPath":"","Methods":null}]}}}},"position":{"Index":4,"MixedIn":false,"MixinIndex":0}},{"name":"updatedAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"position":{"Index":5,"MixedIn":false,"MixinIndex":0}},{"name":"version","type":{"Type":13,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"default":true,"default_kind":19,"position":{"Index":6,"MixedIn":false,"MixinIndex":0},"comment":"Unix time of when the latest update occurred"}],"annotations":{"EntSQL":{"table":"question"}}},{"name":"QuestionHistory","config":{"Table":""},"fields":[{"name":"history_time","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"default":true,"default_kind":19,"immutable":true,"position":{"Index":0,"MixedIn":false,"MixinIndex":0}},{"name":"operation","type":{"Type":6,"Ident":"enthistory.OpType","PkgPath":"github.com/flume/enthistory","PkgName":"enthistory","Nillable":false,"RType":{"Name":"OpType","Ident":"enthistory.OpType","Kind":24,"PkgPath":"github.com/flume/enthistory","Methods":{"String":{"In":[],"Out":[{"Name":"string","Ident":"string","Kind":24,"PkgPath":"","Methods":null}]},"Value":{"In":[],"Out":[{"Name":"Value","Ident":"driver.Value","Kind":20,"PkgPath":"database/sql/driver","Methods":null},{"Name":"error","Ident":"error","Kind":20,"PkgPath":"","Methods":null}]},"Values":{"In":[],"Out":[{"Name":"","Ident":"[]string","Kind":23,"PkgPath":"","Methods":null}]}}}},"enums":[{"N":"INSERT","V":"INSERT"},{"N":"UPDATE","V":"UPDATE"},{"N":"DELETE","V":"DELETE"}],"immutable":true,"position":{"Index":1,"MixedIn":false,"MixinIndex":0}},{"name":"ref","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"optional":true,"immutable":true,"position":{"Index":2,"MixedIn":false,"MixinIndex":0}},{"name":"updated_by","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"immutable":true},{"name":"context","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":4,"MixedIn":false,"MixinIndex":0}},{"name":"contextId","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":5,"MixedIn":false,"MixinIndex":0}},{"name":"position","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":6,"MixedIn":false,"MixinIndex":0}},{"name":"questionType","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":7,"MixedIn":false,"MixinIndex":0}},{"name":"data","type":{"Type":3,"Ident":"json.RawMessage","PkgPath":"encoding/json","PkgName":"json","Nillable":true,"RType":{"Name":"RawMessage","Ident":"json.RawMessage","Kind":23,"PkgPath":"encoding/json","Methods":{"MarshalJSON":{"In":[],"Out":[{"Name":"","Ident":"[]uint8","Kind":23,"PkgPath":"","Methods":null},{"Name":"error","Ident":"error","Kind":20,"PkgPath":"","Methods":null}]},"UnmarshalJSON":{"In":[{"Name":"","Ident":"[]uint8","Kind":23,"PkgPath":"","Methods":null}],"Out":[{"Name":"error","Ident":"error","Kind":20,"PkgPath":"","Methods":null}]}}}},"position":{"Index":8,"MixedIn":false,"MixinIndex":0}},{"name":"updatedAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"position":{"Index":9,"MixedIn":false,"MixinIndex":0}},{"name":"version","type":{"Type":13,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"default":true,"default_kind":19,"position":{"Index":10,"MixedIn":false,"MixinIndex":0},"comment":"Unix time of when the latest update occurred"}],"annotations":{"EntSQL":{"table":"question_history"}}},{"name":"Quiz","config":{"Table":""},"edges":[{"name":"exam","type":"Exam","field":"examId","ref_name":"quizzes","unique":true,"inverse":true},{"name":"submissions","type":"QuizSubmission"}],"fields":[{"name":"title","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":0,"MixedIn":false,"MixinIndex":0}},{"name":"description","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":1,"MixedIn":false,"MixinIndex":0}},{"name":"gradeTag","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":2,"MixedIn":false,"MixinIndex":0}},{"name":"examId","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":3,"MixedIn":false,"MixinIndex":0}},{"name":"isPublished","type":{"Type":1,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":4,"MixedIn":false,"MixinIndex":0}},{"name":"createdAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"position":{"Index":5,"MixedIn":false,"MixinIndex":0}},{"name":"updatedAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"position":{"Index":6,"MixedIn":false,"MixinIndex":0}},{"name":"context","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":7,"MixedIn":false,"MixinIndex":0}},{"name":"contextId","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":8,"MixedIn":false,"MixinIndex":0}},{"name":"parentId","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":9,"MixedIn":false,"MixinIndex":0}},{"name":"startedAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":10,"MixedIn":false,"MixinIndex":0}},{"name":"finishedAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":11,"MixedIn":false,"MixinIndex":0}},{"name":"timeLimit","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":12,"MixedIn":false,"MixinIndex":0}},{"name":"maxAttempt","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":13,"MixedIn":false,"MixinIndex":0}},{"name":"viewPreviousSessions","type":{"Type":1,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":14,"MixedIn":false,"MixinIndex":0}},{"name":"viewPreviousSessionsTime","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":15,"MixedIn":false,"MixinIndex":0}},{"name":"passedScore","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":16,"MixedIn":false,"MixinIndex":0}},{"name":"finalGradedStrategy","type":{"Type":7,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":17,"MixedIn":false,"MixinIndex":0}}],"annotations":{"EntSQL":{"table":"quiz"},"History":{"exclude":true}}},{"name":"QuizSubmission","config":{"Table":""},"edges":[{"name":"quiz","type":"Quiz","field":"quizId","ref_name":"submissions","unique":true,"inverse":true,"required":true}],"fields":[{"name":"quizId","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":0,"MixedIn":false,"MixinIndex":0}},{"name":"userId","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"position":{"Index":1,"MixedIn":false,"MixinIndex":0}},{"name":"startedAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"position":{"Index":2,"MixedIn":false,"MixinIndex":0}},{"name":"submittedAt","type":{"Type":2,"Ident":"","PkgPath":"time","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":3,"MixedIn":false,"MixinIndex":0}},{"name":"questions","type":{"Type":3,"Ident":"json.RawMessage","PkgPath":"encoding/json","PkgName":"json","Nillable":true,"RType":{"Name":"RawMessage","Ident":"json.RawMessage","Kind":23,"PkgPath":"encoding/json","Methods":{"MarshalJSON":{"In":[],"Out":[{"Name":"","Ident":"[]uint8","Kind":23,"PkgPath":"","Methods":null},{"Name":"error","Ident":"error","Kind":20,"PkgPath":"","Methods":null}]},"UnmarshalJSON":{"In":[{"Name":"","Ident":"[]uint8","Kind":23,"PkgPath":"","Methods":null}],"Out":[{"Name":"error","Ident":"error","Kind":20,"PkgPath":"","Methods":null}]}}}},"position":{"Index":4,"MixedIn":false,"MixinIndex":0}},{"name":"answers","type":{"Type":3,"Ident":"map[int][]question.Key","PkgPath":"lms-class/internal/web/dto/question","PkgName":"question","Nillable":true,"RType":{"Name":"","Ident":"map[int][]question.Key","Kind":21,"PkgPath":"","Methods":{}}},"optional":true,"position":{"Index":5,"MixedIn":false,"MixinIndex":0}},{"name":"score","type":{"Type":12,"Ident":"","PkgPath":"","PkgName":"","Nillable":false,"RType":null},"nillable":true,"optional":true,"position":{"Index":6,"MixedIn":false,"MixinIndex":0}}],"annotations":{"EntSQL":{"table":"quiz_submission"},"History":{"exclude":true}}}],"Features":["sql/lock","intercept","schema/snapshot"]}`
