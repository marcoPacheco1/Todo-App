
import React, { useEffect, useReducer, useState } from "react"
import { TodoContext } from "./TodoContext";
import { todoReducer } from "./TodoReducer";
import { parseISO } from "date-fns";
import todoApi from "../api/TodoApi";
import { useLocation } from "react-router";
import { GridSortModel } from "@mui/x-data-grid";
import { MetricsInterface } from "../todo/interfaces/MetricsInterface";
import { ModalInsertFormInput, ModalUpdateFormInput } from "../todo/interfaces/ModalForm";


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

export const TodoProvider = ({ children }) => {
    const myPageSize: number = 3;


    const [filteredList, setFilteredList] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [ todos, dispatch ] = useReducer( todoReducer, [] );
    const [selectedRows, setSelectedRows] = useState([]);

    const location = useLocation();


    const [paginationModel, setPaginationModel] = useState({
        pageSize: myPageSize,
        page: 0,
    });

    const [sortModel, setSortModel] = React.useState<GridSortModel>([]);
    const [rowCount, setRowCount] = useState(0);

    const [metricModel, setMetricModel] = useState<MetricsInterface>({
        averageTimeToFinishByPriority: {
            Low: '',
            Medium: '',
            High: '',
        },
        averageEstimatedTimeToComplete: '', 
    });



    const getAll = async(params?: URLSearchParams ) =>{
        try {
            console.log('params', params);
            if (!params) {
                params = new URLSearchParams();
            }

            if (sortModel.length > 0) {
                params.append('sortBy', sortModel[0].field.toUpperCase());
                params.append('sortDirection', sortModel[0].sort.toUpperCase());
            }
            
            params.append('page', paginationModel.page.toString());
            params.append('size', paginationModel.pageSize.toString());
            
            const {data} = await todoApi.get('',{
                params : params
            });

            await getMetrics();
            
            setPaginationModel({
                pageSize: myPageSize,
                page: data.currentPage,
            });

            // {totalItems: 3, fileredTodo: Array(2), totalPages: 2, currentPage: 1}
            console.log('Todos en db:',data.fileredTodo);

            let donesArray:any = [];
            data.fileredTodo.map( (todo:any) =>{
                if (todo.done)
                    donesArray.push(todo.id);
            });

            setSelectedRows(donesArray);
            

    
            const dataFormatted = data.fileredTodo.map( (todo:any) =>{
                if (todo.dueDate)
                    todo.dueDate = parseISO(todo.dueDate);
                return todo;
            });
            setRowCount(data.totalItems);

            const action = {
                type: 'SetAllDB Todo',
                payload: data.fileredTodo
            }
            dispatch( action );
    
            setIsLoading(false);
            setFilteredList(dataFormatted);
            
        } catch (error) {
            console.log("Error al cargar");
            console.log(error);
        }
    }

    const getMetrics = async() => {
        const metricsCall = await todoApi.get('/metrics');
        const metrics: MetricsInterface = metricsCall.data;
        setMetricModel(metrics);
    }



    const getById = async(id:String) =>{
        try {
            console.log('get by:',id);
            const {data} = await todoApi.get(`/${id}`);
            console.log("Data obtenida");
            await getMetrics();
            
            console.log(data);
            return data;
            
        } catch (error) {
            console.log("Error al cargar");
            console.log(error);
        }
    }

    const postTodo = async(formValues:ModalInsertFormInput) =>{
        try {
            console.log('llamando a post back res:', formValues);
            const { data } = await todoApi.post('', formValues,{
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            } );
        
            // Optimistic UI is it neccesary? 
            if (data.id){
                const action = {
                    type: 'Add Todo',
                    payload: {...formValues, id: data.id }
                }
                dispatch( action ); 
            }
            await getAll();
            await getMetrics();

            
        } catch (error) {
            console.log("There was an error");
            console.log(error);
        }
    }

    const updateTodoDone = async(ids:String[]) =>{
        try {
            const promises = ids.map(async (id) => {
                const data:any = await getById(id);
                console.log(data);
                if (data && !data.done){

                    // POST http://localhost:8080/todos/{id}/done
                    const r= await todoApi.post(`/${data.id}/done`);
                    
                    if (r.data.id){

                        console.log(selectedRows);
                        
                        const action = {
                            type: 'Update Todo',
                            payload: r.data
                        }
                        dispatch( action );

                    }
                    return r.data;
                }
                else{
                    // PUT http://localhost:8080/todos/{id}/undone
                    const r= await todoApi.put(`/${data.id}/undone`);
                    if (r.data.id){
                        const action = {
                            type: 'Update Todo',
                            payload: r.data
                        }
                        dispatch( action );

                    }
                    return r.data;
                }
            });

            const results = await Promise.all(promises);

            setSelectedRows((prevSelectedRows) => {
                let newSelectedRows = [...prevSelectedRows];
                results.forEach((result) => {
                if (result.done) {
                    if (!newSelectedRows.includes(result.id)) {
                        newSelectedRows.push(result.id);
                    }
                } else {
                    newSelectedRows = newSelectedRows.filter((id) => id !== result.id);
                }
                });
                return newSelectedRows;
            });

            
            await getMetrics();

    
        } catch (error) {
            console.log("There was an error ");
            console.log(error);
        }
    }
    const updateTodo = async(formValues:ModalUpdateFormInput) =>{
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
            await getMetrics();

    
        } catch (error) {
            console.log("There was an error ");
            console.log(error);
        }
    }

    
      

    const deleteTodo = async(id:String) =>{
        try {
            const { data } = await todoApi.delete(`/${id}`);
            console.log(data);
            
            const action = {
                type: 'Delete Todo',
                payload: id
            }
            dispatch( action );
            
            await getMetrics();

            // await getAll();

        } catch (error) {
            console.log("Error al ppostear");
            console.log(error);
        }
    }


    useEffect(() => {
        const updateRecords = async () => {
            try {
                const params = new URLSearchParams(location.search);
                
                console.log('QWERTYLocation:', location);
                console.log('Location.search:', location.search);
                
                await getAll(params);
                await getMetrics();

            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        updateRecords();
    }, [location.search, paginationModel.page,sortModel ]);

    // useEffect(() => {
    //     const updateRecords2 = async () => {
    //         try {
                
    //             await getAll();
    //         } catch (error) {
    //             console.error('Error fetching data:', error);
    //         }
    //     };
    //     updateRecords2();
    // }, [paginationModel.page]);

    // useEffect(() => {
    //     const hasQueryParams = location.search !== ''; // Verifica si hay parámetros de consulta
    //     console.log('qwert');
        
    //     if (!hasQueryParams) {
    //         getAll();
    //     }
    // }, [location.search]);


    return (
        
        <TodoContext.Provider value={{ filteredList, setFilteredList,todos, dispatch, isLoading, 
            getAll, postTodo, updateTodo, deleteTodo, getById,
            paginationModel, rowCount, setPaginationModel, 
            sortModel, setSortModel, metricModel, updateTodoDone, selectedRows, setSelectedRows
        }}>
            { children }
        </TodoContext.Provider>
    )
}
