
import { useReducer, useState } from "react"
import { TodoContext } from "./TodoContext";
import { TodoInterface } from "../todo/interfaces/TodoInterface";
import { todoReducer } from "./TodoReducer";


// const todo = {
//     id: 123,
//     name: 'task 1',
//     priority: 'Low',
//     dueDate: Date,
//     done: false
    
// }

const listTodo:TodoInterface[] = [
    {
        done: false,      
        id: 123,
        taskName: 'task 1',
        priority: 'Low',
        dueDate: new Date('2025/04/9'),
    },
    {
        done: false,
        id: 234,
        taskName: 'task 21',
        priority: 'High',
        dueDate: new Date('2026/01/01'),
    },
    {
        done: false,
        id: 3,
        taskName: 'task 31',
        priority: 'Medium',
        dueDate: new Date('2025/04/15'),
    }
    
];

const init = () => {
    return JSON.parse(localStorage.getItem('todos'))  || [];
}

// EFECTO secundario
// useEffect(() => {
//     localStorage.setItem('todos', JSON.stringify(todos));
//   }, [todos]);

export const TodoProvider = ({ children }) => {

    const [allTodos, setAllTodos] = useState(listTodo);
    const [ todos, dispatch ] = useReducer( todoReducer, allTodos );


    return (
        
        <TodoContext.Provider value={{ allTodos, setAllTodos,todos, dispatch }}>
            { children }
        </TodoContext.Provider>
    )
}
