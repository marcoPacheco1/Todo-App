export interface TodoResponseInterface {
    totalItems:  number;
    fileredTodo: FileredTodo[];
    totalPages:  number;
    currentPage: number;
}

export interface FileredTodo {
    id:           string;
    taskName:     string;
    priority:     string;
    dueDate:      Date;
    done:         boolean;
    doneDate:     Date;
    creationDate: Date;
}
