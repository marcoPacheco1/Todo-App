// { type: [todo remove], payload: id }

import { TodoFilter } from "../todo/interfaces/TodoFilter";
import { TodoInterface } from "../todo/interfaces/TodoInterface";

export const todoReducer = ( initialStateTodo = [], action ) => {

    switch ( action.type ) {
        case 'Add Todo':
            console.log("Adding  ");
            console.log(action.payload);
            
            const newTodoElement:TodoInterface = {
                done: false,
                // priority: action.payload.priority,
                ...action.payload
            }
            console.log(newTodoElement);
            

            return [...initialStateTodo, newTodoElement];
            // console.log(initialStateTodo);
            
            // return [ ...initialStateTodo, action.payload ];

        case 'Delete Todo':
            return initialStateTodo.filter( todo => todo.id !== action.payload );

        case 'Toogle Done':
            console.log('toogle');
            console.log(action);
            
            
            return initialStateTodo.map( todo => {
                if (todo.id === action.payload){
                    console.log(todo.done);
                    
                    return {
                        ...todo,
                        done:!todo.done
                    }
                }
                return todo;
            });
        
        case 'SetAllDB Todo':
            // action.payload.forEach(element => {
            //     const exists = initialStateTodo.some( todoDB => todoDB.id === initialStateTodo.id );
            //     if (!exists){
            //         initialStateTodo.push(element)
            //     }
            // });
            initialStateTodo = action.payload;
            console.log("todos:",initialStateTodo);
            
            return initialStateTodo;

        case 'Reset Todo':
            return action.payload;

        case 'Update Todo':
            console.log('actualizando');
            const n = initialStateTodo.map(todo =>
                todo.id === action.payload.id
                    ? {
                        ...todo,
                        ...action.payload,
                        dueDate: new Date(action.payload.dueDate), // Convertir a Date si es necesario
                    }
                    : todo
                );
            console.log(n);
            return n;

        case 'Filter Todo':
            // {taskName: 'query', priority: 'High', state: 'Done'}
            console.log('filtranod');
            // let filteredTasks = [...initialStateTodo];
            let filteredTasks = initialStateTodo.map(todo => ({ ...todo }));
            
            
            if (action.payload.hasOwnProperty('taskName')){
                filteredTasks= filteredTasks.filter( (todo:TodoFilter) => {
                    console.log(todo['taskName'], action.payload['taskName']);
                    console.log(todo['taskName']?.toLocaleLowerCase().includes( action.payload['taskName'] ));
                    
                    return todo['taskName']?.toLocaleLowerCase().includes( action.payload['taskName'] ) 
                });
            }

            if (action.payload.hasOwnProperty('state')){
                filteredTasks= filteredTasks.filter( todo => {
                    if (action.payload['state'] === 'Undone')
                        return todo['done'] === false 
                    else if (action.payload['state'] === 'Done')
                        return todo['done'] === true 
                    return todo    
                });
            }

            if (action.payload.hasOwnProperty('priority')){
                filteredTasks= filteredTasks.filter( todo => {
                    if (action.payload['priority'] !== 'All')
                        return todo['priority'] === action.payload['priority'] 
                    return todo
                });
            }
            return filteredTasks;


            
            // const filters = ['priority','state'];
            // for (const key of filters) {
            //     if (action.payload.hasOwnProperty(key)) {
            //         const filterValue = action.payload[key];
            //         filteredTasks= filteredTasks.filter( todo => {
            //             return todo[key] === filterValue
            //         });
            //     }
            // }

            
            
            // return initialStateTodo.filter( todo => todo.id !== action.payload );

                
        default:
            console.log('entre');
            
            return initialStateTodo;
    }


}