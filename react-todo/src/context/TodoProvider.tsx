
import { useEffect, useReducer, useState } from "react"
import { TodoContext } from "./TodoContext";
import { TodoInterface } from "../todo/interfaces/TodoInterface";
import { todoReducer } from "./TodoReducer";
import { parseISO } from "date-fns";
import todoApi from "../api/TodoApi";


// const todo = {
//     id: 123,
//     name: 'task 1',
//     priority: 'Low',
//     dueDate: Date,
//     done: false
    
// }

// const listTodo:TodoInterface[] = [
//     {
//         done: false,      
//         id: 123,
//         taskName: 'task 1',
//         priority: 'Low',
//         dueDate: new Date('2025/04/9'),
//     },
//     {
//         done: false,
//         id: 234,
//         taskName: 'task 21',
//         priority: 'High',
//         dueDate: new Date('2026/01/01'),
//     },
//     {
//         done: false,
//         id: 3,
//         taskName: 'task 31',
//         priority: 'Medium',
//         dueDate: new Date('2025/04/15'),
//     }
    
// ];

const init = () => {
    return JSON.parse(localStorage.getItem('todos'))  || [];
}

// EFECTO secundario
// useEffect(() => {
//     localStorage.setItem('todos', JSON.stringify(todos));
//   }, [todos]);

export const TodoProvider = ({ children }) => {

    const [filteredList, setFilteredList] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [ todos, dispatch ] = useReducer( todoReducer, [] );


    const getAll = async() =>{
        try {
            const {data} = await todoApi.get('');
            console.log('Todos en db:',data);
    
            const dataFormatted = data.map( (todo:any) =>{
                todo.dueDate = parseISO(todo.dueDate);
                // console.log(todo);
                return todo;
            });

            const action = {
                type: 'SetAllDB Todo',
                payload: data
            }
            dispatch( action );
    
            setIsLoading(false);
            setFilteredList(dataFormatted);
            
        } catch (error) {
            console.log("Error al cargar");
            console.log(error);
        }
    }

    const getById = async(id:String) =>{
        try {
            console.log('get by:',id);
            const {data} = await todoApi.get(`/${id}`);
            console.log("Data obtenida");
            
            console.log(data);
            return data;
            
            // {id: '2', taskName: 'Hacer la tarea', priority: 'High', dueDate: '2025-04-09T03:58:20.330+00:00', done: false}
           
    
            // const dataFormatted = data.map( (todo:any) =>{
            //     todo.dueDate = parseISO(todo.dueDate);
            //     console.log(todo);
            //     return todo;
            // });

            // const action = {
            //     type: 'SetAllDB Todo',
            //     payload: data
            // }
            // dispatch( action );
    
            // setIsLoading(false);
            // setfilteredList(dataFormatted);
            
        } catch (error) {
            console.log("Error al cargar");
            console.log(error);
        }
    }

    const postTodo = async(formValues) =>{
        try {
            const { data } = await todoApi.post('', formValues );
            console.log('llamando a post back res:', data);
        
            // Optimistic UI es necesario? Actualización del Estado Local 
            if (data.id){
                const action = {
                    type: 'Add Todo',
                    payload: {...formValues, id: data.id }
                }
                dispatch( action ); 
            }
            await getAll();
            
        } catch (error) {
            console.log("Error al ppostear");
            console.log(error);
        }
    }

    const updateTodo = async(formValues) =>{
        try {
            console.log('llamando a update back', formValues);
            const { data } = await todoApi.put(`/${formValues.id}`, formValues );
            console.log(data);

            if (data.id){
                const action = {
                    type: 'Update Todo',
                    payload: data
                }
                dispatch( action );
            }
    
        } catch (error) {
            console.log("Error al ppostear");
            console.log(error);
        }
    }

    
      

    const deleteTodo = async(id) =>{
        try {
            console.log('llamando a delete back', id);
            const { data } = await todoApi.delete(`/${id}`);
            console.log(data);
            
            const action = {
                type: 'Delete Todo',
                payload: id
            }
            dispatch( action );
            

            // await getAll();

        } catch (error) {
            console.log("Error al ppostear");
            console.log(error);
        }
    }

    useEffect(() => {
        getAll();
    }, []);


    return (
        
        <TodoContext.Provider value={{ filteredList, setFilteredList,todos, dispatch, isLoading, 
            getAll, postTodo, updateTodo, deleteTodo, getById
        }}>
            { children }
        </TodoContext.Provider>
    )
}
