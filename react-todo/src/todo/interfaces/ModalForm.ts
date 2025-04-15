export interface ModalInsertFormInput {
    done:     boolean;
    taskName: string;
    priority: string;
    dueDate:  Date;
}

export interface ModalUpdateFormInput {
    done:         boolean;
    id:           string;
    taskName:     string;
    priority:     string;
    dueDate:      Date;
    doneDate:     Date;
    creationDate: Date;
}
