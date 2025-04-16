// { type: [todo remove], payload: id }

import { TodoFilter } from "../todo/interfaces/TodoFilter";
import { TodoInterface } from "../todo/interfaces/TodoInterface";

export const todoReducer = ( todos = [], action ) => {

    // switch ( action.type ) {
    //     case 'Add Todo':
    //         console.log("Adding  ");
    //         console.log(action.payload);

    //         const newTodoElement:TodoInterface = {
    //             done: false,
    //             // priority: action.payload.priority,
    //             ...action.payload
    //         }
    //         console.log(newTodoElement);


    //         return [...todos, newTodoElement];
    //         // console.log(todos);

    //         // return [ ...todos, action.payload ];

    //     case 'Delete Todo':
    //         return todos.filter( todo => todo.id !== action.payload );

    //     case 'Toogle Done':
    //         console.log('toogle');
    //         console.log(action);


    //         return todos.map( todo => {
    //             if (todo.id === action.payload){
    //                 console.log(todo.done);

    //                 return {
    //                     ...todo,
    //                     done:!todo.done
    //                 }
    //             }
    //             return todo;
    //         });

    //     case 'SetAllDB Todo':
    //         // action.payload.forEach(element => {
    //         //     const exists = todos.some( todoDB => todoDB.id === todos.id );
    //         //     if (!exists){
    //         //         todos.push(element)
    //         //     }
    //         // });
    //         todos = action.payload;
    //         console.log("todos:",todos);

    //         return todos;

    //     case 'Reset Todo':
    //         return action.payload;

    //     case 'Update Todo':
    //         console.log('actualizando',action.payload.id);
    //         const n = todos.map(todo =>
    //             todo.id === action.payload.id
    //                 ? {
    //                     ...todo,
    //                     ...action.payload,
    //                 }
    //                 : todo
    //             );

    //         console.log("actualizado",n);
    //         return n;

    //     case 'Filter Todo':
    //         // {taskName: 'query', priority: 'High', todos: 'Done'}
    //         console.log('filtranod', todos);

    //         // let filteredTasks = [...todos];
    //         let filteredTasks = todos.map(todo => ({ ...todo }));


    //         if (action.payload.hasOwnProperty('taskName')){
    //             filteredTasks= filteredTasks.filter( (todo:TodoFilter) => {
    //                 console.log(todo['taskName'], action.payload['taskName']);
    //                 console.log(todo['taskName']?.toLocaleLowerCase().includes( action.payload['taskName'] ));

    //                 return todo['taskName']?.toLocaleLowerCase().includes( action.payload['taskName'] )
    //             });
    //         }

    //         if (action.payload.hasOwnProperty('todos')){
    //             filteredTasks= filteredTasks.filter( todo => {
    //                 if (action.payload['todos'] === 'Undone')
    //                     return todo['done'] === false
    //                 else if (action.payload['todos'] === 'Done')
    //                     return todo['done'] === true
    //                 return todo
    //             });
    //         }

    //         if (action.payload.hasOwnProperty('priority')){
    //             filteredTasks= filteredTasks.filter( todo => {
    //                 if (action.payload['priority'] !== 'All')
    //                     return todo['priority'] === action.payload['priority']
    //                 return todo
    //             });
    //         }
    //         return filteredTasks;



    //         // const filters = ['priority','todos'];
    //         // for (const key of filters) {
    //         //     if (action.payload.hasOwnProperty(key)) {
    //         //         const filterValue = action.payload[key];
    //         //         filteredTasks= filteredTasks.filter( todo => {
    //         //             return todo[key] === filterValue
    //         //         });
    //         //     }
    //         // }



    //         // return todos.filter( todo => todo.id !== action.payload );


    //     default:
    //         console.log('entre');

    //         return todos;
    // }


}